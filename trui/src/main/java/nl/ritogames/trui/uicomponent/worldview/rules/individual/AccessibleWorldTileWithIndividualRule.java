package nl.ritogames.trui.uicomponent.worldview.rules.individual;

import nl.ritogames.shared.dto.gameobject.individual.Individual;
import nl.ritogames.shared.dto.gameobject.world.tile.AccessibleWorldTile;
import nl.ritogames.trui.uicomponent.worldview.ruleengine.WorldViewExpression;

public class AccessibleWorldTileWithIndividualRule {

  public boolean evaluate(WorldViewExpression expression, Class<? extends Individual> clazz) {
    return (expression.getTile().getClass().equals(AccessibleWorldTile.class)) &&
        ((AccessibleWorldTile) expression.getTile()).hasIndividual() &&
        ((AccessibleWorldTile) expression.getTile()).getIndividual().getClass().equals(clazz);
  }
}
