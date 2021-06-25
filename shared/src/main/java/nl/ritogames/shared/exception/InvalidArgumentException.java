package nl.ritogames.shared.exception;


/**
 * The Invalid argument exception which extends ParseInputException.
 */
public class InvalidArgumentException extends ParseInputException {

  private final String argument;
  private final String message;

  public InvalidArgumentException(String command, String argument, String message) {
    super(command);
    this.argument = argument;
    this.message = message;
  }

  public String getArgument() {
    return argument;
  }

  @Override
  public String getMessage() {
    return message;
  }
}
