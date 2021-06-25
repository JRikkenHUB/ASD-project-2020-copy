package nl.ritogames.shared.exception;

public class UnknownCharacterFieldException extends RuntimeException{
    public UnknownCharacterFieldException(String message, Throwable e) {
        super(message, e);
    }
}
