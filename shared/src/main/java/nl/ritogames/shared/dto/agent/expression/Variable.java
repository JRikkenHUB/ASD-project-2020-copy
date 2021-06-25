package nl.ritogames.shared.dto.agent.expression;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The expression type Variable.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Variable extends ExpressionDTO {

    @JsonProperty("variable")
    private String variable;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Variable variable1 = (Variable) o;

        return variable != null ? variable.equals(variable1.variable) : variable1.variable == null;
    }

    @Override
    public int hashCode() {
        return variable != null ? variable.hashCode() : 0;
    }
}
