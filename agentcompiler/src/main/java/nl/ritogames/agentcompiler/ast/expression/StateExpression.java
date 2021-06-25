package nl.ritogames.agentcompiler.ast.expression;

import java.util.ArrayList;
import java.util.List;
import nl.ritogames.agentcompiler.ast.ASTNode;

/**
 * The type State expression.
 */
public class StateExpression extends ConditionalExpression {

  /**
   * Instantiates a new State expression.
   *
   * @param lhs the left hand side
   * @param rhs the right hand side
   */
  public StateExpression(Operand lhs, Operand rhs) {
    super(lhs, rhs);
  }

  @Override
  public String toString() {
    return lhs.toString() + " IS " + rhs.toString();
  }

  @Override
  public String getExpressionType() {
    return "state";
  }

  @Override
  public List<ASTNode> getSensor() {
    List<ASTNode> sensorList = new ArrayList<>();
    sensorList.add((ASTNode) lhs);
    return sensorList;
  }
}
