package nl.ritogames.shared;

import nl.ritogames.shared.dto.event.Event;
import nl.ritogames.shared.exception.ParseInputException;

public interface Parser {

  void parseInput(String input, String individualId) throws ParseInputException;

  <T extends Event> void registerEventListener(Class<T> eventType,
      EventListener<T> eventListener);
}
