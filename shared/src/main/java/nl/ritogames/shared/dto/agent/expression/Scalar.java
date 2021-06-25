package nl.ritogames.shared.dto.agent.expression;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The expression type Scalar.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Scalar extends ExpressionDTO {

    @JsonProperty("scalar")
    private String scalar;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Scalar scalar1 = (Scalar) o;

        return scalar != null ? scalar.equals(scalar1.scalar) : scalar1.scalar == null;
    }

    @Override
    public int hashCode() {
        return scalar != null ? scalar.hashCode() : 0;
    }
}
