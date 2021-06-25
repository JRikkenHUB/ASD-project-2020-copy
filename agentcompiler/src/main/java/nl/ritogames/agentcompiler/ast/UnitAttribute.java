package nl.ritogames.agentcompiler.ast;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nl.ritogames.agentcompiler.ast.expression.Operand;

/**
 * The type Unit attribute.
 */
@Getter
@Setter
@AllArgsConstructor
public class UnitAttribute extends ASTNode implements Operand {

    /**
     * The Unit.
     */
    Unit unit;
    /**
     * The Attribute of the Unit
     */
    Attribute attribute;

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other != null && this.getClass() == other.getClass()) {
            return (unit.equals(((UnitAttribute) other).getUnit()) && attribute
                .equals(((UnitAttribute) other).getAttribute()));
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "UnitAttribute (" + unit.toString() + ", " + attribute.toString() + ")";
    }
}
