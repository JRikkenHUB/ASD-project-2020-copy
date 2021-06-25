package nl.ritogames.networkhandler.connection

import java.net.Socket

import nl.ritogames.networkhandler.packet.{Latency, Packet}
import nl.ritogames.networkhandler.wrapper.jsonparser.PacketJsonParser
import nl.ritogames.networkhandler.wrapper.outputstream.OutputStreamWrapper
import nl.ritogames.shared.dto.Packageable
import org.scalamock.scalatest.MockFactory
import org.scalatest.flatspec.AnyFlatSpec

class ConnectionTest extends AnyFlatSpec with MockFactory {
  behavior of "Connection"

  "sendPacket" should "use the PrintWriter" in {
    val mockedSocket = mock[Socket]
    val mockedStream = mock[OutputStreamWrapper]
    val mockedParser = mock[PacketJsonParser]
    val sut = Connection("ip", 8001, false, socket = mockedSocket, outputStream = mockedStream, parser = mockedParser, onReceive = (p) => {})
    val packet = new Packet(mock[Packageable], "", "", Array[Latency](), recipients = null, no_response = null)
    (mockedParser.toJSON _).expects(*).returns("VALUE")
    (mockedStream.println _).expects(*).once()

    sut.sendPacket(packet)

  }

  "disconnect" should "close the socket on disconnect" in {
    val mockedSocket = mock[Socket]
    val mockedStream = mock[OutputStreamWrapper]
    val mockedParser = mock[PacketJsonParser]
    val sut = Connection("ip", 8001, false, socket = mockedSocket, outputStream = mockedStream, parser = mockedParser, onReceive = (p) => {})

    (mockedSocket.close _).expects().once()

    sut.disconnect()
  }
}
