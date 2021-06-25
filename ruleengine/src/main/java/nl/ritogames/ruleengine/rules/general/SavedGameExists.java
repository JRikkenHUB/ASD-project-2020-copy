package nl.ritogames.ruleengine.rules.general;

import nl.ritogames.ruleengine.rules.Rule;
import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.event.InteractionEvent;

/**
 * Checks if a SaveGame exists for the specified name
 *
 * @param <T> an instance of InteractionEvent
 */
public class SavedGameExists<T extends InteractionEvent> implements Rule<T> {
    /**
     * Checks if there is a SaveGame present with the specified name
     *
     * @param event    an instance of InteractionEvent
     * @param modifier contains the GameStateModifier so data from the GameState can be fetched
     * @return a boolean whether there is a SaveGame present or not
     */
    @Override
    public boolean validate(T event, GameStateModifier modifier) {
        return modifier.saveGameExists(event.getGameName());
    }
}
