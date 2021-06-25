package nl.ritogames.trui.render;

import nl.ritogames.shared.GameStateContextProvider;
import nl.ritogames.shared.Parser;
import nl.ritogames.shared.exception.ParseInputException;
import nl.ritogames.trui.Trui;
import nl.ritogames.trui.input.InputHandler;

/**
 * <p>To resolve the issue that commands would be reset after every render this strategy is
 * implemented. This strategy will pause the game so the user can finish typing and sending his
 * command before the next render</p>
 */
public class OnCommandRenderStrategy extends RenderStrategy {

  private final Trui trui;
  private final Parser parser;
  private final ScreenRenderer screenRenderer;
  private final InputHandler inputHandler;
  private final GameStateContextProvider contextProvider;

  public OnCommandRenderStrategy(Trui trui, Parser parser,
      ScreenRenderer screenRenderer, InputHandler inputHandler, GameStateContextProvider provider) {
    this.trui = trui;
    this.parser = parser;
    this.inputHandler = inputHandler;
    this.contextProvider = provider;
    this.screenRenderer = screenRenderer;
  }


  /**
   * <p>Will keep checking if the user is finished typing and sending its command, if so it will
   * put the render strategy back to {@link RealtimeRenderStrategy}</p>
   *
   * @throws ParseInputException when the command does not conform the regulations
   */
  @Override
  public void update(String individualId) throws ParseInputException {
    if (inputHandler.isEmpty()) {
      trui.toggleRenderStrategy();
    } else {
      parser.parseInput(inputHandler.peek(), individualId);
      //set error to be empty so no previous errors are maintained after a succesful parser execution
      screenRenderer.resetError();
      this.displayToggleMessage(individualId);
    }
  }

  @Override
  public Strategy getStrategy() {
    return Strategy.ONCOMMAND;
  }

  @Override
  public void displayToggleMessage(String individualId) {
    screenRenderer.render(contextProvider.getContext(individualId));
    System.out
        .println("Live reloading disabled. Press [Enter] without input to enable live reloading");
  }
}
