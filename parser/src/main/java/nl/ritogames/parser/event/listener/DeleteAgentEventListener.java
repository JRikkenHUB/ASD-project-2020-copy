package nl.ritogames.parser.event.listener;

import nl.ritogames.shared.AgentCompiler;
import nl.ritogames.shared.dto.event.DeleteAgentEvent;

public class DeleteAgentEventListener extends AgentEventListener<DeleteAgentEvent>{

  public DeleteAgentEventListener(AgentCompiler agentCompiler) {
    super(agentCompiler);
  }

  @Override
  public void onEvent(DeleteAgentEvent event) {
    this.agentCompiler.deleteAgent(event.getAgentName());
  }
}
