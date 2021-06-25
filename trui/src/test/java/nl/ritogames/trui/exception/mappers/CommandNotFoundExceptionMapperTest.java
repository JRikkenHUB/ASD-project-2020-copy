package nl.ritogames.trui.exception.mappers;

import nl.ritogames.shared.exception.CommandNotFoundException;
import nl.ritogames.trui.render.ScreenRenderer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CommandNotFoundExceptionMapperTest {

  CommandNotFoundExceptionMapper sut;
  ScreenRenderer mockedScreenRenderer;

  @BeforeEach
  void setUp() {
    mockedScreenRenderer = Mockito.mock(ScreenRenderer.class);
    sut = new CommandNotFoundExceptionMapper(mockedScreenRenderer);
  }

  @Test
  void handleShouldCallScreenRendererSetError() {
    CommandNotFoundException commandNotFoundException = Mockito.mock(CommandNotFoundException.class);
    Mockito.when(commandNotFoundException.getCommand()).thenReturn("test");


    sut.handle(commandNotFoundException);

    Mockito.verify(mockedScreenRenderer, Mockito.times(1)).setError("Command test was not found. Please enter a different command!");
  }
}