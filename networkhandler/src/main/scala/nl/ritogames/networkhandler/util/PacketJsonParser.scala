package nl.ritogames.networkhandler.util

import java.io.StringWriter

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import nl.ritogames.networkhandler.packet.Packet

object PacketJsonParser {

  def toJSON(packet: Packet): String = {
    val mapper = new ObjectMapper() with ScalaObjectMapper
    mapper.registerModule(DefaultScalaModule)
    val data = new StringWriter()
    mapper.writeValue(data, packet)
    data.toString
  }

  def fromJSON(json: String): Packet = {
    val mapper = new ObjectMapper() with ScalaObjectMapper
    mapper.registerModule(DefaultScalaModule)
    mapper.readValue(json, classOf[Packet])
  }
}
