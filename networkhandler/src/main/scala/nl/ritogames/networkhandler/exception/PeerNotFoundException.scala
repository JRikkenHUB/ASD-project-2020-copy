package nl.ritogames.networkhandler.exception

case class PeerNotFoundException(message: String = "", cause: Throwable = None.orNull) extends RuntimeException(message, cause)
