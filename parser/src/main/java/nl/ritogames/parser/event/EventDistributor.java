package nl.ritogames.parser.event;

import nl.ritogames.shared.EventListener;
import nl.ritogames.shared.dto.event.Event;

public interface EventDistributor {

  <T extends Event> void registerListener(Class<T> clazz, EventListener<T> eventListener);

  void distribute(Event event);
}
