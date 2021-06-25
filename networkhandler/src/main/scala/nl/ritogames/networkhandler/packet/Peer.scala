package nl.ritogames.networkhandler.packet

case class Peer(var isHost: Boolean, ip: String, individualId: String, var agentJSON: String)
