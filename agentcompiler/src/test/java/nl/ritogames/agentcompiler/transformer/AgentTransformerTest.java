package nl.ritogames.agentcompiler.transformer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import nl.ritogames.agentcompiler.ast.AgentAST;
import nl.ritogames.agentcompiler.ast.AgentDefinition;
import nl.ritogames.agentcompiler.ast.Attribute;
import nl.ritogames.agentcompiler.ast.Behavior;
import nl.ritogames.agentcompiler.ast.BehaviorCall;
import nl.ritogames.agentcompiler.ast.Body;
import nl.ritogames.agentcompiler.ast.BodyElement;
import nl.ritogames.agentcompiler.ast.ElseClause;
import nl.ritogames.agentcompiler.ast.IfStatement;
import nl.ritogames.agentcompiler.ast.Instruction;
import nl.ritogames.agentcompiler.ast.Unit;
import nl.ritogames.agentcompiler.ast.UnitAttribute;
import nl.ritogames.agentcompiler.ast.expression.Expression;
import nl.ritogames.agentcompiler.ast.expression.GreaterThanExpression;
import nl.ritogames.agentcompiler.ast.literal.ScalarLiteral;
import nl.ritogames.shared.exception.CompilationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AgentTransformerTest {

  private AgentTransformer sut;
  private AgentAST agentAST;

  private final Body VALID_BODY = new Body(Arrays.asList(new BodyElement[]{new BehaviorCall("")}));
  private final Expression VALID_EXPRESSION = new GreaterThanExpression(
      new UnitAttribute(new Unit("my"), new Attribute("health")), new ScalarLiteral(1));

  @BeforeEach
  void setUp() {
    sut = new AgentTransformer();
    agentAST = new AgentAST();
  }

  @Test
  void testAliasReplacementInIfStatement() throws CompilationException {
    String instructionName = "hit";
    Instruction alias = new Instruction(instructionName, new Unit(""));
    Body body = new Body(Arrays.asList(new BodyElement[]{alias}));
    IfStatement ifStatement = new IfStatement(VALID_EXPRESSION, body, new ElseClause(VALID_BODY));
    Body behaviorBody = new Body(Arrays.asList(new BodyElement[]{ifStatement}));
    Behavior defaultBehavior = new Behavior("default", behaviorBody);
    Behavior otherBehavior = new Behavior("other", VALID_BODY);
    AgentDefinition agentDefinition = new AgentDefinition();
    agentDefinition.addChild(defaultBehavior);
    agentDefinition.addChild(otherBehavior);
    agentAST.setRoot(agentDefinition);

    sut.execute(agentAST);

    var result = agentAST.getRoot().getBehaviors().get(0).getChildren().get(0).getChildren().get(0);
    if (result instanceof Instruction) {
      assertNotEquals(instructionName, ((Instruction) result).getInstructionText());
    } else {
      assertTrue(result instanceof Instruction);
    }
  }

  @Test
  void testAliasReplacementInElseClause() throws CompilationException {
    String instructionName = "pickup";
    Instruction alias = new Instruction(instructionName, new Unit(""));
    Body body = new Body(Arrays.asList(new BodyElement[]{alias}));
    IfStatement ifStatement = new IfStatement(VALID_EXPRESSION, VALID_BODY, new ElseClause(body));
    Body behaviorBody = new Body(Arrays.asList(new BodyElement[]{ifStatement}));
    Behavior defaultBehavior = new Behavior("default", behaviorBody);
    Behavior otherBehavior = new Behavior("other", VALID_BODY);
    AgentDefinition agentDefinition = new AgentDefinition();
    agentDefinition.addChild(defaultBehavior);
    agentDefinition.addChild(otherBehavior);
    agentAST.setRoot(agentDefinition);

    sut.execute(agentAST);

    var result = agentAST.getRoot().getBehaviors().get(0).getChildren().get(0);
    if (result instanceof IfStatement) {
      result = ((IfStatement) result).getElseClause().getChildren().get(0);
      if (result instanceof Instruction) {
        assertNotEquals(instructionName, ((Instruction) result).getInstructionText());
      } else {
        assertTrue(result instanceof Instruction);
      }
    } else {
      assertTrue(result instanceof IfStatement);
    }
  }

  @Test
  void testAliasReplacementInBehavior() throws CompilationException {
    String instructionName = "pickup";
    Instruction alias = new Instruction(instructionName, new Unit(""));
    Body behaviorBody = new Body(Arrays.asList(new BodyElement[]{alias}));
    Behavior defaultBehavior = new Behavior("default", behaviorBody);
    Behavior otherBehavior = new Behavior("other", VALID_BODY);
    AgentDefinition agentDefinition = new AgentDefinition();
    agentDefinition.addChild(defaultBehavior);
    agentDefinition.addChild(otherBehavior);
    agentAST.setRoot(agentDefinition);

    sut.execute(agentAST);

    var result = agentAST.getRoot().getBehaviors().get(0).getChildren().get(0);
    if (result instanceof Instruction) {
      assertNotEquals(instructionName, ((Instruction) result).getInstructionText());
    } else {
      assertTrue(result instanceof Instruction);
    }
  }

  @Test
  void testNothingReplaced() throws CompilationException {
    String instructionNameIf = "move towards";
    String instructionNameElse = "grab";
    String instructionNameBehavior = "attack";

    Instruction aliasIf = new Instruction(instructionNameIf, new Unit(""));
    Body bodyIf = new Body(Arrays.asList(new BodyElement[]{aliasIf}));
    Instruction aliasElse = new Instruction(instructionNameElse, new Unit(""));
    Body bodyElse = new Body(Arrays.asList(new BodyElement[]{aliasElse}));
    Instruction aliasBehavior = new Instruction(instructionNameBehavior, new Unit(""));

    IfStatement ifStatement = new IfStatement(VALID_EXPRESSION, bodyIf, new ElseClause(bodyElse));
    Body behaviorBody = new Body(Arrays.asList(ifStatement, aliasBehavior));
    Behavior defaultBehavior = new Behavior("default", behaviorBody);
    Behavior otherBehavior = new Behavior("other", VALID_BODY);
    AgentDefinition agentDefinition = new AgentDefinition();
    agentDefinition.addChild(defaultBehavior);
    agentDefinition.addChild(otherBehavior);
    agentAST.setRoot(agentDefinition);

    sut.execute(agentAST);

    var resultIf = agentAST.getRoot().getBehaviors().get(0).getChildren().get(0).getChildren()
        .get(0);
    if (resultIf instanceof Instruction) {
      assertEquals(instructionNameIf, ((Instruction) resultIf).getInstructionText());
    } else {
      assertTrue(resultIf instanceof Instruction);
    }

    var resultElse = agentAST.getRoot().getBehaviors().get(0).getChildren().get(0);
    if (resultElse instanceof IfStatement) {
      resultElse = ((IfStatement) resultElse).getElseClause().getChildren().get(0);
      if (resultElse instanceof Instruction) {
        assertEquals(instructionNameElse, ((Instruction) resultElse).getInstructionText());
      } else {
        assertTrue(resultElse instanceof Instruction);
      }
    } else {
      assertTrue(resultElse instanceof IfStatement);
    }

    var resultBehavior = agentAST.getRoot().getBehaviors().get(0).getChildren().get(1);
    if (resultBehavior instanceof Instruction) {
      assertEquals(instructionNameBehavior, ((Instruction) resultBehavior).getInstructionText());
    } else {
      assertTrue(resultBehavior instanceof Instruction);
    }
  }
}