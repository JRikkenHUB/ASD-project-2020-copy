package nl.ritogames.trui.exception.mappers;

import nl.ritogames.shared.exception.InvalidArgumentException;
import nl.ritogames.trui.exception.ExceptionMapper;
import nl.ritogames.trui.render.ScreenRenderer;

/**
 * <p>Handles an InvalidArgumentException by mapping it and notifying the user of it's existence
 * without ending the game</p>
 */
public class InvalidArgumentExceptionMapper implements ExceptionMapper<InvalidArgumentException> {

  private final ScreenRenderer renderer;

  public InvalidArgumentExceptionMapper(ScreenRenderer renderer) {
    this.renderer = renderer;
  }

  @Override
  public void handle(InvalidArgumentException exception) {
    renderer.setError(exception.getMessage());
  }
}
