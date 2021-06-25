package nl.ritogames.ruleengine.rules;

import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.event.InteractionEvent;

/**
 * The rule interface provides an abstraction to validate different rules from different events
 *
 * @param <T> an instance of InteractionEvent
 */
public interface Rule<T extends InteractionEvent> {
    /**
     * This method will validate a provided event
     *
     * @param event    the event provided, instance of Interaction event
     * @param modifier an instance of a GameStateModifier to communicate to the GameState
     * @return a boolean whether the event is valid or not
     */
    boolean validate(T event, GameStateModifier modifier);
}