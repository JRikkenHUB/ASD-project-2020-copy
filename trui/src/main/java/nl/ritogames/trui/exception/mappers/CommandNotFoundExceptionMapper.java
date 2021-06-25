package nl.ritogames.trui.exception.mappers;

import nl.ritogames.shared.exception.CommandNotFoundException;
import nl.ritogames.trui.exception.ExceptionMapper;
import nl.ritogames.trui.render.ScreenRenderer;

public class CommandNotFoundExceptionMapper implements ExceptionMapper<CommandNotFoundException> {

  private final ScreenRenderer screenRenderer;

  public CommandNotFoundExceptionMapper(ScreenRenderer renderer) {
    this.screenRenderer = renderer;
  }

  @Override
  public void handle(CommandNotFoundException exception) {
    screenRenderer.setError(
        String.format("Command %s was not found. Please enter a different command!",
            exception.getCommand()));
  }
}
