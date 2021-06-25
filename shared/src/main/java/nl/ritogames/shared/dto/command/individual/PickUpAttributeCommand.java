package nl.ritogames.shared.dto.command.individual;

import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.gameobject.ASDVector;
import nl.ritogames.shared.dto.gameobject.individual.Character;
import nl.ritogames.shared.dto.gameobject.world.tile.AccessibleWorldTile;
import nl.ritogames.shared.enums.Direction;
import nl.ritogames.shared.exception.CommandFailedException;
import nl.ritogames.shared.exception.ModificationException;
import nl.ritogames.shared.exception.NotImplementedException;

public class PickUpAttributeCommand extends IndividualCommand {
    private Direction direction;

    /**
     * Command used when an individual picks up an attribute
     * @param initiatingIndividualId
     * @param direction
     */
    public PickUpAttributeCommand(String initiatingIndividualId, Direction direction) {
        super(initiatingIndividualId);
        this.direction = direction;
    }

    public PickUpAttributeCommand(){
    }

    @Override
    public void execute(GameStateModifier gameStateModifier) throws CommandFailedException {
        try{
        Character character = (Character) gameStateModifier.getGameState().getIndividual(initiatingIndividualId);
        ASDVector attemptedPickupLocation = gameStateModifier.getNextLocation(character.getLocation(), direction);
        AccessibleWorldTile attributeTile = gameStateModifier.getAccessibleTile(attemptedPickupLocation);
            attributeTile.getAttribute().apply(character);
            attributeTile.removeAttribute();
        } catch (ModificationException e) {
            throw new CommandFailedException("", e);
        }
    }

    @Override
    public String generateUIText() {
        throw new NotImplementedException();
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
