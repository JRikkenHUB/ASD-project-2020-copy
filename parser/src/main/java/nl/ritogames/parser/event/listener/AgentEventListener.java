package nl.ritogames.parser.event.listener;

import nl.ritogames.shared.AgentCompiler;
import nl.ritogames.shared.EventListener;
import nl.ritogames.shared.dto.event.AgentEvent;

public abstract class AgentEventListener<T extends AgentEvent> implements EventListener<T> {

  protected AgentCompiler agentCompiler;

  protected AgentEventListener(AgentCompiler agentCompiler) {
    this.agentCompiler = agentCompiler;
  }
}
