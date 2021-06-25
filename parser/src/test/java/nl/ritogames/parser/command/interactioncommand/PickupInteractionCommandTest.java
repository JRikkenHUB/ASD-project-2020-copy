package nl.ritogames.parser.command.interactioncommand;

import nl.ritogames.shared.dto.event.PickUpEvent;
import nl.ritogames.shared.enums.Direction;
import nl.ritogames.shared.exception.InvalidArgumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static junit.framework.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PickupInteractionCommandTest {

  private PickupInteractionCommand sut;

  @BeforeEach
  void setUp() {
    this.sut = new PickupInteractionCommand();
  }

  @Test
  void setDirectionShouldThrowInvalidArgumentExceptionWhenCalledWithInvalidArgument() {
    //arrange
    String argument = "links";
    var expected = InvalidArgumentException.class;

    //act + assert
    assertThrows(expected, () -> {
      sut.setDirection(argument);
    });
  }

  @Test
  void setDirectionShouldSetDirectionWithCorrespondingDirection() throws InvalidArgumentException {
    //arrange
    String argument = "left";
    Direction expected = Direction.WEST;

    //act
    sut.setDirection(argument);

    //assert
    assertEquals(expected, sut.direction);
  }

  @Test
  void toInteractionEventShouldReturnInteractionEvent() throws InvalidArgumentException {
    //arrange
    sut.direction = Direction.WEST;

    //act
    var actual = sut.toInteractionEvent("");

    //assert
    assertTrue(actual instanceof PickUpEvent);
  }
}
