package nl.ritogames.commands;

import nl.ritogames.ASDGameStateModifier;
import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.command.individual.StartAgentCommand;
import nl.ritogames.shared.exception.AgentBuilderException;
import nl.ritogames.shared.exception.CommandFailedException;
import nl.ritogames.shared.exception.ModificationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class StartAgentCommandTest {

    private StartAgentCommand sut;
    private GameStateModifier mockedGamestateModifier;

    @BeforeEach
    void setUp() {
        sut = new StartAgentCommand("individualId");
        mockedGamestateModifier = mock(ASDGameStateModifier.class);
    }

    @Test
    void testExecute() throws CommandFailedException, ModificationException, AgentBuilderException {
        // Setup

        // Run the test
        sut.execute(mockedGamestateModifier);

        // Verify the results
        verify(mockedGamestateModifier).startAgent(Mockito.anyString());
    }
}
