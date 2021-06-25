package nl.ritogames.networkhandler.exception

final class LatencyNotFoundException(message: String = "", cause: Throwable = None.orNull)
  extends RuntimeException(message, cause)

