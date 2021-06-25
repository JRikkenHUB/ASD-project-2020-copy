package nl.ritogames.ruleengine.rules.general;

import nl.ritogames.ruleengine.rules.Rule;
import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.event.InteractionEvent;

/**
 * Checks if the specified game exists
 *
 * @param <T> an instance of InteractionEvent
 */
public class GameExists<T extends InteractionEvent> implements Rule<T> {
    /**
     * Check whether the specified name is the same as the game that exists
     *
     * @param event    an instance of InteractionEvent
     * @param modifier contains the GameStateModifier so data from the GameState can be fetched
     * @return a boolean whether the game exists
     */
    @Override
    public boolean validate(T event, GameStateModifier modifier) {
        return modifier.getGameState().getName().equals(event.getGameName());
    }
}
