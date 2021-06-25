package nl.ritogames.trui.uicomponent.worldview.rules.attribute;

import nl.ritogames.shared.dto.gameobject.attribute.Armour;
import nl.ritogames.trui.uicomponent.worldview.ruleengine.WorldViewExpression;
import nl.ritogames.trui.uicomponent.worldview.ruleengine.WorldViewRule;

public class AccessibleWorldTileWithArmourRule extends
    AccessibleWorldTileWithAttributeRule implements
    WorldViewRule {

  @Override
  public boolean evaluate(WorldViewExpression expression) {
    return super.evaluate(expression, Armour.class);
  }

  @Override
  public String texture() {
    return ")";
  }

}
