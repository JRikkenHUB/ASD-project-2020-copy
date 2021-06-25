package nl.ritogames.ruleengine.rules.winning;

import nl.ritogames.ruleengine.rules.winning.winconditions.LastPlayerStanding;
import nl.ritogames.ruleengine.rules.winning.winconditions.WinCondition;
import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.event.InteractionEvent;
import nl.ritogames.shared.dto.event.WinConditionMetEvent;
import nl.ritogames.shared.enums.GameStatus;

import javax.inject.Inject;
import java.util.ArrayList;

/**
 * Checks if the winning conditions for the game have been met
 * A winning condition should be checked with every event received while playing
 */
public class WinningConditionsMet {
    protected GameStateModifier modifier;

    /**
     * Check whether the winning conditions have been met
     *
     * @return true when passed, throw a WinningConditionsMetException if the conditions have been met
     */
    public boolean isMet() {
        if (modifier.getGameState().getStatus().equals(GameStatus.IN_PROGRESS)) {
            for (WinCondition winningCondition : getWinningConditions()) {
                if (winningCondition.isMet()) {
                    return true;
                }
            }
        }
        return false;
    }

    private ArrayList<WinCondition> getWinningConditions() {
        ArrayList<WinCondition> winningConditions = new ArrayList<>();
        winningConditions.add(new LastPlayerStanding(modifier));
        return winningConditions;
    }

    @Inject
    public void setModifier(GameStateModifier modifier) {
        this.modifier = modifier;
    }

    public InteractionEvent getEvent(InteractionEvent event) {
        return new WinConditionMetEvent(event.getIndividualId(), event.getGameName());
    }
}
