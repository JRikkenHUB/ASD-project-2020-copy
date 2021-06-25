package nl.ritogames.shared.exception;

public class CommandFailedException extends Exception {
    public CommandFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
