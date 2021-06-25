package nl.ritogames.shared.dto.agent.expression;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The expression type Percentage.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Percentage extends ExpressionDTO {

    @JsonProperty("percentage")
    private String percentage;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Percentage that = (Percentage) o;

        return percentage != null ? percentage.equals(that.percentage) : that.percentage == null;
    }

    @Override
    public int hashCode() {
        return percentage != null ? percentage.hashCode() : 0;
    }
}
