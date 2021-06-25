package nl.ritogames.shared.dto.agent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * The Agent definition class which contains a list of behaviourDTOs.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgentDefinitionDTO {

    private List<BehaviorDTO> behaviors;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AgentDefinitionDTO that = (AgentDefinitionDTO) o;

        return behaviors != null ? behaviors.equals(that.behaviors) : that.behaviors == null;
    }

    @Override
    public int hashCode() {
        return behaviors != null ? behaviors.hashCode() : 0;
    }
}
