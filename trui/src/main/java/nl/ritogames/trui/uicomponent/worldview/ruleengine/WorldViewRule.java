package nl.ritogames.trui.uicomponent.worldview.ruleengine;

public interface WorldViewRule {
  boolean evaluate(WorldViewExpression expression);
  String texture();
}
