package nl.ritogames.trui.render;

import nl.ritogames.shared.dto.GameStateContext;
import nl.ritogames.shared.enums.GameStatus;
import nl.ritogames.trui.enums.ScreenState;
import nl.ritogames.trui.render.message.Message;
import nl.ritogames.trui.uicomponent.chat.LimitedQueue;
import nl.ritogames.trui.uicomponent.screen.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>Updates and maintains the screen during the game</p>
 */
public class ScreenRenderer {

  protected final EnumMap<ScreenState, Screen> screens = new EnumMap<>(ScreenState.class);
  protected LimitedQueue<Message> messages = new LimitedQueue<>(9);
  protected ScreenState currentState;
  protected String error = "";

  public ScreenRenderer() {
    this.currentState = ScreenState.STARTUP;
    addScreens();
  }

  private void addScreens() {
    this.screens.put(ScreenState.MENU, new MainMenuScreen());
    this.screens.put(ScreenState.LOBBY, new LobbyScreen());
    this.screens.put(ScreenState.GAME, new GameScreen());
    this.screens.put(ScreenState.STARTUP, new StartupScreen());
    this.screens.put(ScreenState.AGENTMENU, new AgentMenuScreen());
  }

  /**
   * <p>Will put the given context on screen for the user</p>
   *
   * @param context <p>The context that should be printed on screen</p>
   */
  public void render(GameStateContext context) {
    clearScreen();
    checkState(context);
    UIContext uiContext = new UIContext(context, this.messages, this.error);
    screens.get(currentState).draw(uiContext);
  }

  private void clearScreen() {
    try {
      String osName = System.getProperty("os.name");
      if (osName.contains("Windows")) {
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
      } else {
        Runtime.getRuntime().exec("clear");
      }
    } catch (InterruptedException | IOException e) {
      Logger.getAnonymousLogger().log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
    }
  }

  public void setState(ScreenState state) {
    //clear messages because we are moving to a different state, making old messages irrelevant
    this.messages.clear();
    this.currentState = state;
  }

  public boolean isAtState(ScreenState state) {
    return this.currentState == state;
  }

  /**
   * @param message <p>The message that should be given to the user</p>
   */
  public void addMessage(Message message) {
    this.messages.add(message);
  }

  public void checkState(GameStateContext context) {
    if (context.currentGameStatus == GameStatus.IN_PROGRESS && this.currentState != ScreenState.GAME) {
      setState(ScreenState.GAME);
    }
    if (context.currentGameStatus == GameStatus.CREATED && this.currentState != ScreenState.LOBBY) {
      setState(ScreenState.LOBBY);
    }
  }

  public void renderExitScreen() {
    System.out.println("Thank you for playing, goodbye!");
  }

  public void setError(String error) {
    this.error = error;
  }

  public void resetError() {
    this.error = "";
  }
}
