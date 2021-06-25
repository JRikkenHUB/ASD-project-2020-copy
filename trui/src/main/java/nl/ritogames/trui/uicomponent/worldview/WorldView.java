package nl.ritogames.trui.uicomponent.worldview;

import com.indvd00m.ascii.render.api.ICanvas;
import com.indvd00m.ascii.render.elements.Rectangle;
import com.indvd00m.ascii.render.elements.Text;
import nl.ritogames.shared.dto.gameobject.world.tile.ASDTile;
import nl.ritogames.trui.render.UIContext;
import nl.ritogames.trui.uicomponent.UIComponent;
import nl.ritogames.trui.uicomponent.Vector;
import nl.ritogames.trui.uicomponent.worldview.ruleengine.WorldViewExpression;
import nl.ritogames.trui.uicomponent.worldview.ruleengine.WorldViewRule;
import nl.ritogames.trui.uicomponent.worldview.ruleengine.WorldViewRuleEngine;
import nl.ritogames.trui.uicomponent.worldview.rules.attribute.AccessibleWorldTileWithArmourRule;
import nl.ritogames.trui.uicomponent.worldview.rules.attribute.AccessibleWorldTileWithPotionRule;
import nl.ritogames.trui.uicomponent.worldview.rules.attribute.AccessibleWorldTileWithWeaponRule;
import nl.ritogames.trui.uicomponent.worldview.rules.individual.AccessibleWorldTileWithCharacterRule;
import nl.ritogames.trui.uicomponent.worldview.rules.individual.AccessibleWorldTileWithMonsterRule;
import nl.ritogames.trui.uicomponent.worldview.rules.individual.TrapWorldTileWithCharacterRule;
import nl.ritogames.trui.uicomponent.worldview.rules.individual.TrapWorldTileWithMonsterRule;
import nl.ritogames.trui.uicomponent.worldview.rules.tile.AccessibleWorldTileRule;
import nl.ritogames.trui.uicomponent.worldview.rules.tile.InaccessibleWorldTileRule;
import nl.ritogames.trui.uicomponent.worldview.rules.tile.TrapTileRuleNotVisibleRule;
import nl.ritogames.trui.uicomponent.worldview.rules.tile.TrapTileRuleVisibleRule;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

public class WorldView extends UIComponent {

  public static final int MAP_OFFSET = 2;

  public WorldView(Vector position, Vector size) {
    super(position, size);
  }


  /**
   * <p>Draws the view of the world on the user interface</p>
   *
   * @param context <p>The context that needs to be drawn</p>
   * @return <p>The complete map with the field of view of the person</p>
   */
  @Override
  public ICanvas draw(UIContext context) {
    super.draw(context);
    this.builder.element(new Rectangle());
    this.builder.element(new Text("WORLDVIEW", getSize().getX() / 2 - 5, 1, getSize().getX(), 1));
    ASDTile[][] map = context.getGameStateContext().getWorldContext();
    for (int i = 0; i < map.length; i++) {
      StringJoiner builder = new StringJoiner(" ");
      for (int j = 0; j < map[i].length; j++) {
        builder.add(render(map[j][i]));
      }
      this.builder.element(new Text(builder.toString(), 1, i + MAP_OFFSET, getSize().getX(), 1));
    }
    return this.render.render(this.builder.build());
  }

  /**
   * <p>Looks what a tile exists of and displays it correctly to the user interface</p>
   *
   * @param tile The tile that has to be rendered
   * @return String The String that represents what the tile is made of {@see map example}
   */
  public String render(ASDTile tile) {
    WorldViewRuleEngine worldViewRuleEngine = new WorldViewRuleEngine();
    worldViewRuleEngine.addRules(instantiateRules());
    WorldViewExpression worldViewExpression = new WorldViewExpression(tile);
    return worldViewRuleEngine.process(worldViewExpression);
  }

  public List<WorldViewRule> instantiateRules() {
    return Arrays.asList(new InaccessibleWorldTileRule(),
        new AccessibleWorldTileWithCharacterRule(),
        new AccessibleWorldTileWithMonsterRule(),
        new AccessibleWorldTileWithWeaponRule(),
        new AccessibleWorldTileWithArmourRule(),
        new AccessibleWorldTileWithPotionRule(),
        new TrapWorldTileWithCharacterRule(),
        new TrapWorldTileWithMonsterRule(),
        new TrapTileRuleNotVisibleRule(),
        new TrapTileRuleVisibleRule(),
        new AccessibleWorldTileRule()
    );
  }
}
