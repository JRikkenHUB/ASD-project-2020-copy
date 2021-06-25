package nl.ritogames.trui.render;

import nl.ritogames.shared.exception.ParseInputException;
import nl.ritogames.trui.Trui;
import nl.ritogames.trui.input.InputHandler;
import nl.ritogames.trui.render.RenderStrategy.Strategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class RealtimeRenderStrategyTest {

  InputHandler mockInputHandler;
  Trui mockTrui;
  ScreenRenderer mockScreenRenderer;

  RealtimeRenderStrategy sut;

  @BeforeEach
  void setup() {
    mockInputHandler = mock(InputHandler.class);
    mockTrui = mock(Trui.class);
    mockScreenRenderer = mock(ScreenRenderer.class);

    sut = new RealtimeRenderStrategy(mockScreenRenderer, mockInputHandler, mockTrui);
  }

  @Test
  void testGetStrategy() {
    //act
    Strategy actual = sut.getStrategy();
    //assert
    assertEquals(Strategy.REALTIME, actual);
  }

  @Test
  void testIfToggleRenderStrategyGetsCalledOnEmptyInput() throws ParseInputException {
    //arrange
    when(mockInputHandler.isEmpty()).thenReturn(true);
    //act
    sut.update("");
    //assert
    verify(mockTrui, times(1)).toggleRenderStrategy();
  }
}
