package nl.ritogames.trui;

import nl.ritogames.shared.Chat;
import nl.ritogames.shared.GameStateContextProvider;
import nl.ritogames.shared.Parser;
import nl.ritogames.shared.dto.GameStateContext;
import nl.ritogames.shared.exception.ParseInputException;
import nl.ritogames.trui.exception.ExceptionHandler;
import nl.ritogames.trui.input.InputHandler;
import nl.ritogames.trui.render.OnCommandRenderStrategy;
import nl.ritogames.trui.render.RealtimeRenderStrategy;
import nl.ritogames.trui.render.RenderStrategy;
import nl.ritogames.trui.render.RenderStrategy.Strategy;
import nl.ritogames.trui.render.ScreenRenderer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class TruiTest {

  private Trui sut;
  private GameStateContextProvider gameStateContextProvider;
  private Chat chat;
  private Parser parser;
  private InputHandler inputHandler;
  private ScreenRenderer screenRenderer;
  private RenderStrategy renderStrategy;
  private ExceptionHandler exceptionHandler;

  @BeforeEach
  void setUp() {
    sut = new Trui();
    parser = Mockito.mock(Parser.class);
    chat = Mockito.mock(Chat.class);
    gameStateContextProvider = Mockito.mock(GameStateContextProvider.class);
    inputHandler = Mockito.mock(InputHandler.class);
    screenRenderer = Mockito.mock(ScreenRenderer.class);
    renderStrategy = Mockito.mock(RenderStrategy.class);
    exceptionHandler = Mockito.mock(ExceptionHandler.class);

    sut.setParser(parser);
    sut.setContextProvider(gameStateContextProvider);
    sut.setChat(chat);
    sut.inputHandler = inputHandler;
    sut.renderer = screenRenderer;
    sut.renderStrategy = renderStrategy;
    sut.exceptionHandler = exceptionHandler;
  }

  @Test
  void startShouldStartWithOnCommandRenderStrategy() {
    //guarantee exit clause
    Mockito.when(inputHandler.removeAndFetchInput()).thenReturn("test");
    Mockito.when(inputHandler.peek()).thenReturn("exit");

    sut.start();

    assertTrue(sut.renderStrategy instanceof OnCommandRenderStrategy);
  }

  @Test
  void runShouldRequestIndividualIdWhenIndividualIdIsEmpty() {
    //arrange
    Mockito.when(inputHandler.isEmpty()).thenReturn(true);

    //act
    sut.run();

    //assert
    Mockito.verify(inputHandler, Mockito.times(1)).requestInput();
  }

  @Test
  void runShouldSetIndividualIdWithNonEmptyInput() {
    //arrange
    var expected = "test";
    Mockito.when(inputHandler.isEmpty()).thenReturn(false);
    Mockito.when(inputHandler.removeAndFetchInput()).thenReturn(expected);

    //act
    sut.run();

    //assert
    assertEquals(expected, sut.individualId);
  }

  @Test
  void runShouldRequestAnotherInputWithNonEmptyInput() {
    //arrange
    var expected = "";
    Mockito.when(inputHandler.isEmpty()).thenReturn(false);
    Mockito.when(inputHandler.peek()).thenReturn(expected);

    //act
    sut.run();

    //assert
    Mockito.verify(inputHandler, Mockito.times(1)).requestInput();
  }

  @Test
  void runShouldUpdateStrategyWhenRenderingGame() throws ParseInputException {
    //arrange
    sut.individualId = "test";
    Mockito.when(inputHandler.peek()).thenReturn("");

    //act
    sut.run();

    //assert
    Mockito.verify(renderStrategy, Mockito.times(1)).update(sut.individualId);
  }

  @Test
  void runShouldRequestInputWhenRenderingGame() {
    //arrange
    sut.individualId = "test";
    Mockito.when(inputHandler.peek()).thenReturn("");

    //act
    sut.run();

    //assert
    Mockito.verify(inputHandler, Mockito.times(1)).requestInput();
  }

  @Test
  void runShouldRenderWhenExceptionIsThrown() throws ParseInputException {
    //arrange
    sut.individualId = "test";
    Mockito.when(inputHandler.peek()).thenReturn("");
    var exception = new ParseInputException("");
    var mockedContext = Mockito.mock(GameStateContext.class);
    Mockito.doThrow(exception).when(renderStrategy).update(sut.individualId);
    Mockito.when(gameStateContextProvider.getContext(sut.individualId)).thenReturn(mockedContext);

    //act
    sut.run();

    //assert
    Mockito.verify(screenRenderer).render(mockedContext);
  }

  @Test
  void runShouldHandleExceptionWhenExceptionIsThrown() throws ParseInputException {
    //arrange
    sut.individualId = "test";
    Mockito.when(inputHandler.peek()).thenReturn("");
    var exception = new ParseInputException("");
    Mockito.doThrow(exception).when(renderStrategy).update(sut.individualId);

    //act
    sut.run();

    //assert
    Mockito.verify(exceptionHandler, Mockito.times(1)).handleException(exception);
  }

  @Test
  void runShouldCallRenderExitScreenWhenGivenInputExit() throws ParseInputException {
    //arrange
    sut.individualId = "test";
    Mockito.when(inputHandler.peek()).thenReturn("exit");
    var exception = new ParseInputException("");
    Mockito.doThrow(exception).when(renderStrategy).update(sut.individualId);

    //act
    sut.run();

    //assert
    Mockito.verify(screenRenderer, Mockito.times(1)).renderExitScreen();
  }

  @Test
  void runShouldSetRunningToFalseWhenGivenInputExit() {
    //arrange
    sut.running = true;
    sut.individualId = "something";
    Mockito.when(inputHandler.peek()).thenReturn("exit");

    //act
    sut.run();

    //assert
    assertFalse(sut.running);
  }

  @Test
  void toggleRenderStrategyShouldSetOnCommandRenderStrategyWhenOnRealtimeRenderStrategy() {
    Mockito.when(renderStrategy.getStrategy()).thenReturn(Strategy.REALTIME);

    sut.toggleRenderStrategy();

    assertTrue(sut.renderStrategy instanceof OnCommandRenderStrategy);
  }

  @Test
  void toggleRenderStrategyShouldSetRealtimeRenderStrategyWhenOnOnCommandRenderStrategy() {
    Mockito.when(renderStrategy.getStrategy()).thenReturn(Strategy.ONCOMMAND);

    sut.toggleRenderStrategy();

    assertTrue(sut.renderStrategy instanceof RealtimeRenderStrategy);
  }

  @Test
  void getIndividualIdShouldReturnCorrectIndividualId() {
    var expected = "test";
    sut.individualId = expected;

    var actual = sut.getIndividualId();

    assertEquals(expected, actual);
  }
}
