package nl.ritogames.shared.dto.event;

import java.util.Base64;
import java.util.Objects;

public class GameJoinEvent extends MenuEvent {
    private String ip;
    private String individualId;
    private String agentName;
    private String agentJSON;

    public GameJoinEvent(){}

    public GameJoinEvent(String gameName, String ip){
        this.setGameName(gameName);
        this.ip = ip;
    }


    public GameJoinEvent(String ip){
        this("", "", ip);
    }

    public GameJoinEvent(String individualId, String gameName, String ip) {
        super(individualId, gameName);
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIndividualId() {
        return individualId;
    }

    public void setIndividualId(String individualId) {
        this.individualId = individualId;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public void setAgentJSON(String agentJSON) {
        this.agentJSON = agentJSON;
    }

    public String getAgentJSON() {
        return agentJSON;
    }

    public void decodedAgentJSON() {
        this.agentJSON = new String(Base64.getDecoder().decode(agentJSON));
    }

    public void encodeAgentJSON() {
        this.agentJSON = Base64.getEncoder().encodeToString(agentJSON.getBytes());
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GameJoinEvent that = (GameJoinEvent) o;
        return Objects.equals(ip, that.ip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), ip);
    }

    @Override
    public String toString() {
        return "GameJoinEvent{" +
                "ip='" + ip + '\'' +
                '}';
    }
}
