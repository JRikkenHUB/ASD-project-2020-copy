package nl.ritogames.parser.command.interactioncommand;

import nl.ritogames.shared.dto.event.Event;
import nl.ritogames.shared.dto.event.MoveEvent;
import nl.ritogames.shared.logger.Logger;

public class MoveInteractionCommand extends DirectionalInteractionCommand {

  public static final String COMMAND_STRING = "move";

  @Override
  public Event toInteractionEvent(String replaced) {
    Logger.logMethodCall(this);
    return new MoveEvent("", this.direction);
  }

  @Override
  public String getUsage() {
    return "move <direction>";
  }

  @Override
  protected String getCommandString() {
    return COMMAND_STRING;
  }
}
