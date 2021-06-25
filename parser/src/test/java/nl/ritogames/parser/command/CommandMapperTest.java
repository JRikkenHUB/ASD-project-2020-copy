package nl.ritogames.parser.command;

import nl.ritogames.shared.exception.CommandNotFoundException;
import nl.ritogames.shared.exception.InvalidArgumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CommandMapperTest {

  private CommandMapper sut;
  private ArgumentParser argumentParser;

  @BeforeEach
  void setUp() {
    sut = new CommandMapper();
    argumentParser = mock(ArgumentParser.class);
    sut.setParser(argumentParser);
  }

  @Test
  void addCommandShouldOnlyAllowSingleWords() {
    String invalidInput = "wrong input";

    assertThrows(IllegalArgumentException.class, () -> sut.addCommand(invalidInput, Command.class));
  }

  @Test
  void addCommandShouldNotAllowEmptyString() {
    String invalidInput = "";

    assertThrows(IllegalArgumentException.class, () -> sut.addCommand(invalidInput, Command.class));
  }

  @Test
  void getCommandShouldThrowCommandNotFoundExceptionIfNoCommandWasMapped() {
    String invalidInput = "invalid_command";

    assertThrows(CommandNotFoundException.class, () -> {
      sut.getCommand(invalidInput);
    });
  }

  @Test
  void getCommandShouldThrowInvalidArgumentExceptionIfArgumentParserThrowsError()
      throws InvalidArgumentException {
    String invalidInput = "invalid_command";
    doThrow(InvalidArgumentException.class).when(argumentParser)
        .mapArguments(invalidInput, Command.class);

    assertThrows(CommandNotFoundException.class, () -> {
      sut.getCommand(invalidInput);
    });
  }

  @Test
  void getCommandShouldThrowCommandNotFoundExceptionIfCommandNotFound() {
    String invalidInput = "invalid_command";
    sut.addCommand("invalid", Command.class);

    assertThrows(CommandNotFoundException.class, () -> {
      sut.getCommand(invalidInput);
    });
  }

  @Test
  void getCommandShouldReturnTheCorrectCommandGivenAValidInput()
      throws CommandNotFoundException, InvalidArgumentException {
    String validInput = "command";
    var mock = mock(Command.class);
    var expected = Command.class;
    sut.addCommand(validInput, expected);
    when(argumentParser.mapArguments(validInput, expected)).thenReturn(mock);

    var result = sut.getCommand(validInput);

    assertEquals(mock, result);
  }

  @Test
  void getCommandShouldReturnTheCorrectCommandGivenAValidInputWithArguments()
      throws CommandNotFoundException, InvalidArgumentException {
    String validInput = "command with argument";
    var mock = mock(Command.class);
    var expected = Command.class;
    sut.addCommand("command", expected);
    when(argumentParser.mapArguments(validInput, expected)).thenReturn(mock);

    var result = sut.getCommand(validInput);

    assertEquals(mock, result);
  }
}
