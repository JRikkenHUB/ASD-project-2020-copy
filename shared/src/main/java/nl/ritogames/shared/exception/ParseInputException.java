package nl.ritogames.shared.exception;

public class ParseInputException extends Exception {

  private final String command;

  public ParseInputException(String command) {
    this.command = command;
  }

  public String getCommand() {
    return command;
  }
}
