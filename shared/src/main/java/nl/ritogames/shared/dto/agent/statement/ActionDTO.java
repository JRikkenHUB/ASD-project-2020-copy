package nl.ritogames.shared.dto.agent.statement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The statement Action.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActionDTO extends StatementDTO {

    String action;
    String parameter;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ActionDTO actionDTO = (ActionDTO) o;

        if (action != null ? !action.equals(actionDTO.action) : actionDTO.action != null) {
            return false;
        }
        return parameter != null ? parameter.equals(actionDTO.parameter) : actionDTO.parameter == null;
    }

    @Override
    public int hashCode() {
        int result = action != null ? action.hashCode() : 0;
        result = 31 * result + (parameter != null ? parameter.hashCode() : 0);
        return result;
    }
}
