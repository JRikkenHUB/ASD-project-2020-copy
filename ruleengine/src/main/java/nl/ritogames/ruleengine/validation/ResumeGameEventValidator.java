package nl.ritogames.ruleengine.validation;

import nl.ritogames.ruleengine.rules.general.SavedGameExists;
import nl.ritogames.shared.dto.event.ResumeGameEvent;

/**
 * Sets the rules for a ResumeGameEvent
 */
public class ResumeGameEventValidator extends ValidationStrategy<ResumeGameEvent> {
    @Override
    protected void instantiateRules() {
        this.rules.add(new SavedGameExists<>());
    }
}
