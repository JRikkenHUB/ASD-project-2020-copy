package nl.ritogames.shared.dto.agent.expression;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * The abstract class Expression which all other expressions extend.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @Type(value = DualExpression.class, name = "dualExpression"),
        @Type(value = Percentage.class, name = "percentage"),
        @Type(value = Scalar.class, name = "scalar"),
        @Type(value = StateDTO.class, name = "state"),
        @Type(value = StringLiteralDTO.class, name = "stringLiteral"),
        @Type(value = UnitDTO.class, name = "unit"),
        @Type(value = Variable.class, name = "variable"),
        @Type(value = ArtifactDTO.class, name = "artifact"),

})
public abstract class Expression {

}
