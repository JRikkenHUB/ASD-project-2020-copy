package nl.ritogames.ruleengine.validation;

import nl.ritogames.ruleengine.rules.general.GameExists;
import nl.ritogames.ruleengine.rules.general.GameIsInProgress;
import nl.ritogames.shared.dto.event.GameSaveEvent;

/**
 * Sets the rules for a GameSaveEvent
 */
public class GameSaveEventValidator extends ValidationStrategy<GameSaveEvent> {
    /**
     * Instantiates rules for the CreateEvent
     * Check if the game exists
     * Check if the game has started
     */
    @Override
    protected void instantiateRules() {
        this.rules.add(new GameIsInProgress<>());
        this.rules.add(new GameExists<>());
    }
}
