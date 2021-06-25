package nl.ritogames.intelligentagent.exception;

public class TargetNotNearException extends RuntimeException {

  public TargetNotNearException() {
    super("Target is not near!");
  }
}
