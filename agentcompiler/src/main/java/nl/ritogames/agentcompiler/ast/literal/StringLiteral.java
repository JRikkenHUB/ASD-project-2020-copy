package nl.ritogames.agentcompiler.ast.literal;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The type String literal.
 */
@Getter
@AllArgsConstructor
public class StringLiteral extends Literal {

  /**
   * The Value.
   */
  String value;

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (other != null && this.getClass() == other.getClass()) {
      return (this.value.equals(((StringLiteral) other).getValue()));
    } else {
      return false;
    }
  }

  @Override
  public String toString() {
    return value;
  }
}
