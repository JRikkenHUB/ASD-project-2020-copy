package nl.ritogames.trui.render;

import nl.ritogames.shared.GameStateContextProvider;
import nl.ritogames.shared.Parser;
import nl.ritogames.shared.exception.ParseInputException;
import nl.ritogames.trui.Trui;
import nl.ritogames.trui.input.InputHandler;
import nl.ritogames.trui.render.RenderStrategy.Strategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OnCommandRenderStrategyTest {

  OnCommandRenderStrategy sut;
  Trui mockTrui;
  Parser mockParser;
  InputHandler mockInputHandler;
  GameStateContextProvider mockContextProvider;
  ScreenRenderer mockScreenRenderer;

  @BeforeEach
  void setup() {
    mockTrui = mock(Trui.class);
    mockParser = mock(Parser.class);
    mockInputHandler = mock(InputHandler.class);
    mockContextProvider = mock(GameStateContextProvider.class);
    mockScreenRenderer = mock(ScreenRenderer.class);

    sut = new OnCommandRenderStrategy(mockTrui, mockParser, mockScreenRenderer, mockInputHandler,
        mockContextProvider);
  }

  @Test
  void testIfTheReturnedStrategyIsCorrect() {
    //act
    Strategy actual = sut.getStrategy();
    //assert
    assertEquals(Strategy.ONCOMMAND, actual);
  }

  @Test
  void testIfTruiToggleRenderStrategyIsCalled() throws ParseInputException {
    //arrange
    when(mockInputHandler.isEmpty()).thenReturn(true);
    //act
    sut.update("");
    //assert
    verify(mockTrui, times(1)).toggleRenderStrategy();
  }

  @Test
  void testIfUpdateCallsMethodsCorrectly() throws ParseInputException {
    //arrange
    String mockInput = "test";
    String mockId = "";
    when(mockInputHandler.isEmpty()).thenReturn(false);
    when(mockInputHandler.peek()).thenReturn(mockInput);
    //act
    sut.update(mockId);
    //assert
    verify(mockParser, times(1)).parseInput(mockInput, mockId);
    verify(mockScreenRenderer, times(1)).render(any());
  }

  @Test
  void testIfExceptionIsThrown() throws ParseInputException {
    String mockInput = "game update";
    String mockId = "";
    when(mockInputHandler.isEmpty()).thenReturn(false);
    when(mockInputHandler.peek()).thenReturn(mockInput);
    doThrow(new ParseInputException("")).when(mockParser).parseInput(any(), any());

    //act
    Assertions.assertThrows(ParseInputException.class, () -> sut.update(mockId));

  }
}
