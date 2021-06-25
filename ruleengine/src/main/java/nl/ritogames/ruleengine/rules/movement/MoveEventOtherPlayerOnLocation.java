package nl.ritogames.ruleengine.rules.movement;

import nl.ritogames.ruleengine.rules.Rule;
import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.GameState;
import nl.ritogames.shared.dto.event.MoveEvent;
import nl.ritogames.shared.dto.gameobject.ASDVector;
import nl.ritogames.shared.dto.gameobject.individual.Individual;

/**
 * Validate if the tile the Individual wants to move to isn't occupied
 */
public class MoveEventOtherPlayerOnLocation implements Rule<MoveEvent> {
    /**
     * Validate if the tile the Individual wants to move to isn't occupied
     *
     * @param event    an instance of InteractionEvent
     * @param modifier contains the GameStateModifier so data from the GameState can be fetched
     * @return whether the tile is occupied by another Individual or not
     */
    @Override
    public boolean validate(MoveEvent event, GameStateModifier modifier) {
        GameState gameState = modifier.getGameState();
        Individual individual = gameState.getIndividual(event.getIndividualId());
        ASDVector location = new ASDVector(individual.getLocation().getX(), individual.getLocation().getY());
        location = modifier.getNextLocation(location, event.getDirection());

        if(!gameState.getWorld().checkOutOfMap(location.getX(), location.getY())){
            return !gameState.getWorld().getTile(location).hasIndividual();
        }else {
            return false;
        }
    }

}