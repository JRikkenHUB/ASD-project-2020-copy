package nl.ritogames.agentcompiler.ast.expression;

/**
 * The type Lower than expression.
 */
public class LowerThanExpression extends ConditionalExpression {

  /**
   * Instantiates a new Lower than expression.
   *
   * @param lhs the left hand side
   * @param rhs the right hand side
   */
  public LowerThanExpression(Operand lhs, Operand rhs) {
    super(lhs, rhs);
  }

  @Override
  public String toString() {
    return lhs.toString() + " < " + rhs.toString();
  }

  @Override
  public String getExpressionType() {
    return "lt";
  }


}
