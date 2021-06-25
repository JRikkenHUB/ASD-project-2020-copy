package nl.ritogames.agentcompiler.ast.expression;

/**
 * The type Greater than expression.
 */
public class GreaterThanExpression extends ConditionalExpression {

  /**
   * Instantiates a new Greater than expression.
   *
   * @param lhs the left hand side
   * @param rhs the right hand side
   */
  public GreaterThanExpression(Operand lhs, Operand rhs) {
    super(lhs, rhs);
  }

  @Override
  public String toString() {
    return lhs.toString() + " > " + rhs.toString();
  }

  @Override
  public String getExpressionType() {
    return "gt";
  }
}
