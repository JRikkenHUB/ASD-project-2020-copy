package nl.ritogames.shared.dto.agent.statement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The statement BehaviorCall.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BehaviorCallDTO extends StatementDTO {

    String behaviorCall;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BehaviorCallDTO that = (BehaviorCallDTO) o;

        return behaviorCall != null ? behaviorCall.equals(that.behaviorCall)
                : that.behaviorCall == null;
    }

    @Override
    public int hashCode() {
        return behaviorCall != null ? behaviorCall.hashCode() : 0;
    }
}
