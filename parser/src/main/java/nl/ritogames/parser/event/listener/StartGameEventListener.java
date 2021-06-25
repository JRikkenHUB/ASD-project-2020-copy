package nl.ritogames.parser.event.listener;

import nl.ritogames.shared.EventListener;
import nl.ritogames.shared.EventSender;
import nl.ritogames.shared.dto.event.StartGameEvent;

public class StartGameEventListener implements EventListener<StartGameEvent> {

  private EventSender eventSender;

  public StartGameEventListener(EventSender eventSender) {
    this.eventSender = eventSender;
  }

  @Override
  public void onEvent(StartGameEvent event) {
    this.eventSender.sendEvent(event);
  }
}
