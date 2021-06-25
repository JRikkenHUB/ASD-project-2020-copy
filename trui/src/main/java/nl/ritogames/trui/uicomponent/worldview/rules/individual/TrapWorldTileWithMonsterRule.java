package nl.ritogames.trui.uicomponent.worldview.rules.individual;

import nl.ritogames.shared.dto.gameobject.individual.Monster;
import nl.ritogames.trui.uicomponent.worldview.ruleengine.WorldViewExpression;
import nl.ritogames.trui.uicomponent.worldview.ruleengine.WorldViewRule;

public class TrapWorldTileWithMonsterRule extends TrapWorldTileWithIndividualRule implements
    WorldViewRule {

  @Override
  public boolean evaluate(WorldViewExpression expression) {
    return super.evaluate(expression, Monster.class);
  }

  @Override
  public String texture() {
    return "M";
  }
}
