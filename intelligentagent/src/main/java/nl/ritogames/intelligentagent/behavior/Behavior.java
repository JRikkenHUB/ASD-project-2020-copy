package nl.ritogames.intelligentagent.behavior;

import lombok.Getter;
import nl.ritogames.shared.dto.GameStateContext;

/**
 * A behavior is represented by a first-child next-sibling tree. Every behavior is named so that
 * they can be called from other behaviors.
 */
@Getter
public class Behavior {

  private final String behaviorName;
  private final BehaviorTree behaviorTree;

  public Behavior(String behaviorName, BehaviorTree behaviorTree) {
    this.behaviorName = behaviorName;
    this.behaviorTree = behaviorTree;
  }

  /**
   * Handle a new game state update by walking through the behavior tree.
   *
   * @param context The updated context the agent is in.
   */
  public void handle(GameStateContext context) {
    behaviorTree.getFirstBehaviorNode().execute(context);
  }
}
