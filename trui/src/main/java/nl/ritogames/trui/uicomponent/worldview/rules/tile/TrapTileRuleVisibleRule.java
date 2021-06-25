package nl.ritogames.trui.uicomponent.worldview.rules.tile;

import nl.ritogames.shared.dto.gameobject.world.tile.TrapTile;
import nl.ritogames.trui.uicomponent.worldview.ruleengine.WorldViewExpression;
import nl.ritogames.trui.uicomponent.worldview.ruleengine.WorldViewRule;

public class TrapTileRuleVisibleRule implements WorldViewRule {

  @Override
  public boolean evaluate(WorldViewExpression expression) {
    return expression.getTile().getClass().equals(TrapTile.class) &&
        ((TrapTile) expression.getTile()).getTrap().isVisible();
  }

  @Override
  public String texture() {
    return "T";
  }
}
