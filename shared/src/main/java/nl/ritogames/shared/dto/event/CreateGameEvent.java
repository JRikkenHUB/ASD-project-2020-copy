package nl.ritogames.shared.dto.event;

import java.util.Objects;

public class CreateGameEvent extends MenuEvent {
    String ip;
    String hostAgentName;

    public CreateGameEvent() {
        super();
    }

    /**
     * Event used when a player tries to create a game
     *
     * @param individualId
     * @param gameName
     */
    public CreateGameEvent(String individualId, String gameName) {
        super(individualId, gameName);
    }

    public CreateGameEvent(String individualId, String gameName, String ip) {
        super(individualId, gameName);
        this.ip = ip;
    }

    public CreateGameEvent(String gameName) {
        this.setGameName(gameName);
    }

    String getIp() {
        return ip;
    }

    void setIp(String ip) {
        this.ip = ip;
    }

    public String getHostAgentName() {
        return hostAgentName;
    }

    public void setHostAgentName(String hostAgentName) {
        this.hostAgentName = hostAgentName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        CreateGameEvent that = (CreateGameEvent) o;
        return Objects.equals(getIp(), that.getIp());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getIp());
    }
}
