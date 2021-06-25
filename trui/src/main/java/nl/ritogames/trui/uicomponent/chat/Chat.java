package nl.ritogames.trui.uicomponent.chat;

import com.indvd00m.ascii.render.api.ICanvas;
import com.indvd00m.ascii.render.elements.Rectangle;
import com.indvd00m.ascii.render.elements.Text;
import nl.ritogames.shared.ChatHandler;
import nl.ritogames.shared.dto.GameChat;
import nl.ritogames.trui.render.UIContext;
import nl.ritogames.trui.render.message.Message;
import nl.ritogames.trui.uicomponent.UIComponent;
import nl.ritogames.trui.uicomponent.Vector;

import java.util.List;

/**
 * <p>The chat that is displayed in the command line interface</p>
 */
public class Chat extends UIComponent implements ChatHandler {

  public Chat(Vector position, Vector size) {
    super(position, size);
  }

  /**
   * Draws the chat on the command line interface
   *
   * @return The canvas
   */
  @Override
  public ICanvas draw(UIContext uiContext) {
    super.draw(uiContext);
    this.builder.element(new Rectangle());
    this.builder.element(new Text("MESSAGES", getSize().getX() / 2 - 4, 1, getSize().getX(), 1));
    List<Message> messages = uiContext.getMessages();
    int nextY = 2;
    for (Message message : messages) {
      String text = String.format("<%s> %s", message.getSender(), message.getContent());
      int height = getHeight(text);
      this.drawTextDynamicLines(nextY, height, text);
      nextY += height;
    }
    if (uiContext.hasError()) {
      String text = String.format("<Error> %s", uiContext.getError());
      drawTextDynamicLines(nextY, nextY, text);
    }
    return this.render.render(this.builder.build());
  }

  private void drawTextDynamicLines(int y, int height, String text) {
    this.builder.element(
        new Text(text, 1, y,
            getSize().getX() - 2, height));
  }

  private int getHeight(String text) {
    return text.length() > getSize().getX() - 3 ? 2 : 1;
  }

  @Override
  public void onChatChanged(GameChat gameChat) {
    throw new UnsupportedOperationException();
  }

}
