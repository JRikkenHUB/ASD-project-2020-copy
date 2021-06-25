package nl.ritogames.parser.event.listener;

import nl.ritogames.shared.EventSender;
import nl.ritogames.shared.dto.event.StartGameEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class StartGameEventListenerTest {

  private StartGameEventListener sut;
  private EventSender eventSenderMock;
  private StartGameEvent startGameEventMock;

  @BeforeEach
  void setUp() {
    eventSenderMock = mock(EventSender.class);
    startGameEventMock = mock(StartGameEvent.class);
    sut = new StartGameEventListener(eventSenderMock);
  }

  @Test
  void onEvent() {
    sut.onEvent(startGameEventMock);

    verify(eventSenderMock).sendEvent(startGameEventMock);
  }
}
