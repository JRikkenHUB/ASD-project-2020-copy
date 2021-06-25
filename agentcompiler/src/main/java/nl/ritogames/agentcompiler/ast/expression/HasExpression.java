package nl.ritogames.agentcompiler.ast.expression;

/**
 * The type Has expression.
 */
public class HasExpression extends ConditionalExpression {

  /**
   * Instantiates a new Has expression.
   *
   * @param lhs the left hand side
   * @param rhs the right hand side
   */
  public HasExpression(Operand lhs, Operand rhs) {
    super(lhs, rhs);
  }

  @Override
  public String toString() {
    return lhs.toString() + " HAS " + rhs.toString();
  }

  @Override
  public String getExpressionType() {
    return "has";
  }
}
