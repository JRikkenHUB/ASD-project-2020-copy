package nl.ritogames.agentcompiler.ast;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * The type Instruction.
 */
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public class Instruction extends BodyElement {

  /**
   * The Instruction which could be excecuted by the Intelligent Agent.
   */
  @NonNull
  String instructionText;
  /**
   * The Optional Parameter which the instruction needs.
   */
  InstructionParameter parameter;

  @Override
  public String toString() {
    return instructionText + " " + parameter.toString();
  }

  /**
   * Check parameter whether this Instruction contains a Parameter.
   *
   * @return True if this Instruction contains a Parameter
   */
  public boolean checkParameter() {
    return parameter != null;
  }
}
