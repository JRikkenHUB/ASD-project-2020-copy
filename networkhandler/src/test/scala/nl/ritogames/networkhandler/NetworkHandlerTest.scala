package nl.ritogames.networkhandler

import java.net.Socket
import java.time.OffsetDateTime

import nl.ritogames.networkhandler.connection.factory.ConnectionFactory
import nl.ritogames.networkhandler.connection.{Connection, ServerConnection}
import nl.ritogames.networkhandler.exception.{LatencyNotFoundException, PacketDataInvalidException, PeerNotFoundException}
import nl.ritogames.networkhandler.packet.factory.{LatencyFactory, PacketFactory, PeerFactory}
import nl.ritogames.networkhandler.packet.{Latency, Packet, Peer}
import nl.ritogames.networkhandler.wrapper.time.Time
import nl.ritogames.shared.dto.Packageable
import nl.ritogames.shared.dto.command.individual.{MoveCommand, StopGameCommand, UpdatePeerOnJoinCommand}
import nl.ritogames.shared.dto.event.{ChangeHostEvent, CompileEvent, CreateGameEvent, Event, GameJoinEvent, MoveEvent, StopGameEvent}
import nl.ritogames.shared.{CommandExecutor, EventProcessor, FileRepository}
import nl.ritogames.shared.dto.event.{ChangeHostEvent, CompileEvent, CreateGameEvent, Event, GameJoinEvent, MoveEvent, StartAgentEvent, StopGameEvent}
import nl.ritogames.shared.{CommandExecutor, EventProcessor}
import org.scalamock.scalatest.MockFactory
import org.scalatest.flatspec.AnyFlatSpec


class NetworkHandlerTest extends AnyFlatSpec with MockFactory {
  behavior of "NetworkHandler"

  "sendEvent" should "call handleEvent when host" in {
    // Arrange
    val mockedEventProcessor = mock[EventProcessor]
    val sut = new NetworkHandler(eventProcessor = mockedEventProcessor)
    sut.mode = NetworkMode.HOST_SOFTWARE_SWITCH

    // Expected
    (mockedEventProcessor.handleEvent _).expects(*).once()

    // Act
    sut.sendEvent(new CreateGameEvent())
  }

  it should "not call handleEvent when not host" in {
    // Arrange
    val mockedEventProcessor = mock[EventProcessor]
    val sut = new NetworkHandler(eventProcessor = mockedEventProcessor)
    sut.mode = NetworkMode.SOFTWARE_SWITCH

    // Expected
    (mockedEventProcessor.handleEvent _).expects(*).never()

    // Act
    sut.sendEvent(new CreateGameEvent())
  }

  "joinSession" should "call getConnection when usingSwitch with switchIP" in {
    // Arrange
    val mockedConnectionFactory = mock[ConnectionFactory]
    val mockedPeerFactory = mock[PeerFactory]
    val mockedLatencyFactory = mock[LatencyFactory]
    val mockedConnection = mock[Connection]
    val sut = new NetworkHandler()
    sut.connectionFactory = mockedConnectionFactory
    sut.peerFactory = mockedPeerFactory
    sut.latencyFactory = mockedLatencyFactory
    sut.mode = NetworkMode.SOFTWARE_SWITCH

    // Expect
    (mockedConnectionFactory.getConnection(_: String, _: Boolean, _: Packet => Unit, _: Int)).expects("145.74.104.79", *, *, *).once().returns(mockedConnection)
    (mockedPeerFactory.getHost(_: String, _: String, _: String)).expects(*, *, *).returns(mock[Peer])
    (mockedLatencyFactory.getLatency(_: String)).expects(*).returns(mock[Latency])
    (mockedConnection.start _).expects()

    // Act
    sut.joinSession("sessionName", "agentName")
  }

  it should "call getConnection when not usingSwitch" in {
    // Arrange
    val mockedConnectionFactory = mock[ConnectionFactory]
    val mockedPeerFactory = mock[PeerFactory]
    val mockedLatencyFactory = mock[LatencyFactory]
    val mockedConnection = mock[Connection]
    val sut = new NetworkHandler()
    sut.connectionFactory = mockedConnectionFactory
    sut.peerFactory = mockedPeerFactory
    sut.latencyFactory = mockedLatencyFactory
    sut.mode = NetworkMode.PEER_TO_PEER

    // Expect
    (mockedConnectionFactory.getConnection(_: String, _: Boolean, _: Packet => Unit, _: Int)).expects(*, *, *, *).once().returns(mockedConnection)
    (mockedPeerFactory.getHost(_: String, _: String, _: String)).expects(*, *, *).returns(mock[Peer])
    (mockedLatencyFactory.getLatency(_: String)).expects(*).returns(mock[Latency])
    (mockedConnection.start _).expects()

    // Act
    sut.joinSession("sessionName", "agentName")
  }

  it should "start new connection" in {
    // Arrange
    val mockedConnectionFactory = mock[ConnectionFactory]
    val mockedPeerFactory = mock[PeerFactory]
    val mockedLatencyFactory = mock[LatencyFactory]
    val mockedConnection = mock[Connection]
    val sut = new NetworkHandler()
    sut.connectionFactory = mockedConnectionFactory
    sut.peerFactory = mockedPeerFactory
    sut.latencyFactory = mockedLatencyFactory
    sut.mode = NetworkMode.SOFTWARE_SWITCH

    // Expect
    (mockedConnectionFactory.getConnection(_: String, _: Boolean, _: Packet => Unit, _: Int)).expects(*, *, *, *).returns(mockedConnection)
    (mockedPeerFactory.getHost(_: String, _: String, _: String)).expects(*, *, *).returns(mock[Peer])
    (mockedLatencyFactory.getLatency(_: String)).expects(*).returns(mock[Latency])
    (mockedConnection.start _).expects().once()

    // Act
    sut.joinSession("sessionName", "agentName")
  }

  it should "add a connection to connections" in {
    // Arrange
    val mockedConnectionFactory = mock[ConnectionFactory]
    val mockedPeerFactory = mock[PeerFactory]
    val mockedLatencyFactory = mock[LatencyFactory]
    val mockedConnection = mock[Connection]
    val sut = new NetworkHandler()
    sut.connectionFactory = mockedConnectionFactory
    sut.peerFactory = mockedPeerFactory
    sut.latencyFactory = mockedLatencyFactory
    sut.mode = NetworkMode.SOFTWARE_SWITCH
    val expected = sut.connections.length + 1

    // Expect
    (mockedConnectionFactory.getConnection(_: String, _: Boolean, _: Packet => Unit, _: Int)).expects(*, *, *, *).returns(mockedConnection)
    (mockedPeerFactory.getHost(_: String, _: String, _: String)).expects(*, *, *).returns(mock[Peer])
    (mockedLatencyFactory.getLatency(_: String)).expects(*).returns(mock[Latency])
    (mockedConnection.start _).expects()

    // Act
    sut.joinSession("sessionName", "agentName")
    val actual = sut.connections.length

    // Assert
    assertResult(expected)(actual)
  }

  it should "add a peer to peers" in {
    // Arrange
    val mockedConnectionFactory = mock[ConnectionFactory]
    val mockedPeerFactory = mock[PeerFactory]
    val mockedLatencyFactory = mock[LatencyFactory]
    val mockedConnection = mock[Connection]
    val sut = new NetworkHandler()
    sut.connectionFactory = mockedConnectionFactory
    sut.peerFactory = mockedPeerFactory
    sut.latencyFactory = mockedLatencyFactory
    sut.mode = NetworkMode.SOFTWARE_SWITCH
    val expected = sut.peers.length + 1

    // Expect
    (mockedConnectionFactory.getConnection(_: String, _: Boolean, _: Packet => Unit, _: Int)).expects(*, *, *, *).returns(mockedConnection)
    (mockedPeerFactory.getHost(_: String, _: String, _: String)).expects(*, *, *).returns(mock[Peer])
    (mockedLatencyFactory.getLatency(_: String)).expects(*).returns(mock[Latency])
    (mockedConnection.start _).expects()

    // Act
    sut.joinSession("sessionName", "agentName")
    val actual = sut.peers.length

    // Assert
    assertResult(expected)(actual)
  }

  it should "add a latency to latencies" in {
    // Arrange
    val mockedConnectionFactory = mock[ConnectionFactory]
    val mockedPeerFactory = mock[PeerFactory]
    val mockedLatencyFactory = mock[LatencyFactory]
    val mockedConnection = mock[Connection]
    val sut = new NetworkHandler()
    sut.connectionFactory = mockedConnectionFactory
    sut.peerFactory = mockedPeerFactory
    sut.latencyFactory = mockedLatencyFactory
    sut.mode = NetworkMode.SOFTWARE_SWITCH
    val expected = sut.latencies.length + 1

    // Expect
    (mockedConnectionFactory.getConnection(_: String, _: Boolean, _: Packet => Unit, _: Int)).expects(*, *, *, *).returns(mockedConnection)
    (mockedPeerFactory.getHost(_: String, _: String, _: String)).expects(*, *, *).returns(mock[Peer])
    (mockedLatencyFactory.getLatency(_: String)).expects(*).returns(mock[Latency])
    (mockedConnection.start _).expects()

    // Act
    sut.joinSession("sessionName", "agentName")
    val actual = sut.latencies.length

    // Assert
    assertResult(expected)(actual)
  }

  "stopGame" should "call disconnect" in {
    // Arrange
    val mockedEventProcessor = mock[EventProcessor]
    val sut = new NetworkHandler(eventProcessor = mockedEventProcessor)
    val stopGameEvent = new StopGameEvent()

    // Expect
    (mockedEventProcessor.handleEvent _).expects(stopGameEvent).once()

    // Act
    sut.stopGame(stopGameEvent)
  }

  "createSession" should "set mode host when using switch" in {
    // Arrange
    val mockedConnectionFactory = mock[ConnectionFactory]
    val mockedPeerFactory = mock[PeerFactory]
    val mockedLatencyFactory = mock[LatencyFactory]
    val mockedConnection = mock[Connection]
    val mockedFileRepository = mock[FileRepository]
    val sut = new NetworkHandler(mockedFileRepository)
    sut.connectionFactory = mockedConnectionFactory
    sut.peerFactory = mockedPeerFactory
    sut.latencyFactory = mockedLatencyFactory
    sut.mode = NetworkMode.SOFTWARE_SWITCH
    val expected = NetworkMode.HOST_SOFTWARE_SWITCH

    // Expect
    (mockedConnectionFactory.getConnection(_: String, _: Boolean, _: Packet => Unit, _: Int)).expects(*, *, *, *).returns(mockedConnection)
    (mockedPeerFactory.getHost(_: String, _: String, _: String)).expects(*, *, *).returns(mock[Peer])
    (mockedLatencyFactory.getLatency(_: String)).expects(*).returns(mock[Latency])
    (mockedConnection.start _).expects()
    (mockedFileRepository.readFile _).expects(*).returning("agentJSON").once()

    // Act
    sut.createSession("sessionName", "individualId", "agentName")
    val actual = sut.mode

    // Assert
    assertResult(expected)(actual)
  }

  it should "call getConnection" in {
    // Arrange
    val mockedConnectionFactory = mock[ConnectionFactory]
    val mockedPeerFactory = mock[PeerFactory]
    val mockedLatencyFactory = mock[LatencyFactory]
    val mockedConnection = mock[Connection]
    val mockedFileRepository = mock[FileRepository]
    val sut = new NetworkHandler(mockedFileRepository)
    sut.connectionFactory = mockedConnectionFactory
    sut.peerFactory = mockedPeerFactory
    sut.latencyFactory = mockedLatencyFactory
    sut.mode = NetworkMode.SOFTWARE_SWITCH

    // Expect
    (mockedConnectionFactory.getConnection(_: String, _: Boolean, _: Packet => Unit, _: Int)).expects(*, *, *, *).returns(mockedConnection).once()
    (mockedPeerFactory.getHost(_: String, _: String, _: String)).expects(*, *, *).returns(mock[Peer])
    (mockedLatencyFactory.getLatency(_: String)).expects(*).returns(mock[Latency])
    (mockedConnection.start _).expects()
    (mockedFileRepository.readFile _).expects(*).returning("agentJSON").once()

    // Act
    sut.createSession("sessionName", "individualId", "agentName")
  }

  it should "call connection start" in {
    // Arrange
    val mockedConnectionFactory = mock[ConnectionFactory]
    val mockedPeerFactory = mock[PeerFactory]
    val mockedLatencyFactory = mock[LatencyFactory]
    val mockedConnection = mock[Connection]
    val mockedFileRepository = mock[FileRepository]
    val sut = new NetworkHandler(mockedFileRepository)
    sut.connectionFactory = mockedConnectionFactory
    sut.peerFactory = mockedPeerFactory
    sut.latencyFactory = mockedLatencyFactory
    sut.mode = NetworkMode.SOFTWARE_SWITCH

    // Expect
    (mockedConnectionFactory.getConnection(_: String, _: Boolean, _: Packet => Unit, _: Int)).expects(*, *, *, *).returns(mockedConnection)
    (mockedPeerFactory.getHost(_: String, _: String, _: String)).expects(*, *, *).returns(mock[Peer])
    (mockedLatencyFactory.getLatency(_: String)).expects(*).returns(mock[Latency])
    (mockedConnection.start _).expects().once()
    (mockedFileRepository.readFile _).expects(*).returning("agentJSON").once()

    // Act
    sut.createSession("sessionName", "individualId", "agentName")
  }

  it should "add connection to connections" in {
    // Arrange
    val mockedConnectionFactory = mock[ConnectionFactory]
    val mockedPeerFactory = mock[PeerFactory]
    val mockedLatencyFactory = mock[LatencyFactory]
    val mockedConnection = mock[Connection]
    val mockedFileRepository = mock[FileRepository]
    val sut = new NetworkHandler(mockedFileRepository)
    sut.connectionFactory = mockedConnectionFactory
    sut.peerFactory = mockedPeerFactory
    sut.latencyFactory = mockedLatencyFactory
    sut.mode = NetworkMode.SOFTWARE_SWITCH
    val expected = sut.connections.length + 1

    // Expect
    (mockedConnectionFactory.getConnection(_: String, _: Boolean, _: Packet => Unit, _: Int)).expects(*, *, *, *).returns(mockedConnection).once()
    (mockedPeerFactory.getHost(_: String, _: String, _: String)).expects(*, *, *).returns(mock[Peer])
    (mockedLatencyFactory.getLatency(_: String)).expects(*).returns(mock[Latency])
    (mockedConnection.start _).expects()
    (mockedFileRepository.readFile _).expects(*).returning("agentJSON").once()

    // Act
    sut.createSession("sessionName", "individualId", "agentName")
    val actual = sut.connections.length

    // Assert
    assertResult(expected)(actual)
  }

  it should "call close" in {
    // Arrange
    val mockedConnectionFactory = mock[ConnectionFactory]
    val mockedPeerFactory = mock[PeerFactory]
    val mockedLatencyFactory = mock[LatencyFactory]
    val mockedServerConnection = mock[ServerConnection]
    val mockedFileRepository = mock[FileRepository]
    val sut = new NetworkHandler(mockedFileRepository)
    sut.connectionFactory = mockedConnectionFactory
    sut.serverConnection = mockedServerConnection
    sut.peerFactory = mockedPeerFactory
    sut.latencyFactory = mockedLatencyFactory
    sut.mode = NetworkMode.PEER_TO_PEER

    // Expect
    (mockedServerConnection.close _).expects().once()
    (mockedConnectionFactory.getServerConnection(_: Int, _: Socket => Unit)).expects(*, *).returns(mockedServerConnection)
    (mockedPeerFactory.getHost(_: String, _: String, _: String)).expects(*, *, *).returns(mock[Peer])
    (mockedLatencyFactory.getLatency(_: String)).expects(*).returns(mock[Latency])
    (mockedServerConnection.start _).expects()
    (mockedFileRepository.readFile _).expects(*).returning("agentJSON").once()

    // Act
    sut.createSession("sessionName", "individualId", "agentName")
  }

  it should "set mode host when not using switch" in {
    // Arrange
    val mockedConnectionFactory = mock[ConnectionFactory]
    val mockedPeerFactory = mock[PeerFactory]
    val mockedLatencyFactory = mock[LatencyFactory]
    val mockedServerConnection = mock[ServerConnection]
    val mockedFileRepository = mock[FileRepository]
    val sut = new NetworkHandler(mockedFileRepository)
    sut.connectionFactory = mockedConnectionFactory
    sut.peerFactory = mockedPeerFactory
    sut.latencyFactory = mockedLatencyFactory
    sut.mode = NetworkMode.PEER_TO_PEER
    val expected = NetworkMode.HOST_PEER_TO_PEER

    // Expect
    (mockedConnectionFactory.getServerConnection(_: Int, _: Socket => Unit)).expects(*, *).returns(mockedServerConnection)
    (mockedPeerFactory.getHost(_: String, _: String, _: String)).expects(*, *, *).returns(mock[Peer])
    (mockedLatencyFactory.getLatency(_: String)).expects(*).returns(mock[Latency])
    (mockedServerConnection.start _).expects()
    (mockedFileRepository.readFile _).expects(*).returning("agentJSON").once()

    // Act
    sut.createSession("sessionName", "individualId", "agentName")
    val actual = sut.mode

    // Assert
    assertResult(expected)(actual)
  }

  it should "call getServerConnection" in {
    // Arrange
    val mockedConnectionFactory = mock[ConnectionFactory]
    val mockedPeerFactory = mock[PeerFactory]
    val mockedLatencyFactory = mock[LatencyFactory]
    val mockedServerConnection = mock[ServerConnection]
    val mockedFileRepository = mock[FileRepository]
    val sut = new NetworkHandler(mockedFileRepository)
    sut.connectionFactory = mockedConnectionFactory
    sut.peerFactory = mockedPeerFactory
    sut.latencyFactory = mockedLatencyFactory
    sut.mode = NetworkMode.PEER_TO_PEER

    // Expect
    (mockedConnectionFactory.getServerConnection(_: Int, _: Socket => Unit)).expects(*, *).returns(mockedServerConnection).once()
    (mockedPeerFactory.getHost(_: String, _: String, _: String)).expects(*, *, *).returns(mock[Peer])
    (mockedLatencyFactory.getLatency(_: String)).expects(*).returns(mock[Latency])
    (mockedServerConnection.start _).expects()
    (mockedFileRepository.readFile _).expects(*).returning("agentJSON").once()

    // Act
    sut.createSession("sessionName", "individualId", "agentName")
  }

  it should "call serverconnection start" in {
    // Arrange
    val mockedConnectionFactory = mock[ConnectionFactory]
    val mockedPeerFactory = mock[PeerFactory]
    val mockedLatencyFactory = mock[LatencyFactory]
    val mockedServerConnection = mock[ServerConnection]
    val mockedFileRepository = mock[FileRepository]
    val sut = new NetworkHandler(mockedFileRepository)
    sut.connectionFactory = mockedConnectionFactory
    sut.peerFactory = mockedPeerFactory
    sut.latencyFactory = mockedLatencyFactory
    sut.mode = NetworkMode.PEER_TO_PEER

    // Expect
    (mockedConnectionFactory.getServerConnection(_: Int, _: Socket => Unit)).expects(*, *).returns(mockedServerConnection)
    (mockedPeerFactory.getHost(_: String, _: String, _: String)).expects(*, *, *).returns(mock[Peer])
    (mockedLatencyFactory.getLatency(_: String)).expects(*).returns(mock[Latency])
    (mockedServerConnection.start _).expects().once()
    (mockedFileRepository.readFile _).expects(*).returning("agentJSON").once()

    // Act
    sut.createSession("sessionName", "individualId", "agentName")
  }

  it should "add peer to peers" in {
    // Arrange
    val mockedConnectionFactory = mock[ConnectionFactory]
    val mockedPeerFactory = mock[PeerFactory]
    val mockedLatencyFactory = mock[LatencyFactory]
    val mockedServerConnection = mock[ServerConnection]
    val mockedFileRepository = mock[FileRepository]
    val sut = new NetworkHandler(mockedFileRepository)
    sut.connectionFactory = mockedConnectionFactory
    sut.peerFactory = mockedPeerFactory
    sut.latencyFactory = mockedLatencyFactory
    sut.mode = NetworkMode.PEER_TO_PEER
    val expected = sut.peers.length + 1

    // Expect
    (mockedConnectionFactory.getServerConnection(_: Int, _: Socket => Unit)).expects(*, *).returns(mockedServerConnection)
    (mockedPeerFactory.getHost(_: String, _: String, _: String)).expects(*, *, *).returns(mock[Peer])
    (mockedLatencyFactory.getLatency(_: String)).expects(*).returns(mock[Latency])
    (mockedServerConnection.start _).expects()
    (mockedFileRepository.readFile _).expects(*).returning("agentJSON").once()

    // Act
    sut.createSession("sessionName", "individualId", "agentName")
    val actual = sut.peers.length

    // Assert
    assertResult(expected)(actual)
  }

  it should "add latency to latencies" in {
    // Arrange
    val mockedConnectionFactory = mock[ConnectionFactory]
    val mockedPeerFactory = mock[PeerFactory]
    val mockedLatencyFactory = mock[LatencyFactory]
    val mockedServerConnection = mock[ServerConnection]
    val mockedFileRepository = mock[FileRepository]
    val sut = new NetworkHandler(mockedFileRepository)
    sut.connectionFactory = mockedConnectionFactory
    sut.peerFactory = mockedPeerFactory
    sut.latencyFactory = mockedLatencyFactory
    sut.mode = NetworkMode.PEER_TO_PEER
    val expected = sut.latencies.length + 1

    // Expect
    (mockedConnectionFactory.getServerConnection(_: Int, _: Socket => Unit)).expects(*, *).returns(mockedServerConnection)
    (mockedPeerFactory.getHost(_: String, _: String, _: String)).expects(*, *, *).returns(mock[Peer])
    (mockedLatencyFactory.getLatency(_: String)).expects(*).returns(mock[Latency])
    (mockedServerConnection.start _).expects()
    (mockedFileRepository.readFile _).expects(*).returning("agentJSON").once()

    // Act
    sut.createSession("sessionName", "individualId", "agentName")
    val actual = sut.latencies.length

    // Assert
    assertResult(expected)(actual)
  }

  "onAcceptSocket" should "not call getConnection" in {
    // Arrange
    val mockedConnectionFactory = mock[ConnectionFactory]
    val sut = new NetworkHandler()
    sut.connectionFactory = mockedConnectionFactory
    sut.mode = NetworkMode.SOFTWARE_SWITCH

    // Expect
    (mockedConnectionFactory.getConnection(_: String, _: Boolean, _: Packet => Unit, _: Int)).expects(*, *, *, *).never()

    // Act
    sut.onAcceptSocket("0.0.0.0")
  }

  it should "call getConnection" in {
    // Arrange
    val mockedConnectionFactory = mock[ConnectionFactory]
    val sut = new NetworkHandler()
    sut.connectionFactory = mockedConnectionFactory
    sut.mode = NetworkMode.PEER_TO_PEER

    // Expect
    (mockedConnectionFactory.getConnection(_: String, _: Boolean, _: Packet => Unit, _: Int)).expects(*, *, *, *).once()

    // Act
    sut.onAcceptSocket("0.0.0.0")
  }

  "onReceivePacket" should "not call handleEvent when transmitter is you" in {
    // Arrange
    val mockedEventprocessor = mock[EventProcessor]
    val sut = new NetworkHandler(eventProcessor = mockedEventprocessor)
    val ip = "0.0.0.0"
    val individualId = "Barrie"
    val agentJSON = "agentJSON"
    sut.SELF_IP = ip
    val packet = Packet(new MoveEvent(), ip, "time", Array(Latency(ip)), Array[String](), Array(Peer(false, ip, individualId, agentJSON)))

    // Expected
    (mockedEventprocessor.handleEvent _).expects(*).never()

    // Act
    sut.onReceivePacket(packet)
  }

  it should "not call executeCommand when transmitter is you" in {
    // Arrange
    val mockedCommandExecutor = mock[CommandExecutor]
    val sut = new NetworkHandler(commandExecutor = mockedCommandExecutor)
    val ip = "0.0.0.0"
    val individualId = "Barrie"
    val agentJSON = "agentJSON"
    sut.SELF_IP = ip
    val packet = Packet(new MoveCommand(), ip, "time", Array(Latency(ip)), Array[String](), Array(Peer(false, ip, individualId, agentJSON)))

    // Expected
    (mockedCommandExecutor.executeCommand _).expects(*).never()

    // Act
    sut.onReceivePacket(packet)
  }

  it should "call handleEvent when transmitter is not you" in {
    // Arrange
    val mockedEventprocessor = mock[EventProcessor]
    val sut = new NetworkHandler(eventProcessor = mockedEventprocessor)
    val ip = "0.0.0.0"
    val timestamp = "2021-01-10T10:00:00.000000000+01:00"
    val individualId = "Barrie"
    val agentJSON = "agentJSON"
    sut.latencies = Array(Latency(ip))
    sut.SELF_IP = "0.0.0.1"
    sut.mode = NetworkMode.HOST_SOFTWARE_SWITCH
    val packet = Packet(new MoveEvent(), ip, timestamp, Array(Latency(ip)), Array(""), Array(Peer(false, ip, individualId, agentJSON)))

    // Expected
    (mockedEventprocessor.handleEvent _).expects(*).once()

    // Act
    sut.onReceivePacket(packet)
  }

  it should "call executeCommand when transmitter is not you with a command" in {
    // Arrange
    val mockedCommandExecutor = mock[CommandExecutor]
    val sut = new NetworkHandler(commandExecutor = mockedCommandExecutor)
    val ip = "0.0.0.0"
    val timestamp = "2021-01-10T10:00:00.000000000+01:00"
    sut.latencies = Array(Latency(ip))
    sut.SELF_IP = "0.0.0.1"
    val individualId = "Barrie"
    val agentJSON = "agentJSON"
    val packet = Packet(new MoveCommand(), ip, timestamp, Array(Latency(ip)), Array(""), Array(Peer(false, ip, individualId, agentJSON)))

    // Expected
    (mockedCommandExecutor.executeCommand _).expects(*).once()

    // Act
    sut.onReceivePacket(packet)
  }

  it should "call executeCommand when transmitter is not you with a stopgamecommand" in {
    // Arrange
    val mockedCommandExecutor = mock[CommandExecutor]
    val sut = new NetworkHandler(commandExecutor = mockedCommandExecutor)
    val ip = "0.0.0.0"
    val timestamp = "2021-01-10T10:00:00.000000000+01:00"
    sut.latencies = Array(Latency(ip))
    val packet = Packet(new StopGameCommand("id"), ip, timestamp, Array(Latency("")), Array(""), Array(Peer(false, "ip", "individualId", "agentJSON")))

    // Expected
    (mockedCommandExecutor.executeCommand _).expects(*).once()

    // Act
    sut.onReceivePacket(packet)
  }


  it should "throw PacketDataInvalidException when transmitter is you and data is invalid" in {
    // Arrange
    val mockedCommandExecutor = mock[CommandExecutor]
    val sut = new NetworkHandler(commandExecutor = mockedCommandExecutor)
    val ip = "0.0.0.0"
    val timestamp = "2021-01-10T10:00:00.000000000+01:00"
    sut.latencies = Array(Latency(ip))
    sut.SELF_IP = "0.0.0.1"
    val individualId = "Barrie"
    val agentJSON = "agentJSON"
    val packet = Packet(new CompileEvent(), ip, timestamp, Array(Latency(ip)), Array(""), Array(Peer(false, ip, individualId, agentJSON)))

    // Assert
    assertThrows[PacketDataInvalidException](sut.onReceivePacket(packet))
  }

  it should "call the eventProcessor" in {
    // Arrange
    val mockedCommandExecutor = mock[CommandExecutor]
    val mockedEventProcessor = mock[EventProcessor]
    val mockedPacketFactory = mock[PacketFactory]
    val sut = new NetworkHandler(commandExecutor = mockedCommandExecutor, eventProcessor = mockedEventProcessor)
    val ip1 = "10.0.0.1"
    val ip2 = "10.0.0.2"
    val ip3 = "10.0.0.3"
    val isHost1 = false
    val isHost2 = true
    val isHost3 = false
    val latency1 = 2
    val latency2 = 99999
    val latency3 = 1
    sut.SELF_IP = ip1
    sut.packetFactory = mockedPacketFactory
    //-- Add mocked connections
    val connection1 = mock[Connection]
    connection1.isHost = isHost1
    connection1.ip = ip1
    connection1.latency = latency1
    val connection2 = mock[Connection]
    connection2.isHost = isHost2
    connection2.ip = ip2
    connection2.latency = latency2
    val connection3 = mock[Connection]
    connection3.isHost = isHost3
    connection3.ip = ip3
    connection3.latency = latency3
    sut.addConnection(connection1)
    sut.addConnection(connection2)
    sut.addConnection(connection3)
    //-- Add mocked Peers
    sut.addPeer(Peer(isHost = isHost1, ip1, "", "agentJSON"))
    sut.addPeer(Peer(isHost = isHost2, ip2, "", "agentJSON"))
    sut.addPeer(Peer(isHost = isHost3, ip3, "", "agentJSON"))
    //-- Add mocked peers
    sut.addLatency(Latency(ip1, latency1))
    sut.addLatency(Latency(ip2, latency2))
    sut.addLatency(Latency(ip3, latency3))
    //-- Create expected data
    val changeHostEvent: ChangeHostEvent = new ChangeHostEvent(ip3, "", "")
    val changeHostEventPacket: Packet = Packet(changeHostEvent, ip1, "", Array[Latency](), no_response = Array(ip2), Array[Peer]())
    val packet: Packet = Packet(new GameJoinEvent(ip1), ip1, "", Array[Latency](), no_response = Array(ip2), Array[Peer]())

    // Expect
    (mockedPacketFactory.getPacket _).expects(*, *, *, *).returns(changeHostEventPacket)
    (connection3.sendPacket _).expects(changeHostEventPacket)
    (connection2.sendPacket _).expects(changeHostEventPacket)
    (connection1.sendPacket _).expects(changeHostEventPacket)
    (mockedEventProcessor.handleEvent _).expects(*).twice()

    // Act
    sut.onReceivePacket(packet)
  }

  it should "make the correct connection host when the host is in the noResponse list" in {
    // Arrange
    val mockedCommandExecutor = mock[CommandExecutor]
    val mockedEventProcessor = mock[EventProcessor]
    val mockedPacketFactory = mock[PacketFactory]
    val sut = new NetworkHandler(commandExecutor = mockedCommandExecutor, eventProcessor = mockedEventProcessor)
    val ip1 = "10.0.0.1"
    val ip2 = "10.0.0.2"
    val ip3 = "10.0.0.3"
    val isHost1 = false
    val isHost2 = true
    val isHost3 = false
    val latency1 = 2
    val latency2 = 99999
    val latency3 = 1
    sut.SELF_IP = ip1
    sut.packetFactory = mockedPacketFactory
    //-- Add mocked connections
    val connection1 = mock[Connection]
    connection1.isHost = isHost1
    connection1.ip = ip1
    connection1.latency = latency1
    val connection2 = mock[Connection]
    connection2.isHost = isHost2
    connection2.ip = ip2
    connection2.latency = latency2
    val connection3 = mock[Connection]
    connection3.isHost = isHost3
    connection3.ip = ip3
    connection3.latency = latency3
    sut.addConnection(connection1)
    sut.addConnection(connection2)
    sut.addConnection(connection3)
    //-- Add mocked Peers
    val peer1 = Peer(isHost = isHost1, ip1, "", "agentJSON")
    val peer2 = Peer(isHost = isHost2, ip2, "", "agentJSON")
    val peer3 = Peer(isHost = isHost3, ip3, "", "agentJSON")
    sut.addPeer(peer1)
    sut.addPeer(peer2)
    sut.addPeer(peer3)
    //-- Add mocked peers
    sut.addLatency(Latency(ip1, latency1))
    sut.addLatency(Latency(ip2, latency2))
    sut.addLatency(Latency(ip3, latency3))
    //-- Create expected data
    val changeHostEvent: ChangeHostEvent = new ChangeHostEvent(ip3, "", "")
    val changeHostEventPacket: Packet = Packet(changeHostEvent, ip1, "", Array[Latency](), no_response = Array(ip2), Array[Peer]())
    val packet: Packet = Packet(new GameJoinEvent(ip1), ip1, "", Array[Latency](), no_response = Array(ip2), Array[Peer]())

    // Expect
    (mockedPacketFactory.getPacket _).expects(*, *, *, *).returns(changeHostEventPacket)
    (connection3.sendPacket _).expects(changeHostEventPacket)
    (connection2.sendPacket _).expects(changeHostEventPacket)
    (connection1.sendPacket _).expects(changeHostEventPacket)
    (mockedEventProcessor.handleEvent _).expects(*).twice()

    // Act
    sut.onReceivePacket(packet)

    // Arrange
    assert(!peer1.isHost)
    assert(peer3.isHost)
  }

  it should "change the host status of the old host when its connection is in the noResponse list" in {
    // Arrange
    val mockedCommandExecutor = mock[CommandExecutor]
    val mockedEventProcessor = mock[EventProcessor]
    val mockedPacketFactory = mock[PacketFactory]
    val sut = new NetworkHandler(commandExecutor = mockedCommandExecutor, eventProcessor = mockedEventProcessor)
    val ip1 = "10.0.0.1"
    val ip2 = "10.0.0.2"
    val ip3 = "10.0.0.3"
    val isHost1 = false
    val isHost2 = true
    val isHost3 = false
    val latency1 = 2
    val latency2 = 99999
    val latency3 = 1
    sut.SELF_IP = ip1
    sut.packetFactory = mockedPacketFactory
    //-- Add mocked connections
    val connection1 = mock[Connection]
    connection1.isHost = isHost1
    connection1.ip = ip1
    connection1.latency = latency1
    val connection2 = mock[Connection]
    connection2.isHost = isHost2
    connection2.ip = ip2
    connection2.latency = latency2
    val connection3 = mock[Connection]
    connection3.isHost = isHost3
    connection3.ip = ip3
    connection3.latency = latency3
    sut.addConnection(connection1)
    sut.addConnection(connection2)
    sut.addConnection(connection3)
    //-- Add mocked Peers
    val peer1 = Peer(isHost = isHost1, ip1, "", "agentJSON")
    val peer2 = Peer(isHost = isHost2, ip2, "", "agentJSON")
    val peer3 = Peer(isHost = isHost3, ip3, "", "agentJSON")
    sut.addPeer(peer1)
    sut.addPeer(peer2)
    sut.addPeer(peer3)
    //-- Add mocked peers
    sut.addLatency(Latency(ip1, latency1))
    sut.addLatency(Latency(ip2, latency2))
    sut.addLatency(Latency(ip3, latency3))
    //-- Create expected data
    val changeHostEvent: ChangeHostEvent = new ChangeHostEvent(ip3, "", "")
    val changeHostEventPacket: Packet = Packet(changeHostEvent, ip1, "", Array[Latency](), no_response = Array(ip2), Array[Peer]())
    val packet: Packet = Packet(new GameJoinEvent(ip1), ip1, "", Array[Latency](), no_response = Array(ip2), Array[Peer]())

    // Expect
    (mockedPacketFactory.getPacket _).expects(*, *, *, *).returns(changeHostEventPacket)
    (connection3.sendPacket _).expects(changeHostEventPacket)
    (connection2.sendPacket _).expects(changeHostEventPacket)
    (connection1.sendPacket _).expects(changeHostEventPacket)
    (mockedEventProcessor.handleEvent _).expects(*).twice()

    // Act
    sut.onReceivePacket(packet)

    // Arrange
    assert(!peer2.isHost)
  }

  it should "change the mode to HSW when the newHostIp matches SELF_IP and the mode is SW" in {
    // Arrange
    val mockedCommandExecutor = mock[CommandExecutor]
    val mockedEventProcessor = mock[EventProcessor]
    val mockedPacketFactory = mock[PacketFactory]
    val sut = new NetworkHandler(commandExecutor = mockedCommandExecutor, eventProcessor = mockedEventProcessor)
    val ip1 = "10.0.0.1"
    val ip2 = "10.0.0.2"
    val ip3 = "10.0.0.3"
    val isHost1 = false
    val isHost2 = true
    val isHost3 = false
    val latency1 = 1
    val latency2 = 99999
    val latency3 = 2
    sut.SELF_IP = ip1
    sut.mode = NetworkMode.SOFTWARE_SWITCH
    sut.packetFactory = mockedPacketFactory
    //-- Add mocked connections
    val connection1 = mock[Connection]
    connection1.isHost = isHost1
    connection1.ip = ip1
    connection1.latency = latency1
    val connection2 = mock[Connection]
    connection2.isHost = isHost2
    connection2.ip = ip2
    connection2.latency = latency2
    val connection3 = mock[Connection]
    connection3.isHost = isHost3
    connection3.ip = ip3
    connection3.latency = latency3
    sut.addConnection(connection1)
    sut.addConnection(connection2)
    sut.addConnection(connection3)
    //-- Add mocked Peers
    val peer1 = Peer(isHost = isHost1, ip1, "", "agentJSON")
    val peer2 = Peer(isHost = isHost2, ip2, "", "agentJSON")
    val peer3 = Peer(isHost = isHost3, ip3, "", "agentJSON")
    sut.addPeer(peer1)
    sut.addPeer(peer2)
    sut.addPeer(peer3)
    //-- Add mocked peers
    sut.addLatency(Latency(ip1, latency1))
    sut.addLatency(Latency(ip2, latency2))
    sut.addLatency(Latency(ip3, latency3))
    //-- Create expected data
    val changeHostEvent: ChangeHostEvent = new ChangeHostEvent(ip1, "", "")
    val changeHostEventPacket: Packet = Packet(changeHostEvent, ip1, "", Array[Latency](), no_response = Array(ip2), Array[Peer]())
    val packet: Packet = Packet(new GameJoinEvent(ip1), ip1, "", Array[Latency](), no_response = Array(ip2), Array[Peer]())

    // Expect
    (mockedPacketFactory.getPacket _).expects(*, *, *, *).returns(changeHostEventPacket)
    (connection3.sendPacket _).expects(changeHostEventPacket)
    (connection2.sendPacket _).expects(changeHostEventPacket)
    (connection1.sendPacket _).expects(changeHostEventPacket)
    (mockedEventProcessor.handleEvent _).expects(*).twice()

    // Act
    sut.onReceivePacket(packet)

    // Arrange
    assert(sut.mode == NetworkMode.HOST_SOFTWARE_SWITCH)
  }

  it should "change the mode to HPTP when the newHostIp matches SELF_IP and the mode is PTP" in {
    // Arrange
    val mockedCommandExecutor = mock[CommandExecutor]
    val mockedEventProcessor = mock[EventProcessor]
    val mockedPacketFactory = mock[PacketFactory]
    val sut = new NetworkHandler(commandExecutor = mockedCommandExecutor, eventProcessor = mockedEventProcessor)
    val ip1 = "10.0.0.1"
    val ip2 = "10.0.0.2"
    val ip3 = "10.0.0.3"
    val isHost1 = false
    val isHost2 = true
    val isHost3 = false
    val latency1 = 1
    val latency2 = 99999
    val latency3 = 2
    sut.SELF_IP = ip1
    sut.mode = NetworkMode.PEER_TO_PEER
    sut.packetFactory = mockedPacketFactory
    //-- Add mocked connections
    val connection1 = mock[Connection]
    connection1.isHost = isHost1
    connection1.ip = ip1
    connection1.latency = latency1
    val connection2 = mock[Connection]
    connection2.isHost = isHost2
    connection2.ip = ip2
    connection2.latency = latency2
    val connection3 = mock[Connection]
    connection3.isHost = isHost3
    connection3.ip = ip3
    connection3.latency = latency3
    sut.addConnection(connection1)
    sut.addConnection(connection2)
    sut.addConnection(connection3)
    //-- Add mocked Peers
    val peer1 = Peer(isHost = isHost1, ip1, "", "agentJSON")
    val peer2 = Peer(isHost = isHost2, ip2, "", "agentJSON")
    val peer3 = Peer(isHost = isHost3, ip3, "", "agentJSON")
    sut.addPeer(peer1)
    sut.addPeer(peer2)
    sut.addPeer(peer3)
    //-- Add mocked peers
    sut.addLatency(Latency(ip1, latency1))
    sut.addLatency(Latency(ip2, latency2))
    sut.addLatency(Latency(ip3, latency3))
    //-- Create expected data
    val changeHostEvent: ChangeHostEvent = new ChangeHostEvent(ip1, "", "")
    val changeHostEventPacket: Packet = Packet(changeHostEvent, ip1, "", Array[Latency](), no_response = Array(ip2), Array[Peer]())
    val packet: Packet = Packet(new GameJoinEvent(ip1), ip1, "", Array[Latency](), no_response = Array(ip2), Array[Peer]())

    // Expect
    (mockedPacketFactory.getPacket _).expects(*, *, *, *).returns(changeHostEventPacket)
    (connection3.sendPacket _).expects(changeHostEventPacket)
    (connection2.sendPacket _).expects(changeHostEventPacket)
    (connection1.sendPacket _).expects(changeHostEventPacket)
    (mockedEventProcessor.handleEvent _).expects(*).twice()

    // Act
    sut.onReceivePacket(packet)

    // Arrange
    assert(sut.mode == NetworkMode.HOST_PEER_TO_PEER)
  }

  it should "do nothing if the newHostIp is the client's ip, but the client is already host" in {
    // Arrange
    val mockedCommandExecutor = mock[CommandExecutor]
    val mockedEventProcessor = mock[EventProcessor]
    val mockedPacketFactory = mock[PacketFactory]
    val sut = new NetworkHandler(commandExecutor = mockedCommandExecutor, eventProcessor = mockedEventProcessor)
    val ip1 = "10.0.0.1"
    val ip2 = "10.0.0.2"
    val ip3 = "10.0.0.3"
    val isHost1 = false
    val isHost2 = true
    val isHost3 = false
    val latency1 = 1
    val latency2 = 99999
    val latency3 = 2
    sut.SELF_IP = ip1
    sut.mode = NetworkMode.HOST_PEER_TO_PEER
    sut.packetFactory = mockedPacketFactory
    //-- Add mocked connections
    val connection1 = mock[Connection]
    connection1.isHost = isHost1
    connection1.ip = ip1
    connection1.latency = latency1
    val connection2 = mock[Connection]
    connection2.isHost = isHost2
    connection2.ip = ip2
    connection2.latency = latency2
    val connection3 = mock[Connection]
    connection3.isHost = isHost3
    connection3.ip = ip3
    connection3.latency = latency3
    sut.addConnection(connection1)
    sut.addConnection(connection2)
    sut.addConnection(connection3)
    //-- Add mocked Peers
    val peer1 = Peer(isHost = isHost1, ip1, "", "agentJSON")
    val peer2 = Peer(isHost = isHost2, ip2, "", "agentJSON")
    val peer3 = Peer(isHost = isHost3, ip3, "", "agentJSON")
    sut.addPeer(peer1)
    sut.addPeer(peer2)
    sut.addPeer(peer3)
    //-- Add mocked peers
    sut.addLatency(Latency(ip1, latency1))
    sut.addLatency(Latency(ip2, latency2))
    sut.addLatency(Latency(ip3, latency3))
    //-- Create expected data
    val changeHostEvent: ChangeHostEvent = new ChangeHostEvent(ip1, "", "")
    val changeHostEventPacket: Packet = Packet(changeHostEvent, ip1, "", Array[Latency](), no_response = Array(ip2), Array[Peer]())
    val packet: Packet = Packet(new GameJoinEvent(ip1), ip1, "", Array[Latency](), no_response = Array(ip2), Array[Peer]())

    // Expect
    (mockedPacketFactory.getPacket _).expects(*, *, *, *).returns(changeHostEventPacket)
    (connection3.sendPacket _).expects(changeHostEventPacket)
    (connection2.sendPacket _).expects(changeHostEventPacket)
    (connection1.sendPacket _).expects(changeHostEventPacket)
    (mockedEventProcessor.handleEvent _).expects(*).twice()

    // Act
    sut.onReceivePacket(packet)

    // Arrange
    assert(sut.mode == NetworkMode.HOST_PEER_TO_PEER)
  }

  "handleGameJoinEvent" should "not add peer when not host" in {
    // Arrange
    val mockedEventProcessor = mock[EventProcessor]
    val sut = new NetworkHandler(eventProcessor = mockedEventProcessor)
    sut.mode = NetworkMode.SOFTWARE_SWITCH
    val ip = "0.0.0.0"
    val individualId = "Barrie"
    val agentJSON = "agentJSON"
    val packet = Packet(new MoveEvent(), ip, "time", Array(Latency(ip)), Array(""), Array(Peer(false, ip, individualId, agentJSON)))
    val expected = sut.peers.length

    // Act
    sut.handleGameJoinEvent(packet)
    val actual = sut.peers.length

    // Assert
    assertResult(expected)(actual)
  }

  it should "not call handleEvent when not host" in {
    // Arrange
    val mockedEventProcessor = mock[EventProcessor]
    val sut = new NetworkHandler(eventProcessor = mockedEventProcessor)
    sut.mode = NetworkMode.SOFTWARE_SWITCH
    val ip = "0.0.0.0"
    val individualId = "Barrie"
    val agentJSON = "agentJSON"
    val packet = Packet(new MoveEvent(), ip, "time", Array(Latency(ip)), Array(""), Array(Peer(false, ip, individualId, agentJSON)))

    // Expected
    (mockedEventProcessor.handleEvent _).expects(*).never()

    // Act
    sut.handleGameJoinEvent(packet)
  }

  it should "add peer when host" in {
    // Arrange
    val mockedEventProcessor = mock[EventProcessor]
    val sut = new NetworkHandler(eventProcessor = mockedEventProcessor)
    sut.mode = NetworkMode.HOST_SOFTWARE_SWITCH
    val ip = "0.0.0.0"
    val individualId = "Barrie"
    val agentJSON = "agentJSON"
    val packet = Packet(new GameJoinEvent(ip), ip, "time", Array(Latency(ip)), Array(""), Array(Peer(false, ip, individualId, agentJSON)))
    val expected = sut.peers.length + 1

    // Expected
    (mockedEventProcessor.handleEvent _).expects(*)

    // Act
    sut.handleGameJoinEvent(packet)
    val actual = sut.peers.length

    // Assert
    assertResult(expected)(actual)
  }

  it should "call handleEvent when host" in {
    // Arrange
    val mockedEventProcessor = mock[EventProcessor]
    val sut = new NetworkHandler(eventProcessor = mockedEventProcessor)
    sut.mode = NetworkMode.HOST_SOFTWARE_SWITCH
    val ip = "0.0.0.0"
    val individualId = "Barrie"
    val agentJSON = "agentJSON"
    val packet = Packet(new GameJoinEvent(ip), ip, "time", Array(Latency(ip)), Array(""), Array(Peer(false, ip, individualId, agentJSON)))

    // Expected
    (mockedEventProcessor.handleEvent _).expects(*).once()

    // Act
    sut.handleGameJoinEvent(packet)
  }

//  "handlePeerListUpdateEvent" should "update peerlist" in {
//    // Arrange
//    val mockedEventProcessor = mock[EventProcessor]
//    val sut = new NetworkHandler(eventProcessor = mockedEventProcessor)
//    val ip = "0.0.0.0"
//    val updatePeerOnJoinEvent = new UpdatePeerOnJoinCommand()
//    updatePeerOnJoinEvent.addIsHost(false)
//    updatePeerOnJoinEvent.addIsHost(false)
//    updatePeerOnJoinEvent.addIsHost(true)
//    updatePeerOnJoinEvent.addIp(ip)
//    updatePeerOnJoinEvent.addIp(ip)
//    updatePeerOnJoinEvent.addIp(ip)
//    updatePeerOnJoinEvent.addInvidualId("id")
//    updatePeerOnJoinEvent.addInvidualId("id")
//    updatePeerOnJoinEvent.addInvidualId("id")
//    updatePeerOnJoinEvent.addAgentJSON("agentJSON")
//    updatePeerOnJoinEvent.addAgentJSON("agentJSON")
//    updatePeerOnJoinEvent.addAgentJSON("agentJSON")
//    val peersPresent = Array(Peer(false, ip, "id", "agentJSON"), Peer(false, ip, "id", "agentJSON"))
//    val expected = peersPresent :+ Peer(true, ip, "id", "agentJSON")
//
//    // Expected
//    (mockedEventProcessor.handleEvent _).expects(*).once()
//
//    // Act
//    sut.handlePeerListUpdateEvent(updatePeerOnJoinEvent)
//    val actual = sut.peers
//
//    // Assert
//    assertResult(expected)(actual)
//  }

  "sendCommand" should "call executeCommand" in {
    // Arrange
    val mockedCommandExecutor = mock[CommandExecutor]
    val sut = new NetworkHandler(commandExecutor = mockedCommandExecutor)
    val mockedCommand = new StopGameCommand()

    // Expect
    (mockedCommandExecutor.executeCommand _).expects(*).once()

    // Act
    sut.sendCommand(mockedCommand)
  }

  "handleStopGameCommand" should "call disconnect" in {
    // Arrange
    val mockedConnection = mock[Connection]
    val sut = new NetworkHandler()
    sut.connections = sut.connections :+ mockedConnection

    // Expected
    (mockedConnection.disconnect _).expects().once()

    // Act
    sut.handleStopGameCommand()
  }

  it should " call close when serverconnection is not null" in {
    // Arrange
    val mockedServerConnection = mock[ServerConnection]
    val sut = new NetworkHandler()
    sut.serverConnection = mockedServerConnection

    // Expected
    (mockedServerConnection.close _).expects().once()

    // Act
    sut.handleStopGameCommand()
  }

  it should "not call close when serverconnection is null" in {
    // Arrange
    val mockedServerConnection = mock[ServerConnection]
    val sut = new NetworkHandler()

    // Expected
    (mockedServerConnection.close _).expects().never()

    // Act
    sut.handleStopGameCommand()
  }

  it should "empty connections" in {
    // Arrange
    val mockedConnection = mock[Connection]
    val sut = new NetworkHandler()
    sut.addConnection(mockedConnection)
    val expected = 0

    // Expected
    (mockedConnection.disconnect _).expects()

    // Act
    sut.handleStopGameCommand()
    val actual = sut.connections.length

    // Assert
    assertResult(expected)(actual)
  }

  it should "empty peers" in {
    // Arrange
    val mockedPeer = mock[Peer]
    val sut = new NetworkHandler()
    sut.addPeer(mockedPeer)
    val expected = 0

    // Act
    sut.handleStopGameCommand()
    val actual = sut.peers.length

    // Assert
    assertResult(expected)(actual)
  }

  it should "empty latencies" in {
    // Arrange
    val mockedLatency = mock[Latency]
    val sut = new NetworkHandler()
    sut.addLatency(mockedLatency)
    val expected = 0

    // Act
    sut.handleStopGameCommand()
    val actual = sut.latencies.length

    // Assert
    assertResult(expected)(actual)
  }

  "updateLatencyList" should "update the latency list" in {
    // Arrange
    val sut = new NetworkHandler()
    sut.mode = NetworkMode.HOST_SOFTWARE_SWITCH
    val transmitterIP = "0.0.0.0"
    val latencyList = Array(Latency(transmitterIP), Latency("0.0.0.1"))
    val latency = 10
    val timestampReceived = "2021-01-10T10:00:00.010000000+01:00"
    val timestampSend = "2021-01-10T10:00:00.000000000+01:00"
    val packet = new Packet(mock[Packageable], transmitterIP, timestampSend, latencyList, Array(), Array())
    val mockedTime = mock[Time]
    (mockedTime.now _).expects().returns(OffsetDateTime.parse(timestampReceived))
    (mockedTime.parse _).expects(*).returns(OffsetDateTime.parse(timestampSend))
    sut.time = mockedTime
    sut.latencies = latencyList

    val expected = Array(Latency(transmitterIP, latency), Latency("0.0.0.1"))
    // Expect

    // Act
    sut.updateLatencyList(packet)
    val actual = sut.latencies

    // Assert
    assertResult(expected)(actual)
  }

  it should "set the latency list with the one in the packet" in {
    // Arrange
    val sut = new NetworkHandler()
    sut.mode = NetworkMode.SOFTWARE_SWITCH
    val transmitterIP = "0.0.0.0"
    val latencyList = Array(Latency(transmitterIP), Latency("0.0.0.1"))
    val timestampSend = "2021-01-10T10:00:00.000000000+01:00"
    val packet = new Packet(mock[Packageable], transmitterIP, timestampSend, latencyList, Array(), Array())
    val expected = Array(Latency(transmitterIP), Latency("0.0.0.1"))
    // Expect

    // Act
    sut.updateLatencyList(packet)
    val actual = sut.latencies

    // Assert
    assertResult(expected)(actual)
  }


  it should "throw a LatencyNotFoundException when latency cant be found" in {
    // Arrange
    val sut = new NetworkHandler()
    sut.mode = NetworkMode.HOST_SOFTWARE_SWITCH
    val transmitterIP = "0.0.0.0"
    val latencyList = Array(Latency(transmitterIP), Latency("0.0.0.1"))
    val timestampSend = "2021-01-10T10:00:00.000000000+01:00"
    val packet = new Packet(mock[Packageable], transmitterIP, timestampSend, latencyList, Array(), Array())
    // Expect

    // Act / Assert
    assertThrows[LatencyNotFoundException] {
      sut.updateLatencyList(packet)
    }
  }

  "updateRecipientsListPeer" should "update the recipients list when called" in {
    // Arrange
    val networkHandler = new NetworkHandler
    val mockConnection = mock[Connection]
    val peer1 = Peer(true, "1.1.1.1.1", "Piet", "agentJSON")
    val peer2 = Peer(false, "2.2.2.2.2", "Henk", "agentJSON")
    val peer3 = Peer(false, "3.3.3.3.3", "Barrie", "agentJSON")
    val packet = Packet(null, "1.1.1.1.1", "1-1-1111", null, null, Array(peer2, peer3))

    // Act
    networkHandler.addConnection(mockConnection)
    networkHandler.addPeer(peer1)
    networkHandler.addPeer(peer2)
    assert(networkHandler.peers.length == 2)
    networkHandler.updateRecipientsList(packet)

    //Assert
    assert(networkHandler.peers.length == 3)
  }
}
