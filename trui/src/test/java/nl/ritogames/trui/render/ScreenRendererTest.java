package nl.ritogames.trui.render;

import nl.ritogames.shared.dto.GameStateContext;
import nl.ritogames.trui.enums.ScreenState;
import nl.ritogames.trui.render.message.Message;
import nl.ritogames.trui.uicomponent.chat.LimitedQueue;
import nl.ritogames.trui.uicomponent.screen.Screen;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ScreenRendererTest {

  private static final ScreenState ANY_SCREEN_STATE = ScreenState.GAME;
  ScreenRenderer sut;
  GameStateContext gameStateContextMock;
  Screen mockedScreen;

  @BeforeEach
  void setUp() {
    sut = new ScreenRenderer();
    gameStateContextMock = mock(GameStateContext.class);
    mockedScreen = mock(Screen.class);
    sut.messages = mock(LimitedQueue.class);
    sut.screens.clear();
    sut.screens.put(ANY_SCREEN_STATE, mockedScreen);
    sut.currentState = ANY_SCREEN_STATE;
  }

  @Test
  void render() {
    sut.render(gameStateContextMock);

    verify(mockedScreen, times(1)).draw(any(UIContext.class));
  }

  @Test
  void addMessage() {
    //arrange
    ArgumentCaptor<Message> messageArgumentCaptor = ArgumentCaptor.forClass(Message.class);
    var expected = new Message("S", "");

    //act
    sut.addMessage(expected);

    //assert
    verify(sut.messages, times(1)).add(messageArgumentCaptor.capture());
    assertEquals("S", messageArgumentCaptor.getValue().getContent());
  }

  @Test
  void renderExitScreen() {
    //arrange
    PrintStream standard = System.out;
    ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStreamCaptor));

    //act
    sut.renderExitScreen();

    //assert
    assertEquals("Thank you for playing, goodbye!", outputStreamCaptor.toString().trim());

    //teardown
    System.setOut(standard);
  }

  @Test
  void isAtStateShouldReturnTrueWhenOnSameState() {
    sut.currentState = ScreenState.GAME;

    var actual = sut.isAtState(ScreenState.GAME);

    assertTrue(actual);
  }

  @Test
  void isAtStateShouldReturnFalseWhenNotSameState() {
    sut.currentState = ScreenState.LOBBY;

    var actual = sut.isAtState(ScreenState.GAME);

    assertFalse(actual);
  }

  @Test
  void setStateShouldClearMessages() {
    sut.messages.add(Mockito.mock(Message.class));

    sut.setState(ScreenState.GAME);

    assertEquals(0, sut.messages.size());
  }

  @Test
  void setStateShouldSetState() {
    var expected = ScreenState.GAME;

    sut.setState(expected);

    assertEquals(expected, sut.currentState);
  }
}
