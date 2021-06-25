package nl.ritogames.networkhandler.connection.factory

import java.net.{InetAddress, Socket}

import nl.ritogames.networkhandler.connection.{Connection, ServerConnection}
import org.scalamock.scalatest.MockFactory
import org.scalatest.flatspec.AnyFlatSpec

class ConnectionFactoryTest extends AnyFlatSpec with MockFactory {
  behavior of "ConnectionFactory"

  "getConnection" should "should return a Connection when giving it an IP" in {
    val sut = new ConnectionFactory()
    val result = sut.getConnection("ip", false, packet => {})
    assert(result.isInstanceOf[Connection])
  }

  "getServerConnection" should "should return a ServerConnection" in {
    val sut = new ConnectionFactory()
    val result = sut.getServerConnection(port = 420, onAccept = socket => {})
    assert(result.isInstanceOf[ServerConnection])
  }

  it should "should return a ServerConnection using the given port" in {
    val sut = new ConnectionFactory()
    val givenPort = 420
    val result = sut.getServerConnection(port = givenPort, onAccept = socket => {})
    assert(result.port == givenPort)
  }

  it should "should return a ServerConnection using the default port" in {
    val sut = new ConnectionFactory()
    val defaultPort = 8001
    val result = sut.getServerConnection(onAccept = socket => {})
    assert(result.port == defaultPort)
  }

}
