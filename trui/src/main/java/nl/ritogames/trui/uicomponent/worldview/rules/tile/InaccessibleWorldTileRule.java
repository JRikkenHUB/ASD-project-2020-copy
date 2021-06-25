package nl.ritogames.trui.uicomponent.worldview.rules.tile;

import nl.ritogames.shared.dto.gameobject.world.tile.EdgeTile;
import nl.ritogames.shared.dto.gameobject.world.tile.InaccessibleWorldTile;
import nl.ritogames.trui.uicomponent.worldview.ruleengine.WorldViewExpression;
import nl.ritogames.trui.uicomponent.worldview.ruleengine.WorldViewRule;

public class InaccessibleWorldTileRule implements WorldViewRule {

  @Override
  public boolean evaluate(WorldViewExpression expression) {
    return expression.getTile().getClass().equals(InaccessibleWorldTile.class) ||
        expression.getTile().getClass().equals(EdgeTile.class);
  }

  @Override
  public String texture() {
    return "#";
  }
}
