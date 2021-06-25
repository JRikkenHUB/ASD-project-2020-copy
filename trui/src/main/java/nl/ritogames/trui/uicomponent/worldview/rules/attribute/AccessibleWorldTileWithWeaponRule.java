package nl.ritogames.trui.uicomponent.worldview.rules.attribute;

import nl.ritogames.shared.dto.gameobject.attribute.Weapon;
import nl.ritogames.trui.uicomponent.worldview.ruleengine.WorldViewExpression;
import nl.ritogames.trui.uicomponent.worldview.ruleengine.WorldViewRule;

public class AccessibleWorldTileWithWeaponRule extends
    AccessibleWorldTileWithAttributeRule implements WorldViewRule {

  @Override
  public boolean evaluate(WorldViewExpression expression) {
    return super.evaluate(expression, Weapon.class);

  }

  @Override
  public String texture() {
    return "+";
  }
}
