package nl.ritogames.shared;

import nl.ritogames.shared.dto.GameStateContext;
import nl.ritogames.shared.dto.command.Command;


public interface GameStateContextProvider {
    /**
     * Get the context (visible area) for an individual
     * @param characterId Identifier for the individual
     * @return
     */
    GameStateContext getContext(String characterId);

    /**
     * Subscribes a ContextListener to the provider. As soon as the contextProvider has a new GameStateContext it will update ALL subscribers
     *
     * @param contextListener ContextListener that is subscribing
     * @param individualId
     */
    void subscribe(GameStateContextListener contextListener, String individualId);

    /**
     * Unsubscribes a ContextListener from the provider.
     * @param individualId
     */
    void unsubscribe(String individualId);

    /**
     * Checks if in individual is subscribed or not
     * @param individualId
     * @return
     */
    boolean isSubscribed(String individualId);

    /**
     * Processes a command and checks which subscribers need an update
     * @param command
     */
    void processCommand(Command command);
}
