package nl.ritogames.agentcompiler.ast;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * The type Agent definition.
 */
@Getter
@Setter
@AllArgsConstructor
public class AgentDefinition extends ASTNode {

  List<Behavior> behaviors;

  public AgentDefinition() {
    this.behaviors = new ArrayList<>();
  }

  @Override
  public List<ASTNode> getChildren() {
    return new ArrayList<>(this.behaviors);
  }

  @Override
  public ASTNode addChild(ASTNode child) {
    behaviors.add((Behavior) child);
    return this;
  }

  @Override
  public String toString() {
    return behaviors.toString();
  }
}
