package nl.ritogames.shared.dto.event;

import java.util.Objects;

public class ChangeHostEvent extends InteractionEvent {
    private String newHostIp;

    public ChangeHostEvent() {
    }

    public ChangeHostEvent(String newHostIp) {
        this.newHostIp = newHostIp;
    }

    public ChangeHostEvent(String newHostIp, String individualId, String gameName) {
        super(individualId, gameName);
        this.newHostIp = newHostIp;
    }

    public ChangeHostEvent(String newHostIp, String individualId) {
        super(individualId);
        this.newHostIp = newHostIp;
    }

    public String getNewHostIp() {
        return newHostIp;
    }

    public void setNewHostIp(String newHostIp) {
        this.newHostIp = newHostIp;
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
        ChangeHostEvent that = (ChangeHostEvent) o;
        return Objects.equals(newHostIp, that.newHostIp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), newHostIp);
    }
}
