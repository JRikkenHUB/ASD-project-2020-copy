package nl.ritogames.ruleengine.rules.helpers;

import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.event.GameEventWithDirection;
import nl.ritogames.shared.dto.gameobject.ASDVector;
import nl.ritogames.shared.dto.gameobject.individual.Individual;
import nl.ritogames.shared.dto.gameobject.world.tile.WorldTile;
import nl.ritogames.shared.enums.Direction;

public class Helper {
    private Helper() {
        //Standard constructor
    }

    /**
     * Gets the tile where a given event will be executed.
     *
     * @param event     An instance of Event.
     * @param modifier  Contains the GameStateModifier so data from the GameState can be fetched.
     * @return          The tile where the event will be executed
     */
    public static WorldTile getTileFromEvent(GameEventWithDirection event, GameStateModifier modifier) {
        Direction direction = event.getDirection();
        Individual individual = modifier.getGameState().getIndividual(event.getIndividualId());
        ASDVector vector = new ASDVector(individual.getLocation().getX(), individual.getLocation().getY());
        switch (direction) {
            case EAST -> vector.setX(vector.getX() + 1);
            case WEST -> vector.setX(vector.getX() - 1);
            case NORTH -> vector.setY(vector.getY() - 1);
            case SOUTH -> vector.setY(vector.getY() + 1);
            default -> throw new IllegalArgumentException();
        }
        return modifier.getGameState().getWorld().getTile(vector);
    }
}
