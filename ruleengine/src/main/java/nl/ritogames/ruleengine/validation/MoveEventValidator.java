package nl.ritogames.ruleengine.validation;

import nl.ritogames.ruleengine.rules.general.GameIsInProgress;
import nl.ritogames.ruleengine.rules.general.PlayerIsAlive;
import nl.ritogames.ruleengine.rules.movement.MoveEventLocationInaccessible;
import nl.ritogames.ruleengine.rules.movement.MoveEventOtherPlayerOnLocation;
import nl.ritogames.shared.dto.event.MoveEvent;

/**
 * Sets the rules for a MoveEvent
 */
public class MoveEventValidator extends ValidationStrategy<MoveEvent> {

    /**
     * Instantiates rules for the MoveEvent
     * <p>
     * Check if the player is alive
     * Check if the location to move to is accessible
     * Check if the location to move to isn't occupied
     * Check if the game has started
     */
    @Override
    public void instantiateRules() {
        this.rules.add(new PlayerIsAlive<>());
        this.rules.add(new MoveEventLocationInaccessible());
        this.rules.add(new MoveEventOtherPlayerOnLocation());
        this.rules.add(new GameIsInProgress<>());
    }
}