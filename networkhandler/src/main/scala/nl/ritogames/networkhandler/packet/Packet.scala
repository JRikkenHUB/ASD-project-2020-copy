package nl.ritogames.networkhandler.packet

import nl.ritogames.shared.dto.Packageable

case class Packet(var data: Packageable,
                  var transmitter: String,
                  var timestamp: String,
                  var latency_list: Array[Latency],
                  var no_response: Array[String],
                  var recipients: Array[Peer]) {

}
