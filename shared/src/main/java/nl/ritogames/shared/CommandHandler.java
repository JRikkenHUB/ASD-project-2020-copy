package nl.ritogames.shared;

import nl.ritogames.shared.dto.event.InteractionEvent;
import nl.ritogames.shared.exception.EventFailedException;

public interface CommandHandler {
    /**
     * Turns an Event into a Command
     * @param event
     * @throws EventFailedException
     */
    void handleEventIntoCommand(InteractionEvent event) throws EventFailedException;
}
