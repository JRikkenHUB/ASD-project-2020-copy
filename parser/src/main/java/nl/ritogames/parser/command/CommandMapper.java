package nl.ritogames.parser.command;

import nl.ritogames.shared.exception.CommandNotFoundException;
import nl.ritogames.shared.exception.InvalidArgumentException;
import nl.ritogames.shared.logger.Logger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;



public class CommandMapper {

  private static final String SPACE = " ";
  private static final String SINGLE_WORD_REGEX = "^(\\w)+$";
  private static final String KEYWORD_ERROR_MESSAGE = "keyword has to be exactly 1 word";
  private final Map<String, Class<? extends Command>> commandMap;
  private ArgumentParser parser;


  public CommandMapper() {
    parser = new ArgumentParser();
    commandMap = new HashMap<>();
  }

  public Command getCommand(String commandInput)
      throws CommandNotFoundException, InvalidArgumentException {
        Logger.logMethodCall(this);
    String initialWord = Arrays.stream(commandInput.split(SPACE)).findFirst()
        .orElseThrow(() -> new CommandNotFoundException(commandInput));
    Optional<Class<? extends Command>> commandClass = Optional.ofNullable(commandMap.get(initialWord));
    if (commandClass.isPresent()) {
      return parser.mapArguments(commandInput, commandClass.get());
    } else {
      throw new CommandNotFoundException(initialWord);
    }
  }

  public void addCommand(String keyword, Class<? extends Command> commandClass) {
    if (keyword.matches(SINGLE_WORD_REGEX)) {
      commandMap.put(keyword, commandClass);
    } else {
      throw new IllegalArgumentException(KEYWORD_ERROR_MESSAGE);
    }
  }

  public void setParser(ArgumentParser parser) {
    this.parser = parser;
  }

}
