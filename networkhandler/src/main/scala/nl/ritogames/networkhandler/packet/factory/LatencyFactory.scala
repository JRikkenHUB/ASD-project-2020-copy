package nl.ritogames.networkhandler.packet.factory

import nl.ritogames.networkhandler.packet.Latency

class LatencyFactory {

  def getLatency(ip: String): Latency = {
    Latency(ip)
  }

}
