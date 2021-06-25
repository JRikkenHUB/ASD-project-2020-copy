package nl.ritogames.parser.event.listener;

import nl.ritogames.shared.EventListener;
import nl.ritogames.shared.EventSender;
import nl.ritogames.shared.dto.event.GameEvent;

public class GameEventListener<T extends GameEvent> implements EventListener<T> {

  private EventSender eventSender;

  public GameEventListener(EventSender eventSender) {
    this.eventSender = eventSender;
  }

  @Override
  public void onEvent(T event) {
    eventSender.sendEvent(event);
  }
}
