package nl.ritogames.agentcompiler.checker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
import nl.ritogames.agentcompiler.ast.State;
import nl.ritogames.agentcompiler.ast.Unit;
import nl.ritogames.agentcompiler.ast.UnitAttribute;
import nl.ritogames.agentcompiler.ast.expression.AndExpression;
import nl.ritogames.agentcompiler.ast.expression.EqualsExpression;
import nl.ritogames.agentcompiler.ast.expression.Expression;
import nl.ritogames.agentcompiler.ast.expression.GreaterThanExpression;
import nl.ritogames.agentcompiler.ast.expression.LowerThanExpression;
import nl.ritogames.agentcompiler.ast.expression.StateExpression;
import nl.ritogames.agentcompiler.ast.literal.ScalarLiteral;
import nl.ritogames.agentcompiler.ast.literal.StringLiteral;
import nl.ritogames.shared.exception.CompilationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class AgentCheckerTest {

    private AgentChecker sut;
    private AgentAST agentAST;
    private final AgentInstructionChecker instructionCheckerMock = Mockito.mock(AgentInstructionChecker.class);

    private final Body VALID_BODY = new Body(Arrays.asList(new BodyElement[]{new Instruction("", new Unit(""))}));
    private final Behavior VALID_BEHAVIOR = new Behavior("testbehavior", VALID_BODY);
    private final Behavior DEFAULT_BEHAVIOR = new Behavior("default", VALID_BODY);
    private final Expression VALID_EXPRESSION = new GreaterThanExpression(
        new UnitAttribute(new Unit("my"), new Attribute("health")), new ScalarLiteral(1));

    @BeforeEach
    void setUp() {
        sut = new AgentChecker(instructionCheckerMock);
        agentAST = new AgentAST();
    }

    @Test
    void agentDefinitionWithoutDefaultBehaviorShouldThrowCompilationException() {
        List<Behavior> behaviors = new ArrayList<>();
        behaviors.add(VALID_BEHAVIOR);
        AgentDefinition node = new AgentDefinition(behaviors);
        agentAST.setRoot(node);

        assertThrows(CompilationException.class, () -> sut.execute(agentAST));
    }

    @Test
    void agentDefinitionWithoutOtherBehaviorsShouldThrowCompilationException() {
        List<Behavior> behaviors = new ArrayList<>();
        behaviors.add(DEFAULT_BEHAVIOR);
        AgentDefinition node = new AgentDefinition(behaviors);
        agentAST.setRoot(node);

        assertThrows(CompilationException.class, () -> sut.execute(agentAST));
    }

    @Test
    void agentDefinitionWithDuplicateBehaviorsShouldThrowCompilationException() {
        List<Behavior> behaviors = new ArrayList<>();
        behaviors.add(DEFAULT_BEHAVIOR);
        behaviors.add(new Behavior("agressive", VALID_BODY));
        behaviors.add(new Behavior("agressive", VALID_BODY));
        AgentDefinition node = new AgentDefinition(behaviors);
        agentAST.setRoot(node);

        assertThrows(CompilationException.class, () -> sut.execute(agentAST));
    }

    @Test
    void validAgentDefinitionShouldPassChecker() throws CompilationException {
        List<Behavior> behaviors = new ArrayList<>();
        behaviors.add(DEFAULT_BEHAVIOR);
        behaviors.add(VALID_BEHAVIOR);
        AgentDefinition node = new AgentDefinition(behaviors);
        agentAST.setRoot(node);

        assertEquals(agentAST, sut.execute(agentAST));
    }

    @Test
    void validBodyShouldPassChecker() throws CompilationException {
        List<Behavior> behaviors = new ArrayList<>();
        behaviors.add(DEFAULT_BEHAVIOR);
        behaviors.add(VALID_BEHAVIOR);
        AgentDefinition agentDefinition = new AgentDefinition(behaviors);
        agentAST.setRoot(agentDefinition);

        assertEquals(agentAST, sut.execute(agentAST));
    }

    @Test
    void emptyBodyShouldThrowCompilationException() {
        List<Behavior> behaviors = new ArrayList<>();
        behaviors.add(DEFAULT_BEHAVIOR);
        Body emptyBody = new Body(new ArrayList<>());
        Behavior invalidBehavior = new Behavior("agressive", emptyBody);
        behaviors.add(invalidBehavior);
        AgentDefinition agentDefinition = new AgentDefinition(behaviors);
        agentAST.setRoot(agentDefinition);

        assertThrows(CompilationException.class, () -> sut.execute(agentAST));
    }

    @Test
    void behaviorSelfCallShouldThrowCompilationException() {
        List<Behavior> behaviors = new ArrayList<>();
        behaviors.add(DEFAULT_BEHAVIOR);
        BehaviorCall selfCall = new BehaviorCall("agressive");
        Body body = new Body(new ArrayList<>());
        body.getBodyElements().add(selfCall);
        Behavior calledBehavior = new Behavior("agressive", body);
        behaviors.add(calledBehavior);
        agentAST.setRoot(new AgentDefinition(behaviors));

        assertThrows(CompilationException.class, () -> sut.execute(agentAST));
    }

    @Test
    void behaviorCallThatDoesNotExistShouldThrowCompilationException() {
        List<Behavior> behaviors = new ArrayList<>();
        behaviors.add(DEFAULT_BEHAVIOR);
        BehaviorCall invalidCall = new BehaviorCall("passive");
        Body body = new Body(new ArrayList<>());
        body.getBodyElements().add(invalidCall);
        Behavior agressive = new Behavior("agressive", body);
        behaviors.add(agressive);
        agentAST.setRoot(new AgentDefinition(behaviors));

        assertThrows(CompilationException.class, () -> sut.execute(agentAST));
    }

    @Test
    void checkInstructionShouldCallAgentInstructionChecker() throws CompilationException {
        Instruction instruction = new Instruction("", null);
        List<Behavior> behaviors = new ArrayList<>();
        Body body = new Body(Arrays.asList(instruction));
        behaviors.add(new Behavior("default", body));
        behaviors.add(VALID_BEHAVIOR);
        AgentDefinition agentDefinition = new AgentDefinition(behaviors);
        agentAST.setRoot(agentDefinition);

        sut.execute(agentAST);

        Mockito.verify(instructionCheckerMock, Mockito.times(2)).check(any(), any());
    }

    @Test
    void checkIfStatementWithTwoLiteralsThrowsCompilationException() {
        StringLiteral stringLiteral1 = new StringLiteral("test1");
        StringLiteral stringLiteral2 = new StringLiteral("test2");

        Expression expression1 = new EqualsExpression(stringLiteral1, stringLiteral2);
        IfStatement ifStatement = new IfStatement(expression1, VALID_BODY,
            new ElseClause(VALID_BODY));
        Body body = new Body();
        body.addChild(ifStatement);
        Behavior behavior = new Behavior("test", body);

        AgentDefinition agentDefinition = new AgentDefinition();
        agentDefinition.addChild(DEFAULT_BEHAVIOR);
        agentDefinition.addChild(VALID_BEHAVIOR);
        agentDefinition.addChild(behavior);
        agentAST.setRoot(agentDefinition);

        assertThrows(CompilationException.class, () -> sut.execute(agentAST));
    }

    @Test
    void checkIfStatementLogicalExpressionUnitAttributeRunsCheck() throws CompilationException {
        UnitAttribute unitAttribute1 = new UnitAttribute(new Unit("my"), new Attribute("health"));
        UnitAttribute unitAttribute2 = new UnitAttribute(new Unit("my"), new Attribute("strength"));
        UnitAttribute unitAttribute3 = new UnitAttribute(new Unit("my"), new Attribute("defence"));

        ScalarLiteral scalarLiteral1 = new ScalarLiteral(1);

        Expression expression1 = new EqualsExpression(unitAttribute1, unitAttribute2);
        Expression expression2 = new GreaterThanExpression(scalarLiteral1, unitAttribute3);
        Expression expression3 = new AndExpression(expression1, expression2);
        IfStatement ifStatement = new IfStatement(expression3, VALID_BODY,
            new ElseClause(VALID_BODY));
        Body body = new Body();
        body.addChild(ifStatement);
        Behavior behavior = new Behavior("test", body);

        AgentDefinition agentDefinition = new AgentDefinition();
        agentDefinition.addChild(DEFAULT_BEHAVIOR);
        agentDefinition.addChild(behavior);
        agentAST.setRoot(agentDefinition);

        assertEquals(agentAST, sut.execute(agentAST));
    }

    @Test
    void checkIfStatementWithIdenticalVariableReferenceThrowCompilationException() {
        UnitAttribute unitAttribute1 = new UnitAttribute(new Unit("my"), new Attribute("health"));
        UnitAttribute unitAttribute2 = new UnitAttribute(new Unit("my"), new Attribute("health"));
        UnitAttribute unitAttribute3 = new UnitAttribute(new Unit("my"), new Attribute("defence"));

        ScalarLiteral scalarLiteral1 = new ScalarLiteral(1);

        Expression expression1 = new EqualsExpression(unitAttribute1, unitAttribute2);
        Expression expression2 = new GreaterThanExpression(scalarLiteral1, unitAttribute3);
        Expression expression3 = new AndExpression(expression1, expression2);
        ElseClause elseClause = new ElseClause(VALID_BODY);
        IfStatement ifStatement = new IfStatement(expression3, VALID_BODY, elseClause);
        Body body = new Body();
        body.addChild(ifStatement);
        Behavior behavior = new Behavior("test", body);

        AgentDefinition agentDefinition = new AgentDefinition();
        agentDefinition.addChild(DEFAULT_BEHAVIOR);
        agentDefinition.addChild(VALID_BEHAVIOR);
        agentDefinition.addChild(behavior);
        agentAST.setRoot(agentDefinition);

        assertThrows(CompilationException.class, () -> sut.execute(agentAST));
    }

    @Test
    void checkSensorAvailableInScopeOfConditionalExpression() throws CompilationException {
        UnitAttribute unitAttributeOther = new UnitAttribute(new Unit("enemy"),
            new Attribute("health"));
        UnitAttribute unitAttributeSelf = new UnitAttribute(new Unit("my"),
            new Attribute("health"));

        Expression expression2 = new LowerThanExpression(unitAttributeOther, unitAttributeSelf);
        Expression expression1 = new StateExpression(new Unit("enemy"), new State("near"));

        IfStatement ifStatement2 = new IfStatement(expression2, VALID_BODY,
            new ElseClause(VALID_BODY));
        Body bodySub = new Body();
        bodySub.addChild(ifStatement2);

        IfStatement ifStatement1 = new IfStatement(expression1, bodySub,
            new ElseClause(VALID_BODY));
        Body bodyMain = new Body();
        bodyMain.addChild(ifStatement1);

        Behavior behaviorDefault = new Behavior("default", bodyMain);

        AgentDefinition agentDefinition = new AgentDefinition();
        agentDefinition.addChild(behaviorDefault);
        agentDefinition.addChild(VALID_BEHAVIOR);
        agentAST.setRoot(agentDefinition);

        assertEquals(agentAST, sut.execute(agentAST));
    }

    @Test
    void checkSensorNotAvailableInScopeOfConditionalExpression() {
        UnitAttribute unitAttributeOther = new UnitAttribute(new Unit("enemy"),
            new Attribute("health"));
        UnitAttribute unitAttributeSelf = new UnitAttribute(new Unit("my"),
            new Attribute("health"));

        Expression expression2 = new LowerThanExpression(unitAttributeOther, unitAttributeSelf);
        Expression expression1 = new StateExpression(new Unit("teammate"), new State("near"));

        IfStatement ifStatement2 = new IfStatement(expression2, VALID_BODY,
            new ElseClause(VALID_BODY));
        Body bodySub = new Body();
        bodySub.addChild(ifStatement2);

        IfStatement ifStatement1 = new IfStatement(expression1, bodySub,
            new ElseClause(VALID_BODY));
        Body bodyMain = new Body();
        bodyMain.addChild(ifStatement1);

        Behavior behaviorDefault = new Behavior("default", bodyMain);

        AgentDefinition agentDefinition = new AgentDefinition();
        agentDefinition.addChild(behaviorDefault);
        agentDefinition.addChild(VALID_BEHAVIOR);
        agentAST.setRoot(agentDefinition);

        assertThrows(CompilationException.class, () -> sut.execute(agentAST));
    }

    @Test
    void checkSensorNotAvailableInScopeOfElseStatementOfConditionalExpression() {
        UnitAttribute unitAttributeOther = new UnitAttribute(new Unit("enemy"),
            new Attribute("health"));
        UnitAttribute unitAttributeSelf = new UnitAttribute(new Unit("my"),
            new Attribute("health"));

        Expression expression2 = new LowerThanExpression(unitAttributeOther, unitAttributeSelf);
        Expression expression1 = new StateExpression(new Unit("teammate"), new State("near"));

        IfStatement ifStatement2 = new IfStatement(expression2, VALID_BODY,
            new ElseClause(VALID_BODY));
        Body bodyElse = new Body();
        bodyElse.addChild(ifStatement2);

        IfStatement ifStatement1 = new IfStatement(expression1, VALID_BODY,
            new ElseClause(bodyElse));
        Body bodyMain = new Body();
        bodyMain.addChild(ifStatement1);

        Behavior behaviorDefault = new Behavior("default", bodyMain);

        AgentDefinition agentDefinition = new AgentDefinition();
        agentDefinition.addChild(behaviorDefault);
        agentDefinition.addChild(VALID_BEHAVIOR);
        agentAST.setRoot(agentDefinition);

        assertThrows(CompilationException.class, () -> sut.execute(agentAST));
    }

    @Test
    void agentDoingNothingInIfClauseCausesErrorToBeThrown() {
        IfStatement ifStatement2 = new IfStatement(VALID_EXPRESSION, VALID_BODY, null);
        Body bodyIf = new Body();
        bodyIf.addChild(ifStatement2);

        IfStatement ifStatement1 = new IfStatement(VALID_EXPRESSION, bodyIf,
            new ElseClause(VALID_BODY));
        Body bodyMain = new Body();
        bodyMain.addChild(ifStatement1);

        Behavior behaviorDefault = new Behavior("default", bodyMain);

        AgentDefinition agentDefinition = new AgentDefinition();
        agentDefinition.addChild(behaviorDefault);
        agentDefinition.addChild(VALID_BEHAVIOR);
        agentAST.setRoot(agentDefinition);

        assertThrows(CompilationException.class, () -> sut.execute(agentAST));
    }

    @Test
    void agentDoingSomethingNotCausingAnErrorToBeThrown() throws CompilationException {
        IfStatement ifStatement2 = new IfStatement(VALID_EXPRESSION, VALID_BODY,
            new ElseClause(VALID_BODY));
        Body bodyIf = new Body();
        bodyIf.addChild(ifStatement2);

        IfStatement ifStatement1 = new IfStatement(VALID_EXPRESSION, bodyIf,
            new ElseClause(VALID_BODY));
        Body bodyMain = new Body();
        bodyMain.addChild(ifStatement1);

        Behavior behaviorDefault = new Behavior("default", bodyMain);

        AgentDefinition agentDefinition = new AgentDefinition();
        agentDefinition.addChild(behaviorDefault);
        agentDefinition.addChild(VALID_BEHAVIOR);
        agentAST.setRoot(agentDefinition);

        assertEquals(agentAST, sut.execute(agentAST));
    }
}