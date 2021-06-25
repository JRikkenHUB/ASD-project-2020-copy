package nl.ritogames.agentcompiler.ast.expression;

/**
 * The type Equals expression.
 */
public class EqualsExpression extends ConditionalExpression {

  /**
   * Instantiates a new Equals expression.
   *
   * @param lhs the left hand side
   * @param rhs the right hand side
   */
  public EqualsExpression(Operand lhs, Operand rhs) {
    super(lhs, rhs);
  }

  @Override
  public String toString() {
    return lhs.toString() + " == " + rhs.toString();
  }

  @Override
  public String getExpressionType() {
    return "eq";
  }
}
