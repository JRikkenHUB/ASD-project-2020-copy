package nl.ritogames.ruleengine;

import nl.ritogames.ruleengine.rules.winning.WinningConditionsMet;
import nl.ritogames.ruleengine.validation.ValidationStrategyFactory;
import nl.ritogames.shared.CommandHandler;
import nl.ritogames.shared.EventProcessor;
import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.event.CreateGameEvent;
import nl.ritogames.shared.dto.event.GameJoinEvent;
import nl.ritogames.shared.dto.event.InteractionEvent;
import nl.ritogames.shared.dto.event.StartGameEvent;
import nl.ritogames.shared.exception.EventFailedException;
import nl.ritogames.shared.logger.Logger;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * The main class for the EventProcessor that validates the rules
 */
public class ASDEventProcessor implements EventProcessor {
    private GameStateModifier modifier;
    private CommandHandler commandHandler;
    private WinningConditionsMet winningConditionsMet;
    private final Map<String, Long> lastCommandsFromIndividuals = new HashMap<>();

    public ASDEventProcessor() {
        //Standard constructor
    }

    /**
     * Handles an incoming event to see if it's valid, and sends it to the commandHandler
     *
     * @param event an instance of Interaction Event
     * @throws EventFailedException when the event could not be processed.
     */
    public void handleEvent(InteractionEvent event) throws EventFailedException {
        Logger.logMethodCall(this);
        if (event instanceof CreateGameEvent) {
            commandHandler.handleEventIntoCommand(event);
        } else if(event instanceof GameJoinEvent){
            modifier.playerJoin((GameJoinEvent) event);
        }else{
            if (winningConditionsMet.isMet()) {
                commandHandler.handleEventIntoCommand(winningConditionsMet.getEvent(event));
            } else {
                if (validateEvent(event) && individualIsNotSpamming(event.getIndividualId())) {
                    if (event instanceof StartGameEvent) {
                        ((StartGameEvent) event).setGameState(modifier.getGameState());
                    }
                    commandHandler.handleEventIntoCommand(event);
                    addEventToCommandMap(event);
                }
            }
        }
    }

    /**
     * Makes sure an individual can only execute 1 event per second.
     *
     * @return If the time between the last events is less than 1 second
     */
    private boolean individualIsNotSpamming(String individualId) {
        long timeBetweenCommands = 1000;
        if (lastCommandsFromIndividuals.containsKey(individualId)) {
            Long currentTimestamp = new Timestamp(System.currentTimeMillis()).getTime();
            Long previousTimestamp = lastCommandsFromIndividuals.get(individualId);
            return currentTimestamp - previousTimestamp >= timeBetweenCommands;
        }
        return true;
    }

    private void addEventToCommandMap(InteractionEvent event) {
        Long currentTimestamp = new Timestamp(System.currentTimeMillis()).getTime();
        if (lastCommandsFromIndividuals.containsKey(event.getIndividualId())) {
            lastCommandsFromIndividuals.replace(event.getIndividualId(), currentTimestamp);
        } else {
            lastCommandsFromIndividuals.put(event.getIndividualId(), currentTimestamp);
        }
    }

    /**
     * This function validates an event with the right strategy
     *
     * @param event an instance of Interaction Event
     * @return a boolean if the given event is valid
     */
    public boolean validateEvent(InteractionEvent event) {
        return ValidationStrategyFactory.getStrategy(event).validateEvent(event, modifier);
    }

    @Inject
    public void setCommandHandler(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Inject
    public void setModifier(GameStateModifier modifier) {
        this.modifier = modifier;
    }

    @Inject
    public void setWinningConditionsMet(WinningConditionsMet winningConditionsMet) {
        this.winningConditionsMet = winningConditionsMet;
    }
}
