package nl.ritogames.agentcompiler.ast.expression;

import lombok.Getter;

/**
 * The type Logical expression.
 */
@Getter
public abstract class LogicalExpression extends Expression {

  Expression lhs;
  Expression rhs;

  /**
   * Instantiates a new Logical expression.
   *
   * @param lhs the left hand side
   * @param rhs the right hand side
   */
  protected LogicalExpression(Expression lhs, Expression rhs) {
    this.lhs = lhs;
    this.rhs = rhs;
  }
}
