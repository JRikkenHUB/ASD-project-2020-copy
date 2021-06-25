package nl.ritogames.trui.uicomponent.screen;

import nl.ritogames.trui.render.UIContext;
import nl.ritogames.trui.render.message.Message;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MenuScreenTest {

  private final PrintStream standardOut = System.out;
  private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
  private MainMenuScreen sut;

  @BeforeEach
  void setUp() {
    sut = new MainMenuScreen();
    System.setOut(new PrintStream(outputStreamCaptor));
  }

  @Test
  void drawShouldPrint() {
    var context = Mockito.mock(UIContext.class);
    Mockito.when(context.getMessages()).thenReturn(List.of(new Message("", "")));

    sut.draw(context);

    assertEquals("== Main Menu ==\n"
        + "- To manage your agents write 'agent configure'\n"
        + "- To start a game write 'game create <gamename>'\n"
        + "- To exit the game write 'exit'\n"
        + "Message Log: showing 1 recent messages\n"
        + " >", outputStreamCaptor.toString().trim().replace("\r", ""));
  }

  @AfterEach
  void tearDown() {
    System.setOut(standardOut);
  }
}
