package nl.ritogames.trui.uicomponent.worldview.rules.tile;

import nl.ritogames.shared.dto.gameobject.world.tile.AccessibleWorldTile;
import nl.ritogames.trui.uicomponent.worldview.ruleengine.WorldViewExpression;
import nl.ritogames.trui.uicomponent.worldview.ruleengine.WorldViewRule;

public class AccessibleWorldTileRule implements WorldViewRule {

  @Override
  public boolean evaluate(WorldViewExpression expression) {
    return expression.getTile().getClass().equals(AccessibleWorldTile.class);
  }

  @Override
  public String texture() {
    return ".";
  }
}
