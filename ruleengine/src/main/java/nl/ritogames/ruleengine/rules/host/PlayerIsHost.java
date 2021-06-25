package nl.ritogames.ruleengine.rules.host;

import nl.ritogames.ruleengine.rules.Rule;
import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.event.InteractionEvent;

/**
 * Checks whether the player initiating the command is the host
 * @param <T> an instance of InteractionEvent
 */
public class PlayerIsHost<T extends InteractionEvent> implements Rule<T> {
    /**
     * Validate if the initiating player is the host
     * @param event the event provided, instance of Interaction event
     * @param modifier an instance of a GameStateModifier to communicate to the GameState
     * @return whether the player is the host or not
     */
    @Override
    public boolean validate(T event, GameStateModifier modifier) {
        return modifier.getGameState().getHostIndividualId().equals(event.getIndividualId());
    }
}
