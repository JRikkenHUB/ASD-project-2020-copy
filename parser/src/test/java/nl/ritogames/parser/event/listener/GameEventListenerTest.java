package nl.ritogames.parser.event.listener;


import nl.ritogames.shared.EventSender;
import nl.ritogames.shared.dto.event.GameEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
class GameEventListenerTest {

  private GameEventListener<GameEvent> sut;
  private EventSender eventSender;

  @BeforeEach
  void setUp() {
    eventSender = mock(EventSender.class);
    sut = new GameEventListener(eventSender);
  }

  @Test
  void onEvent() {
    GameEvent expected = mock(GameEvent.class);
    var argumentCaptor = ArgumentCaptor.forClass(GameEvent.class);
    doNothing().when(eventSender).sendEvent(argumentCaptor.capture());

    sut.onEvent(expected);

    assertEquals(expected, argumentCaptor.getValue());
  }
}
