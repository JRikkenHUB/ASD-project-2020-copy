package nl.ritogames.ruleengine.validation;

import nl.ritogames.ruleengine.rules.Rule;
import nl.ritogames.ruleengine.rules.general.GameIsInProgress;
import nl.ritogames.ruleengine.rules.join.GameJoinEventDisallowDuplicate;
import nl.ritogames.ruleengine.rules.join.GameJoinEventGameHasNotStarted;
import nl.ritogames.ruleengine.rules.join.GameJoinEventPlayerExists;
import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.event.GameJoinEvent;
import nl.ritogames.shared.enums.GameStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Sets the rules for a GameJoinEvent
 */
public class GameJoinEventValidator extends ValidationStrategy<GameJoinEvent> {

    protected List<Rule<GameJoinEvent>> lobbyRules;
    protected List<Rule<GameJoinEvent>> gameRules;

    @Override
    public boolean validateEvent(GameJoinEvent event, GameStateModifier modifier) {
        if (modifier.getGameState().getStatus() == GameStatus.CREATED) {
            super.setRules(lobbyRules);
        } else {
            super.setRules(gameRules);
        }
        for (Rule<GameJoinEvent> rule : rules) {
            if (!rule.validate(event, modifier)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Instantiates rules for the CreateEvent
     * <p>
     * If GameStatus is CREATED:
     * Check if the game exists
     * Check if game has started
     * Check for duplicates
     * <p>
     * If GameStatus is STARTED:
     * Check if game has started
     * Check if player already exists
     */

    @Override
    public void instantiateRules() {
        lobbyRules = new ArrayList<>();
        gameRules = new ArrayList<>();
        addRulesToLists();
    }

    private void addRulesToLists() {
        lobbyRules.add(new GameJoinEventGameHasNotStarted());
        lobbyRules.add(new GameJoinEventDisallowDuplicate());
        gameRules.add(new GameIsInProgress<>());
        gameRules.add(new GameJoinEventPlayerExists());
    }

}
