package nl.ritogames.trui.render.message;

import nl.ritogames.shared.dto.command.Command;

@FunctionalInterface
public interface MessageFormatter<T extends Command> {
  public String toMessage(T command);
}
