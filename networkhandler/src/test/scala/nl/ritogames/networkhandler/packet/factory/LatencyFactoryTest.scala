package nl.ritogames.networkhandler.packet.factory

import nl.ritogames.networkhandler.packet.Latency
import org.scalamock.scalatest.MockFactory
import org.scalatest.flatspec.AnyFlatSpec

class LatencyFactoryTest extends AnyFlatSpec with MockFactory {
  behavior of "LatencyFactory"

  "getLatency" should "should return a Latency Object" in {
    val sut = new LatencyFactory()
    val result = sut.getLatency("ip")
    assert(result.isInstanceOf[Latency])
  }

  it should "should return a Latency Object wth a default latency of Int.MAXVALUE" in {
    val sut = new LatencyFactory()
    val expected = Int.MaxValue
    val result = sut.getLatency("ip")
    assert(result.latency == expected)
  }

  it should "should return a Latency Object with the given IP" in {
    val sut = new LatencyFactory()
    val expected = "0.0.0.0"
    val result = sut.getLatency(expected)
    assert(result.ip == expected)
  }
}
