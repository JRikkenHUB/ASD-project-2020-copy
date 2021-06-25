package nl.ritogames.networkhandler.connection.factory

import java.net.Socket

import nl.ritogames.networkhandler.connection.{Connection, ServerConnection}
import nl.ritogames.networkhandler.packet.Packet

class ConnectionFactory {

  val DEFAULT_PORT: Int = 8001

  def getConnection(ip: String, isHost: Boolean, onReceive: Packet => Unit, port: Int = DEFAULT_PORT): Connection = {
    Connection(ip, port, isHost, onReceive = onReceive)
  }

  def getConnection(socket: Socket, isHost: Boolean, onReceive: Packet => Unit): Connection = {
    Connection(ip = socket.getInetAddress.getHostAddress,
      port = socket.getPort,
      isHost,
      socket = socket,
      onReceive = onReceive)
  }

  def getServerConnection(port: Int = DEFAULT_PORT, onAccept: Socket => Unit): ServerConnection = {
    ServerConnection(port = port, onAccept = onAccept)
  }
}
