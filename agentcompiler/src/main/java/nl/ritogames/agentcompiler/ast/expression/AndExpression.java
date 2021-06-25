package nl.ritogames.agentcompiler.ast.expression;

/**
 * The AndExpression checks if both the left and right expression are true.
 */
public class AndExpression extends LogicalExpression {

  /**
   * Instantiates a new And expression.
   *
   * @param lhs the left hand side
   * @param rhs the right hand side
   */
  public AndExpression(Expression lhs, Expression rhs) {
    super(lhs, rhs);
  }

  @Override
  public String toString() {
    return "(" + lhs.toString() + " && " + rhs.toString() + ")";
  }

  @Override
  public String getExpressionType() {
    return "and";
  }
}
