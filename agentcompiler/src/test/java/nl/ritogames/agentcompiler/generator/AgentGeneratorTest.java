package nl.ritogames.agentcompiler.generator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.util.Collections;
import java.util.List;
import nl.ritogames.agentcompiler.ast.ASTNode;
import nl.ritogames.agentcompiler.ast.AgentAST;
import nl.ritogames.agentcompiler.ast.AgentDefinition;
import nl.ritogames.agentcompiler.ast.Artifact;
import nl.ritogames.agentcompiler.ast.Attribute;
import nl.ritogames.agentcompiler.ast.Behavior;
import nl.ritogames.agentcompiler.ast.BehaviorCall;
import nl.ritogames.agentcompiler.ast.Body;
import nl.ritogames.agentcompiler.ast.BodyElement;
import nl.ritogames.agentcompiler.ast.ElseClause;
import nl.ritogames.agentcompiler.ast.IfStatement;
import nl.ritogames.agentcompiler.ast.Instruction;
import nl.ritogames.agentcompiler.ast.InstructionParameter;
import nl.ritogames.agentcompiler.ast.State;
import nl.ritogames.agentcompiler.ast.Unit;
import nl.ritogames.agentcompiler.ast.UnitAttribute;
import nl.ritogames.agentcompiler.ast.VariableReference;
import nl.ritogames.agentcompiler.ast.expression.AndExpression;
import nl.ritogames.agentcompiler.ast.expression.EqualsExpression;
import nl.ritogames.agentcompiler.ast.expression.Expression;
import nl.ritogames.agentcompiler.ast.expression.Operand;
import nl.ritogames.agentcompiler.ast.literal.Literal;
import nl.ritogames.agentcompiler.ast.literal.PercentageLiteral;
import nl.ritogames.agentcompiler.ast.literal.ScalarLiteral;
import nl.ritogames.agentcompiler.ast.literal.StringLiteral;
import nl.ritogames.shared.dto.agent.AgentDefinitionDTO;
import nl.ritogames.shared.dto.agent.BehaviorDTO;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AgentGeneratorTest {

  private AgentGenerator sut;

  @BeforeEach
  void setUp() {
    sut = new AgentGenerator();
  }

  @Test
  void testThatExecuteReturnsTheStartNode() throws CompilationException {
    //arrange
    Behavior behavior = new Behavior("test", new Body(Collections.emptyList()));
    BehaviorDTO behaviorDTO = new BehaviorDTO("test", Collections.emptyList());
    AgentDefinition agentDefinition = new AgentDefinition(Collections.singletonList(behavior));
    AgentAST ast = new AgentAST(agentDefinition);
    String expected = "{\"behaviors\":[{\"behaviorName\":\"test\",\"body\":[]}]}";
    //act
    String actual = sut.execute(ast);
    //assert
    assertEquals(expected, actual);
  }

  @Test
  void testWalkReturnsAgentDefinitionDTO() {
    //arrange
    AgentGenerator agentGeneratorSpy = spy(sut);

    BehaviorDTO behaviorDTOMock = mock(BehaviorDTO.class);
    Behavior behaviorMock = mock(Behavior.class);
    List<Behavior> behaviors = Collections.singletonList(behaviorMock);
    ASTNode agentDefinition = new AgentDefinition(behaviors);
    doReturn(behaviorDTOMock).when(agentGeneratorSpy).map(any(Behavior.class));
    List<BehaviorDTO> behaviorDTOS = Collections.singletonList(behaviorDTOMock);
    AgentDefinitionDTO expected = new AgentDefinitionDTO(behaviorDTOS);
    //act
    AgentDefinitionDTO actual = agentGeneratorSpy.walk(agentDefinition);
    //assert
    assertEquals(expected, actual);
  }

  @Test
  void testMapBehaviorToBehaviorDTO() {
    //arrange
    AgentGenerator agentGeneratorSpy = spy(sut);

    BodyElement elementMock = mock(BodyElement.class);
    BehaviorCallDTO behaviorCallDTO = new BehaviorCallDTO("test");
    List<BodyElement> elements = Collections.singletonList(elementMock);
    Behavior behaviorNode = new Behavior("test", new Body(elements));
    doReturn(behaviorCallDTO).when(agentGeneratorSpy).map(any(BodyElement.class));

    List<StatementDTO> statements = Collections.singletonList(behaviorCallDTO);
    BehaviorDTO expected = new BehaviorDTO("test", statements);
    //act
    BehaviorDTO actual = agentGeneratorSpy.map(behaviorNode);
    //assert
    assertEquals(expected, actual);
  }

  @Test
  void testMapBodyElementToBehaviorCallDTO() {
    //arrange
    BodyElement behaviorCall = new BehaviorCall("test");
    StatementDTO expected = new BehaviorCallDTO("test");
    //act
    StatementDTO actual = sut.map(behaviorCall);
    //assert
    assertEquals(expected, actual);
  }

  @Test
  void testMapBodyElementToDecisionDTO() {
    //arrange
    AgentGenerator agentGeneratorSpy = spy(sut);

    Expression expressionMock = mock(Expression.class);
    BehaviorCall behaviorCallMock = mock(BehaviorCall.class);
    BehaviorCallDTO behaviorCallDTO = new BehaviorCallDTO("test");
    StringLiteralDTO stringLiteralDTO = new StringLiteralDTO("test");
    List<BodyElement> elements = Collections.singletonList(behaviorCallMock);
    Body body = new Body(elements);
    doReturn(behaviorCallDTO).when(agentGeneratorSpy).map(any(BehaviorCall.class));
    doReturn(stringLiteralDTO).when(agentGeneratorSpy).map(any(Expression.class));
    BodyElement ifStatement = new IfStatement(expressionMock, body, new ElseClause(body));
    List<StatementDTO> statements = Collections.singletonList(behaviorCallDTO);
    StatementDTO expected = new DecisionDTO(stringLiteralDTO, statements, statements);
    //act
    StatementDTO actual = agentGeneratorSpy.map(ifStatement);
    //assert
    assertEquals(expected, actual);
  }

  @Test
  void testMapBodyElementToActionDTO() {
    //arrange
    InstructionParameter artifact = new Artifact("test");
    BodyElement instruction = new Instruction("test", artifact);
    StatementDTO expected = new ActionDTO("test", "test");
    //act
    StatementDTO actual = sut.map(instruction);
    //assert
    assertEquals(expected, actual);
  }

  @Test
  void testMapExpressionToLogicalExpression() {
    //arrange
    AgentGenerator agentGeneratorSpy = spy(sut);
    Literal literalMock = mock(Literal.class);
    ExpressionDTO expressionDTOMock = mock(ExpressionDTO.class);

    Expression lhs = new EqualsExpression(literalMock, literalMock);
    Expression rhs = new EqualsExpression(literalMock, literalMock);
    Expression logicalExpression = new AndExpression(lhs, rhs);
    doReturn(expressionDTOMock).when(agentGeneratorSpy).map(any(Operand.class));
    ExpressionDTO lhsExpected = new DualExpression("eq", expressionDTOMock, expressionDTOMock);
    ExpressionDTO rhsExpected = new DualExpression("eq", expressionDTOMock, expressionDTOMock);
    ExpressionDTO expected = new DualExpression("and", lhsExpected, rhsExpected);
    //act
    ExpressionDTO actual = agentGeneratorSpy.map(logicalExpression);
    //assert
    assertEquals(expected, actual);
  }

  @Test
  void testMapOperandToPercentage() {
    //arrange
    Operand percentageLiteral = new PercentageLiteral("1");
    ExpressionDTO expected = new Percentage("1");
    //act
    ExpressionDTO actual = sut.map(percentageLiteral);
    //assert
    assertEquals(expected, actual);
  }

  @Test
  void testMapOperandToScalar() {
    //arrange
    Operand scalarLiteral = new ScalarLiteral(1);
    ExpressionDTO expected = new Scalar("1");
    //act
    ExpressionDTO actual = sut.map(scalarLiteral);
    //assert
    assertEquals(expected, actual);
  }

  @Test
  void testMapOperandToString() {
    //arrange
    Operand stringLiteral = new StringLiteral("test");
    ExpressionDTO expected = new StringLiteralDTO("test");
    //act
    ExpressionDTO actual = sut.map(stringLiteral);
    //assert
    assertEquals(expected, actual);
  }

  @Test
  void testMapOperandToUnitAttribute() {
    //arrange
    Unit unit = new Unit("test");
    Attribute attribute = new Attribute("test");
    Operand unitAttribute = new UnitAttribute(unit, attribute);
    ExpressionDTO expected = new UnitDTO("test", "test");
    //act
    ExpressionDTO actual = sut.map(unitAttribute);
    //assert
    assertEquals(expected, actual);
  }

  @Test
  void testMapOperandToState() {
    //arrange
    Operand state = new State("test");
    ExpressionDTO expected = new StateDTO("test");
    //act
    ExpressionDTO actual = sut.map(state);
    //assert
    assertEquals(expected, actual);
  }

  @Test
  void testMapOperandToVariable() {
    //arrange
    Operand variable = new VariableReference("test");
    ExpressionDTO expected = new Variable("test");
    //act
    ExpressionDTO actual = sut.map(variable);
    //assert
    assertEquals(expected, actual);
  }
}
