package nl.ritogames.agentcompiler.ast;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Body.
 */
@Getter
@Setter
@AllArgsConstructor
public class Body extends ASTNode {

  List<BodyElement> bodyElements;

  public Body() {
    this.bodyElements = new ArrayList<>();
  }

  @Override
  public List<ASTNode> getChildren() {
    return new ArrayList<>(this.bodyElements);
  }

  @Override
  public ASTNode addChild(ASTNode child) {
    bodyElements.add((BodyElement) child);
    return this;
  }

  @Override
  public String toString() {
    return bodyElements.toString();
  }
}
