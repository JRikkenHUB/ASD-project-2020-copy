package nl.ritogames.agentcompiler.ast;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.ritogames.agentcompiler.ast.expression.Operand;

/**
 * The type Unit (my, enemy, teammate)
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Unit extends ASTNode implements InstructionParameter, Operand {

    /**
     * The Unit type.
     */
    String unitType;

    @Override
    public String getParameterName() {
        return this.unitType;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other != null && this.getClass() == other.getClass()) {
            return (this.unitType.equals(((Unit) other).getUnitType()));
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return unitType;
    }

    /**
     * Checks if the Unit refers to itself (my)
     *
     * @return boolean whether or not this Unit refers to itself
     */
    public boolean isSelf() {
        return this.unitType.equals("my");
    }
}
