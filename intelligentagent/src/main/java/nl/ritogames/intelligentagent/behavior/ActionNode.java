package nl.ritogames.intelligentagent.behavior;

import java.util.function.Consumer;
import nl.ritogames.shared.dto.GameStateContext;

/**
 * An ActionNode is a leaf node that contains an action for the agent to execute. This action is a
 * Consumer function that takes in the GameStateContext.
 */
public class ActionNode extends BehaviorNode {

  private final Consumer<GameStateContext> action;

  public ActionNode(Consumer<GameStateContext> action) {
    super(null);
    this.action = action;
  }

  public ActionNode(BehaviorNode nextNode, Consumer<GameStateContext> action) {
    super(nextNode);
    this.action = action;
  }

  /**
   * Perform the action in this ActionNode based on the context.
   *
   * @param context The context the agent is in.
   */
  @Override
  public void execute(GameStateContext context) {
    action.accept(context);
    executeNext(context);
  }
}
