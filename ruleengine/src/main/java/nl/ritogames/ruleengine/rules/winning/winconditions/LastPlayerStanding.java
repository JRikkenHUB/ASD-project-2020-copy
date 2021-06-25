package nl.ritogames.ruleengine.rules.winning.winconditions;

import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.gameobject.individual.Character;
import nl.ritogames.shared.dto.gameobject.individual.Individual;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * An implementation of WinCondition that checks if there's only one player standing
 */
public class LastPlayerStanding extends WinCondition {

    public LastPlayerStanding(GameStateModifier modifier) {
        super(modifier);
    }

    /**
     * Validation to see if there is only 1 player left.
     *
     * @return whether the playerCount is 1
     */
    @Override
    public boolean isMet() {
        Map<String, Individual> individualsInGame = modifier.getGameState().getIndividuals();
        List<Individual> characters = individualsInGame.values().stream().filter(individual -> individual instanceof Character).collect(Collectors.toList());
        return characters.size() == 1;
    }
}
