package nl.ritogames.parser.event.listener;

import nl.ritogames.shared.AgentCompiler;
import nl.ritogames.shared.EventListener;
import nl.ritogames.shared.EventSender;
import nl.ritogames.shared.dto.event.CreateGameEvent;

public class CreateGameEventListener implements EventListener<CreateGameEvent> {

  private EventSender eventSender;
  private AgentCompiler agentCompiler;

  public CreateGameEventListener(EventSender eventSender,
      AgentCompiler agentCompiler) {
    this.eventSender = eventSender;
    this.agentCompiler = agentCompiler;
  }

  @Override
  public void onEvent(CreateGameEvent event) {
    String agentName = agentCompiler.getActiveAgent();
    this.eventSender.createSession(event.getGameName(), event.getIndividualId(), agentName);
    event.setHostAgentName(agentName);
    this.eventSender.sendEvent(event);
  }
}
