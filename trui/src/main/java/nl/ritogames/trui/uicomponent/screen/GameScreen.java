package nl.ritogames.trui.uicomponent.screen;

import com.indvd00m.ascii.render.api.ICanvas;
import com.indvd00m.ascii.render.elements.Overlay;
import com.indvd00m.ascii.render.elements.Rectangle;
import nl.ritogames.trui.render.UIContext;
import nl.ritogames.trui.uicomponent.UIComponent;
import nl.ritogames.trui.uicomponent.Vector;
import nl.ritogames.trui.uicomponent.chat.Chat;
import nl.ritogames.trui.uicomponent.stats.Stats;
import nl.ritogames.trui.uicomponent.worldview.WorldView;

import java.util.ArrayList;
import java.util.List;

/**
 * The screen of the command line interface during the state {@link nl.ritogames.trui.enums.ScreenState#GAME}
 */
public class GameScreen extends AsciiRenderedScreen {

  final List<UIComponent> uiComponents = new ArrayList<>();
  Chat chat;

  public GameScreen() {
    super();
    chat = new Chat(new Vector(18, 0), new Vector(62, 22));
    uiComponents.add(new WorldView(new Vector(0, 0), new Vector(19, 12)));
    uiComponents.add(new Stats(new Vector(0, 12), new Vector(19, 10)));
    uiComponents.add(chat);
  }

  /**
   *
   * @param context
   */
  @Override
  public void draw(UIContext context) {
    this.resetRenderer();
    this.contextBuilder.element(new Rectangle());
    this.uiComponents.forEach(uiComponent -> this.contextBuilder.element(
        new Overlay(uiComponent.getPosition().getX(), uiComponent.getPosition().getY(),
            uiComponent.draw(context))));
    ICanvas canvas = this.render.render(this.contextBuilder.build());
    System.out.println(canvas.getText());
  }

}
