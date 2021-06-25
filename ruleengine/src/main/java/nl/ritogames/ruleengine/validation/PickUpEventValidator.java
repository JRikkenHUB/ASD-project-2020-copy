package nl.ritogames.ruleengine.validation;

import nl.ritogames.ruleengine.rules.general.GameIsInProgress;
import nl.ritogames.ruleengine.rules.interaction.AttributeBetterThanCurrent;
import nl.ritogames.ruleengine.rules.interaction.AttributeInReach;
import nl.ritogames.shared.dto.event.PickUpEvent;

/**
 * Sets the rules for a PickUpEvent
 */
public class PickUpEventValidator extends ValidationStrategy<PickUpEvent> {
    @Override
    protected void instantiateRules() {
        this.rules.add(new AttributeInReach());
        this.rules.add(new AttributeBetterThanCurrent());
        this.rules.add(new GameIsInProgress<>());
    }
}
