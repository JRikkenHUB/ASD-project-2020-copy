package nl.ritogames.networkhandler.packet.factory

import nl.ritogames.networkhandler.packet.Peer

class PeerFactory {

  def getPeer(isHost: Boolean, ip: String, individualId: String, agentJSON: String): Peer = {
    Peer(isHost, ip, individualId, agentJSON)
  }

  def getHost(ip: String, individualId: String, agentJSON: String): Peer = {
    getPeer(isHost = true, ip, individualId, agentJSON)
  }

  def getClient(ip: String, individualId: String, agentJSON: String): Peer = {
    getPeer(isHost = false, ip, individualId, agentJSON)
  }
}
