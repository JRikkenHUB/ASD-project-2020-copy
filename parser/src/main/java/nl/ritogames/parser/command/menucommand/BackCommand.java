package nl.ritogames.parser.command.menucommand;

import nl.ritogames.parser.command.Command;
import nl.ritogames.shared.dto.event.BackEvent;
import nl.ritogames.shared.dto.event.Event;
import nl.ritogames.shared.exception.InvalidArgumentException;
import nl.ritogames.shared.logger.Logger;

public class BackCommand implements Command {

  @Override
  public Event toInteractionEvent(String replaced) throws InvalidArgumentException {
    Logger.logMethodCall(this);
    return new BackEvent();
  }

  @Override
  public String getUsage() {
    return "back";
  }
}
