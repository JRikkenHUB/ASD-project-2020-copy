package nl.ritogames.ruleengine.validation;

import nl.ritogames.ruleengine.rules.general.GameIsInProgress;
import nl.ritogames.ruleengine.rules.general.PlayerIsAlive;
import nl.ritogames.shared.dto.event.StartAgentEvent;

/**
 * Sets the rules for a StartAgentEvent
 */
public class StartAgentValidator extends ValidationStrategy<StartAgentEvent> {
    /**
     * Instantiates the rules for a StartAgentEvent
     * <p>
     * Check if the player is alive
     */
    @Override
    protected void instantiateRules() {
        this.rules.add(new PlayerIsAlive<>());
        this.rules.add(new GameIsInProgress<>());
    }
}
