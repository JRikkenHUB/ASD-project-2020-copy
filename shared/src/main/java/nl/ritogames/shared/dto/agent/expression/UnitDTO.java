package nl.ritogames.shared.dto.agent.expression;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The expression type Unit.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UnitDTO extends ExpressionDTO {

    @JsonProperty("unit")
    private String unit;
    @JsonProperty("attribute")
    private String attribute;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UnitDTO unitDTO = (UnitDTO) o;

        if (unit != null ? !unit.equals(unitDTO.unit) : unitDTO.unit != null) {
            return false;
        }
        return attribute != null ? attribute.equals(unitDTO.attribute) : unitDTO.attribute == null;
    }

    @Override
    public int hashCode() {
        int result = unit != null ? unit.hashCode() : 0;
        result = 31 * result + (attribute != null ? attribute.hashCode() : 0);
        return result;
    }
}
