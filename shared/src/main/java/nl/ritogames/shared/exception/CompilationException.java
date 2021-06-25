package nl.ritogames.shared.exception;

import java.util.ArrayList;
import java.util.List;

/**
 * A compilation exception is thrown when a player tries to compile invalid agent source code.
 * The compilation exception provides a list of all error messages that occurred during compilation,
 * be it a syntax error or a semantic error.
 */
public class CompilationException extends RuntimeException {
    private final List<String> messages;

    public CompilationException(String message) {
        this.messages = new ArrayList<>();
        this.messages.add(message);
    }

    public CompilationException(List<String> messages) {
        this.messages = messages;
    }

    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder();
        messages.forEach(message -> sb.append(message).append("\n"));
        return sb.toString();
    }
}
