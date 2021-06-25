package nl.ritogames.agentcompiler.transformer;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import nl.ritogames.agentcompiler.Stage;
import nl.ritogames.agentcompiler.ast.ASTNode;
import nl.ritogames.agentcompiler.ast.AgentAST;
import nl.ritogames.agentcompiler.ast.AgentDefinition;
import nl.ritogames.agentcompiler.ast.Behavior;
import nl.ritogames.agentcompiler.ast.IfStatement;
import nl.ritogames.agentcompiler.ast.Instruction;
import nl.ritogames.shared.exception.CompilationException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * The AgentTransformer transforms the AgentAST. It replaces aliases with the correct instruction.
 */
public class AgentTransformer implements Stage<AgentAST, AgentAST> {

  private List<Alias> aliasList;

  private static final String ALIAS_FILE_NAME = "instructions.json";
  private static final String ALIAS_KEY = "alias";

  @Override
  public AgentAST execute(AgentAST input) {
    aliasList = new ArrayList<>();
    loadInstructions();
    walkAST(input.getRoot());
    return input;
  }

  private void loadInstructions() {
    InputStream aliasStream = getClass().getClassLoader()
        .getResourceAsStream(ALIAS_FILE_NAME);
    if (aliasStream == null) {
      throw new CompilationException("Couldn't load the predefined instructions from file.");
    }
    JSONObject instructions = new JSONObject(new JSONTokener(aliasStream));
    for (String instruction : instructions.keySet()) {
      JSONObject instructionObject = instructions.getJSONObject(instruction);
      JSONArray aliasesArray = instructionObject.getJSONArray(ALIAS_KEY);

      for (int a = 0; a < aliasesArray.length(); a++) {
        aliasList.add(new Alias(aliasesArray.getString(a), instruction));
      }
    }
  }

  private void walkAST(ASTNode node) {
    if (node instanceof AgentDefinition || node instanceof Behavior
        || node instanceof IfStatement) {
      for (ASTNode childNode : node.getChildren()) {
        walkAST(childNode);
      }
      if (node instanceof IfStatement && ((IfStatement) node).hasElseClause()) {
        for (ASTNode childNode : ((IfStatement) node).getElseClause().getChildren()) {
          walkAST(childNode);
        }
      }
    } else if (node instanceof Instruction) {
      for (Alias alias : aliasList) {
        checkInstruction((Instruction) node, alias);
      }
    }
  }

  private void checkInstruction(Instruction node, Alias alias) {
    if (node.getInstructionText().equals(alias.getAliasName())) {
      node.setInstructionText(alias.getInstruction());
    }
  }

  @Getter
  @AllArgsConstructor
  private static class Alias {

    private final String aliasName;
    private final String instruction;
  }
}
