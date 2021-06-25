package nl.ritogames.trui.render;

import nl.ritogames.shared.dto.GameStateContext;
import nl.ritogames.trui.render.message.Message;

import java.util.List;

/**
 * <p>A data structure to hold the UI it's context</p>
 */
public class UIContext {

  private final GameStateContext gameStateContext;
  private final List<Message> messages;
  private final String error;

  public UIContext(GameStateContext gameStateContext,
      List<Message> messages) {
    this(gameStateContext, messages, "");
  }

  public UIContext(GameStateContext context, List<Message> messages, String error) {
    this.error = error;
    this.gameStateContext = context;
    this.messages = messages;
  }

  public GameStateContext getGameStateContext() {
    return gameStateContext;
  }

  public List<Message> getMessages() {
    return messages;
  }

  public String getError() {
    return error;
  }

  @Override
  public String toString() {
    return "UIContext{" +
        "gameStateContext=" + gameStateContext +
        ", messages=" + messages +
        ", error='" + error + '\'' +
        '}';
  }

  public boolean hasError() {
    return this.error != null && !this.error.isEmpty();
  }
}
