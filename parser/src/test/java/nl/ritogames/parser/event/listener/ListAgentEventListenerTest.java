package nl.ritogames.parser.event.listener;

import nl.ritogames.parser.event.EventDistributor;
import nl.ritogames.shared.AgentCompiler;
import nl.ritogames.shared.dto.event.ListAgentEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ListAgentEventListenerTest {

  private ListAgentEventListener sut;
  private EventDistributor eventDistributerMock;
  private AgentCompiler agentCompilerMock;
  private ListAgentEvent listAgentEventMock;

  @BeforeEach
  void setUp() {
    eventDistributerMock = mock(EventDistributor.class);
    listAgentEventMock = mock(ListAgentEvent.class);
    agentCompilerMock = mock(AgentCompiler.class);
    sut = new ListAgentEventListener(agentCompilerMock,eventDistributerMock);
  }

  @Test
  void onEvent() {
    List<String> agents = new ArrayList<>();
    agents.add("Agent 1");
    agents.add("Test");
    when(agentCompilerMock.getAgents()).thenReturn(agents);

    sut.onEvent(listAgentEventMock);

    verify(eventDistributerMock).distribute(any());
  }
}
