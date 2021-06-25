package nl.ritogames.networkhandler.util

import nl.ritogames.networkhandler.packet._
import nl.ritogames.networkhandler.wrapper.jsonparser.PacketJsonParser
import nl.ritogames.shared.dto.Packageable
import nl.ritogames.shared.dto.event.GameJoinEvent
import org.scalatest.BeforeAndAfterEach
import org.scalatest.flatspec.AnyFlatSpec

class PacketJsonParserTest extends AnyFlatSpec with BeforeAndAfterEach {
  var parser: PacketJsonParser = new PacketJsonParser()

  override def beforeEach() {
    parser = new PacketJsonParser()
  }

  behavior of "JsonParserTest"

  it should "return a json string from a packet object" in {
    // Arrange
    val expectedJson = "{\"data\":{\"@type\":\"GameJoinEvent\",\"timeStamp\":0,\"individualId\":null,\"gameName\":\"\",\"ip\":\"192.168.2.1\",\"agentName\":null,\"agentJSON\":null},\"transmitter\":\"test\",\"timestamp\":\"10\",\"latency_list\":[{\"ip\":\"192.168.2.2\",\"latency\":11}],\"no_response\":[\"testdata\"],\"recipients\":[{\"isHost\":false,\"ip\":\"ip\",\"individualId\":\"individualId\",\"agentJSON\":\"agentJSON\"}]}"
    val data: Packageable = new GameJoinEvent("192.168.2.1")
    val transmitter = "test"
    val timestamp = "10"
    val latencyList = Array(Latency("192.168.2.2", 11))
    val noResponse = Array("testdata")
    val recipients = Array(Peer(isHost = false, "ip", "individualId", "agentJSON"))
    val packet: Packet = Packet(data, transmitter, timestamp, latencyList, noResponse, recipients)

    // Act
    val result = parser.toJSON(packet)

    // Assert
    assertResult(expectedJson)(result)
  }

  it should "return a Packet object from a json string" in {
    // Arrange
    val inputJson = "{\"data\":{\"@type\":\"GameJoinEvent\",\"timeStamp\":0,\"individualId\":null,\"gameName\":null,\"ip\":\"192.168.2.1\",\"agentJSON\":null},\"transmitter\":\"test\",\"timestamp\":\"10\",\"latency_list\":[{\"ip\":\"192.168.2.2\",\"latency\":11}],\"no_response\":[\"testdata\"],\"recipients\":[{\"isHost\":false,\"ip\":\"ip\",\"individualId\":\"individualId\"}]}"

    // Act
    val result = parser.fromJSON(inputJson)

    // Assert
    result.asInstanceOf[Packet]
  }
}

