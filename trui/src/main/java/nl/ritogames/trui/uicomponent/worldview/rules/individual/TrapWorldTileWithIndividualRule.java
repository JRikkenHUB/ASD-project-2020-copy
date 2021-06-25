package nl.ritogames.trui.uicomponent.worldview.rules.individual;

import nl.ritogames.shared.dto.gameobject.individual.Individual;
import nl.ritogames.shared.dto.gameobject.world.tile.TrapTile;
import nl.ritogames.trui.uicomponent.worldview.ruleengine.WorldViewExpression;

public class TrapWorldTileWithIndividualRule {

  public boolean evaluate(WorldViewExpression expression, Class<? extends Individual> clazz) {
    return (expression.getTile().getClass().equals(TrapTile.class)) &&
        ((TrapTile) expression.getTile()).hasIndividual() &&
        ((TrapTile) expression.getTile()).getIndividual().getClass().equals(clazz);
  }
}
