package nl.ritogames.shared.dto.event;

import nl.ritogames.shared.enums.Direction;

import java.util.Objects;

public class GameEventWithDirection extends GameEvent {
    protected Direction direction;

    public GameEventWithDirection() {
    }

    public GameEventWithDirection(String individualId, String gameName, Direction direction) {
        super(individualId, gameName);
        this.direction = direction;
    }

    public GameEventWithDirection(String individualId, Direction direction) {
        super(individualId);
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GameEventWithDirection that = (GameEventWithDirection) o;
        return direction == that.direction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), direction);
    }

    @Override
    public String toString() {
        return "GameEventWithDirection{" +
                "timeStamp=" + timeStamp +
                ", individualId='" + individualId + '\'' +
                ", gameName='" + gameName + '\'' +
                ", direction=" + direction +
                '}';
    }
}
