package nl.ritogames.networkhandler.wrapper.jsonparser

import nl.ritogames.networkhandler.packet.Packet

trait JsonParser {

  def toJSON(packet: Packet): String

  def fromJSON(string: String): Packet

}
