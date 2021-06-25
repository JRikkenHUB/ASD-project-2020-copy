package nl.ritogames.trui.input;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InputHandlerTest {

  private InputHandler sut;
  private IScanner mockedScanner;

  @BeforeEach
  void setUp() {
    mockedScanner = mock(IScanner.class);
    sut = new InputHandler(mockedScanner);
  }

  @Test
  void TestRequestInputStripsLeadingAndTrailingSpaces() {
    String input = " dit is een test input     ";
    String expected = "dit is een test input";
    when(mockedScanner.nextLine()).thenReturn(input);

    sut.requestInput();
    String actual = sut.peek();

    assertEquals(expected, actual);
  }

  @Test
  void TestRequestInputKeepsMultipleSpacesMidSentence() {
    String input = " dit is een  test input";
    String expected = "dit is een  test input";
    when(mockedScanner.nextLine()).thenReturn(input);

    sut.requestInput();
    String actual = sut.peek();

    assertEquals(expected, actual);
  }

  @Test
  void TestRequestInputKeepsTabsMidSentence() {
    String input = " dit is een   test input";
    String expected = "dit is een   test input";
    when(mockedScanner.nextLine()).thenReturn(input);

    sut.requestInput();
    String actual = sut.peek();

    assertEquals(expected, actual);
  }

  @Test
  void TestRequestInputReplacesInputWhenEmpty() {
    String input = " dit is een test input     ";
    String expected = "";
    when(mockedScanner.nextLine()).thenReturn(input).thenReturn(" ");

    sut.requestInput();
    sut.requestInput();
    String actual = sut.peek();

    assertEquals(expected, actual);
  }

  @Test
  void TestIsEmptyReturnsCorrectValue() {
    String input = " dit is een test input     ";
    when(mockedScanner.nextLine()).thenReturn(input).thenReturn(" ");

    assertTrue(sut.isEmpty());

    sut.requestInput();
    assertFalse(sut.isEmpty());

    sut.requestInput();
    assertTrue(sut.isEmpty());
  }


}
