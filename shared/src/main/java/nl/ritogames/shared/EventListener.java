package nl.ritogames.shared;

import nl.ritogames.shared.dto.event.Event;

public interface EventListener<T extends Event> {

  void onEvent(T event);
}
