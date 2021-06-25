package nl.ritogames.parser.command.interactioncommand;

import nl.ritogames.shared.dto.event.AttackEvent;
import nl.ritogames.shared.dto.event.Event;
import nl.ritogames.shared.enums.Direction;
import nl.ritogames.shared.exception.InvalidArgumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AttackInteractionCommandTest {

  private AttackInteractionCommand sut;

  @BeforeEach
  void setUp() {
    this.sut = new AttackInteractionCommand();
  }

  @Test
  void setDirectionShouldThrowInvalidArgumentExceptionWhenCalledWithInvalidArgument() {
    //arrange
    String argument = "rechts";
    var expected = InvalidArgumentException.class;

    //act + assert
    assertThrows(expected, () -> {
      sut.setDirection(argument);
    });
  }

  @Test
  void setDirectionShouldSetDirectionWithCorrespondingDirection() throws InvalidArgumentException {
    //arrange
    String argument = "right";
    Direction expected = Direction.EAST;

    //act
    sut.setDirection(argument);

    //assert
    assertEquals(expected, sut.direction);
  }

  @Test
  void toInteractionEventShouldReturnInteractionEvent() throws InvalidArgumentException {
    //arrange
    sut.setDirection("right");

    //act
    Event actual = sut.toInteractionEvent("");

    //assert
    assertTrue(actual instanceof AttackEvent);
  }
}
