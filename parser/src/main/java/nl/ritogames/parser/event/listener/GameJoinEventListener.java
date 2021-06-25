package nl.ritogames.parser.event.listener;

import nl.ritogames.shared.AgentCompiler;
import nl.ritogames.shared.EventListener;
import nl.ritogames.shared.EventSender;
import nl.ritogames.shared.dto.event.GameJoinEvent;

public class GameJoinEventListener implements EventListener<GameJoinEvent> {

  private EventSender eventSender;
  private AgentCompiler agentCompiler;

  public GameJoinEventListener(EventSender eventSender,
      AgentCompiler agentCompiler) {
    this.eventSender = eventSender;
    this.agentCompiler = agentCompiler;
  }

  @Override
  public void onEvent(GameJoinEvent event) {
    eventSender.joinSession(event.getIp(), event.getIndividualId());
    event.setAgentName(agentCompiler.getActiveAgent());
    eventSender.sendEvent(event);
  }
}
