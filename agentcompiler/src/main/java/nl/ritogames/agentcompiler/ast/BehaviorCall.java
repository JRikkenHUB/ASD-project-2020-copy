package nl.ritogames.agentcompiler.ast;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The type Behavior call.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BehaviorCall extends BodyElement {

  /**
   * The Behavior name.
   */
  String behaviorName;

  @Override
  public String toString() {
    return behaviorName;
  }
}
