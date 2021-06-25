package nl.ritogames.networkhandler.wrapper.jsonparser

import nl.ritogames.networkhandler.packet.Packet
import nl.ritogames.networkhandler.util.PacketJsonParser

class PacketJsonParser extends JsonParser {
  override def toJSON(packet: Packet): String = {
    PacketJsonParser.toJSON(packet)
  }

  override def fromJSON(string: String): Packet = {
    PacketJsonParser.fromJSON(string)
  }
}
