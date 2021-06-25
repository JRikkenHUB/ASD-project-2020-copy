package nl.ritogames.shared.exception;

/**
 * This exception is thrown when the player is trying to select an agent that does not exist.
 */
public class AgentNotFoundException extends RuntimeException {

  public AgentNotFoundException(String message) {
    super(message);
  }
}
