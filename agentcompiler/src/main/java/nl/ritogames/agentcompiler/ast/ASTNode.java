package nl.ritogames.agentcompiler.ast;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Ast node.
 */
public class ASTNode {

  /**
   * Different AST nodes use different attributes to store their children. This method provides a
   * unified interface.
   *
   * @return the children
   */

  public List<ASTNode> getChildren() {
    return new ArrayList<>();
  }

  /**
   * By implementing this method in a subclass you can easily create AST nodes incrementally.
   *
   * @param child the child
   * @return the ast node
   */

  public ASTNode addChild(ASTNode child) {
    return this;
  }

  /**
   * By implementing this method you can easily make transformations that prune the AST.
   *
   * @param child the child
   * @return the ast node
   */

  public ASTNode removeChild(ASTNode child) {
    return this;
  }
}
