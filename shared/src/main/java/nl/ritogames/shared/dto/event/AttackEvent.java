package nl.ritogames.shared.dto.event;

import nl.ritogames.shared.enums.Direction;

import java.util.Objects;

public class AttackEvent extends GameEventWithDirection {
    private String individualUnderAttack;

    public AttackEvent() {
        super();
    }

    /**
     * Event used when an individual tries to attack in a certain direction
     *
     * @param individiualId
     * @param direction
     * @param individualUnderAttack
     */
    public AttackEvent(String individiualId, Direction direction, String individualUnderAttack) {
        super(individiualId, direction);
        this.individualUnderAttack = individualUnderAttack;
    }

    public AttackEvent(String individualId, String gameName, Direction direction) {
        super(individualId, gameName, direction);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AttackEvent that = (AttackEvent) o;
        return direction == that.direction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), direction);
    }

    @Override
    public String toString() {
        return "AttackEvent{" +
                "direction=" + direction +
                '}';
    }
}
