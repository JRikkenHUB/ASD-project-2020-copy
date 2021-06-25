package nl.ritogames.trui;

import com.indvd00m.ascii.render.Render;
import com.indvd00m.ascii.render.api.ICanvas;
import com.indvd00m.ascii.render.api.IContextBuilder;
import com.indvd00m.ascii.render.elements.Rectangle;
import nl.ritogames.trui.uicomponent.Vector;

public class Fixtures {


  /**
   *
   */
  private Fixtures() {
  }

  public static Vector testScreenSize() {
    return new Vector(16, 16);
  }

  public static ICanvas testCanvas(Vector size) {
    Render render = new Render();
    IContextBuilder builder = render.newBuilder().width(size.getX()).height(size.getY());
    builder.element(new Rectangle());
    return render.render(builder.build());
  }

  public static String getDrawnUIComponent() {
    return "┌──────────────┐\n"
        + "│              │\n"
        + "│              │\n"
        + "│              │\n"
        + "│              │\n"
        + "│              │\n"
        + "│              │\n"
        + "│              │\n"
        + "│              │\n"
        + "│              │\n"
        + "│              │\n"
        + "│              │\n"
        + "│              │\n"
        + "│              │\n"
        + "│              │\n"
        + "└──────────────┘";
  }

  public static String getDrawnChat() {
    return "┌──────────────┐\n"
        + "│   MESSAGES   │\n"
        + "│              │\n"
        + "│              │\n"
        + "│              │\n"
        + "│              │\n"
        + "│              │\n"
        + "│              │\n"
        + "│              │\n"
        + "│              │\n"
        + "│              │\n"
        + "│              │\n"
        + "│              │\n"
        + "│              │\n"
        + "│              │\n"
        + "└──────────────┘";
  }

  public static String getDrawnChatWithOneMessage() {
    return "┌──────────────┐\n"
        + "│   MESSAGES   │\n"
        + "│<Chat> Message│\n"
        + "│              │\n"
        + "│              │\n"
        + "│              │\n"
        + "│              │\n"
        + "│              │\n"
        + "│              │\n"
        + "│              │\n"
        + "│              │\n"
        + "│              │\n"
        + "│              │\n"
        + "│              │\n"
        + "│              │\n"
        + "└──────────────┘";
  }

  public static String getDrawnChatWithMultipleMessages() {
    return "┌──────────────┐\n"
        + "│   MESSAGES   │\n"
        + "│<Chat> Message│\n"
        + "│1             │\n"
        + "│<Chat> Message│\n"
        + "│2             │\n"
        + "│<Chat> Message│\n"
        + "│3             │\n"
        + "│<Chat> Message│\n"
        + "│4             │\n"
        + "│              │\n"
        + "│              │\n"
        + "│              │\n"
        + "│              │\n"
        + "│              │\n"
        + "└──────────────┘";
  }

  public static String getDrawnStats() {
    return "┌──────────────┐\n"
        + "│    STATS     │\n"
        + "│              │\n"
        + "│ HEALTH   100 │\n"
        + "│ ATTACK   0   │\n"
        + "│ DEFENCE  0   │\n"
        + "│              │\n"
        + "│              │\n"
        + "│              │\n"
        + "│              │\n"
        + "│              │\n"
        + "│              │\n"
        + "│              │\n"
        + "│              │\n"
        + "│              │\n"
        + "└──────────────┘";
  }


  public static String getDrawnWorldView() {
    return "┌─────────────────┐\n"
        + "│   WORLDVIEW     │\n"
        + "│                 │\n"
        + "│_ _ _ _ _ _ _ _ _│\n"
        + "│_ _ _ _ _ _ _ _ _│\n"
        + "│_ _ _ _ _ _ _ _ _│\n"
        + "│_ _ _ _ _ _ _ _ _│\n"
        + "│_ _ _ _ _ _ _ _ _│\n"
        + "│_ _ _ _ _ _ _ _ _│\n"
        + "│_ _ _ _ _ _ _ _ _│\n"
        + "│_ _ _ _ _ _ _ _ _│\n"
        + "│_ _ _ _ _ _ _ _ _│\n"
        + "│                 │\n"
        + "│                 │\n"
        + "│                 │\n"
        + "└─────────────────┘";
  }

  public static String getWorldViewWithCorrectTilePlacement() {
    return "┌───────────┐\n"
        + "│WORLDVIEW  │\n"
        + "│# . #      │\n"
        + "│# . #      │\n"
        + "│# . #      │\n"
        + "│           │\n"
        + "└───────────┘";
  }

  public static String getWorldViewWithAllTileTypes() {
    return "┌───────────┐\n"
        + "│WORLDVIEW  │\n"
        + "│# + #      │\n"
        + "│M . .      │\n"
        + "│^ ) P      │\n"
        + "│           │\n"
        + "└───────────┘";
  }

  public static String getWorldViewWithEmptyMap() {
    return "┌───────────┐\n"
        + "│WORLDVIEW  │\n"
        + "│           │\n"
        + "│           │\n"
        + "│           │\n"
        + "│           │\n"
        + "└───────────┘";
  }

  public static String getDrawnStatsNotCharacter() {
    return "┌──────────────┐\n"
        + "│    STATS     │\n"
        + "│              │\n"
        + "│ HEALTH   100 │\n"
        + "│ ATTACK   10  │\n"
        + "│ DEFENCE  20  │\n"
        + "│              │\n"
        + "│              │\n"
        + "│              │\n"
        + "│              │\n"
        + "│              │\n"
        + "│              │\n"
        + "│              │\n"
        + "│              │\n"
        + "│              │\n"
        + "└──────────────┘";
  }
}
