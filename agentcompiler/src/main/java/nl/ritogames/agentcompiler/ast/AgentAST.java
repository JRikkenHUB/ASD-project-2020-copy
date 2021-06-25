package nl.ritogames.agentcompiler.ast;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The type Agent ast.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AgentAST {

  /**
   * The Root.
   */
  AgentDefinition root;

}
