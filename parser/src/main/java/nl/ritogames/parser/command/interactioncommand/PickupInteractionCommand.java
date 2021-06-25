package nl.ritogames.parser.command.interactioncommand;

import nl.ritogames.shared.dto.event.Event;
import nl.ritogames.shared.dto.event.PickUpEvent;
import nl.ritogames.shared.exception.InvalidArgumentException;
import nl.ritogames.shared.logger.Logger;

public class PickupInteractionCommand extends DirectionalInteractionCommand {

  private static final String COMMAND_STRING = "pickup";

  @Override
  public Event toInteractionEvent(String replaced) throws InvalidArgumentException {
    Logger.logMethodCall(this);
    PickUpEvent event = new PickUpEvent();
    event.setDirection(this.direction);
    return event;
  }

  @Override
  public String getUsage() {
    return "pickup direction";
  }

  @Override
  protected String getCommandString() {
    return COMMAND_STRING;
  }
}
