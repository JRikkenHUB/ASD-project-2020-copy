package nl.ritogames.ruleengine.validation;

import nl.ritogames.ruleengine.rules.general.GameExists;
import nl.ritogames.ruleengine.rules.startgame.StartGameEventMinimumPlayercount;
import nl.ritogames.shared.dto.event.StartGameEvent;

/**
 * Sets the rules for a StartGameEvent
 */
public class StartGameEventValidator extends ValidationStrategy<StartGameEvent> {
    /**
     * Instantiates the rules for a StartGameEvent
     * Check if the game exists
     * Check if the minimum player count is reached
     */
    @Override
    public void instantiateRules() {
        this.rules.add(new GameExists<>());
        this.rules.add(new StartGameEventMinimumPlayercount());
    }
}
