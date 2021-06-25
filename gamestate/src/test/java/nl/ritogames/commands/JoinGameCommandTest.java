package nl.ritogames.commands;

import nl.ritogames.ASDGameStateModifier;
import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.command.individual.JoinGameCommand;
import nl.ritogames.shared.exception.CommandFailedException;
import nl.ritogames.shared.exception.ModificationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class JoinGameCommandTest {

    private JoinGameCommand sut;
    private GameStateModifier mockedGamestateModifier;

    @BeforeEach
    void setUp() {
        sut = new JoinGameCommand("UUID");
        mockedGamestateModifier = mock(ASDGameStateModifier.class);
    }

    @Test
    void testExecute() throws CommandFailedException, ModificationException {
        // Setup

        // Run the test
        sut.execute(mockedGamestateModifier);

        // Verify the results
        verify(mockedGamestateModifier).addCharacter(Mockito.anyString());
    }
}
