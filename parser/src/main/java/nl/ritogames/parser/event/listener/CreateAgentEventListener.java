package nl.ritogames.parser.event.listener;

import nl.ritogames.shared.AgentCompiler;
import nl.ritogames.shared.dto.event.CreateAgentEvent;
import nl.ritogames.shared.exception.ASDIOException;

import java.io.IOException;

public class CreateAgentEventListener extends AgentEventListener<CreateAgentEvent> {

  public CreateAgentEventListener(AgentCompiler agentCompiler) {
    super(agentCompiler);
  }

  @Override
  public void onEvent(CreateAgentEvent event) {
    try {
      this.agentCompiler.createAgent(event.getAgentName());
    } catch (IOException e) {
      throw new ASDIOException(e);
    }
  }
}
