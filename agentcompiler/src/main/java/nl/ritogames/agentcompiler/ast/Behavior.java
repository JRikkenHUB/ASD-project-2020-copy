package nl.ritogames.agentcompiler.ast;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * The type Behavior.
 */
@Getter
@Setter
@AllArgsConstructor
public class Behavior extends ASTNode {

  String behaviorName;
  Body body;

  /**
   * Instantiates a new Behavior.
   *
   * @param name the name
   */
  public Behavior(String name) {
    this.behaviorName = name;
    this.body = new Body();
  }

  @Override
  public ASTNode addChild(ASTNode child) {
    body.addChild(child);
    return this;
  }

  @Override
  public List<ASTNode> getChildren() {
    return this.body.getChildren();
  }

  @Override
  public String toString() {
    return behaviorName + ": " + body.toString();
  }
}
