package nl.ritogames.intelligentagent.exception;

public class UnknownActionException extends RuntimeException {

  public UnknownActionException(String instruction) {
    super("Unknown instruction: " + instruction);
  }
}
