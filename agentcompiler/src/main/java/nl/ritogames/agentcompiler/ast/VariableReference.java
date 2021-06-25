package nl.ritogames.agentcompiler.ast;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.ritogames.agentcompiler.ast.expression.Operand;

/**
 * The type Variable reference. The name of the VariableReference (gamemode, playercount)
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VariableReference extends ASTNode implements Operand {

  String variableName;

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (other != null && this.getClass() == other.getClass()) {
      return (this.variableName.equals(((VariableReference) other).getVariableName()));
    } else {
      return false;
    }
  }

  @Override
  public String toString() {
    return variableName;
  }
}
