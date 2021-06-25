package nl.ritogames.shared.exception;

/**
 * This exception is thrown when the player is trying to create an agent that already exists.
 */
public class AgentAlreadyExistsException extends RuntimeException {

  public AgentAlreadyExistsException(String message) {
    super(message);
  }
}
