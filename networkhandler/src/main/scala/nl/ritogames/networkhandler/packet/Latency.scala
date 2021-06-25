package nl.ritogames.networkhandler.packet

case class Latency(var ip: String, var latency: Int = Int.MaxValue)
