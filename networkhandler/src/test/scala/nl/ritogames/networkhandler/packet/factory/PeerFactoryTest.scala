package nl.ritogames.networkhandler.packet.factory

import nl.ritogames.networkhandler.packet.Peer
import org.scalamock.scalatest.MockFactory
import org.scalatest.flatspec.AnyFlatSpec

class PeerFactoryTest extends AnyFlatSpec with MockFactory {
  behavior of "PeerFactory"

  "getPeer" should "should return a Peer Object" in {
    val sut = new PeerFactory()
    val result = sut.getPeer(false, "ip", "individualId", "agentJSON")
    assert(result.isInstanceOf[Peer])
  }

  it should "should return a Peer with the given ip" in {
    val sut = new PeerFactory()
    val expected = "0.0.0.0"
    val result = sut.getPeer(false, expected, "individualId", "agentJSON")
    assert(result.ip == expected)
  }

  "getHost" should "should return a Peer with 'isHost' set to true" in {
    val sut = new PeerFactory()
    val result = sut.getHost("ip", "individualId", "agentJSON")
    assert(result.isHost)
  }

  it should "should return a Peer with the given ip" in {
    val sut = new PeerFactory()
    val expected = "0.0.0.0"
    val result = sut.getHost(expected, "individualId", "agentJSON")
    assert(result.ip == expected)
  }

  "getClient" should "should return a Peer with 'isHost' set to false" in {
    val sut = new PeerFactory()
    val result = sut.getClient("ip", "individualId", "agentJSON")
    assert(!result.isHost)
  }

  it should "should return a Peer with the given ip" in {
    val sut = new PeerFactory()
    val expected = "0.0.0.0"
    val result = sut.getClient(expected, "individualId", "agentJSON")
    assert(result.ip == expected)
  }
}
