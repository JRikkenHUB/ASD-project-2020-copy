package nl.ritogames.agentcompiler.parser;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.EmptyStackException;

import nl.ritogames.agentcompiler.Stage;
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
import nl.ritogames.agentcompiler.ast.expression.GreaterThanExpression;
import nl.ritogames.agentcompiler.ast.expression.HasExpression;
import nl.ritogames.agentcompiler.ast.expression.LowerThanExpression;
import nl.ritogames.agentcompiler.ast.expression.Operand;
import nl.ritogames.agentcompiler.ast.expression.OrExpression;
import nl.ritogames.agentcompiler.ast.expression.StateExpression;
import nl.ritogames.agentcompiler.ast.literal.PercentageLiteral;
import nl.ritogames.agentcompiler.ast.literal.ScalarLiteral;
import nl.ritogames.agentcompiler.ast.literal.StringLiteral;
import nl.ritogames.shared.exception.CompilationException;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

/**
 * The AgentParser creates an AgentAST from the lexer TokenStream.
 */
public class AgentParser extends ASDAgentBaseListener implements Stage<CommonTokenStream, AgentAST>,
    ANTLRErrorListener {

  private AgentAST ast;
  private final List<String> errors;
  private final Stack<ASTNode> stack;

  private static final String MISSING_DEFAULT_BEHAVIOR = "A default behavior for the agent should be defined.";

  private ASDAgentParser parser;

  /**
   * Instantiates a new Agent parser.
   */
  public AgentParser() {
    this(new AgentAST(), new ArrayList<>(), new Stack<>());
  }

  /**
   * Instantiates a new Agent parser.
   *
   * @param ast    the ast
   * @param errors the errors
   * @param stack  the stack
   */
  AgentParser(AgentAST ast, List<String> errors, Stack<ASTNode> stack) {
    this.ast = ast;
    this.errors = errors;
    this.stack = stack;
  }

  @Override
  public AgentAST execute(CommonTokenStream tokenStream) {
    try {
      initializeParser(tokenStream);
      walkParseTree();
    } catch (RecognitionException | ParseCancellationException e) {
      this.ast = new AgentAST();
      errors.add(e.getMessage());
    } catch (EmptyStackException e){
      throw new CompilationException("Agent does not contain behaviour, please add or see documentation on how to");
    }
    if (errors.isEmpty()) {
      return ast;
    } else {
      throw new CompilationException(errors);
    }
  }


  @Override
  public void exitAgentDefinition(ASDAgentParser.AgentDefinitionContext ctx) {
    List<Behavior> behaviors = new ArrayList<>();
    while (!stack.isEmpty()) {
      behaviors.add((Behavior) stack.pop());
    }
    Collections.reverse(behaviors);
    AgentDefinition agentDefinition = new AgentDefinition(behaviors);
    ast.setRoot(agentDefinition);
  }

  @Override
  public void exitDefaultBehavior(ASDAgentParser.DefaultBehaviorContext ctx) {
    if (!stack.isEmpty()) {
      Body body = (Body) stack.pop();
      stack.push(new Behavior("default", body));
    } else {
      errors.add(MISSING_DEFAULT_BEHAVIOR);
    }
  }

  @Override
  public void exitBehavior(ASDAgentParser.BehaviorContext ctx) {
      Body body = (Body) stack.pop();
      String behaviorName = ctx.getChild(1).getText();
      stack.push(new Behavior(behaviorName, body));
  }

  @Override
  public void exitIfStatement(ASDAgentParser.IfStatementContext ctx) {
    ElseClause elseClause = null;
    if (stack.peek() instanceof ElseClause) {
      elseClause = (ElseClause) stack.pop();
    }
    Body body = (Body) stack.pop();
    Expression expression = (Expression) stack.pop();
    stack.push(new IfStatement(expression, body, elseClause));
  }

  @Override
  public void exitElseClause(ASDAgentParser.ElseClauseContext ctx) {
    Body body = (Body) stack.pop();
    stack.push(new ElseClause(body));
  }

  @Override
  public void exitBody(ASDAgentParser.BodyContext ctx) {
    List<BodyElement> bodyElements = new ArrayList<>();
    while (!stack.isEmpty()) {
      if (stack.peek() instanceof BodyElement) {
        bodyElements.add((BodyElement) stack.pop());
      } else {
        break;
      }
    }

    Collections.reverse(bodyElements);
    stack.push(new Body(bodyElements));
  }

  @Override
  public void exitUnitAttribute(ASDAgentParser.UnitAttributeContext ctx) {
    Attribute attribute = (Attribute) stack.pop();
    Unit unit = (Unit) stack.pop();
    stack.push(new UnitAttribute(unit, attribute));
  }

  @Override
  public void exitState(ASDAgentParser.StateContext ctx) {
    stack.push(new State(ctx.getText()));
  }

  @Override
  public void exitAttribute(ASDAgentParser.AttributeContext ctx) {
    stack.push(new Attribute(ctx.getChild(0).getText()));
  }

  @Override
  public void exitVariableReference(ASDAgentParser.VariableReferenceContext ctx) {
    stack.push(new VariableReference(ctx.getChild(0).getText()));
  }

  @Override
  public void exitBehaviorCall(ASDAgentParser.BehaviorCallContext ctx) {
    stack.push(new BehaviorCall(ctx.getChild(1).getText()));
  }

  @Override
  public void exitInstruction(ASDAgentParser.InstructionContext ctx) {
    Instruction instruction = new Instruction("", null);
    int identifierCount = ctx.getChildCount();
    if (!stack.isEmpty() && stack.peek() instanceof InstructionParameter) {
      instruction.setParameter((InstructionParameter) stack.pop());
      identifierCount--;
    }

    List<String> instructionParts = new LinkedList<>();
    for (int i = 0; i < identifierCount; i++) {
      instructionParts.add(ctx.getChild(i).getText());
    }

    String instructionName = String.join(" ", instructionParts);
    instruction.setInstructionText(instructionName);
    stack.push(instruction);
  }

  @Override
  public void exitUnit(ASDAgentParser.UnitContext ctx) {
    stack.push(new Unit(ctx.getChild(0).getText()));
  }

  @Override
  public void exitArtifact(ASDAgentParser.ArtifactContext ctx) {
    stack.push(new Artifact(ctx.getChild(0).getText()));
  }


  @Override
  public void exitScalarLiteral(ASDAgentParser.ScalarLiteralContext ctx) {
    stack.push(new ScalarLiteral(Integer.parseInt(ctx.getChild(0).getText())));
  }

  @Override
  public void exitPercentageLiteral(ASDAgentParser.PercentageLiteralContext ctx) {
    stack.push(new PercentageLiteral(ctx.getChild(0).getText()));
  }

  @Override
  public void exitStringLiteral(ASDAgentParser.StringLiteralContext ctx) {
    stack.push(new StringLiteral(ctx.getChild(0).getText()));
  }

  @Override
  public void exitOrExpression(ASDAgentParser.OrExpressionContext ctx) {
    Expression rhs = (Expression) stack.pop();
    Expression lhs = (Expression) stack.pop();
    OrExpression orExpression = new OrExpression(lhs, rhs);
    stack.push(orExpression);
  }

  @Override
  public void exitAndExpression(ASDAgentParser.AndExpressionContext ctx) {
    Expression rhs = (Expression) stack.pop();
    Expression lhs = (Expression) stack.pop();
    AndExpression andExpression = new AndExpression(lhs, rhs);
    stack.push(andExpression);
  }

  @Override
  public void exitEqualsExpression(ASDAgentParser.EqualsExpressionContext ctx) {
    Operand rhs = (Operand) stack.pop();
    Operand lhs = (Operand) stack.pop();
    EqualsExpression equalsExpression = new EqualsExpression(lhs, rhs);
    stack.push(equalsExpression);
  }

  @Override
  public void exitStateExpression(ASDAgentParser.StateExpressionContext ctx) {
    Operand rhs = (Operand) stack.pop();
    Operand lhs = (Operand) stack.pop();
    StateExpression stateExpression = new StateExpression(lhs, rhs);
    stack.push(stateExpression);
  }

  @Override
  public void exitHasExpression(ASDAgentParser.HasExpressionContext ctx) {
    Operand rhs = (Operand) stack.pop();
    Operand lhs = (Operand) stack.pop();
    HasExpression hasExpression = new HasExpression(lhs, rhs);
    stack.push(hasExpression);
  }

  @Override
  public void exitLowerThanExpression(ASDAgentParser.LowerThanExpressionContext ctx) {
    Operand rhs = (Operand) stack.pop();
    Operand lhs = (Operand) stack.pop();
    LowerThanExpression ltExpression = new LowerThanExpression(lhs, rhs);
    stack.push(ltExpression);
  }

  @Override
  public void exitGreaterThanExpression(ASDAgentParser.GreaterThanExpressionContext ctx) {
    Operand rhs = (Operand) stack.pop();
    Operand lhs = (Operand) stack.pop();
    GreaterThanExpression gtExpression = new GreaterThanExpression(lhs, rhs);
    stack.push(gtExpression);
  }

  @Override
  public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int lineNumber,
      int col, String message, RecognitionException e) {
    String errorMessage = String.format("Syntax error at (%d,%d): %s", lineNumber, col, message);
    errors.add(errorMessage);
  }


  @Override
  public void reportAmbiguity(Parser parser, DFA dfa, int i, int i1, boolean b, BitSet bitSet,
      ATNConfigSet atnConfigSet) {
    // Ignore default implementation
  }

  @Override
  public void reportAttemptingFullContext(Parser parser, DFA dfa, int i, int i1, BitSet bitSet,
      ATNConfigSet atnConfigSet) {
    // Ignore default implementation
  }

  @Override
  public void reportContextSensitivity(Parser parser, DFA dfa, int i, int i1, int i2,
      ATNConfigSet atnConfigSet) {
    // Ignore default implementation
  }

  private void initializeParser(CommonTokenStream tokenStream) {
    parser = new ASDAgentParser(tokenStream);
    parser.removeErrorListeners();
    parser.addErrorListener(this);
  }

  private void walkParseTree() {
    ParseTree parseTree = parser.agentDefinition();
    ParseTreeWalker walker = new ParseTreeWalker();
    walker.walk(this, parseTree);
  }
}
