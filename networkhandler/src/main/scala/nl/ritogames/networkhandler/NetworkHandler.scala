package nl.ritogames.networkhandler

import java.io.{BufferedReader, InputStreamReader}
import java.net.{Socket, URL}
import java.nio.file.Paths
import java.time.temporal.{ChronoUnit, Temporal}
import java.util.Base64

import javax.inject.{Inject, Singleton}
import nl.ritogames.networkhandler.connection.factory.ConnectionFactory
import nl.ritogames.networkhandler.connection.{Connection, ServerConnection}
import nl.ritogames.networkhandler.exception.{LatencyNotFoundException, PacketDataInvalidException, PeerNotFoundException}
import nl.ritogames.networkhandler.packet.factory.{LatencyFactory, PacketFactory, PeerFactory}
import nl.ritogames.networkhandler.packet.{Latency, Packet, Peer}
import nl.ritogames.networkhandler.wrapper.time.{OffsetDateTimeWrapper, Time}
import nl.ritogames.shared._
import nl.ritogames.shared.dto.command.Command
import nl.ritogames.shared.dto.command.menu.StartGameCommand
import nl.ritogames.shared.dto.command.individual.{ClearGameCommand, StartAgentCommand, StopGameCommand, UpdatePeerOnJoinCommand}
import nl.ritogames.shared.dto.event._
import nl.ritogames.shared.dto.{ChatMessage, Packageable}
import nl.ritogames.shared.exception.PlayerHasDiedException
import nl.ritogames.shared.logger.Logger

@Singleton
class NetworkHandler @Inject()(
                                fileRepository: FileRepository = null,
                                eventProcessor: EventProcessor = null,
                                commandExecutor: CommandExecutor = null,
                              ) extends EventSender with MessageSender with CommandSender {

  var connectionFactory: ConnectionFactory = new ConnectionFactory
  var peerFactory: PeerFactory = new PeerFactory
  var packetFactory: PacketFactory = new PacketFactory
  var latencyFactory: LatencyFactory = new LatencyFactory
  var time: Time = new OffsetDateTimeWrapper
  val SWITCH_IP: String = "145.74.104.79"
  var serverConnection: ServerConnection = _
  var mode: NetworkMode.Value = NetworkMode.SOFTWARE_SWITCH
  var connections: Array[Connection] = Array[Connection]()
  var peers: Array[Peer] = Array[Peer]()
  var latencies: Array[Latency] = Array[Latency]()
  val AGENT_DIRECTORY_NAME = "/data/agents/"
  val AGENT_SOURCE_FORMAT: String = AGENT_DIRECTORY_NAME + "%s.json"
  // We use this website to find our public ip
  // Originates from: https://stackoverflow.com/questions/38392549/get-public-ip-address-of-the-current-machine-using-scala
  var SELF_IP: String = {
    val whatIsMyIP = new URL("http://checkip.amazonaws.com")
    val in: BufferedReader = new BufferedReader(new InputStreamReader(
      whatIsMyIP.openStream()))
    in.readLine()
  }

  override def sendEvent(event: InteractionEvent): Unit = {
    Logger.logMethodCall(this)
    if (isHost) {
      eventProcessor.handleEvent(event)
    } else {
      event match {
        case gameJoinEvent: GameJoinEvent => gameJoinEvent.setAgentJSON(getAgentJSON(gameJoinEvent.getAgentName)); gameJoinEvent.encodeAgentJSON()
        case _ =>
      }
      sendPacket(event)
    }
  }

  override def sendCommand(command: Command): Unit = {
    if(!command.isInstanceOf[StartAgentCommand]) sendPacket(command)
    handleCommand(command)
    if (command.isInstanceOf[StopGameCommand]) {
      handleStopGameCommand()
    }
  }

  override def sendMessage(message: ChatMessage): Unit = sendPacket(message)

  private def sendPacket(packages: Packageable, peers: Array[Peer] = peers.filter(peer => peer.isHost != isHost)): Unit = {
    Logger.logMethodCall(this)
    if (!packages.isInstanceOf[StartGameCommand]) {
      peers.foreach(peer => peer.agentJSON = "")
    }
    val packet = packetFactory.getPacket(packages, SELF_IP, peers, latencies)
    connections.foreach(connection => connection.sendPacket(packet))
  }

  override def joinSession(ip: String, individualId: String): Unit = {
    markAsClient()
    var connection: Connection = null
    if (isUsingSwitch) {
      connection = connectionFactory.getConnection(SWITCH_IP, isHost = false, onReceivePacket)
    } else {
      connection = connectionFactory.getConnection(ip, isHost = true, onReceivePacket)
    }
    connection.start()
    addConnection(connection)
    addPeer(peerFactory.getHost(ip, individualId, ""))
    addLatency(latencyFactory.getLatency(ip))
  }

  override def createSession(sessionName: String, individualId: String, agentName: String): Unit = {
    Logger.logMethodCall(this)
    if (isUsingSwitch) {
      val connection = connectionFactory.getConnection(SWITCH_IP, isHost = false, onReceivePacket)
      connection.start()
      addConnection(connection)
    } else {
      if (serverConnection != null) {
        serverConnection.close()
      }
      serverConnection = connectionFactory.getServerConnection(onAccept = onAcceptSocket)
      serverConnection.start()
    }
    markAsHost()
    addPeer(peerFactory.getHost(SELF_IP, individualId, Base64.getEncoder.encodeToString(getAgentJSON(agentName).getBytes())))
    addLatency(latencyFactory.getLatency(SELF_IP))
  }

  override def stopGame(stopGameEvent: StopGameEvent): Unit = {
    eventProcessor.handleEvent(stopGameEvent)
  }

  def onAcceptSocket(socket: Socket): Unit = {
    onAcceptSocket(socket.getInetAddress.getHostAddress)
  }

  def onAcceptSocket(ip: String): Unit = {
    if (!isUsingSwitch) {
      addConnection(connectionFactory.getConnection(ip, isHost = false, onReceivePacket))
    }
  }

  def onReceivePacket(packet: Packet): Unit = {
    updateRecipientsList(packet)
    if (packet.transmitter == SELF_IP) {
      if (!packet.data.isInstanceOf[ChangeHostEvent]) {
        processNoResponseList(packet.no_response)
      }
    } else {
      packet.data match {
        case gameJoinEvent: GameJoinEvent => handleGameJoinEvent(packet)
        case changeHostEvent: ChangeHostEvent => handleChangeHostEvent(changeHostEvent)
//        case updatePeerOnJoinCommand: UpdatePeerOnJoinCommand => updatePeerOnJoinCommand.decodedAgentJSON(); handlePeerListUpdateEvent(updatePeerOnJoinCommand)
        case event: InteractionEvent => if (isHost) eventProcessor.handleEvent(event)
        case stopGameCommand: StopGameCommand => commandExecutor.executeCommand(stopGameCommand); handleStopGameCommand()
        case command: Command => handleCommand(command)
        case _ => throw PacketDataInvalidException()
      }
      updateLatencyList(packet)
    }
  }

  def handleCommand(command: Command): Unit = {
    try {
      commandExecutor.executeCommand(command)
    } catch {
      case e: PlayerHasDiedException =>
        if (e.getIndividualId == peers.find(peer => peer.ip == SELF_IP).get.individualId) {
          handleStopGameCommand()
          commandExecutor.executeCommand(new ClearGameCommand)
        }
    }
  }

  def updateRecipientsList(packet: Packet): Unit = {
    packet.recipients.foreach(recipient => {
      val peer = peers.find(peer => peer.ip == recipient.ip)
      if (peer.isEmpty) {
        addPeer(recipient)
      }
    })
  }

  def updateLatencyList(packet: Packet): Unit = {
    if (isHost) {
      val latency = latencies.find(latency => latency.ip.equals(packet.transmitter)).getOrElse(throw new LatencyNotFoundException)
      latency.latency = calculateDifference(time.now(), time.parse(packet.timestamp))
    } else {
      latencies = packet.latency_list
    }
  }

  def handleGameJoinEvent(packet: Packet): Unit = {
    if (!isHost) return
    addPeer(peerFactory.getClient(packet.transmitter, packet.data.asInstanceOf[GameJoinEvent].getIndividualId, packet.data.asInstanceOf[GameJoinEvent].getAgentJSON))
    addLatency(Latency(packet.transmitter))
    eventProcessor.handleEvent(packet.data.asInstanceOf[InteractionEvent])
  }

  def handleChangeHostEvent(changeHostEvent: ChangeHostEvent): Unit = {
    eventProcessor.handleEvent(changeHostEvent)
    if (changeHostEvent.getNewHostIp != SELF_IP) {
      changeHost(changeHostEvent.getNewHostIp)
    } else {
      markAsHost()
    }
  }

//  def handlePeerListUpdateEvent(updatePeerOnJoinCommand: UpdatePeerOnJoinCommand): Unit = {
//    var tempPeers = Array[Peer]()
//    val tempIsHost = updatePeerOnJoinCommand.getIsHost
//    val tempIp = updatePeerOnJoinCommand.getIp
//    val tempIndividualId = updatePeerOnJoinCommand.getIndividualIDs
//    val tempAgentJson = updatePeerOnJoinCommand.getAgentJSONs
//
//    for (i <- 0 until tempIsHost.size()) {
//      tempPeers = tempPeers :+ Peer(tempIsHost.get(i), tempIp.get(i), tempIndividualId.get(i), tempAgentJson.get(i))
//    }
//    peers = tempPeers
//    commandExecutor.executeCommand(updatePeerOnJoinCommand)
//  }

  def handleStopGameCommand(): Unit = {
    connections.foreach(connection => connection.disconnect())
    if (serverConnection != null) serverConnection.close()

    connections = Array[Connection]()
    peers = Array[Peer]()
    latencies = Array[Latency]()
  }

  private def processNoResponseList(no_response: Array[String]): Unit = {
    no_response.foreach(ip => {
      val hostPeer = peers.find(peer => peer.ip == ip && peer.isHost).orNull
      if (hostPeer != null) {
        val ipWithLowestLatency: String = getIpWithLowestLatency
        val changeHostEvent: ChangeHostEvent = new ChangeHostEvent(ipWithLowestLatency, peers.find(peer => peer.ip.equals(ipWithLowestLatency)).get.individualId)
        handleChangeHostEvent(changeHostEvent)
        sendPacket(changeHostEvent, peers)
      }
      activateAgent(peers.find(peer => peer.ip.equals(ip)).getOrElse(throw PeerNotFoundException("Peer not found")))
    })
  }

  def createUpdatePeerOnJoinCommand(): UpdatePeerOnJoinCommand = {
    val updatePeerOnJoinCommand = new UpdatePeerOnJoinCommand()
    peers.foreach(peer => {
      updatePeerOnJoinCommand.addIsHost(peer.isHost);
      updatePeerOnJoinCommand.addInvidualId(peer.individualId);
      updatePeerOnJoinCommand.addIp(peer.ip)
      updatePeerOnJoinCommand.addAgentJSON(peer.agentJSON)
    })
    updatePeerOnJoinCommand
  }

  private def calculateDifference(timeA: Temporal, timeB: Temporal): Int = {
    math.abs(ChronoUnit.MILLIS.between(timeA, timeB)).toInt
  }

  def addConnection(connection: Connection): Unit = {
    Logger.logMethodCall(this)
    connections = connections :+ connection
  }

  def addPeer(peer: Peer): Unit = {
    Logger.logMethodCall(this)
    peers = peers :+ peer
  }

  def addLatency(latency: Latency): Unit = {
    Logger.logMethodCall(this)
    latencies = latencies :+ latency
  }

  private def isUsingSwitch: Boolean = {
    Logger.logMethodCall(this)
    mode == NetworkMode.SOFTWARE_SWITCH | mode == NetworkMode.HOST_SOFTWARE_SWITCH
  }

  private def isHost: Boolean = {
    mode == NetworkMode.HOST_SOFTWARE_SWITCH | mode == NetworkMode.HOST_PEER_TO_PEER
  }

  def activateAgent(peer: Peer): Unit = {
    eventProcessor.handleEvent(new StartAgentEvent(peer.individualId))
  }

  private def getIpWithLowestLatency: String = {
    var ipWithLowestLatency = latencies(0)
    latencies.foreach(latency => if (ipWithLowestLatency.latency > latency.latency) ipWithLowestLatency = latency)
    ipWithLowestLatency.ip
  }

  private def changeHost(newHost: String): Unit = {
    peers.find(peer => peer.isHost).get.isHost = false
    peers.find(peer => peer.ip == newHost).get.isHost = true
  }

  private def markAsHost(): Unit = {
    mode match {
      case NetworkMode.PEER_TO_PEER => mode = NetworkMode.HOST_PEER_TO_PEER
      case NetworkMode.SOFTWARE_SWITCH => mode = NetworkMode.HOST_SOFTWARE_SWITCH;
      case _ =>
    }
  }

  private def markAsClient(): Unit = {
    mode match {
      case NetworkMode.HOST_PEER_TO_PEER => mode = NetworkMode.PEER_TO_PEER
      case NetworkMode.HOST_SOFTWARE_SWITCH => mode = NetworkMode.SOFTWARE_SWITCH
      case _ =>
    }
  }

  private def getAgentJSON(agentName: String): String = {
    fileRepository.readFile(Paths.get(String.format(AGENT_SOURCE_FORMAT, agentName)))
  }

  override def registerChatMessageHandler(chatMessageHandler: ChatMessageHandler): Unit = ???
}
