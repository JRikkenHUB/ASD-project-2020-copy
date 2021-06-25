package nl.ritogames.shared.dto.agent.expression;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The expression type Dual expression.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DualExpression extends ExpressionDTO {

    @JsonProperty("expressionType")
    private String expressionType;
    @JsonProperty("lhs")
    private ExpressionDTO lhs;
    @JsonProperty("rhs")
    private ExpressionDTO rhs;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DualExpression that = (DualExpression) o;

        if (expressionType != null ? !expressionType.equals(that.expressionType)
                : that.expressionType != null) {
            return false;
        }
        if (lhs != null ? !lhs.equals(that.lhs) : that.lhs != null) {
            return false;
        }
        return rhs != null ? rhs.equals(that.rhs) : that.rhs == null;
    }

    @Override
    public int hashCode() {
        int result = expressionType != null ? expressionType.hashCode() : 0;
        result = 31 * result + (lhs != null ? lhs.hashCode() : 0);
        result = 31 * result + (rhs != null ? rhs.hashCode() : 0);
        return result;
    }
}
