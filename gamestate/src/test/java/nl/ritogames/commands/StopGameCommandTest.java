package nl.ritogames.commands;

import nl.ritogames.ASDGameStateModifier;
import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.command.individual.StopGameCommand;
import nl.ritogames.shared.exception.CommandFailedException;
import nl.ritogames.shared.exception.ModificationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class StopGameCommandTest {
    private StopGameCommand sut;
    private GameStateModifier mockedGamestateModifier;
    private String individual = "HOST";
    @BeforeEach
    void setUp() {
        sut = new StopGameCommand(individual);
        mockedGamestateModifier = mock(ASDGameStateModifier.class);
    }

    @Test
    void testExecute() throws CommandFailedException, ModificationException {
        // Setup

        // Run the test
        sut.execute(mockedGamestateModifier);

        // Verify the results
        verify(mockedGamestateModifier).endGame();
    }
}
