package nl.ritogames.networkhandler.connection

import java.net.{ServerSocket, Socket}

case class ServerConnection(
                             port: Int,
                             private var IS_RUNNING: Boolean = true,
                             var serverSocket: ServerSocket = null,
                             onAccept: Socket => Unit) {

  def start(): Unit = {
    serverSocket = new ServerSocket(port)
    new Thread(() => run()).start()
  }

  private def run(): Unit = {
    while (IS_RUNNING) {
      onAccept(serverSocket.accept())
    }
  }

  def close(): Unit = {
    IS_RUNNING = false
    serverSocket.close()
  }
}
