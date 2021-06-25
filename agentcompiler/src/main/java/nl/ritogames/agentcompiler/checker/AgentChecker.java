package nl.ritogames.agentcompiler.checker;

import java.util.ArrayList;
import java.util.List;
import nl.ritogames.agentcompiler.Stage;
import nl.ritogames.agentcompiler.ast.ASTNode;
import nl.ritogames.agentcompiler.ast.AgentAST;
import nl.ritogames.agentcompiler.ast.AgentDefinition;
import nl.ritogames.agentcompiler.ast.Behavior;
import nl.ritogames.agentcompiler.ast.BehaviorCall;
import nl.ritogames.agentcompiler.ast.Body;
import nl.ritogames.agentcompiler.ast.ElseClause;
import nl.ritogames.agentcompiler.ast.IfStatement;
import nl.ritogames.agentcompiler.ast.Instruction;
import nl.ritogames.agentcompiler.ast.expression.ConditionalExpression;
import nl.ritogames.agentcompiler.ast.expression.Expression;
import nl.ritogames.agentcompiler.ast.expression.LogicalExpression;
import nl.ritogames.agentcompiler.ast.expression.StateExpression;
import nl.ritogames.agentcompiler.ast.literal.Literal;
import nl.ritogames.shared.exception.CompilationException;

/**
 * The AgentChecker walks the AgentAST while checking for errors.
 */
public class AgentChecker implements Stage<AgentAST, AgentAST> {

  private final List<String> errorMessages;

  private final List<String> behaviorNames;
  private final List<ASTNode> definedSensors;
  private final List<Boolean> agentActions;
  private Behavior currentBehavior;

  private final AgentInstructionChecker instructionChecker;

  private static final String BEHAVIOR_ALREADY_DEFINED = "Behavior %s is already defined.";
  private static final String BEHAVIOR_NOT_DEFINED = "Behavior %s is not defined.";
  private static final String SELF_CALL = "A behavior cannot call itself.";
  private static final String MISSING_DEFAULT_BEHAVIOR = "Agent definition is missing a default behavior.";
  private static final String NO_OTHER_BEHAVIOR = "At least one non-default behavior should be defined.";
  private static final String EMPTY_BODY = "Body cannot be empty.";
  private static final String IDENTICAL_EXPRESSION = "The expression %s will always return the same result.";
  private static final String LITERAL_EXPRESSION = "It is not allowed to have two literals in one expression.";
  private static final String UNDEFINED_SENSOR = "The sensor %s hasn't been defined in this scope.";
  private static final String AGENT_DOES_NOTHING = "The Agent doesn't always perform an action.";


  /**
   * Instantiates a new Agent checker.
   *
   * @param instructionChecker the instruction checker
   */
  public AgentChecker(AgentInstructionChecker instructionChecker) {
    this.errorMessages = new ArrayList<>();
    this.behaviorNames = new ArrayList<>();
    this.definedSensors = new ArrayList<>();
    this.agentActions = new ArrayList<>();
    this.instructionChecker = instructionChecker;
  }

  @Override
  public AgentAST execute(AgentAST input) {
    check(input.getRoot());

    if (errorMessages.isEmpty()) {
      return input;
    } else {
      throw new CompilationException(errorMessages);
    }
  }

  private void check(AgentDefinition agentDefinition) {
    List<Behavior> agentBehaviors = agentDefinition.getBehaviors();

    for (Behavior behavior : agentBehaviors) {
      String behaviorName = behavior.getBehaviorName();
      if (behaviorNames.contains(behaviorName)) {
        errorMessages.add(String.format(BEHAVIOR_ALREADY_DEFINED, behaviorName));
      }
      behaviorNames.add(behaviorName);
    }

    if (!behaviorNames.contains("default")) {
      errorMessages.add(MISSING_DEFAULT_BEHAVIOR);
    } else if (agentBehaviors.size() < 2) {
      errorMessages.add(NO_OTHER_BEHAVIOR);
    }

    agentBehaviors.forEach(this::check);
  }

  private void check(Behavior behavior) {
    currentBehavior = behavior;
    check(behavior.getBody());
  }

  private void check(Body body) {
    if (body.getBodyElements().isEmpty()) {
      errorMessages.add(EMPTY_BODY);
    } else {
      agentActions.add(false);
      body.getBodyElements().forEach(bodyElement -> {
        if (bodyElement instanceof BehaviorCall) {
          check((BehaviorCall) bodyElement);
          agentActions.set(agentActions.size() - 1, true);
        } else if (bodyElement instanceof Instruction) {
          check((Instruction) bodyElement);
          agentActions.set(agentActions.size() - 1, true);
        } else if (bodyElement instanceof IfStatement) {
          check((IfStatement) bodyElement);
        }
      });
      if ((agentActions.get(agentActions.size() - 1)).equals(Boolean.FALSE)) {
        errorMessages.add(AGENT_DOES_NOTHING);
      }
      agentActions.remove(agentActions.size() - 1);
    }
  }

  private void check(BehaviorCall behaviorCall) {
    if (!behaviorNames.contains(behaviorCall.getBehaviorName())) {
      errorMessages.add(String.format(BEHAVIOR_NOT_DEFINED, behaviorCall.getBehaviorName()));
    }

    if (behaviorCall.getBehaviorName().equals(currentBehavior.getBehaviorName())) {
      errorMessages.add(SELF_CALL);
    }
  }

  private void check(Instruction instruction) {
    instructionChecker.check(instruction, definedSensors);
    errorMessages.addAll(instructionChecker.getErrorMessages());
    instructionChecker.clearErrorMessages();
  }

  private void check(IfStatement ifStatement) {
    int sensorCount = definedSensors.size();
    check(ifStatement.getCondition());
    check(ifStatement.getBody());
    while (definedSensors.size() > sensorCount) {
      definedSensors.remove(definedSensors.size() - 1);
    }
    if (ifStatement.hasElseClause()) {
      check(ifStatement.getElseClause());
      agentActions.set(agentActions.size() - 1, true);
    }
  }

  private void check(ElseClause elseClause) {
    check(elseClause.getBody());
  }

  private void check(Expression expression) {
    if (expression instanceof ConditionalExpression) {
      check((ConditionalExpression) expression);
    } else if (expression instanceof LogicalExpression) {
      check((LogicalExpression) expression);
    }
  }

  private void check(ConditionalExpression expression) {
    if (expression.getLhs() instanceof Literal
        && expression.getRhs() instanceof Literal) {
      errorMessages.add(LITERAL_EXPRESSION);
    } else if (expression.getLhs()
        .equals(expression.getRhs())) {
      errorMessages.add(String.format(IDENTICAL_EXPRESSION, expression.toString()));
    }
    if (expression.getSensor() != null) {
      if (expression instanceof StateExpression) {
        definedSensors.addAll(expression.getSensor());
      } else if (!definedSensors.containsAll(expression.getSensor())) {
        errorMessages.add(
            String.format(UNDEFINED_SENSOR, expression.getSensor().toString()));
      }
    }
  }

  private void check(LogicalExpression expression) {
    check(expression.getLhs());
    check(expression.getRhs());
  }
}
