package nl.ritogames.networkhandler.packet.factory

import java.time.OffsetDateTime

import nl.ritogames.networkhandler.packet.{Latency, Packet, Peer}
import nl.ritogames.shared.dto.Packageable

class PacketFactory {

  def getPacket(packageable: Packageable,
                transmitter: String,
                recipients: Array[Peer],
                latencies: Array[Latency]): Packet = {
    Packet(
      data = packageable,
      transmitter = transmitter,
      recipients = recipients,
      latency_list = latencies,
      timestamp = OffsetDateTime.now().toString,
      no_response = Array[String]())
  }

}
