package nl.ritogames.shared.dto.command.individual;

import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.gameobject.ASDVector;
import nl.ritogames.shared.dto.gameobject.individual.Individual;
import nl.ritogames.shared.dto.gameobject.world.tile.AccessibleWorldTile;
import nl.ritogames.shared.dto.gameobject.world.tile.TrapTile;
import nl.ritogames.shared.enums.Direction;
import nl.ritogames.shared.exception.CommandFailedException;
import nl.ritogames.shared.exception.ModificationException;
import nl.ritogames.shared.exception.NotImplementedException;
import nl.ritogames.shared.exception.PlayerHasDiedException;

public class MoveCommand extends IndividualCommand {
    private Direction direction;

    public MoveCommand() {
    }

    /**
     * A command used when moving an individual in a certain direction
     * @param initiatingIndividualId
     * @param direction
     */
    public MoveCommand(String initiatingIndividualId, Direction direction) {
        super(initiatingIndividualId);
        this.direction = direction;
    }

    @Override
    public void execute(GameStateModifier gameStateModifier) throws CommandFailedException, PlayerHasDiedException {
        try {
            Individual individual = gameStateModifier.getGameState().getIndividual(initiatingIndividualId);

            ASDVector currentLocation = new ASDVector(individual.getLocation().getX(), individual.getLocation().getY());
            AccessibleWorldTile currentTile = gameStateModifier.getAccessibleTile(currentLocation);

            gameStateModifier.removeIndividualFromAccessibleTile(currentTile);

            ASDVector nextLocation = gameStateModifier.getNextLocation(currentLocation, direction);
            AccessibleWorldTile nextTile = gameStateModifier.getAccessibleTile(nextLocation);

            if(nextTile instanceof TrapTile){
                gameStateModifier.damageIndividual(individual, ((TrapTile) nextTile).getTrap().getDamage());
                ((TrapTile) nextTile).getTrap().setVisible(true);
            }
            gameStateModifier.addIndividualToAccessibleTile(individual, nextTile);
            individual.setLocation(nextLocation);
        } catch (ModificationException e) {
//            throw new CommandFailedException("MoveCommand failed to execute",e);
        } catch (PlayerHasDiedException e) {
            throw e;
        }
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }


    @Override
    public String generateUIText() {
        throw new NotImplementedException();
    }
}
