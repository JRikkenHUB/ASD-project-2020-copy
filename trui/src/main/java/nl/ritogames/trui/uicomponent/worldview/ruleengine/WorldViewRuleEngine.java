package nl.ritogames.trui.uicomponent.worldview.ruleengine;

import nl.ritogames.trui.uicomponent.worldview.rules.tile.UnknownTileRule;

import java.util.ArrayList;
import java.util.List;

public class WorldViewRuleEngine {

  private List<WorldViewRule> rules = new ArrayList<>();

  public void addRule(WorldViewRule rule) {
    rules.add(rule);
  }

  public void addRules(List<WorldViewRule> rules) {
    this.rules = rules;
  }

  public String process(WorldViewExpression expression) {
    return rules
        .stream()
        .filter(r -> r.evaluate(expression))
        .findFirst()
        .orElse(new UnknownTileRule())
        .texture();
  }
}
