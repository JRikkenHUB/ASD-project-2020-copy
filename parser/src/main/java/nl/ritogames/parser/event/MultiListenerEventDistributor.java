package nl.ritogames.parser.event;

import nl.ritogames.shared.EventListener;
import nl.ritogames.shared.dto.event.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"unchecked", "rawtypes"})
public class MultiListenerEventDistributor implements EventDistributor {

  Map<Class<?>, List<EventListener>> eventListenerMap;

  public MultiListenerEventDistributor() {
    this.eventListenerMap = new HashMap<>();
  }

  /**
   * Method to register listeners to be called when an event results from the parser.
   * @param clazz
   * @param eventListener
   * @param <T>
   */
  @Override
  public <T extends Event> void registerListener(Class<T> clazz,
      EventListener<T> eventListener) {
    eventListenerMap
        .computeIfAbsent(clazz, c -> new ArrayList<>())
        .add(eventListener);
  }

  /**
   * Method to distribute an event to all listeners.
   * @param event
   */
  @Override
  public void distribute(Event event) {
    eventListenerMap
        .computeIfAbsent(event.getClass(), c -> new ArrayList<>())
        .forEach(
            listener -> listener.onEvent(event)
        );
  }
}
