package nl.ritogames.networkhandler.packet.factory

import nl.ritogames.networkhandler.packet.Packet
import nl.ritogames.shared.dto.Packageable
import org.scalamock.scalatest.MockFactory
import org.scalatest.flatspec.AnyFlatSpec

class PacketFactoryTest extends AnyFlatSpec with MockFactory {
  behavior of "PacketFactory"

  "getPacket" should "return a packet" in {
    val sut = new PacketFactory
    val result = sut.getPacket(mock[Packageable], "transmitterIP", null, null)
    assert(result.isInstanceOf[Packet])
  }

}
