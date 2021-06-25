package nl.ritogames.trui.exception.mappers;

import nl.ritogames.shared.exception.InvalidArgumentException;
import nl.ritogames.trui.render.ScreenRenderer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class InvalidArgumentExceptionMapperTest {

  private InvalidArgumentExceptionMapper sut;
  private ScreenRenderer screenRendererMock;
  private InvalidArgumentException invalidArgumentExceptionMock;

  @BeforeEach
  void setUp() {
    screenRendererMock = Mockito.mock(ScreenRenderer.class);
    invalidArgumentExceptionMock = Mockito.mock(InvalidArgumentException.class);
    sut = new InvalidArgumentExceptionMapper(screenRendererMock);
  }

  @Test
  void handleCallsAddMessageOnTheRenderer() {
    Mockito.when(invalidArgumentExceptionMock.getMessage()).thenReturn("S");
    sut.handle(invalidArgumentExceptionMock);
    Mockito.verify(screenRendererMock).setError(Mockito.any(String.class));
  }
}
