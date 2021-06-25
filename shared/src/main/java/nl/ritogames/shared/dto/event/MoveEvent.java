package nl.ritogames.shared.dto.event;

import nl.ritogames.shared.enums.Direction;

public class MoveEvent extends GameEventWithDirection {

    /**
     * An Event used for when an individual tries to move in a direction
     */
    public MoveEvent() {
    }

    public MoveEvent(String individualId, Direction direction) {
        super(individualId, direction);
    }


}
