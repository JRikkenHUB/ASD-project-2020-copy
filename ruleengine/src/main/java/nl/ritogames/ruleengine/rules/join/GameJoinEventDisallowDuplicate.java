package nl.ritogames.ruleengine.rules.join;

import nl.ritogames.ruleengine.rules.Rule;
import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.GameState;
import nl.ritogames.shared.dto.event.GameJoinEvent;

/**
 * A rule that checks if there is a duplicate IndividualID in the GameState
 */
public class GameJoinEventDisallowDuplicate implements Rule<GameJoinEvent> {
    /**
     * Check duplicates individuals in the GameState
     *
     * @param event    an instance of InteractionEvent
     * @param modifier contains the GameStateModifier so data from the GameState can be fetched
     * @return whether the individuals in the GameState contains the specified individual
     */
    @Override
    public boolean validate(GameJoinEvent event, GameStateModifier modifier) {
        GameState gameState = modifier.getGameState();
        String user = event.getIndividualId();
        return !gameState.getIndividuals().containsKey(user);
    }
}
