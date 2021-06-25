package nl.ritogames.agentcompiler.ast;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The type Attribute.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Attribute extends ASTNode {

  /**
   * The Attribute type.
   */
  String attributeType;

  @Override
  public String toString() {
    return attributeType;
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (other != null && this.getClass() == other.getClass()) {
      return this.attributeType.equals(((Attribute) other).getAttributeType());
    } else {
      return false;
    }
  }
}
