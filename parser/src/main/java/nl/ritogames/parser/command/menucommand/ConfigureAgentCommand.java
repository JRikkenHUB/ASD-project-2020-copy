package nl.ritogames.parser.command.menucommand;

import nl.ritogames.parser.command.Command;
import nl.ritogames.shared.dto.event.AgentConfigureEvent;
import nl.ritogames.shared.dto.event.Event;
import nl.ritogames.shared.exception.InvalidArgumentException;
import nl.ritogames.shared.logger.Logger;

public class ConfigureAgentCommand implements Command {

  @Override
  public Event toInteractionEvent(String replaced) throws InvalidArgumentException {
    Logger.logMethodCall(this);
    return new AgentConfigureEvent();
  }

  @Override
  public String getUsage() {
    return "agent configure";
  }
}
