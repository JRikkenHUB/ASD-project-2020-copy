package nl.ritogames.ruleengine.rules.general;

import nl.ritogames.ruleengine.rules.Rule;
import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.event.InteractionEvent;
import nl.ritogames.shared.enums.GameStatus;

/**
 * Checks if the specified game has started
 *
 * @param <T> an instance of InteractionEvent
 */
public class GameIsInProgress<T extends InteractionEvent> implements Rule<T> {
    /**
     * Check whether the GameStatus is started
     *
     * @param event    an instance of InteractionEvent
     * @param modifier contains the GameStateModifier so data from the GameState can be fetched
     * @return a boolean whether the game has started or not
     */
    @Override
    public boolean validate(T event, GameStateModifier modifier) {
        return modifier.getGameState().getStatus().equals(GameStatus.IN_PROGRESS);
    }
}
