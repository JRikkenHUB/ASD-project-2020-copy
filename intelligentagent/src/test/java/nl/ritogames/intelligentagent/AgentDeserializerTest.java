package nl.ritogames.intelligentagent;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import nl.ritogames.shared.dto.agent.AgentDefinitionDTO;
import nl.ritogames.shared.dto.agent.BehaviorDTO;
import nl.ritogames.shared.dto.agent.expression.ArtifactDTO;
import nl.ritogames.shared.dto.agent.expression.DualExpression;
import nl.ritogames.shared.dto.agent.expression.Percentage;
import nl.ritogames.shared.dto.agent.expression.Scalar;
import nl.ritogames.shared.dto.agent.expression.StateDTO;
import nl.ritogames.shared.dto.agent.expression.StringLiteralDTO;
import nl.ritogames.shared.dto.agent.expression.UnitDTO;
import nl.ritogames.shared.dto.agent.expression.Variable;
import nl.ritogames.shared.dto.agent.statement.ActionDTO;
import nl.ritogames.shared.dto.agent.statement.BehaviorCallDTO;
import nl.ritogames.shared.dto.agent.statement.DecisionDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AgentDeserializerTest {

  @Test
  void dualExpressionTest() throws IOException {
    String json =
        "{\n"
            + "            \"type\": \"dualExpression\",\n"
            + "            \"expressionType\": \"eq\",\n"
            + "            \"lhs\": {\n"
            + "              \"type\": \"variable\",\n"
            + "              \"variable\": \"gamemode\"\n"
            + "            },\n"
            + "            \"rhs\": {\n"
            + "              \"type\": \"stringLiteral\",\n"
            + "              \"stringLiteral\": \"lms\"\n"
            + "            }\n"
            + "          }";

    DualExpression dualExpression = new ObjectMapper().readValue(json, DualExpression.class);

    Assertions.assertEquals("eq", dualExpression.getExpressionType());
    Assertions.assertEquals("gamemode", ((Variable) dualExpression.getLhs()).getVariable());
    Assertions.assertEquals("lms", ((StringLiteralDTO) dualExpression.getRhs()).getStringLiteral());
  }

  @Test
  void percentageTest() throws IOException {
    String json =
        "{\n"
            + "                  \"type\": \"percentage\",\n"
            + "                  \"percentage\": \"10%\"\n"
            + "                }";

    Percentage percentage = new ObjectMapper().readValue(json, Percentage.class);

    Assertions.assertEquals("10%", percentage.getPercentage());
  }

  @Test
  void artifactTest() throws IOException {
    String json =
        "{\n"
            + "                  \"type\": \"artifact\",\n"
            + "                  \"artifact\": \"flag\"\n"
            + "                }";

    ArtifactDTO artifact = new ObjectMapper().readValue(json, ArtifactDTO.class);

    Assertions.assertEquals("flag", artifact.getArtifact());
  }

  @Test
  void scalarTest() throws IOException {
    String json =
        "{\n"
            + "                  \"type\": \"scalar\",\n"
            + "                  \"scalar\": \"69\"\n"
            + "                }";

    Scalar scalar = new ObjectMapper().readValue(json, Scalar.class);

    Assertions.assertEquals("69", scalar.getScalar());
  }

  @Test
  void stateTest() throws IOException {
    String json =
        "{\n"
            + "                    \"type\": \"state\",\n"
            + "                    \"state\": \"near\"\n"
            + "                  }";

    StateDTO state = new ObjectMapper().readValue(json, StateDTO.class);

    Assertions.assertEquals("near", state.getState());
  }

  @Test
  void stringLiteralTest() throws IOException {
    String json =
        "{\n"
            + "              \"type\": \"stringLiteral\",\n"
            + "              \"stringLiteral\": \"lms\"\n"
            + "            }";

    StringLiteralDTO stringLiteral = new ObjectMapper().readValue(json, StringLiteralDTO.class);

    Assertions.assertEquals("lms", stringLiteral.getStringLiteral());
  }

  @Test
  void unitTest() throws IOException {
    String json =
        "{\n"
            + "                    \"type\": \"unit\",\n"
            + "                    \"unit\": \"enemy\"\n"
            + "                  }";

    UnitDTO unit = new ObjectMapper().readValue(json, UnitDTO.class);

    Assertions.assertEquals("enemy", unit.getUnit());
  }

  @Test
  void unitAttributeTest() throws IOException {
    String json =
        "{\n"
            + "                    \"type\": \"unit\",\n"
            + "                    \"unit\": \"my\",\n"
            + "                    \"attribute\": \"health\"\n"
            + "                  }";

    UnitDTO unit = new ObjectMapper().readValue(json, UnitDTO.class);

    Assertions.assertEquals("my", unit.getUnit());
    Assertions.assertEquals("health", unit.getAttribute());
  }

  @Test
  void variableTest() throws IOException {
    String json =
        "{\n"
            + "              \"type\": \"variable\",\n"
            + "              \"variable\": \"gamemode\"\n"
            + "            }";

    Variable variable = new ObjectMapper().readValue(json, Variable.class);

    Assertions.assertEquals("gamemode", variable.getVariable());
  }

  @Test
  void actionTest() throws IOException {
    String json =
        "{\n"
            + "          \"type\": \"action\",\n"
            + "          \"action\": \"move\",\n"
            + "          \"parameter\": \"up\"\n"
            + "        }";

    ActionDTO action = new ObjectMapper().readValue(json, ActionDTO.class);

    Assertions.assertEquals("move", action.getAction());
    Assertions.assertEquals("up", action.getParameter());
  }

  @Test
  void behaviorCallTest() throws IOException {
    String json =
        "{\n"
            + "              \"type\": \"behaviorCall\",\n"
            + "              \"behaviorCall\": \"agressive\"\n"
            + "            }";

    BehaviorCallDTO behaviorCall = new ObjectMapper().readValue(json, BehaviorCallDTO.class);

    Assertions.assertEquals("agressive", behaviorCall.getBehaviorCall());
  }

  @Test
  void decisionTest() throws IOException {
    String json =
        "{\n"
            + "  \"type\": \"decision\",\n"
            + "  \"condition\": {\n"
            + "    \"type\": \"dualExpression\",\n"
            + "    \"expressionType\": \"state\",\n"
            + "    \"lhs\": {\n"
            + "      \"type\": \"unit\",\n"
            + "      \"unit\": \"enemy\"\n"
            + "    },\n"
            + "    \"rhs\": {\n"
            + "      \"type\": \"state\",\n"
            + "      \"state\": \"near\"\n"
            + "    }\n"
            + "  },\n"
            + "  \"ifTrue\": [\n"
            + "    {\n"
            + "      \"type\": \"behaviorCall\",\n"
            + "      \"behaviorCall\": \"flee\"\n"
            + "    }\n"
            + "  ],\n"
            + "  \"ifFalse\": [\n"
            + "    {\n"
            + "      \"type\": \"action\",\n"
            + "      \"action\": \"move randomly\"\n"
            + "    }\n"
            + "  ]\n"
            + "}";

    DecisionDTO decision = new ObjectMapper().readValue(json, DecisionDTO.class);

    Assertions.assertEquals(DualExpression.class, decision.getCondition().getClass());
    Assertions.assertEquals(BehaviorCallDTO.class, decision.getIfTrue().get(0).getClass());
    Assertions.assertEquals(ActionDTO.class, decision.getIfFalse().get(0).getClass());
  }

  @Test
  void agentDefinitionTest() throws IOException {
    String json =
        "{\n"
            + "  \"behaviors\": [\n"
            + "    {\n"
            + "      \"behaviorName\": \"agressive\",\n"
            + "      \"body\": [\n"
            + "        {\n"
            + "          \"type\": \"action\",\n"
            + "          \"action\": \"attack\",\n"
            + "          \"parameter\": \"enemy\"\n"
            + "        }\n"
            + "      ]\n"
            + "    },\n"
            + "    {\n"
            + "      \"behaviorName\": \"flee\",\n"
            + "      \"body\": [\n"
            + "        {\n"
            + "          \"type\": \"action\",\n"
            + "          \"action\": \"move towards\",\n"
            + "          \"parameter\": \"teammate\"\n"
            + "        }\n"
            + "      ]\n"
            + "    }\n"
            + "  ]\n"
            + "}";

    AgentDefinitionDTO agentDefinition =
        new ObjectMapper().readValue(json, AgentDefinitionDTO.class);

    Assertions.assertEquals("agressive", agentDefinition.getBehaviors().get(0).getBehaviorName());
    Assertions.assertEquals("flee", agentDefinition.getBehaviors().get(1).getBehaviorName());
  }

  @Test
  void behaviorTest() throws IOException {
    String json =
        "{\n"
            + "      \"behaviorName\": \"agressive\",\n"
            + "      \"body\": [\n"
            + "        {\n"
            + "          \"type\": \"action\",\n"
            + "          \"action\": \"attack\",\n"
            + "          \"parameter\": \"enemy\"\n"
            + "        }\n"
            + "      ]\n"
            + "    }";

    BehaviorDTO behaviorDTO = new ObjectMapper().readValue(json, BehaviorDTO.class);

    Assertions.assertEquals("agressive", behaviorDTO.getBehaviorName());
    Assertions.assertEquals(ActionDTO.class, behaviorDTO.getBody().get(0).getClass());
  }
}