package nl.ritogames.trui.uicomponent.screen;

import nl.ritogames.shared.dto.GameStateContext;
import nl.ritogames.shared.dto.gameobject.individual.Character;
import nl.ritogames.shared.dto.gameobject.world.tile.ASDTile;
import nl.ritogames.trui.render.UIContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

class LobbyScreenTest {

  private final PrintStream standardOut = System.out;
  private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
  private LobbyScreen sut;

  @BeforeEach
  void setUp() {
    sut = new LobbyScreen();
    System.setOut(new PrintStream(outputStreamCaptor));
  }

  @Test
  void drawShouldPrint() {
    var context = Mockito.mock(UIContext.class);
    var gameStateContext = spy(new GameStateContext(new ASDTile[][]{}));
    Map<String, Character> players = new HashMap<>();
    players.put("whatever", new Character());
    when(context.getGameStateContext()).thenReturn(gameStateContext);
    context.getGameStateContext().setActivePlayers(players);

    sut.draw(context);

    assertEquals("Welcome to your lobby\n"
        + "- To start the game write 'game start <gamename>'\nActive players:\n"
        + "whatever", outputStreamCaptor.toString().trim().replace("\r", ""));
  }

  @AfterEach
  void tearDown() {
    System.setOut(standardOut);
  }
}
