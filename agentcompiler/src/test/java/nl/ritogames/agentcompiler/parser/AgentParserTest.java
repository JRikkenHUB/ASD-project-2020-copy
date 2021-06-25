package nl.ritogames.agentcompiler.parser;

import nl.ritogames.agentcompiler.ast.*;
import nl.ritogames.agentcompiler.ast.expression.*;
import nl.ritogames.agentcompiler.ast.literal.PercentageLiteral;
import nl.ritogames.agentcompiler.ast.literal.ScalarLiteral;
import nl.ritogames.agentcompiler.ast.literal.StringLiteral;
import nl.ritogames.shared.exception.CompilationException;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AgentParserTest {

    private AgentParser sut;

    private AgentAST ast;
    private List<String> errors;
    private Stack<ASTNode> stackMock;

    private final String TEST_SOURCE = "default {if gamemode = lms {passive} else {agressive}} " +
            "behavior passive {move randomly}";

    @BeforeEach
    void setUp() {
        errors = mock(List.class);
        ast = new AgentAST();
        stackMock = Mockito.mock(Stack.class);
        sut = new AgentParser(ast, errors, stackMock);
    }

    @Test
    void errorMessagesShouldBeMappedToCompilationException() throws CompilationException {
        when(errors.isEmpty()).thenReturn(false);
        when(stackMock.isEmpty()).thenReturn(true);
        CommonTokenStream tokenStream = new CommonTokenStream(new ASDAgentLexer(CharStreams.fromString(TEST_SOURCE)));

        assertThrows(CompilationException.class, () -> {
            sut.execute(tokenStream);
        });
    }

    @Test
    void exitAgentDefinitionShouldSetDefinitionAsRoot() {
        Behavior defaultBehavior = new Behavior("default", null);
        Behavior behavior = new Behavior(null, null);
        Mockito.when(stackMock.isEmpty()).thenReturn(false, false, true);
        Mockito.when(stackMock.peek()).thenReturn(behavior, defaultBehavior);
        Mockito.when(stackMock.pop()).thenReturn(behavior, defaultBehavior);

        sut.exitAgentDefinition(null);

        assert (ast.getRoot() != null);
    }

    @Test
    void exitDefaultBehaviorShouldPushDefaultBehaviorToStack() {
        Body body = new Body(null);
        Mockito.when(stackMock.pop()).thenReturn(body);

        sut.exitDefaultBehavior(null);

        verify(stackMock, times(1)).push(any(Behavior.class));
    }

    @Test
    void exitBehaviorShouldPushBehaviorToStack() {
        ASDAgentParser.BehaviorContext contextMock = Mockito.mock(ASDAgentParser.BehaviorContext.class);
        ParseTree behaviorNameMock = mock(ParseTree.class);
        when(contextMock.getChild(1)).thenReturn(behaviorNameMock);
        when(behaviorNameMock.getText()).thenReturn("testbehavior");
        Body body = new Body(null);
        when(stackMock.pop()).thenReturn(body);

        sut.exitBehavior(contextMock);

        verify(stackMock, times(1)).push(any(Behavior.class));
    }

    @Test
    void exitIfStatementShouldPushIfStatementToStack() {
        when(stackMock.peek()).thenReturn(new ElseClause(null), new Body(null),
                new GreaterThanExpression(null, null));

        sut.exitIfStatement(null);

        verify(stackMock, times(1)).push(any(IfStatement.class));
    }

    @Test
    void exitElseClauseShouldPushElseClauseToStack() {
        when(stackMock.pop()).thenReturn(new Body(null));

        sut.exitElseClause(null);

        verify(stackMock, times(1)).push(any(ElseClause.class));
    }

    @Test
    void exitBodyShouldPushBodyToStack() {
        BodyElement bodyElement = mock(BodyElement.class);
        when(stackMock.isEmpty()).thenReturn(false, false, true);
        when(stackMock.peek()).thenReturn(bodyElement, bodyElement, new Behavior("default", null));

        sut.exitBody(null);

        verify(stackMock, times(1)).push(any(Body.class));
    }

    @Test
    void exitUnitAttributeShouldPushUnitAttributeToStack() {
        when(stackMock.pop()).thenReturn(new Attribute(null), new Unit(null));

        sut.exitUnitAttribute(null);

        verify(stackMock, times(1)).push(any(UnitAttribute.class));
    }

    @Test
    void exitStateShouldPushStateToStack() {
        ASDAgentParser.StateContext contextMock = mock(ASDAgentParser.StateContext.class);
        when(contextMock.getText()).thenReturn("teststate");

        sut.exitState(contextMock);

        verify(stackMock, times(1)).push(any(State.class));
    }

    @Test
    void exitAttributeShouldPushAttributeToStack() {
        ASDAgentParser.AttributeContext contextMock = mock(ASDAgentParser.AttributeContext.class);
        ParseTree attributeNameMock = mock(ParseTree.class);
        when(contextMock.getChild(0)).thenReturn(attributeNameMock);
        when(attributeNameMock.getText()).thenReturn("testattribute");

        sut.exitAttribute(contextMock);

        verify(stackMock, times(1)).push(any(Attribute.class));
    }

    @Test
    void exitVariableReferenceShouldPushVariableReferenceToStack() {
        ASDAgentParser.VariableReferenceContext contextMock = mock(ASDAgentParser.VariableReferenceContext.class);
        ParseTree variableNameMock = mock(ParseTree.class);
        when(contextMock.getChild(0)).thenReturn(variableNameMock);
        when(variableNameMock.getText()).thenReturn("testvariable");

        sut.exitVariableReference(contextMock);

        verify(stackMock, times(1)).push(any(VariableReference.class));
    }

    @Test
    void exitBehaviorCallShouldPushBehaviorCallToStack() {
        ASDAgentParser.BehaviorCallContext contextMock = mock(ASDAgentParser.BehaviorCallContext.class);
        ParseTree behaviorNameMock = mock(ParseTree.class);
        when(contextMock.getChild(1)).thenReturn(behaviorNameMock);
        when(behaviorNameMock.getText()).thenReturn("testbehavior");

        sut.exitBehaviorCall(contextMock);

        verify(stackMock, times(1)).push(any(BehaviorCall.class));
    }

    @Test
    void exitInstructionShouldPushInstructionToStack() {
        ASDAgentParser.InstructionContext contextMock = mock(ASDAgentParser.InstructionContext.class);
        ParseTree instructionNameMock = mock(ParseTree.class);
        when(instructionNameMock.getText()).thenReturn("testinstruction");
        when(contextMock.getChildCount()).thenReturn(3);
        when(contextMock.getChild(anyInt())).thenReturn(instructionNameMock);
        when(stackMock.isEmpty()).thenReturn(false);
        when(stackMock.peek()).thenReturn(new Unit(null));

        sut.exitInstruction(contextMock);

        verify(stackMock, times(1)).push(any(Instruction.class));
    }

    @Test
    void exitUnitShouldPushUnitToStack() {
        ASDAgentParser.UnitContext contextMock = mock(ASDAgentParser.UnitContext.class);
        ParseTree unitTypeMock = mock(ParseTree.class);
        when(contextMock.getChild(0)).thenReturn(unitTypeMock);
        when(unitTypeMock.getText()).thenReturn("testunit");

        sut.exitUnit(contextMock);

        verify(stackMock, times(1)).push(any(Unit.class));
    }

    @Test
    void exitArtifactShouldPushArtifactToStack() {
        ASDAgentParser.ArtifactContext contextMock = mock(ASDAgentParser.ArtifactContext.class);
        ParseTree artifactTypeMock = mock(ParseTree.class);
        when(contextMock.getChild(0)).thenReturn(artifactTypeMock);
        when(artifactTypeMock.getText()).thenReturn("testartifact");

        sut.exitArtifact(contextMock);

        verify(stackMock, times(1)).push(any(Artifact.class));
    }

    @Test
    void exitScalarLiteralShouldPushScalarLiteralToStack() {
        ASDAgentParser.ScalarLiteralContext contextMock = mock(ASDAgentParser.ScalarLiteralContext.class);
        ParseTree scalarLiteralMock = mock(ParseTree.class);
        when(contextMock.getChild(0)).thenReturn(scalarLiteralMock);
        when(scalarLiteralMock.getText()).thenReturn("10");

        sut.exitScalarLiteral(contextMock);

        verify(stackMock, times(1)).push(any(ScalarLiteral.class));
    }

    @Test
    void exitPercentageLiteralShouldPushPercentageLiteralToStack() {
        ASDAgentParser.PercentageLiteralContext contextMock = mock(ASDAgentParser.PercentageLiteralContext.class);
        ParseTree percentageLiteralMock = mock(ParseTree.class);
        when(contextMock.getChild(0)).thenReturn(percentageLiteralMock);
        when(percentageLiteralMock.getText()).thenReturn("10%");

        sut.exitPercentageLiteral(contextMock);

        verify(stackMock, times(1)).push(any(PercentageLiteral.class));
    }

    @Test
    void exitStringLiteralShouldPushStringLiteralToStack() {
        ASDAgentParser.StringLiteralContext contextMock = mock(ASDAgentParser.StringLiteralContext.class);
        ParseTree stringLiteralMock = mock(ParseTree.class);
        when(contextMock.getChild(0)).thenReturn(stringLiteralMock);
        when(stringLiteralMock.getText()).thenReturn("lms");

        sut.exitStringLiteral(contextMock);

        verify(stackMock, times(1)).push(any(StringLiteral.class));
    }

    private void arrangeLogicalExpressionStack() {
        Expression exp = mock(Expression.class);
        when(stackMock.pop()).thenReturn(exp, exp);
    }

    private void arrangeComparisonExpressionStack() {
        Unit op = mock(Unit.class);
        when(stackMock.pop()).thenReturn(op, op);
    }

    @Test
    void exitOrExpressionShouldPushOrExpressionToStack() {
        arrangeLogicalExpressionStack();

        sut.exitOrExpression(null);

        verify(stackMock, times(1)).push(any(OrExpression.class));
    }

    @Test
    void exitAndExpressionShouldPushAndExpressionToStack() {
        arrangeLogicalExpressionStack();

        sut.exitAndExpression(null);

        verify(stackMock, times(1)).push(any(AndExpression.class));
    }

    @Test
    void exitEqualsExpressionShouldPushEqualsExpressionToStack() {
        arrangeComparisonExpressionStack();

        sut.exitEqualsExpression(null);

        verify(stackMock, times(1)).push(any(EqualsExpression.class));
    }

    @Test
    void exitStateExpressionShouldPushStateExpressionToStack() {
        arrangeComparisonExpressionStack();

        sut.exitStateExpression(null);

        verify(stackMock, times(1)).push(any(StateExpression.class));
    }

    @Test
    void exitHasExpressionShouldPushHasExpressionToStack() {
        arrangeComparisonExpressionStack();

        sut.exitHasExpression(null);

        verify(stackMock, times(1)).push(any(HasExpression.class));
    }

    @Test
    void exitLowerThanExpressionShouldPushLowerThanExpressionToStack() {
        arrangeComparisonExpressionStack();

        sut.exitLowerThanExpression(null);

        verify(stackMock, times(1)).push(any(LowerThanExpression.class));
    }

    @Test
    void exitGreaterThanExpressionShouldPushGreaterThanExpressionToStack() {
        arrangeComparisonExpressionStack();

        sut.exitGreaterThanExpression(null);

        verify(stackMock, times(1)).push(any(GreaterThanExpression.class));
    }

}