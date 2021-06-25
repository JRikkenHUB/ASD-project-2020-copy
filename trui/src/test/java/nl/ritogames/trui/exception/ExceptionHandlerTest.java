package nl.ritogames.trui.exception;

import nl.ritogames.shared.exception.InvalidArgumentException;
import nl.ritogames.trui.exception.mappers.InvalidArgumentExceptionMapper;
import nl.ritogames.trui.render.ScreenRenderer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.mockito.Mockito.*;

class ExceptionHandlerTest {

  ExceptionHandler sut;
  ScreenRenderer screenRendererMock;
  InvalidArgumentException exceptionMock;
  InvalidArgumentExceptionMapper invalidArgumentExceptionMapperMock;

  @BeforeEach
  void setUp() {
    screenRendererMock = mock(ScreenRenderer.class);
    exceptionMock = mock(InvalidArgumentException.class);
    invalidArgumentExceptionMapperMock = mock(InvalidArgumentExceptionMapper.class);
    sut = new ExceptionHandler();
    sut.exceptionMapperMap = mock(HashMap.class);
  }

  @Test
  void handleExceptionCallsHandleWhenClassInTheMap() {
    //arrange
    when(sut.exceptionMapperMap.containsKey(exceptionMock.getClass())).thenReturn(true);
    sut.exceptionMapperMap.put(exceptionMock.getClass(), invalidArgumentExceptionMapperMock);
    when(sut.exceptionMapperMap.get(exceptionMock.getClass()))
        .thenReturn(invalidArgumentExceptionMapperMock);

    //act
    sut.handleException(exceptionMock);

    //assert
    verify(sut.exceptionMapperMap).get(exceptionMock.getClass());
  }

  @Test
  void handleExceptionDoesNotAddWhenDoesntContainKey() {
    //arrange
    when(exceptionMock.getMessage()).thenReturn("exception");
    when(sut.exceptionMapperMap.containsKey(exceptionMock.getClass())).thenReturn(false);
    sut.exceptionMapperMap.put(exceptionMock.getClass(), invalidArgumentExceptionMapperMock);

    //act
    sut.handleException(exceptionMock);

    //assert
    verify(sut.exceptionMapperMap, never()).get(exceptionMock.getClass());
  }

  @Test
  void addExceptionAddsException() {
    //act
    sut.addExceptionMapper(InvalidArgumentException.class, invalidArgumentExceptionMapperMock);

    //assert
    verify(sut.exceptionMapperMap)
        .put(InvalidArgumentException.class, invalidArgumentExceptionMapperMock);
  }
}
