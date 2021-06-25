package nl.ritogames.shared.dto.agent.statement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.ritogames.shared.dto.agent.expression.ExpressionDTO;

import java.util.List;

/**
 * The statement Decision.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DecisionDTO extends StatementDTO {

    private ExpressionDTO condition;
    private List<StatementDTO> ifTrue;
    private List<StatementDTO> ifFalse;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DecisionDTO that = (DecisionDTO) o;

        if (condition != null ? !condition.equals(that.condition) : that.condition != null) {
            return false;
        }
        if (ifTrue != null ? !ifTrue.equals(that.ifTrue) : that.ifTrue != null) {
            return false;
        }
        return ifFalse != null ? ifFalse.equals(that.ifFalse) : that.ifFalse == null;
    }

    @Override
    public int hashCode() {
        int result = condition != null ? condition.hashCode() : 0;
        result = 31 * result + (ifTrue != null ? ifTrue.hashCode() : 0);
        result = 31 * result + (ifFalse != null ? ifFalse.hashCode() : 0);
        return result;
    }
}
