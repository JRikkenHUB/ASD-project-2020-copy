package nl.ritogames.parser.event.listener;

import nl.ritogames.parser.event.EventDistributor;
import nl.ritogames.shared.AgentCompiler;
import nl.ritogames.shared.dto.event.AgentsListedEvent;
import nl.ritogames.shared.dto.event.ListAgentEvent;

import java.util.List;

public class ListAgentEventListener extends AgentEventListener<ListAgentEvent>{

  private final EventDistributor eventDistributor;

  public ListAgentEventListener(AgentCompiler agentCompiler, EventDistributor eventDistributor) {
    super(agentCompiler);
    this.eventDistributor = eventDistributor;
  }

  @Override
  public void onEvent(ListAgentEvent event) {
    List<String> agents = agentCompiler.getAgents();
    eventDistributor.distribute(new AgentsListedEvent(agents));
  }
}
