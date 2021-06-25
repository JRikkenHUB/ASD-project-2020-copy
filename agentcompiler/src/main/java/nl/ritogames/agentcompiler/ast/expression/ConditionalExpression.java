package nl.ritogames.agentcompiler.ast.expression;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import nl.ritogames.agentcompiler.ast.ASTNode;
import nl.ritogames.agentcompiler.ast.Artifact;
import nl.ritogames.agentcompiler.ast.Unit;
import nl.ritogames.agentcompiler.ast.UnitAttribute;

/**
 * The type Conditional expression.
 */
@Getter
public abstract class ConditionalExpression extends Expression {

  Operand lhs;
  Operand rhs;

  /**
   * Instantiates a new Conditional expression.
   *
   * @param lhs the left hand side
   * @param rhs the right hand side
   */
  protected ConditionalExpression(Operand lhs, Operand rhs) {
    this.lhs = lhs;
    this.rhs = rhs;
  }

  /**
   * Gets sensors.
   *
   * @return A List of Sensors in the current Expression
   */
  public List<ASTNode> getSensor() {
    List<ASTNode> sensorList = new ArrayList<>();
    if ((lhs instanceof Unit && !((Unit) lhs).isSelf()) || lhs instanceof Artifact) {
      sensorList.add((ASTNode) lhs);
    } else if (lhs instanceof UnitAttribute && !(((UnitAttribute) lhs).getUnit().isSelf())) {
      sensorList.add(((UnitAttribute) lhs).getUnit());
    }
    if ((rhs instanceof Unit && !((Unit) rhs).isSelf()) || rhs instanceof Artifact) {
      sensorList.add((ASTNode) rhs);
    } else if (rhs instanceof UnitAttribute && !(((UnitAttribute) rhs).getUnit().isSelf())) {
      sensorList.add(((UnitAttribute) rhs).getUnit());
    }
    return sensorList;
  }
}
