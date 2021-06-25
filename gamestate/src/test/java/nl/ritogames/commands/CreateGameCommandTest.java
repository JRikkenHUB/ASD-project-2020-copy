package nl.ritogames.commands;

import nl.ritogames.ASDGameStateModifier;
import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.command.individual.CreateGameCommand;
import nl.ritogames.shared.exception.CommandFailedException;
import nl.ritogames.shared.exception.ModificationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class CreateGameCommandTest {

    private CreateGameCommand sut;
    private GameStateModifier mockedGamestateModifier;

    @BeforeEach
    void setUp() {
        mockedGamestateModifier = mock(ASDGameStateModifier.class);
        sut = new CreateGameCommand("gameName", "host", "agentName");
    }

    @Test
    void testExecute() throws CommandFailedException, ModificationException, IOException {
        // Setup

        // Run the test
        sut.execute(mockedGamestateModifier);

        // Verify the results
        verify(mockedGamestateModifier).createGame(Mockito.anyString(), eq("host"), anyString());
    }
}
