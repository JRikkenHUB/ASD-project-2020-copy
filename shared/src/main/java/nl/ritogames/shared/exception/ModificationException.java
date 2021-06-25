package nl.ritogames.shared.exception;

public class ModificationException extends Exception {
    public ModificationException(String message) {
        super(message);
    }

    public ModificationException(String message, Throwable cause) {
        super(message, cause);
    }
}
