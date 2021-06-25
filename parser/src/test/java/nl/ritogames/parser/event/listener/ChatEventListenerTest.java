package nl.ritogames.parser.event.listener;

import nl.ritogames.shared.Chat;
import nl.ritogames.shared.dto.event.ChatEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ChatEventListenerTest {

  private ChatEventListener sut;
  private Chat chat;

  @BeforeEach
  void setUp() {
    chat = Mockito.mock(Chat.class);
    sut = new ChatEventListener(chat);
  }

  @Test
  void onEventShouldCallChatWithCorrectMessage() {
    var expected = "some string";
    var event = new ChatEvent();
    event.setMessage(expected);
    ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
    Mockito.when(chat.sendMessage(argumentCaptor.capture())).thenReturn(true);

    sut.onEvent(event);

    assertEquals(expected, argumentCaptor.getValue());
  }
}
