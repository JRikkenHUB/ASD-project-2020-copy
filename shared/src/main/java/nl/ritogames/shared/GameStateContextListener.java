package nl.ritogames.shared;

import nl.ritogames.shared.dto.GameStateContext;

public interface GameStateContextListener {

    /**
     * Is called when a new GameStateContext is available.
     *
     * @param context The context to process.
     */
    void updateContext(GameStateContext context);

    /**
     * Is called when the listener is unsubscribed
     */
    void unsubscribedSignal();
}
