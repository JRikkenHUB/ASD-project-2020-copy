package nl.ritogames.parser.command.interactioncommand;

import nl.ritogames.shared.dto.event.MoveEvent;
import nl.ritogames.shared.enums.Direction;
import nl.ritogames.shared.exception.InvalidArgumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MoveInteractionCommandTest {

    private MoveInteractionCommand sut;

    @BeforeEach
    void setUp() {
        this.sut = new MoveInteractionCommand();
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
        String user = "";
        Direction direction = Direction.WEST;
        var expected = new MoveEvent(user, direction);
        expected.setGameName("gameName");

        //act
        sut.setDirection("left");

        //assert
        var result = sut.toInteractionEvent("");
        result.setGameName("gameName");
        assertEquals(expected, result);
    }
}
