package nl.ritogames.intelligentagent;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import nl.ritogames.intelligentagent.behavior.ActionNode;
import nl.ritogames.intelligentagent.behavior.Behavior;
import nl.ritogames.intelligentagent.behavior.BehaviorNode;
import nl.ritogames.intelligentagent.behavior.BehaviorTree;
import nl.ritogames.intelligentagent.behavior.DecisionNode;
import nl.ritogames.intelligentagent.exception.UnknownActionException;
import nl.ritogames.shared.dto.GameStateContext;
import nl.ritogames.shared.dto.agent.AgentDefinitionDTO;
import nl.ritogames.shared.dto.agent.BehaviorDTO;
import nl.ritogames.shared.dto.agent.expression.ArtifactDTO;
import nl.ritogames.shared.dto.agent.expression.DualExpression;
import nl.ritogames.shared.dto.agent.expression.ExpressionDTO;
import nl.ritogames.shared.dto.agent.expression.Percentage;
import nl.ritogames.shared.dto.agent.expression.Scalar;
import nl.ritogames.shared.dto.agent.expression.UnitDTO;
import nl.ritogames.shared.dto.agent.statement.ActionDTO;
import nl.ritogames.shared.dto.agent.statement.BehaviorCallDTO;
import nl.ritogames.shared.dto.agent.statement.DecisionDTO;
import nl.ritogames.shared.dto.agent.statement.StatementDTO;
import nl.ritogames.shared.dto.gameobject.attribute.Attribute;
import nl.ritogames.shared.dto.gameobject.individual.Character;
import nl.ritogames.shared.dto.gameobject.individual.Individual;
import nl.ritogames.shared.dto.gameobject.individual.Monster;
import nl.ritogames.shared.enums.Direction;
import nl.ritogames.shared.exception.AgentBuilderException;


/**
 * Builder class for converting an agent's JSON definition into a working agent. It will map
 * if-statements to Decision nodes and instructions to Action nodes. The agent builder will build a
 * behavior tree for every behavior and add it to the agent.
 */
public class AgentBuilder {

  private Agent agent;

  public AgentBuilder() {
    this.agent = new Agent();
  }

  /**
   * Builds the agent from a JSON format String.
   *
   * @param agentJSON  the JSON formatted agent String.
   * @param individual the individual the agent will be controlling
   * @return the Agent
   * @throws AgentBuilderException when the JSON can not be parsed.
   * @see Agent
   * @see Individual
   */
  public Agent build(String agentJSON, Individual individual) throws AgentBuilderException {
    agent.setIndividual(individual);
    AgentDefinitionDTO definition;
    try {
      definition = new ObjectMapper().readValue(agentJSON, AgentDefinitionDTO.class);
    } catch (IOException e) {
      throw new AgentBuilderException("There was an error while parsing the Agent's JSON.", e);
    }

    for (BehaviorDTO behaviorDTO : definition.getBehaviors()) {
      agent.addBehavior(mapBehavior(behaviorDTO));
    }

    agent.setBehavior("default");
    return agent;
  }

  Behavior mapBehavior(BehaviorDTO behaviorDTO) {
    BehaviorTree behaviorTree = new BehaviorTree();
    behaviorTree.setFirstBehaviorNode(mapListOfStatements(behaviorDTO.getBody()));
    return new Behavior(behaviorDTO.getBehaviorName(), behaviorTree);
  }

  BehaviorNode mapListOfStatements(List<StatementDTO> statements) {
    BehaviorNode currentNode = null;
    for (StatementDTO statement : statements) {
      BehaviorNode nextNode = mapStatement(statement);
      if (currentNode != null) {
        currentNode.setNextNode(nextNode);
      }
      currentNode = nextNode;
    }
    return currentNode;
  }

  BehaviorNode mapStatement(StatementDTO statement) {
    if (statement instanceof ActionDTO) {
      return mapAction((ActionDTO) statement);
    } else if (statement instanceof BehaviorCallDTO) {
      return mapBehaviorCall((BehaviorCallDTO) statement);
    } else if (statement instanceof DecisionDTO) {
      return mapDecision((DecisionDTO) statement);
    } else {
      throw new UnknownActionException("Statement could not be mapped.");
    }
  }


  DecisionNode mapDecision(DecisionDTO decision) {
    Predicate<GameStateContext> condition = mapExpression((DualExpression) decision.getCondition());

    BehaviorNode ifTrue = mapListOfStatements(decision.getIfTrue());
    if (decision.getIfFalse() != null && !decision.getIfFalse().isEmpty()) {
      BehaviorNode ifFalse = mapListOfStatements(decision.getIfFalse());
      return new DecisionNode(condition, ifTrue, ifFalse);
    }
    return new DecisionNode(condition, ifTrue, null);
  }

  Predicate<GameStateContext> mapExpression(DualExpression dualExpression) {
    String expressionType = dualExpression.getExpressionType();
    // and, or, state, eq, lt, gt, has
    return switch (expressionType) {
      case "and" -> mapExpression((DualExpression) dualExpression.getLhs())
          .and(mapExpression((DualExpression) dualExpression.getRhs()));
      case "or" -> mapExpression((DualExpression) dualExpression.getLhs())
          .or(mapExpression((DualExpression) dualExpression.getRhs()));
      case "state" -> mapStateExpression(dualExpression);
      case "eq", "lt", "gt" -> mapComparison(dualExpression);
      case "has" -> mapHasExpression(dualExpression);
      default -> throw new UnknownActionException("Invalid expression type: " + expressionType);
    };
  }

  Predicate<GameStateContext> mapComparison(DualExpression dualExpression) {
    int expectedResult = switch (dualExpression.getExpressionType()) {
      case "lt" -> -1;
      case "gt" -> 1;
      default -> 0;
    };

    ExpressionDTO lhs = dualExpression.getLhs();
    ExpressionDTO rhs = dualExpression.getRhs();
    Function<GameStateContext, Integer> leftAttribute;
    Function<GameStateContext, Integer> rightAttribute;

    if (lhs instanceof UnitDTO) {
      leftAttribute = getUnitAttribute((UnitDTO) lhs);
    } else {
      throw new UnknownActionException("Unknown operation.");
    }

    if (rhs instanceof UnitDTO) {
      rightAttribute = getUnitAttribute((UnitDTO) rhs);
    } else if (rhs instanceof Scalar) {
      rightAttribute = context -> Integer.valueOf(((Scalar) rhs).getScalar());
    } else if (rhs instanceof Percentage) {
      rightAttribute = context -> Integer.valueOf(((Percentage) rhs).getPercentage());
    } else {
      throw new UnknownActionException("Unknown operation.");
    }

    return context -> leftAttribute.apply(context).compareTo(rightAttribute.apply(context))
        == expectedResult;
  }

  Function<GameStateContext, Integer> getUnitAttribute(UnitDTO unit) {
    Function<GameStateContext, Individual> individual;
    if (unit.getUnit().equals("my")) {
      individual = context -> agent.getIndividual();
    } else {
      individual = context -> (Individual) agent.getSensor(unit.getUnit()).getOutput();
    }

    return switch (unit.getAttribute()) {
      case "health" -> (context -> individual.apply(context).getHp());
      case "armor" -> getUnitArmor(individual.apply(agent.getGameStateContext()));
      case "strength" -> getUnitStrength(individual.apply(agent.getGameStateContext()));
      default -> throw new UnknownActionException("Attribute does not exist");
    };
  }

  private Function<GameStateContext, Integer> getUnitArmor(Individual individual) {
    if (individual instanceof Character) {
      return (context -> individual.getDefense());
    } else if (individual instanceof Monster) {
      return (context -> individual.getDefense());
    } else {
      throw new UnknownActionException("Individual type does not exist");
    }
  }

  private Function<GameStateContext, Integer> getUnitStrength(Individual individual) {
    if (individual instanceof Character) {
      return (context -> individual.getAttack());
    } else if (individual instanceof Monster) {
      return (context -> individual.getAttack());
    } else {
      throw new UnknownActionException("Individual type does not exist");
    }
  }

  ActionNode mapBehaviorCall(BehaviorCallDTO behaviorCall) {
    return new ActionNode(context -> agent.setBehavior(behaviorCall.getBehaviorCall()));
  }

  ActionNode mapAction(ActionDTO action) {
    Consumer<GameStateContext> consumer = switch (action.getAction()) {
      case "move up", "move right", "move down", "move left", "move randomly", "move towards" -> mapMovement(
          action);
      case "attack" -> mapFight(action);
      case "grab" -> mapPickup(action);
      default -> throw new UnknownActionException("Unexpected action: " + action.getAction());
    };

    return new ActionNode(consumer);
  }

  Consumer<GameStateContext> mapPickup(ActionDTO action) {
    String targetSensor = action.getParameter();
    if (agent.getSensor(targetSensor) == null) {
      throw new UnknownActionException("Invalid artifact: " + targetSensor);
    }

    return context -> agent.pickup((Attribute) agent.getSensor(targetSensor).getOutput());
  }

  Consumer<GameStateContext> mapFight(ActionDTO action) {
    String targetSensor = action.getParameter();
    if (agent.getSensor(targetSensor) == null) {
      throw new UnknownActionException("Invalid target: " + targetSensor);
    }

    return context -> agent.fight((Individual) agent.getSensor(targetSensor).getOutput());
  }

  Consumer<GameStateContext> mapMovement(ActionDTO action) {
    if (action.getAction().equals("move towards")) {
      String sensorName = action.getParameter();

      return context -> {
        if (agent.getSensor(sensorName).getOutput() instanceof Individual) {
          agent.moveTowards(((Individual) agent.getSensor(sensorName).getOutput()));
        } else if (agent.getSensor(sensorName).getOutput() instanceof Attribute) {
          agent.moveTowards(((Attribute) agent.getSensor(sensorName).getOutput()));
        }
      };
    }

    Direction direction = switch (action.getAction()) {
      case "move up" -> Direction.NORTH;
      case "move right" -> Direction.EAST;
      case "move down" -> Direction.SOUTH;
      case "move left" -> Direction.WEST;
      case "move randomly" -> Direction.RANDOM;
      default -> throw new UnknownActionException(
          "Unexpected action parameter: " + action.getParameter());
    };
    return context -> agent.move(direction);
  }

  private Predicate<GameStateContext> mapStateExpression(DualExpression stateExpression) {
    String sensorName = "";
    if (stateExpression.getLhs() instanceof UnitDTO) {
      sensorName = ((UnitDTO) stateExpression.getLhs()).getUnit();
    }
    if (stateExpression.getLhs() instanceof ArtifactDTO) {
      sensorName = ((ArtifactDTO) stateExpression.getLhs()).getArtifact();
    }
    // Create sensor
    if (agent.getSensor(sensorName) == null) {
      agent.createSensor(sensorName);
    }

    String finalSensorName = sensorName;
    return context -> agent.getSensor(finalSensorName).getOutput() != null;
  }

  private Predicate<GameStateContext> mapHasExpression(DualExpression hasExpression) {
    String sensorName = ((UnitDTO) hasExpression.getLhs()).getUnit();
    if (agent.getSensor(sensorName) == null) {
      throw new UnknownActionException("Invalid sensor: " + sensorName);
    }
    if (hasExpression.getLhs() instanceof UnitDTO && hasExpression
        .getRhs() instanceof ArtifactDTO) {
      ArtifactDTO artifact = (ArtifactDTO) hasExpression.getRhs();
      if (artifact.getArtifact().equals("flag")) {
        return (context -> ((Character) agent.getSensor(sensorName).getOutput()).getHasFlag());
      }
    }
    throw new UnknownActionException("Invalid has expression.");
  }

  public void setAgent(Agent agent) {
    this.agent = agent;
  }
}
