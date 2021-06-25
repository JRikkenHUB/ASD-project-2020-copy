package nl.ritogames.parser.command.interactioncommand;

import nl.ritogames.shared.dto.event.*;
import nl.ritogames.shared.exception.InvalidArgumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class GameCommandTest {

  private GameCommand sut;

  @BeforeEach
  void setUp() {
    sut = new GameCommand();
  }

  @Test
  void setCommandTypeShouldThrowInvalidArgumentExceptionWhenCalledWithNonExistingGameCommandType() {
    //arrange
    String input = "";
    var expected = InvalidArgumentException.class;

    //act + assert
    assertThrows(expected, () -> {
      sut.setCommandType(input);
    });
  }

  @Test
  void setCommandTypeShouldSetCommandTypeWithCorrespondingStartStringValue()
      throws InvalidArgumentException {
    //arrange
    String input = "START";
    GameCommandType expected = GameCommandType.START;

    //act
    sut.setCommandType(input);

    //assert
    assertEquals(expected, sut.commandType);

  }

  @Test
  void setCommandTypeShouldSetCommandTypeWithCorrespondingLowercaseStartStringValue()
      throws InvalidArgumentException {
    //arrange
    String input = "start";
    GameCommandType expected = GameCommandType.START;

    //act
    sut.setCommandType(input);

    //assert
    assertEquals(expected, sut.commandType);

  }

  @Test
  void setCommandTypeShouldSetCommandTypeWithCorrespondingSaveStringValue()
      throws InvalidArgumentException {
    //arrange
    String input = "SAVE";
    GameCommandType expected = GameCommandType.SAVE;

    //act
    sut.setCommandType(input);

    //assert
    assertEquals(expected, sut.commandType);

  }

  @Test
  void setCommandTypeShouldSetCommandTypeWithCorrespondingLowercaseSaveStringValue()
      throws InvalidArgumentException {
    //arrange
    String input = "save";
    GameCommandType expected = GameCommandType.SAVE;

    //act
    sut.setCommandType(input);

    //assert
    assertEquals(expected, sut.commandType);

  }

  @Test
  void setCommandShouldWrapInputInOptional()
      throws InvalidArgumentException {
    //arrange
    String expected = "start";

    //act
    sut.setName(expected);
    var actual = sut.name.get();

    //assert
    assertEquals(expected, actual);

  }

  @Test
  void setIpShouldWrapInputInOptional()
      throws InvalidArgumentException {
    //arrange
    String expected = "0.0.0.0";

    //act
    sut.setIp(expected);
    var actual = sut.ip.get();

    //assert
    assertEquals(expected, actual);

  }

  @Test
  void toInteractionEventShouldReturnGameStartEventWithGameCommandTypeStart()
      throws InvalidArgumentException {
    //arrange
    var name = "any";
    sut.name = Optional.of(name);
    sut.commandType = GameCommandType.START;

    //act
    var actual = sut.toInteractionEvent(null);

    //assert
    assertTrue(actual instanceof StartGameEvent);
  }

  @Test
  void toInteractionEventShouldReturnGameCreateEventWithGameCommandTypeCreateAndName()
      throws InvalidArgumentException {
    //arrange
    var name = "any";
    sut.name = Optional.of(name);
    sut.commandType = GameCommandType.CREATE;

    //act
    var actual = sut.toInteractionEvent(name);

    //assert
    assertTrue(actual instanceof CreateGameEvent);
  }

  @Test
  void toInteractionEventShouldReturnGameSaveEventWithGameCommandTypeSaveAndNameAndTimestamp()
      throws InvalidArgumentException {
    //arrange
    var expected = "save";
    sut.name = Optional.of(expected);
    sut.commandType = GameCommandType.SAVE;

    //act
    var actual = sut.toInteractionEvent("");

    //assert
    assertTrue(actual instanceof GameSaveEvent);
  }

  @Test
  void toInteractionEventShouldReturnGameJoinEventWithGameCommandTypeCreateAndIp()
      throws InvalidArgumentException {
    //arrange
    var ip = "0.0.0.0";
    var gamename = "";
    var expected = new GameJoinEvent(ip);
    sut.ip = Optional.of(ip);
    sut.name = Optional.of(gamename);
    sut.commandType = GameCommandType.JOIN;

    //act
    var actual = sut.toInteractionEvent("");

    //assert
    assertEquals(expected, actual);
  }

  @Test
  void toInteractionEventShouldThrowInvalidArgumentExceptionWithJoinAndNoIp() {
    //arrange
    var expected = InvalidArgumentException.class;
    sut.commandType = GameCommandType.JOIN;

    //act + assert
    assertThrows(expected, () -> {
      var actual = sut.toInteractionEvent("");
    });

  }

  @Test
  void toInteractionEventShouldReturnGameExitEventWithGameCommandTypeExit()
      throws InvalidArgumentException {
    //arrange
    sut.commandType = GameCommandType.EXIT;

    //act
    var actual = sut.toInteractionEvent(null);

    //assert
    assertTrue(actual instanceof GameExitEvent);
  }
}
