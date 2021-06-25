package nl.ritogames.agentcompiler.ast.literal;

import lombok.Getter;

/**
 * The type Percentage literal.
 */
@Getter
public class PercentageLiteral extends Literal {

  /**
   * The Value.
   */
  int value;

  /**
   * Instantiates a new Percentage literal.
   *
   * @param value the value
   */
  public PercentageLiteral(String value) {
    this.value = Integer.parseInt(value.replace("%", ""));
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (other != null && this.getClass() == other.getClass()) {
      return (this.value == ((PercentageLiteral) other).getValue());
    } else {
      return false;
    }
  }

  @Override
  public String toString() {
    return value + "%";
  }
}
