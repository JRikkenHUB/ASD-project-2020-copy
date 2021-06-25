package nl.ritogames.trui.input;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.Reader;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class ScannerImplTest {

  ScannerImpl sut;
  Reader reader;

  @BeforeEach
  void setUp() {
    reader = Mockito.mock(Reader.class);
    sut = new ScannerImpl(reader);
    sut.scanner = Mockito.mock(Scanner.class);
  }

  @Test
  void nextLineCallsScannerNextLine() throws IOException {
    String expected = "S";

    when(sut.scanner.nextLine()).thenReturn(expected);

    String actual = sut.nextLine();

    assertEquals(actual, expected);
  }


}
