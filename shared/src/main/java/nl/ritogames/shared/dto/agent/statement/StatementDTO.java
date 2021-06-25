package nl.ritogames.shared.dto.agent.statement;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * The abstract class StatementDTO which all other statements extend.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @Type(value = ActionDTO.class, name = "action"),
        @Type(value = BehaviorCallDTO.class, name = "behaviorCall"),
        @Type(value = DecisionDTO.class, name = "decision")
})
public abstract class StatementDTO {

}
