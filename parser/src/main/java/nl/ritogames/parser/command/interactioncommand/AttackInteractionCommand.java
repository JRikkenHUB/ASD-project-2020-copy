package nl.ritogames.parser.command.interactioncommand;

import nl.ritogames.shared.dto.event.AttackEvent;
import nl.ritogames.shared.dto.event.Event;
import nl.ritogames.shared.exception.InvalidArgumentException;
import nl.ritogames.shared.logger.Logger;

public class AttackInteractionCommand extends DirectionalInteractionCommand {

  public static final String COMMAND_STRING = "attack";

  @Override
  public Event toInteractionEvent(String replaced) throws InvalidArgumentException {
    Logger.logMethodCall(this);
    return new AttackEvent("",direction, "");
  }

  @Override
  public String getUsage() {
    return "attack <direction>";
  }

  @Override
  protected String getCommandString() {
    return COMMAND_STRING;
  }
}
