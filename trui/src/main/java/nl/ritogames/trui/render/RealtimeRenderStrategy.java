package nl.ritogames.trui.render;

import nl.ritogames.shared.GameStateContextListener;
import nl.ritogames.shared.GameStateContextProvider;
import nl.ritogames.shared.dto.GameStateContext;
import nl.ritogames.trui.Trui;
import nl.ritogames.trui.input.InputHandler;

/**
 * <p>The default strategy that will render the game by incoming events</p>
 */
public class RealtimeRenderStrategy extends RenderStrategy implements GameStateContextListener {

  private final InputHandler inputHandler;
  private final Trui trui;
  private final ScreenRenderer screenRenderer;

  public RealtimeRenderStrategy(ScreenRenderer screenRenderer,
      InputHandler inputHandler, Trui trui) {
    this.inputHandler = inputHandler;
    this.trui = trui;
    this.screenRenderer = screenRenderer;
  }

  /**
   * <p>Looks if the user is trying to send a command, if not it will stay subscribed to {@link GameStateContextProvider}
   * so it will update on every event the user receives if the user is trying to type
   * It will switch to {@link OnCommandRenderStrategy}</p>
   */
  @Override
  public void update(String individualId) {
    if (inputHandler.isEmpty()) {
      this.stop(individualId);
      trui.toggleRenderStrategy();
    }
  }


  /**
   * Unsubscribes to the contextProvider
   */
  public void stop(String individualId) {

  }

  @Override
  public Strategy getStrategy() {
    return Strategy.REALTIME;
  }

  @Override
  public void displayToggleMessage(String individualId) {
    System.out.println("Live reloading enabled. Press [Enter] to start entering a command.");
  }

  @Override
  public void updateContext(GameStateContext context) {
    this.screenRenderer.render(context);
    this.displayToggleMessage(trui.getIndividualId());
  }

  @Override
  public void unsubscribedSignal() {

  }
}
