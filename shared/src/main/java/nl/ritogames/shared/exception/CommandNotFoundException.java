package nl.ritogames.shared.exception;

public class CommandNotFoundException extends ParseInputException {


    public CommandNotFoundException(String command) {
        super(command);
    }
}
