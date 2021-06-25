package nl.ritogames.ruleengine.validation;

import nl.ritogames.ruleengine.rules.Rule;
import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.event.InteractionEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The validationStrategy will loop through a set of rules
 * Every class that extends this class only has to call the instantiateRules function to set custom rules
 */
public abstract class ValidationStrategy<T extends InteractionEvent> {
    protected List<Rule<T>> rules = new ArrayList<>();

    protected ValidationStrategy() {
        instantiateRules();
    }

    /**
     * Loops through a set of defined rules to see if the event is valid
     *
     * @param event    an instance of InteractionEvent
     * @param modifier an instance of GameStateModifier used in the rules
     * @return if the specified event is valid
     */
    public boolean validateEvent(T event, GameStateModifier modifier) {
        for (Rule<T> rule : rules) {
            if (!rule.validate(event, modifier)) {
                return false;
            }
        }
        return true;
    }

    protected abstract void instantiateRules();

    public void setRules(List<Rule<T>> rules) {
        this.rules = rules;
    }

    /**
     * @return An unmodifiable list of rules
     */
    public List<Rule<T>> getRules() {
        return Collections.unmodifiableList(rules);
    }
}