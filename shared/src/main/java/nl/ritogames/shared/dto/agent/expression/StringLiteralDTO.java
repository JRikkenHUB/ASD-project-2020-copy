package nl.ritogames.shared.dto.agent.expression;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The expression type String literal.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StringLiteralDTO extends ExpressionDTO {

    @JsonProperty("stringLiteral")
    private String stringLiteral;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StringLiteralDTO that = (StringLiteralDTO) o;

        return stringLiteral != null ? stringLiteral.equals(that.stringLiteral)
                : that.stringLiteral == null;
    }

    @Override
    public int hashCode() {
        return stringLiteral != null ? stringLiteral.hashCode() : 0;
    }
}
