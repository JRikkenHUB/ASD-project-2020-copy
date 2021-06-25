package nl.ritogames.parser.event.listener;

import nl.ritogames.shared.AgentCompiler;
import nl.ritogames.shared.EventSender;
import nl.ritogames.shared.dto.event.GameJoinEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class GameJoinEventListenerTest {

  private GameJoinEventListener sut;
  private AgentCompiler agentCompilerMock;
  private EventSender eventSenderMock;
  private GameJoinEvent eventMock;

  @BeforeEach
  void setUp() {
    agentCompilerMock = mock(AgentCompiler.class);
    eventSenderMock = mock(EventSender.class);
    eventMock = mock(GameJoinEvent.class);
    sut = new GameJoinEventListener(eventSenderMock, agentCompilerMock);
  }

  @Test
  void onEventCallsJoinSession() {
    var expected = "some.ip";
    when(eventMock.getIp()).thenReturn(expected);

    sut.onEvent(eventMock);

    verify(eventSenderMock).joinSession(expected, null);
  }

  @Test
  void onEvent() {
    sut.onEvent(eventMock);

    verify(eventSenderMock).sendEvent(eventMock);
  }
}
