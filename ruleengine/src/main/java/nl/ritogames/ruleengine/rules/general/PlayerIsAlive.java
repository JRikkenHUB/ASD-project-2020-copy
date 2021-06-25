package nl.ritogames.ruleengine.rules.general;

import nl.ritogames.ruleengine.rules.Rule;
import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.event.InteractionEvent;

/**
 * Rule that checks if a player is alive
 *
 * @param <T> an instance of InteractionEvent
 */
public class PlayerIsAlive<T extends InteractionEvent> implements Rule<T> {
    /**
     * Checks if the specified IndividualId already exists in the GameState
     *
     * @param event    an instance of InteractionEvent
     * @param modifier contains the GameStateModifier so data from the GameState can be fetched
     * @return a boolean whether player is alive or not
     */
    @Override
    public boolean validate(T event, GameStateModifier modifier) {
        return modifier.getGameState().getIndividuals().containsKey(event.getIndividualId());
    }
}
