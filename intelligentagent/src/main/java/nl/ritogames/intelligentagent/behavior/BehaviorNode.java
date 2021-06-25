package nl.ritogames.intelligentagent.behavior;

import lombok.Setter;
import nl.ritogames.shared.dto.GameStateContext;

/** The abstract class BehaviorNode which all other nodes extend. */
@Setter
public abstract class BehaviorNode {

  private BehaviorNode nextNode;

  /**
   * Constructor for abstract class BehaviorNode
   *
   * @param nextNode The next behaviorNode
   */
  protected BehaviorNode(BehaviorNode nextNode) {
    this.nextNode = nextNode;
  }

  /**
   * Executes action or decision.
   *
   * @param context the GameStateContext
   * @see GameStateContext
   */
  public abstract void execute(GameStateContext context);

  /**
   * Executes the sibling node.
   *
   * @param context A game state context for the agent to respond to.
   */
  protected void executeNext(GameStateContext context) {
    if (nextNode != null) {
      nextNode.execute(context);
    }
  }
}
