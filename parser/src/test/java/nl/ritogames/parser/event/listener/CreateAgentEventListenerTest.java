package nl.ritogames.parser.event.listener;

import nl.ritogames.shared.AgentCompiler;
import nl.ritogames.shared.dto.event.CreateAgentEvent;
import nl.ritogames.shared.exception.ASDIOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

class CreateAgentEventListenerTest {

  private CreateAgentEventListener sut;
  private AgentCompiler agentCompiler;
  private CreateAgentEvent createAgentEventMock;

  @BeforeEach
  void setUp() {
    agentCompiler = mock(AgentCompiler.class);
    createAgentEventMock = mock(CreateAgentEvent.class);
    sut = new CreateAgentEventListener(agentCompiler);
  }

  @Test
  void onEventShouldCallAgentCompiler() {
    var expected = "someName";
    var event = new CreateAgentEvent(expected);
    ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
//    doNothing().when(agentCompiler).createAgent(argumentCaptor.capture());

    sut.onEvent(event);

//    assertEquals(expected, argumentCaptor.getValue());
  }

  @Test
  void onIOExceptionShouldThrowASDIOException() throws IOException {
    doThrow(IOException.class).when(agentCompiler).createAgent(any());

    assertThrows(ASDIOException.class,
        () -> sut.onEvent(createAgentEventMock));
  }
}
