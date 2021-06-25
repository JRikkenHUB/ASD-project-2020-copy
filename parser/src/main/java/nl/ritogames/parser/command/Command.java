package nl.ritogames.parser.command;

import nl.ritogames.shared.dto.event.Event;
import nl.ritogames.shared.exception.InvalidArgumentException;


public interface Command {

  Event toInteractionEvent(String replaced) throws InvalidArgumentException;

  String getUsage();
}
