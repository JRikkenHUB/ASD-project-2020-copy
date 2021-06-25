package nl.ritogames.trui.uicomponent.worldview.rules.attribute;

import nl.ritogames.shared.dto.gameobject.attribute.Potion;
import nl.ritogames.trui.uicomponent.worldview.ruleengine.WorldViewExpression;
import nl.ritogames.trui.uicomponent.worldview.ruleengine.WorldViewRule;

public class AccessibleWorldTileWithPotionRule extends
    AccessibleWorldTileWithAttributeRule implements
    WorldViewRule {

  @Override
  public boolean evaluate(WorldViewExpression expression) {
    return super.evaluate(expression, Potion.class);
  }

  @Override
  public String texture() {
    return "^";
  }
}
