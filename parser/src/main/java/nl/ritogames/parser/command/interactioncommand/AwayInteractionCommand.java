package nl.ritogames.parser.command.interactioncommand;

import nl.ritogames.parser.command.Command;
import nl.ritogames.shared.dto.event.Event;
import nl.ritogames.shared.dto.event.StartAgentEvent;
import nl.ritogames.shared.exception.InvalidArgumentException;
import nl.ritogames.shared.logger.Logger;

public class AwayInteractionCommand implements Command {

  @Override
  public Event toInteractionEvent(String replaced) throws InvalidArgumentException {
    Logger.logMethodCall(this);
    return new StartAgentEvent();
  }

  @Override
  public String getUsage() {
    return "afk";
  }
}
