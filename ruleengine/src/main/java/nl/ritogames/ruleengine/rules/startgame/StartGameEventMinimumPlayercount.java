package nl.ritogames.ruleengine.rules.startgame;

import nl.ritogames.ruleengine.rules.Rule;
import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.GameState;
import nl.ritogames.shared.dto.event.StartGameEvent;
import nl.ritogames.shared.dto.gameobject.individual.Character;
import nl.ritogames.shared.dto.gameobject.individual.Individual;

import java.util.Map;

/**
 * Validation to see if the minimum amount of players is met
 */
public class StartGameEventMinimumPlayercount implements Rule<StartGameEvent> {
    /**
     * Validation to see if the minimum amount of players is met
     *
     * @param event    an instance of InteractionEvent
     * @param modifier contains the GameStateModifier so data from the GameState can be fetched
     * @return whether the playerCount is larger than the required players
     */
    @Override
    public boolean validate(StartGameEvent event, GameStateModifier modifier) {
        GameState gameState = modifier.getGameState();
        int minimumPlayercount = 1;
        return countPlayers(gameState.getIndividuals()) >= minimumPlayercount;
    }

    /**
     * Counts the players in a map of individuals.
     *
     * @param individuals   A map of individuals.
     * @return              The amount of players in the map.
     */
    private int countPlayers(Map<String, Individual> individuals) {
        int playerCount = 0;
        for (Individual individual : individuals.values()) {
            if (individual instanceof Character) {
                playerCount++;
            }
        }
        return playerCount;
    }
}
