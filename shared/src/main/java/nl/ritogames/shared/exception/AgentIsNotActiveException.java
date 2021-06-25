package nl.ritogames.shared.exception;

/**
 * This exception is thrown when the player is trying to join a game without selecting an agent.
 */
public class AgentIsNotActiveException extends RuntimeException {

  public AgentIsNotActiveException(String message) {
    super(message);
  }
}
