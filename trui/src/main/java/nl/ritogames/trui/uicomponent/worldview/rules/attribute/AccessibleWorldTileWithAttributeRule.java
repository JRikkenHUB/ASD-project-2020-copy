package nl.ritogames.trui.uicomponent.worldview.rules.attribute;

import nl.ritogames.shared.dto.gameobject.attribute.Attribute;
import nl.ritogames.shared.dto.gameobject.world.tile.AccessibleWorldTile;
import nl.ritogames.trui.uicomponent.worldview.ruleengine.WorldViewExpression;

public class AccessibleWorldTileWithAttributeRule {

  public boolean evaluate(WorldViewExpression expression, Class<? extends Attribute> clazz) {
    return (expression.getTile().getClass().equals(AccessibleWorldTile.class)) &&
        ((AccessibleWorldTile) expression.getTile()).hasAttribute() &&
        ((AccessibleWorldTile) expression.getTile()).getAttribute().getClass().equals(clazz);
  }
}
