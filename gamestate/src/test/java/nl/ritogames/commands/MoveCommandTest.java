package nl.ritogames.commands;

import nl.ritogames.ASDGameStateModifier;
import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.command.individual.MoveCommand;
import nl.ritogames.shared.enums.Direction;
import org.junit.jupiter.api.BeforeEach;

import static org.mockito.Mockito.mock;

class MoveCommandTest {
    private MoveCommand sut;
    private GameStateModifier mockedGamestateModifier;
    private final String UUID = "UUID";
    private final Direction direction = Direction.NORTH;

    @BeforeEach
    void setUp() {
        mockedGamestateModifier = mock(ASDGameStateModifier.class);
        sut = new MoveCommand(UUID, direction);
    }

//    @Test
//    void testExecute() throws CommandFailedException, ModificationException {
//        // Act
//        sut.execute(mockedGamestateModifier);
//
//        // Assert
//        verify(mockedGamestateModifier).moveIndividual(UUID, direction);
//    }
}
