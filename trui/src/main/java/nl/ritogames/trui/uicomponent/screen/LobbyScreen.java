package nl.ritogames.trui.uicomponent.screen;

import nl.ritogames.shared.dto.gameobject.individual.Character;
import nl.ritogames.trui.render.UIContext;

import java.util.Map;

/**
 * The screen of the command line interface during the state {@link nl.ritogames.trui.enums.ScreenState#LOBBY}
 */
public class LobbyScreen extends MenuScreen {

  @Override
  public void draw(UIContext context) {
    System.out.println("Welcome to your lobby");
    System.out.println("- To start the game write 'game start <gamename>'");
    this.renderCurrentPlayers(context.getGameStateContext().getActivePlayers());
    this.renderMessages(context.getMessages());
    this.renderError(context);
  }

  private void renderCurrentPlayers(Map<String, Character> activePlayers) {
    System.out.println("Active players:");
    activePlayers.keySet().forEach(System.out::println);
  }
}
