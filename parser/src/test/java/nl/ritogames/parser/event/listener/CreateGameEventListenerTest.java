package nl.ritogames.parser.event.listener;

import nl.ritogames.shared.AgentCompiler;
import nl.ritogames.shared.EventSender;
import nl.ritogames.shared.dto.event.CreateGameEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;

import static org.mockito.Mockito.*;

class CreateGameEventListenerTest {

  private CreateGameEventListener sut;
  private AgentCompiler agentCompiler;
  private EventSender eventSender;
  private CreateGameEvent createGameEventMock;

  @BeforeEach
  void setUp() {
    createGameEventMock = mock(CreateGameEvent.class);
    agentCompiler = mock(AgentCompiler.class);
    eventSender = mock(EventSender.class);
    sut = new CreateGameEventListener(eventSender, agentCompiler);
  }

  @Test
  void onEventShouldCallEventSender() throws IOException {
    var expected = new CreateGameEvent();
    var argumentCaptor = ArgumentCaptor.forClass(CreateGameEvent.class);
    when(agentCompiler.getActiveAgent()).thenReturn("test");
    doNothing().when(eventSender).sendEvent(argumentCaptor.capture());

    sut.onEvent(expected);

    Assertions.assertEquals(expected, argumentCaptor.getValue());
  }
}
