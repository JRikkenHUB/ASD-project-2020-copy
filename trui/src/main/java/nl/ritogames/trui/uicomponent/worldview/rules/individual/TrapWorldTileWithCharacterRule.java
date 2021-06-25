package nl.ritogames.trui.uicomponent.worldview.rules.individual;

import nl.ritogames.shared.dto.gameobject.individual.Character;
import nl.ritogames.trui.uicomponent.worldview.ruleengine.WorldViewExpression;
import nl.ritogames.trui.uicomponent.worldview.ruleengine.WorldViewRule;

public class TrapWorldTileWithCharacterRule extends TrapWorldTileWithIndividualRule implements
    WorldViewRule {

  @Override
  public boolean evaluate(WorldViewExpression expression) {
    return super.evaluate(expression, Character.class);
  }

  @Override
  public String texture() {
    return "P";
  }
}
