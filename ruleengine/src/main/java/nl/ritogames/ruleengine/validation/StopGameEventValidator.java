package nl.ritogames.ruleengine.validation;

import nl.ritogames.ruleengine.rules.general.GameExists;
import nl.ritogames.ruleengine.rules.host.PlayerIsHost;
import nl.ritogames.shared.dto.event.StopGameEvent;

/**
 * Sets the rules for a StopGameEvent
 */
public class StopGameEventValidator  extends ValidationStrategy<StopGameEvent> {
    /**
     * Instantiates the rules for a StopGameEvent
     * Check if the game exists
     * Check if player is host
     */
    @Override
    protected void instantiateRules() {
        this.rules.add(new GameExists<>());
        this.rules.add(new PlayerIsHost<>());
    }
}
