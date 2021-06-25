package nl.ritogames.parser.event;

import nl.ritogames.shared.EventListener;
import nl.ritogames.shared.dto.event.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@SuppressWarnings({"unchecked", "rawtypes"})
class MultiListenerEventDistributorTest {

  private MultiListenerEventDistributor sut;
  private List<EventListener> listMock;

  @BeforeEach
  void setUp() {
    sut = new MultiListenerEventDistributor();
    listMock = Mockito.mock(List.class);
    sut.eventListenerMap = Mockito.mock(Map.class);
    Mockito.when(sut.eventListenerMap.computeIfAbsent(Mockito.any(), Mockito.any()))
        .thenReturn(listMock);
    Mockito.when(sut.eventListenerMap.get(Mockito.any(Class.class))).thenReturn(listMock);
  }

  @Test
  void registerListenerShouldRegisterTheListenerInAList() {

    EventListener<Event> eventListener = Mockito.mock(EventListener.class);
    Class<Event> eventClass = Event.class;

    //act
    sut.registerListener(eventClass, eventListener);

    Mockito.verify(sut.eventListenerMap, Mockito.times(1))
        .computeIfAbsent(Mockito.eq(eventClass), Mockito.any());
    Mockito.verify(listMock, Mockito.times(1)).add(eventListener);
  }

  @Test
  void distributeShouldCallAllRegisteredListeners() {
    Event event = Mockito.mock(Event.class);
    EventListener eventListener = Mockito.mock(EventListener.class);
    ArgumentCaptor<Consumer<EventListener>> argumentCaptor = ArgumentCaptor
        .forClass(Consumer.class);
    Mockito.doNothing().when(listMock).forEach(argumentCaptor.capture());

    //act
    sut.distribute(event);

    //assert
    argumentCaptor.getValue().accept(eventListener);

    Mockito.verify(eventListener, Mockito.times(1)).onEvent(event);

  }
}
