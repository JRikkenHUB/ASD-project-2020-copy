package nl.ritogames.shared;

import nl.ritogames.shared.dto.event.InteractionEvent;
import nl.ritogames.shared.exception.EventFailedException;

public interface EventProcessor {
    void handleEvent(InteractionEvent event) throws EventFailedException;

    boolean validateEvent(InteractionEvent event);
}
