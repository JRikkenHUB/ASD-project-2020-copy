package nl.ritogames.trui.uicomponent.screen;

import nl.ritogames.trui.render.UIContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AgentMenuScreenTest {

  private AgentMenuScreen sut;
  private UIContext uiContextMock;
  private final PrintStream standardOut = System.out;
  private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

  @BeforeEach
  void setUp() {
    sut = new AgentMenuScreen();
    uiContextMock = Mockito.mock(UIContext.class);
    System.setOut(new PrintStream(outputStreamCaptor));
  }

  @Test
  void drawShouldOnlyPrintAgentMenuWithoutError() {
    var notExpected = "not expected";
    Mockito.when(uiContextMock.hasError()).thenReturn(false);
    Mockito.when(uiContextMock.getError()).thenReturn(notExpected);

    sut.draw(uiContextMock);

    assertTrue(getOutputString().contains("Agent Menu"));
    assertFalse(getOutputString().contains(notExpected));
  }

  @Test
  void drawShouldPrintErrorAndAgentMenuWithError() {
    var expected = "expected";
    Mockito.when(uiContextMock.hasError()).thenReturn(true);
    Mockito.when(uiContextMock.getError()).thenReturn(expected);

    sut.draw(uiContextMock);

    assertTrue(getOutputString().contains("Agent Menu"));
    assertTrue(getOutputString().contains(expected));
  }

  @AfterEach
  void tearDown() {
    System.setOut(standardOut);
  }

  String getOutputString() {
    return outputStreamCaptor.toString().trim().replace("\r", "");
  }
}
