package nl.ritogames.shared.dto.event;

import nl.ritogames.shared.enums.Direction;

public class PickUpEvent extends GameEventWithDirection {

    /**
     * Event for when an individual tries to pick up an attribute
     */
    public PickUpEvent() {
        super();
    }

    public PickUpEvent(String individualId, String gameName, Direction direction) {
        super(individualId, gameName, direction);
    }

    @Override
    public String toString() {
        return "PickUpEvent{" +
            "timeStamp=" + timeStamp +
            ", individualId='" + individualId + '\'' +
            ", gameName='" + gameName + '\'' +
            ", direction=" + direction +
            '}';
    }
}
