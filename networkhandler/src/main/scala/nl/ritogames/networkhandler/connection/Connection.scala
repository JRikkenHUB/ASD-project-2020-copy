package nl.ritogames.networkhandler.connection

import java.io.{BufferedReader, InputStreamReader}
import java.net.Socket

import nl.ritogames.networkhandler.packet.Packet
import nl.ritogames.networkhandler.wrapper.jsonparser.{JsonParser, PacketJsonParser}
import nl.ritogames.networkhandler.wrapper.outputstream.{OutputStreamWrapper, PrintStreamWrapper}
import nl.ritogames.shared.logger.Logger

case class Connection(var ip: String,
                      var port: Int,
                      var isHost: Boolean,
                      var latency: Int = Integer.MAX_VALUE,
                      var socket: Socket = null,
                      var outputStream: OutputStreamWrapper = null,
                      var parser: JsonParser = new PacketJsonParser,
                      onReceive: Packet => Unit) {
  var IS_RUNNING: Boolean = true
  var in: BufferedReader = _
  var thread: Thread = _

  def start(): Unit = {
    Logger.logMethodCall(this)
    socket = new Socket(ip, port)
    outputStream = new PrintStreamWrapper(socket.getOutputStream)
    in = new BufferedReader(new InputStreamReader(socket.getInputStream))
    thread = new Thread(() => handleIncomingData())
    thread.setName(ip)
  }

  def sendPacket(packet: Packet): Unit = {
    outputStream.println(parser.toJSON(packet))
  }

  def handleIncomingData(): Unit = {
    while (IS_RUNNING) {
      val json = in.readLine()
      if (json != null) {
        onReceive(parser.fromJSON(json))
      }
    }
  }

  def disconnect(): Unit = {
    IS_RUNNING = false
    thread.interrupt()
    socket.close()
  }
}

