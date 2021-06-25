package nl.ritogames.parser.event.listener;

import nl.ritogames.shared.AgentCompiler;
import nl.ritogames.shared.dto.event.SelectAgentEvent;

public class SelectAgentEventListener extends AgentEventListener<SelectAgentEvent>{

  public SelectAgentEventListener(AgentCompiler agentCompiler) {
    super(agentCompiler);
  }

  @Override
  public void onEvent(SelectAgentEvent event) {
    this.agentCompiler.compile(event.getAgentName());
    this.agentCompiler.setActiveAgent(event.getAgentName());
  }
}
