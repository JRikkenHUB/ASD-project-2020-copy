package nl.ritogames.trui.uicomponent.screen;

import nl.ritogames.trui.render.UIContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StartupScreenTest {

  private final PrintStream standardOut = System.out;
  private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
  private StartupScreen sut;

  @BeforeEach
  void setUp() {
    sut = new StartupScreen();
    System.setOut(new PrintStream(outputStreamCaptor));
  }

  @Test
  void drawShouldPrint() {
    sut.draw(Mockito.mock(UIContext.class));

    assertEquals("Welcome to ASDungeon\n"
        + "Enter a username to begin:", outputStreamCaptor.toString().trim().replace("\r", ""));
  }

  @AfterEach
  void tearDown() {
    System.setOut(standardOut);
  }
}
