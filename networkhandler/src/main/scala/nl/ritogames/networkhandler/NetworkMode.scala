package nl.ritogames.networkhandler

object NetworkMode extends Enumeration {
  val PEER_TO_PEER: NetworkMode.Value = Value("PEER_TO_PEER")
  val SOFTWARE_SWITCH: NetworkMode.Value = Value("SOFTWARE_SWITCH")
  val HOST_PEER_TO_PEER: NetworkMode.Value = Value("HOST_PEER_TO_PEER")
  val HOST_SOFTWARE_SWITCH: NetworkMode.Value = Value("HOST_SOFTWARE_SWITCH")
}
