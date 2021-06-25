package nl.ritogames.agentcompiler.generator;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import nl.ritogames.agentcompiler.Stage;
import nl.ritogames.agentcompiler.ast.ASTNode;
import nl.ritogames.agentcompiler.ast.AgentAST;
import nl.ritogames.agentcompiler.ast.AgentDefinition;
import nl.ritogames.agentcompiler.ast.Artifact;
import nl.ritogames.agentcompiler.ast.Behavior;
import nl.ritogames.agentcompiler.ast.BehaviorCall;
import nl.ritogames.agentcompiler.ast.BodyElement;
import nl.ritogames.agentcompiler.ast.IfStatement;
import nl.ritogames.agentcompiler.ast.Instruction;
import nl.ritogames.agentcompiler.ast.State;
import nl.ritogames.agentcompiler.ast.Unit;
import nl.ritogames.agentcompiler.ast.UnitAttribute;
import nl.ritogames.agentcompiler.ast.VariableReference;
import nl.ritogames.agentcompiler.ast.expression.ConditionalExpression;
import nl.ritogames.agentcompiler.ast.expression.Expression;
import nl.ritogames.agentcompiler.ast.expression.LogicalExpression;
import nl.ritogames.agentcompiler.ast.expression.Operand;
import nl.ritogames.agentcompiler.ast.literal.PercentageLiteral;
import nl.ritogames.agentcompiler.ast.literal.ScalarLiteral;
import nl.ritogames.agentcompiler.ast.literal.StringLiteral;
import nl.ritogames.shared.dto.agent.AgentDefinitionDTO;
import nl.ritogames.shared.dto.agent.BehaviorDTO;
import nl.ritogames.shared.dto.agent.expression.ArtifactDTO;
import nl.ritogames.shared.dto.agent.expression.DualExpression;
import nl.ritogames.shared.dto.agent.expression.ExpressionDTO;
import nl.ritogames.shared.dto.agent.expression.Percentage;
import nl.ritogames.shared.dto.agent.expression.Scalar;
import nl.ritogames.shared.dto.agent.expression.StateDTO;
import nl.ritogames.shared.dto.agent.expression.StringLiteralDTO;
import nl.ritogames.shared.dto.agent.expression.UnitDTO;
import nl.ritogames.shared.dto.agent.expression.Variable;
import nl.ritogames.shared.dto.agent.statement.ActionDTO;
import nl.ritogames.shared.dto.agent.statement.BehaviorCallDTO;
import nl.ritogames.shared.dto.agent.statement.DecisionDTO;
import nl.ritogames.shared.dto.agent.statement.StatementDTO;
import nl.ritogames.shared.exception.CompilationException;


/**
 * The AgentGenerator generates a JSONObject from the AgentAST, which can be used by the
 * intelligentagent package.
 */
public class AgentGenerator implements Stage<AgentAST, String> {

  @Override
  public String execute(AgentAST input) {
    ASTNode startNode = input.getRoot();
    AgentDefinitionDTO agentDefinitionDTO = walk(startNode);

    try {
      ObjectMapper mapper = new ObjectMapper();
      mapper.setSerializationInclusion(Include.NON_NULL);
      return mapper.writeValueAsString(agentDefinitionDTO);
    } catch (JsonProcessingException e) {
      throw new CompilationException("There was an error while converting the agent to JSON.");
    }
  }

  /**
   * Walks the AST while creating a JSONObject from the current node.
   *
   * @param astnode The current Node being walked
   * @return the AgentDefinitionDTO which was generated from the Node
   */
  public AgentDefinitionDTO walk(ASTNode astnode) {
    AgentDefinitionDTO agentDefinition = new AgentDefinitionDTO(new ArrayList<>());
    List<BehaviorDTO> behaviorDTOS = new ArrayList<>();
    if (astnode instanceof AgentDefinition) {
      for (ASTNode childNode : astnode.getChildren()) {
        behaviorDTOS.add(map((Behavior) childNode));
      }
      agentDefinition.setBehaviors(behaviorDTOS);
    }
    return agentDefinition;
  }

  public BehaviorDTO map(Behavior behavior) {
    List<StatementDTO> statements = new ArrayList<>();
    for (BodyElement element : behavior.getBody().getBodyElements()) {
      statements.add(map(element));
    }
    return new BehaviorDTO(behavior.getBehaviorName(), statements);
  }

  public StatementDTO map(BodyElement bodyElement) {
    if (bodyElement instanceof BehaviorCall) {
      BehaviorCall behaviorCall = (BehaviorCall) bodyElement;
      return new BehaviorCallDTO(behaviorCall.getBehaviorName());
    } else if (bodyElement instanceof IfStatement) {
      IfStatement ifStatement = (IfStatement) bodyElement;
      List<StatementDTO> ifBody = new ArrayList<>();
      for (BodyElement element : ifStatement.getBody().getBodyElements()) {
        ifBody.add(map(element));
      }
      List<StatementDTO> elseBody = null;
      if (ifStatement.hasElseClause()) {
        elseBody = new ArrayList<>();
        for (BodyElement element : ifStatement.getElseClause().getBody().getBodyElements()) {
          elseBody.add(map(element));
        }
      }

      return new DecisionDTO(map(ifStatement.getCondition()), ifBody, elseBody);

    } else {
      Instruction instruction = (Instruction) bodyElement;
      if (instruction.getParameter() != null) {
        return new ActionDTO(instruction.getInstructionText(),
            instruction.getParameter().getParameterName());
      } else {
        ActionDTO actionDTO = new ActionDTO();
        actionDTO.setAction(instruction.getInstructionText());
        return actionDTO;
      }
    }
  }

  public ExpressionDTO map(Expression expression) {
    if (expression instanceof LogicalExpression) {
      LogicalExpression logicalExpression = (LogicalExpression) expression;
      return new DualExpression(expression.getExpressionType(), map(logicalExpression.getLhs()),
          map(logicalExpression.getRhs()));
    } else {
      ConditionalExpression conditionalExpression = (ConditionalExpression) expression;
      return new DualExpression(expression.getExpressionType(), map(conditionalExpression.getLhs()),
          map(conditionalExpression.getRhs()));
    }
  }

  public ExpressionDTO map(Operand operand) {
    if (operand instanceof PercentageLiteral) {
      return new Percentage(String.valueOf(((PercentageLiteral) operand).getValue()));
    } else if (operand instanceof ScalarLiteral) {
      return new Scalar(String.valueOf(((ScalarLiteral) operand).getValue()));
    } else if (operand instanceof StringLiteral) {
      return new StringLiteralDTO(((StringLiteral) operand).getValue());
    } else if (operand instanceof UnitAttribute) {
      UnitAttribute unitAttribute = (UnitAttribute) operand;
      return new UnitDTO(unitAttribute.getUnit().getUnitType(),
          unitAttribute.getAttribute().getAttributeType());
    } else if (operand instanceof Unit) {
      UnitDTO unitDTO = new UnitDTO();
      unitDTO.setUnit(((Unit) operand).getUnitType());
      return unitDTO;
    } else if (operand instanceof State) {
      return new StateDTO(((State) operand).getStateText());
    } else if (operand instanceof Artifact) {
      return new ArtifactDTO(((Artifact) operand).getArtifactType());
    } else {
      return new Variable(((VariableReference) operand).getVariableName());
    }
  }

}
