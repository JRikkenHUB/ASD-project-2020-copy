package nl.ritogames.trui.render;

import nl.ritogames.shared.dto.GameStateContext;
import nl.ritogames.trui.render.message.Message;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;

public class UIContextTest {

  GameStateContext mockGameStateContext;
  List<Message> messages;
  UIContext sut;

  @BeforeEach
  void setup() {
    mockGameStateContext = mock(GameStateContext.class);
    Message mockMessage = mock(Message.class);
    messages = new ArrayList<>();
    messages.add(mockMessage);
    sut = new UIContext(mockGameStateContext, messages);
  }

  @Test
  void TestIfUIContextReturnsGameStateContext() {
    //act
    GameStateContext actual = sut.getGameStateContext();

    //assert
    Assertions.assertEquals(mockGameStateContext, actual);
  }

  @Test
  void TestIfUIContextReturnsMessages() {
    //act
    List<Message> actual = sut.getMessages();

    //assert
    Assertions.assertEquals(messages, actual);
  }

}
