package nl.ritogames.intelligentagent.behavior;

import java.util.function.Predicate;
import nl.ritogames.shared.dto.GameStateContext;

/**
 * A decision node represents an if-statement for an agent. The agent will evaluate the decision
 * based on the game state context. If the condition is true, it will execute the ifTrue behavior
 * node, otherwise it will execute ifFalse (if present).
 */
public class DecisionNode extends BehaviorNode {

  private final Predicate<GameStateContext> condition;
  private final BehaviorNode ifTrue;
  private final BehaviorNode ifFalse;

  public DecisionNode(
      Predicate<GameStateContext> condition,
      BehaviorNode ifTrue,
      BehaviorNode ifFalse,
      BehaviorNode nextNode) {
    super(nextNode);
    this.condition = condition;
    this.ifTrue = ifTrue;
    this.ifFalse = ifFalse;
  }

  public DecisionNode(
      Predicate<GameStateContext> condition, BehaviorNode ifTrue, BehaviorNode ifFalse) {
    super(null);
    this.condition = condition;
    this.ifTrue = ifTrue;
    this.ifFalse = ifFalse;
  }

  /**
   * Evaluates the condition and executes a child node accordingly.
   *
   * @param context An updated game state context the agent is in.
   */
  @Override
  public void execute(GameStateContext context) {
    if (condition.test(context)) {
      ifTrue.execute(context);
    } else if (ifFalse != null) {
      ifFalse.execute(context);
    }

    executeNext(context);
  }
}
