package nl.ritogames.ruleengine.rules.join;

import nl.ritogames.ruleengine.rules.Rule;
import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.GameState;
import nl.ritogames.shared.dto.event.GameJoinEvent;

/**
 * A validation for joinEvent if the player that wants to join used to be part of the game
 */

public class GameJoinEventPlayerExists implements Rule<GameJoinEvent> {

    /**
     * validate if the player that wants to join used to be part of the game
     *
     * @param event    the event provided, instance of Interaction event
     * @param modifier an instance of a GameStateModifier to communicate to the GameState
     * @return a boolean whether the player used to be part of the game
     */

    public boolean validate(GameJoinEvent event, GameStateModifier modifier) {
        String individualId = event.getIndividualId();
        GameState gameState = modifier.getGameState();
        return gameState.hasIndividual(individualId);
    }
}
