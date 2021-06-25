package nl.ritogames.shared.dto.agent;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.ritogames.shared.dto.agent.statement.StatementDTO;

import java.util.List;

/**
 * The type BehaviorDTO which contains a name and a list of Statements.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BehaviorDTO {

    @JsonProperty("behaviorName")
    private String behaviorName;
    @JsonProperty("body")
    private List<StatementDTO> body;

    @Override
    public String toString() {
        return "BehaviorDTO{" +
                "behaviorName='" + behaviorName + '\'' +
                ", body=" + body +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BehaviorDTO that = (BehaviorDTO) o;

        if (behaviorName != null ? !behaviorName.equals(that.behaviorName)
                : that.behaviorName != null) {
            return false;
        }
        return body != null ? body.equals(that.body) : that.body == null;
    }

    @Override
    public int hashCode() {
        int result = behaviorName != null ? behaviorName.hashCode() : 0;
        result = 31 * result + (body != null ? body.hashCode() : 0);
        return result;
    }
}
