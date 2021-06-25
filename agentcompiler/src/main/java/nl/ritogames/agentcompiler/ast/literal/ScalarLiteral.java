package nl.ritogames.agentcompiler.ast.literal;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The type Scalar literal.
 */
@Getter
@AllArgsConstructor
public class ScalarLiteral extends Literal {

  int value;

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (other != null && this.getClass() == other.getClass()) {
      return (this.value == ((ScalarLiteral) other).getValue());
    } else {
      return false;
    }
  }

  @Override
  public String toString() {
    return Integer.toString(value);
  }
}
