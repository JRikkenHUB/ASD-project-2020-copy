package nl.ritogames.agentcompiler.ast;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.ritogames.agentcompiler.ast.expression.Operand;

/**
 * The type State. The State for a Unit or Artifact (near, attacking)
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class State extends ASTNode implements Operand {

  String stateText;

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (other != null && this.getClass() == other.getClass()) {
      return (this.stateText.equals(((State) other).getStateText()));
    } else {
      return false;
    }
  }

  @Override
  public String toString() {
    return stateText;
  }
}
