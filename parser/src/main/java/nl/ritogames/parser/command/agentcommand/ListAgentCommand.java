package nl.ritogames.parser.command.agentcommand;

import nl.ritogames.parser.command.Command;
import nl.ritogames.shared.dto.event.Event;
import nl.ritogames.shared.dto.event.ListAgentEvent;
import nl.ritogames.shared.exception.InvalidArgumentException;
import nl.ritogames.shared.logger.Logger;

public class ListAgentCommand implements Command {

  @Override
  public Event toInteractionEvent(String replaced) throws InvalidArgumentException {
    Logger.logMethodCall(this);
    return new ListAgentEvent("");
  }

  @Override
  public String getUsage() {
    return "agent list";
  }

}
