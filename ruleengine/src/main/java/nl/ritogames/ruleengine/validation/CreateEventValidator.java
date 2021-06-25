package nl.ritogames.ruleengine.validation;

import nl.ritogames.shared.dto.event.CreateGameEvent;

/**
 * Sets the rules for a CreateEvent
 */
public class CreateEventValidator extends ValidationStrategy<CreateGameEvent> {

    /**
     * Instantiates rules for the CreateEvent
     * No rules are present at this time
     */
    @Override
    public void instantiateRules() {
        //No rules are present at this time
    }
}
