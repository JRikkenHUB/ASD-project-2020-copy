package nl.ritogames.shared.dto.command.individual;

import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.gameobject.individual.Individual;
import nl.ritogames.shared.dto.gameobject.world.tile.AccessibleWorldTile;
import nl.ritogames.shared.enums.Direction;
import nl.ritogames.shared.exception.CommandFailedException;
import nl.ritogames.shared.exception.ModificationException;
import nl.ritogames.shared.exception.NotImplementedException;
import nl.ritogames.shared.exception.PlayerHasDiedException;

public class DamageIndividualCommand extends IndividualCommand {
    private Direction direction;

    /**
     * Damages a single individual for a certain amount of damage, for example walking on a trap
     */
    public DamageIndividualCommand(String initiatingIndividual, Direction direction) {
        super(initiatingIndividual);
        this.direction = direction;
    }

    @Override
    public void execute(GameStateModifier gameStateModifier) throws CommandFailedException, PlayerHasDiedException {
        try {
            Individual individual = gameStateModifier.getGameState().getIndividual(initiatingIndividualId);
            AccessibleWorldTile accessibleWorldTile = gameStateModifier.getAccessibleTile(gameStateModifier.getNextLocation(individual.getLocation(), direction));
            if(accessibleWorldTile.hasIndividual()){
                gameStateModifier.damageIndividual(accessibleWorldTile.getIndividual(), individual.getAttack());
            }
        } catch (ModificationException e) {
            throw new CommandFailedException("DamageIndividualCommand failed", e);
        } catch (PlayerHasDiedException e) {
            throw e;
        }
    }

    @Override
    public String generateUIText() {
        throw new NotImplementedException();
    }

  public Direction getDirection() {
      return this.direction;
  }
}
