package nl.ritogames.parser.command.agentcommand;

import nl.ritogames.parser.command.Command;
import nl.ritogames.parser.command.annotation.CommandParam;
import nl.ritogames.shared.dto.event.CreateAgentEvent;
import nl.ritogames.shared.dto.event.Event;
import nl.ritogames.shared.exception.InvalidArgumentException;
import nl.ritogames.shared.logger.Logger;

import java.util.Optional;

public class CreateAgentCommand implements Command {

  private static final String COMMAND_STRING = "create";

  String name;

  @CommandParam(index = 1)
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public Event toInteractionEvent(String replaced) throws InvalidArgumentException {
    Logger.logMethodCall(this);
    return new CreateAgentEvent(this.getName()
        .orElseThrow(() -> new InvalidArgumentException(
            COMMAND_STRING, "name",
            "Expected a name as a first positional argument for create. Got nothing."))
    );
  }

  @Override
  public String getUsage() {
    return "agent create <name>";
  }

  private Optional<String> getName() {
    return Optional.ofNullable(name);
  }
}
