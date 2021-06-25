package nl.ritogames.parser.command.interactioncommand;

import nl.ritogames.parser.command.Command;
import nl.ritogames.parser.command.annotation.CommandParam;
import nl.ritogames.shared.enums.Direction;
import nl.ritogames.shared.exception.InvalidArgumentException;

public abstract class DirectionalInteractionCommand implements Command {

  Direction direction;

  @CommandParam(index = 1)
  public void setDirection(String direction) throws InvalidArgumentException {
    try {
      this.direction = Direction.getDirection(direction);
    } catch (IllegalArgumentException e) {
      throw new InvalidArgumentException(getCommandString(), direction,
          String.format("Expected first argument direction, but got %s. Valid inputs: North, East, South, West", direction));
    }
  }

  protected abstract String getCommandString();

}
