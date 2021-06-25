package nl.ritogames.networkhandler.exception

final case class PacketDataInvalidException(message: String = "", cause: Throwable = None.orNull)
  extends RuntimeException(message, cause)
