package nl.ritogames.parser.event.listener;

import nl.ritogames.shared.AgentCompiler;
import nl.ritogames.shared.dto.event.SelectAgentEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class SelectAgentEventListenerTest {

  private SelectAgentEventListener sut;
  private AgentCompiler agentCompilerMock;
  private SelectAgentEvent selectAgentEventMock;

  @BeforeEach
  void setUp() {
    selectAgentEventMock = mock(SelectAgentEvent.class);
    agentCompilerMock = mock(AgentCompiler.class);
    sut = new SelectAgentEventListener(agentCompilerMock);
  }

  @Test
  void onEvent() {
    String agent = "AgentName";
    when(selectAgentEventMock.getAgentName()).thenReturn(agent);
    
    sut.onEvent(selectAgentEventMock);

    verify(agentCompilerMock).compile(agent);
    verify(agentCompilerMock).setActiveAgent(agent);
  }
}
