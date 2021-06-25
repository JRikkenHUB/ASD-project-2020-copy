package nl.ritogames.shared.dto.event;


import java.util.Objects;

public abstract class GameEvent extends InteractionEvent {
    protected GameEvent() {}

    protected GameEvent(String individualId, String gameName) {
        super(individualId, gameName);
    }

    protected GameEvent(String individualId) {
        super(individualId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode());
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }
    @Override
    public String toString() {
        return "GameEvent{" +
                "timeStamp=" + timeStamp +
                ", individualId='" + individualId + '\'' +
                ", gameName='" + gameName + '\'' +
                '}';
    }


}
