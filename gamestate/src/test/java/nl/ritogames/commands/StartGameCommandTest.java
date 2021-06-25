package nl.ritogames.commands;

import nl.ritogames.ASDGameStateModifier;
import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.GameState;
import nl.ritogames.shared.dto.command.menu.StartGameCommand;
import nl.ritogames.shared.enums.GameStatus;
import nl.ritogames.shared.exception.AbsentEntityException;
import nl.ritogames.shared.exception.CommandFailedException;
import nl.ritogames.shared.exception.ModificationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class StartGameCommandTest {

    private StartGameCommand sut;
    private GameStateModifier mockedGamestateModifier;

    @BeforeEach
    void setUp() {
        sut = new StartGameCommand(30, 0, new GameState());
        mockedGamestateModifier = mock(ASDGameStateModifier.class);
    }

    @Test
    void testExecute() throws CommandFailedException, ModificationException, AbsentEntityException, IOException {
        // Setup
        sut.getGameState().setStatus(GameStatus.CREATED);

        // Run the test
        sut.execute(mockedGamestateModifier);

        // Verify the results
        verify(mockedGamestateModifier).initGame(Mockito.anyInt(), Mockito.anyInt());
        verify(mockedGamestateModifier).startGame();
    }
}
