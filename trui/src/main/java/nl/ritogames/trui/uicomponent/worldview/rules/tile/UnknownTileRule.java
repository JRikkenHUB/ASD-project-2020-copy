package nl.ritogames.trui.uicomponent.worldview.rules.tile;

import nl.ritogames.trui.uicomponent.worldview.ruleengine.WorldViewExpression;
import nl.ritogames.trui.uicomponent.worldview.ruleengine.WorldViewRule;

public class UnknownTileRule implements WorldViewRule {

  @Override
  public boolean evaluate(WorldViewExpression expression) {
    return true;
  }

  @Override
  public String texture() {
    return "?";
  }
}
