package nl.ritogames.agentcompiler.ast;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * The type Else clause.
 */
@Getter
@Setter
@AllArgsConstructor
public class ElseClause extends ASTNode {

  Body body;

  @Override
  public ASTNode addChild(ASTNode child) {
    body.addChild(child);
    return this;
  }

  @Override
  public List<ASTNode> getChildren() {
    return body.getChildren();
  }

  @Override
  public String toString() {
    return "Else: " + body.toString();
  }
}
