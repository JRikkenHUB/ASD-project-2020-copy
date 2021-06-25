package nl.ritogames.shared.dto.agent.expression;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The expression type Artifact.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArtifactDTO extends ExpressionDTO {

    @JsonProperty("artifact")
    private String artifact;
}
