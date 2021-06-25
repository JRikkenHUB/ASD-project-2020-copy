package nl.ritogames.ruleengine.rules.join;

import nl.ritogames.ruleengine.rules.Rule;
import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.event.GameJoinEvent;
import nl.ritogames.shared.enums.GameStatus;

/**
 * A validation for joinEvent whether the game is still in lobby mode so it knows if the player is allowed to join the game
 */

public class GameJoinEventGameHasNotStarted implements Rule<GameJoinEvent> {

    /**
     * validate if the game is still in lobby mode and therefore joinable
     *
     * @param modifier contains the GameStateModifier so data from the GameState can be fetched
     * @return a boolean whether the game is still in lobby mode and therefore joinable
     */

    public boolean validate(GameJoinEvent event, GameStateModifier modifier) {
        return modifier.getGameState().getStatus() == GameStatus.CREATED;
    }
}
