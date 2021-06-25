package nl.ritogames.parser.command;

import nl.ritogames.parser.command.annotation.CommandParam;
import nl.ritogames.shared.dto.event.Event;
import nl.ritogames.shared.exception.InvalidArgumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class ArgumentParserTest {

  private ArgumentParser sut;

  @BeforeEach
  void setUp() {
    sut = new ArgumentParser();
  }

  @Test
  void mapArgumentsShouldNotCallMethodWhenNonVoid() throws InvalidArgumentException {
    //arrange
    String expected = null;
    var commandClass = CommandWithNonVoidMethod.class;

    //act
    var command = sut.mapArguments("someValue", commandClass);

    //assert
    assertEquals(expected, command.toString());
  }

  @Test
  void mapArgumentsShouldNotCallMethodWithNoParams() throws InvalidArgumentException {
    //arrange
    String expected = null;
    var commandClass = CommandWithNoParam.class;

    //act
    var command = sut.mapArguments("someValue", commandClass);

    //assert
    assertEquals(expected, command.toString());
  }

  @Test
  void mapArgumentsShouldNotCallMethodWithTwoParams() throws InvalidArgumentException {
    //arrange
    String expected = null;
    var commandClass = CommandWithTwoParams.class;

    //act
    var command = sut.mapArguments("someValue", commandClass);

    //assert
    assertEquals(expected, command.toString());
  }

  @Test
  void mapArgumentsShouldNotCallMethodWithoutAnnotation() throws InvalidArgumentException {
    //arrange
    String expected = null;
    var commandClass = CommandWithoutAnnotation.class;

    //act
    var command = sut.mapArguments("someValue", commandClass);

    //assert
    assertEquals(expected, command.toString());
  }

  @Test
  void mapArgumentsShouldNotCallMethodWithNoStringParam() throws InvalidArgumentException {
    //arrange
    String expected = null;
    var commandClass = CommandWithWrongParameterType.class;

    //act
    var command = sut.mapArguments("someValue", commandClass);

    //assert
    assertEquals(expected, command.toString());
  }

  @Test
  void mapArgumentsShouldThrowInvalidArgumentExceptionWhenIndexIsOutOfBounds()
      throws InvalidArgumentException {
    //arrange
    var commandClass = getCommandWithIndexOne.class;

    //act
    assertThrows(InvalidArgumentException.class, () -> sut.mapArguments("any", commandClass));
  }

  @Test
  void mapArgumentsShouldThrowInvalidArgumentExceptionWhenCommandThrows()
      throws InvalidArgumentException {
    //arrange
    var command = getCommandWithInvalidArgument.class;

    //act
    assertThrows(InvalidArgumentException.class, () -> sut.mapArguments("any", command));
  }

  @Test
  void mapArgumentsShouldMapStringToMethod() throws InvalidArgumentException {
    //arrange
    String expected = "myValue";
    var commandClass = getValidCommand.class;

    //act
    var command = sut.mapArguments(expected, commandClass);
    var actual = command.toString();

    assertEquals(expected, actual);
  }

  @Test
  void mapArgumentsShouldNotThrowOnOptionalArgument() {
    //arrange
    String expected = null;
    var commandClass = getValidCommandForOptionalArgument.class;

    //act
    assertDoesNotThrow(() -> {
      var command = sut.mapArguments("somecommand", commandClass);
      var actual = command.toString();

      assertEquals(expected, actual);
    });
  }

  public static class CommandWithNonVoidMethod implements Command {

    private String value;

    @CommandParam(index = 0)
    public String setSomeValue(String value) {
      this.value = value;
      return null;
    }

    @Override
    public Event toInteractionEvent(String replaced) {
      return null;
    }

    @Override
    public String getUsage() {
      return null;
    }

    @Override
    public String toString() {
      return value;
    }
  }

  public static class CommandWithNoParam implements Command {
    private String value;

    @CommandParam(index = 0)
    public void setSomeValue() {
      this.value = "anyValue";
    }

    @Override
    public Event toInteractionEvent(String replaced) {
      return null;
    }

    @Override
    public String getUsage() {
      return null;
    }

    @Override
    public String toString() {
      return value;
    }
  }

  public static class CommandWithTwoParams implements Command {
    private String value;

    @CommandParam(index = 0)
    public void setSomeValue(String a, String b) {
      this.value = "anyValue";
    }

    @Override
    public Event toInteractionEvent(String replaced) {
      return null;
    }

    @Override
    public String getUsage() {
      return null;
    }

    @Override
    public String toString() {
      return value;
    }
  }

  public static class CommandWithoutAnnotation implements Command {
    private String value;

    public void setSomeValue(String a) {
      this.value = a;
    }

    @Override
    public Event toInteractionEvent(String replaced) {
      return null;
    }

    @Override
    public String getUsage() {
      return null;
    }

    @Override
    public String toString() {
      return value;
    }
  }

  public static class CommandWithWrongParameterType implements Command {
    private String value;

    @CommandParam(index = 0)
    public void setSomeValue(int a) {
      this.value = a + " test";
    }

    @Override
    public Event toInteractionEvent(String replaced) {
      return null;
    }

    @Override
    public String getUsage() {
      return null;
    }

    @Override
    public String toString() {
      return value;
    }
  }

  public static class getCommandWithIndexOne implements Command {

      private String value;

      @CommandParam(index = 1)
      public void setSomeValue(String input) {
        value = value;
      }

      @Override
      public Event toInteractionEvent(String replaced) {
        return null;
      }

    @Override
    public String getUsage() {
      return null;
    }

    @Override
      public String toString() {
        return value;
      }
  }


  public static class getCommandWithInvalidArgument implements Command {

      private String value;

      @CommandParam(index = 0)
      public void setSomeValue(String input) throws InvalidArgumentException {
        throw new InvalidArgumentException("", "", "");
      }

      @Override
      public Event toInteractionEvent(String replaced) {
        return null;
      }

    @Override
    public String getUsage() {
      return null;
    }

    @Override
      public String toString() {
        return value;
      }
  }

  public static class getValidCommand implements Command {

      private String value;

      @CommandParam(index = 0)
      public void setSomeValue(String input) {
        value = input;
      }

      @Override
      public Event toInteractionEvent(String replaced) {
        return null;
      }

    @Override
    public String getUsage() {
      return null;
    }

    @Override
      public String toString() {
        return value;
      }
  }

  public static class getValidCommandForOptionalArgument implements Command {
      private String value;

      @CommandParam(index = 1, optional = true)
      public void setSomeValue(String input) {
        value = input;
      }

      @Override
      public Event toInteractionEvent(String replaced) {
        return null;
      }

    @Override
    public String getUsage() {
      return null;
    }

    @Override
      public String toString() {
        return value;
      }
  }

}
