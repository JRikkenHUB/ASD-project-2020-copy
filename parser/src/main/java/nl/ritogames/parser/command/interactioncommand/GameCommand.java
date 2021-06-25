package nl.ritogames.parser.command.interactioncommand;

import nl.ritogames.parser.command.Command;
import nl.ritogames.parser.command.annotation.CommandParam;
import nl.ritogames.shared.dto.event.*;
import nl.ritogames.shared.exception.InvalidArgumentException;
import nl.ritogames.shared.logger.Logger;

import java.util.Optional;
import java.util.StringJoiner;

public class GameCommand implements Command {

  private static final String COMMAND = "game";

  GameCommandType commandType;
  Optional<String> name = Optional.empty();
  Optional<String> ip = Optional.empty();

  @CommandParam(index = 1)
  public void setCommandType(String string) throws InvalidArgumentException {
    try {
      this.commandType = GameCommandType.valueOf(string.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new InvalidArgumentException(COMMAND, "action",
          String.format(
              "Command game expects a valid GameCommandType at position 1. Got %s but expected: %s",
              string, getPossibleTypes()));
    }
  }

  @CommandParam(index = 2, optional = true)
  public void setIp(String ip) {
    this.ip = Optional.of(ip);
  }

  @CommandParam(index = 2, optional = true)
  public void setName(String name) {
    this.name = Optional.of(name);
  }

  @Override
  public Event toInteractionEvent(String replaced) throws InvalidArgumentException {
    Logger.logMethodCall(this);
    return switch (commandType) {
      case START -> createStartGameEvent();
      case CREATE -> createCreateGameEvent();
      case SAVE -> createSaveGameEvent();
      case JOIN -> createGameJoinEvent();
      case EXIT -> createGameExitEvent();
      default -> throw new InvalidArgumentException(COMMAND, "action",
          "Unhandled action encountered. This is the mistake of a developer!");
    };
  }

  @Override
  public String getUsage() {
    return "game <action> [param]";
  }

  private GameSaveEvent createSaveGameEvent() throws InvalidArgumentException {
    return new GameSaveEvent(null,
        name.orElseThrow(() -> new InvalidArgumentException(COMMAND, "name",
            "Missing parameter name. Usage: game save <name>"))
    );
  }

  private StartGameEvent createStartGameEvent() throws InvalidArgumentException {
    Logger.logMethodCall(this);
    return new StartGameEvent(null,
        name.orElseThrow(() -> new InvalidArgumentException(COMMAND, "name",
            "Missing parameter name. Usage: game save <name>")), null
    );
  }

  private GameJoinEvent createGameJoinEvent() throws InvalidArgumentException {
    return new GameJoinEvent(
        ip.orElseThrow(() -> new InvalidArgumentException(COMMAND, "ip",
            "Missing parameter ip. Usage: game join <ip>")));
  }

  private CreateGameEvent createCreateGameEvent() throws InvalidArgumentException {
    Logger.logMethodCall(this);
    return new CreateGameEvent(
        name.orElseThrow(() -> new InvalidArgumentException(COMMAND, "name",
            "Missing parameter name. Usage: game create <name>"))
    );
  }

  private GameExitEvent createGameExitEvent() {
    return new GameExitEvent();
  }

  private String getPossibleTypes() {
    StringJoiner stringJoiner = new StringJoiner(", ");
    for (GameCommandType value : GameCommandType.values()) {
      stringJoiner.add(value.name().toLowerCase());
    }
    return stringJoiner.toString();
  }
}
