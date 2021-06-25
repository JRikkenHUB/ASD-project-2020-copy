package nl.ritogames.intelligentagent.behavior;

import lombok.Getter;
import lombok.Setter;

/**
 * A behavior tree is a first-child next-sibling representation of an agent's behavior. Every node
 * in this tree is a statement in the agent's code. For every game update, the behavior tree is
 * traversed.
 */
@Getter
@Setter
public class BehaviorTree {

  private BehaviorNode firstBehaviorNode;
}
