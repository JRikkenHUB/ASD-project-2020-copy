package nl.ritogames.agentcompiler.ast.expression;

/**
 * The type Or expression.
 */
public class OrExpression extends LogicalExpression {

  /**
   * Instantiates a new Or expression.
   *
   * @param lhs the left hand side
   * @param rhs the right hand side
   */
  public OrExpression(Expression lhs, Expression rhs) {
    super(lhs, rhs);
  }

  @Override
  public String toString() {
    return "(" + lhs.toString() + " || " + rhs.toString() + ")";
  }

  @Override
  public String getExpressionType() {
    return "or";
  }
}
