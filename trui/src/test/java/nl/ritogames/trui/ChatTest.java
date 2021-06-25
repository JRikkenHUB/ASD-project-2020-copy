package nl.ritogames.trui;

import nl.ritogames.trui.render.UIContext;
import nl.ritogames.trui.render.message.Message;
import nl.ritogames.trui.uicomponent.Vector;
import nl.ritogames.trui.uicomponent.chat.Chat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ChatTest {

  private Chat sut;
  private UIContext uiContextMock;
  @Spy
  private List<Message> messagesMock;
  private Message messageMock;
  private Message messageMockSecond;
  private Message messageMockThird;
  private Message messageMockFourth;

  @BeforeEach
  void setUp() {
    sut = new Chat(Vector.ZERO, Fixtures.testScreenSize());
    uiContextMock = Mockito.mock(UIContext.class);
    messagesMock = new ArrayList<>();
    messageMock = mock(Message.class);
    messageMockSecond = mock(Message.class);
    messageMockThird = mock(Message.class);
    messageMockFourth = mock(Message.class);
  }

  @Test
  void drawEmptyChat() {
    String result = sut.draw(uiContextMock).getText();
    Assertions.assertEquals(result, Fixtures.getDrawnChat());
  }

  @Test
  void drawChatWithOneMessage() {
    when(messageMock.getContent()).thenReturn("Message");
    when(messageMock.getSender()).thenReturn("Chat");
    messagesMock.add(messageMock);
    when(uiContextMock.getMessages()).thenReturn(messagesMock);

    String result = sut.draw(uiContextMock).getText();
    Assertions.assertEquals(result, Fixtures.getDrawnChatWithOneMessage());
  }

  @Test
  void drawChatWithMultipleMessages() {
    setupMessages();
    when(uiContextMock.getMessages()).thenReturn(messagesMock);

    String result = sut.draw(uiContextMock).getText();
    Assertions.assertEquals(result, Fixtures.getDrawnChatWithMultipleMessages());
  }

  private void setupMessages() {
    when(messageMock.getContent()).thenReturn("Message1");
    when(messageMock.getSender()).thenReturn("Chat");
    messagesMock.add(messageMock);

    when(messageMockSecond.getContent()).thenReturn("Message2");
    when(messageMockSecond.getSender()).thenReturn("Chat");
    messagesMock.add(messageMockSecond);

    when(messageMockThird.getContent()).thenReturn("Message3");
    when(messageMockThird.getSender()).thenReturn("Chat");
    messagesMock.add(messageMockThird);

    when(messageMockFourth.getContent()).thenReturn("Message4");
    when(messageMockFourth.getSender()).thenReturn("Chat");
    messagesMock.add(messageMockFourth);
  }

}
