package nl.ritogames.agentcompiler.ast;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.ritogames.agentcompiler.ast.expression.Operand;

/**
 * The type Artifact.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Artifact extends ASTNode implements InstructionParameter, Operand {

  /**
   * The type of the Artifact as String
   */
  String artifactType;

  @Override
  public String getParameterName() {
    return this.artifactType;
  }

  @Override
  public String toString() {
    return artifactType;
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (other != null && this.getClass() == other.getClass()) {
      return (this.artifactType.equals(((Artifact) other).getArtifactType()));
    } else {
      return false;
    }
  }
}
