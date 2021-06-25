package nl.ritogames.commands;

import nl.ritogames.ASDGameStateModifier;
import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.command.menu.SaveGameCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class SaveGameCommandTest {

    private SaveGameCommand sut;
    private GameStateModifier mockedGamestateModifier;

    @BeforeEach
    void setUp() {
        sut = new SaveGameCommand();
        mockedGamestateModifier = mock(ASDGameStateModifier.class);
    }

    @Test
    void testExecute() throws Exception {
        // Setup
        sut.execute(mockedGamestateModifier);
        // Run the test
        verify(mockedGamestateModifier).saveGame();

        // Verify the results
    }
}
