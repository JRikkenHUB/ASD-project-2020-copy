package nl.ritogames.ruleengine.validation;

import nl.ritogames.shared.dto.event.*;
import nl.ritogames.shared.exception.NotImplementedException;

import java.util.HashMap;
import java.util.Map;

/**
 * The validationStrategyFactory will return the correct strategy based on the received event
 */
public class ValidationStrategyFactory {
    private static final Map<Class<? extends InteractionEvent>, ValidationStrategy<? extends InteractionEvent>> strategies = new HashMap<>();

    static {
        strategies.put(MoveEvent.class, new MoveEventValidator());
        strategies.put(CreateGameEvent.class, new CreateEventValidator());
        strategies.put(StartGameEvent.class, new StartGameEventValidator());
        strategies.put(StopGameEvent.class, new StopGameEventValidator());
        strategies.put(GameJoinEvent.class, new GameJoinEventValidator());
        strategies.put(StartAgentEvent.class, new StartAgentValidator());
        strategies.put(GameSaveEvent.class, new GameSaveEventValidator());
        strategies.put(AttackEvent.class, new AttackEventValidator());
        strategies.put(PickUpEvent.class, new PickUpEventValidator());
        strategies.put(ResumeGameEvent.class, new ResumeGameEventValidator());
        strategies.put(ChangeHostEvent.class, new ChangeHostEventValidator());
        strategies.put(StopAgentEvent.class, new StopAgentValidator());
    }

    private ValidationStrategyFactory() {

    }

    /**
     * This method will return the correct validator based on the received event
     *
     * @param event the InteractionEvent received
     * @return the validator to process the event
     */
    public static ValidationStrategy getStrategy(InteractionEvent event) {
        for (Map.Entry<Class<? extends InteractionEvent>, ValidationStrategy<? extends InteractionEvent>> strategy : strategies.entrySet()) {
            if (strategy.getKey().equals(event.getClass())) {
                return strategy.getValue();
            }
        }
        throw new NotImplementedException();
    }
}
