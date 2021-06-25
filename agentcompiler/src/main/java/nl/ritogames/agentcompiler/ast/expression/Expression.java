package nl.ritogames.agentcompiler.ast.expression;

import nl.ritogames.agentcompiler.ast.ASTNode;

/**
 * The type Expression.
 */
public abstract class Expression extends ASTNode {

  public abstract String toString();

  public abstract String getExpressionType();
}
