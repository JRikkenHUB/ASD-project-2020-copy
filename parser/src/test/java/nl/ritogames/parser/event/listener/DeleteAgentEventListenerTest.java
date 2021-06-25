package nl.ritogames.parser.event.listener;

import nl.ritogames.shared.AgentCompiler;
import nl.ritogames.shared.dto.event.DeleteAgentEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class DeleteAgentEventListenerTest {

  private DeleteAgentEventListener sut;
  private AgentCompiler agentCompilerMock;
  private DeleteAgentEvent deleteAgentEventMock;

  @BeforeEach
  void setUp() {
    agentCompilerMock = mock(AgentCompiler.class);
    deleteAgentEventMock = mock(DeleteAgentEvent.class);
    sut = new DeleteAgentEventListener(agentCompilerMock);
  }

  @Test
  void onEvent() {
    String agentName = "testAgent";
    when(deleteAgentEventMock.getAgentName()).thenReturn(agentName);

    sut.onEvent(deleteAgentEventMock);

    verify(agentCompilerMock).deleteAgent(agentName);
  }
}
