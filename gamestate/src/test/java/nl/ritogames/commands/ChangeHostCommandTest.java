package nl.ritogames.commands;

import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.GameState;
import nl.ritogames.shared.dto.command.host.ChangeHostCommand;
import nl.ritogames.shared.dto.gameobject.individual.Character;
import nl.ritogames.shared.exception.CommandFailedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class ChangeHostCommandTest {
    private final String oldHostId = "oldHost";
    private final String newHostId = "newHost";
    private ChangeHostCommand sut;
    private GameStateModifier mockedGameStateModifier;
    private GameState gameState;

    @BeforeEach
    void setUp() {
        sut = new ChangeHostCommand(newHostId);
        mockedGameStateModifier = Mockito.mock(GameStateModifier.class);

        Character oldHost = new Character();
        oldHost.setIndividualID(oldHostId);

        Character newHost = new Character();
        oldHost.setIndividualID(newHostId);

        gameState = new GameState("gameName");
        gameState.setHostIndividualId(oldHostId);
        gameState.setIndividuals(new HashMap<>() {{
            put(oldHostId, oldHost);
            put(newHostId, newHost);
        }});

        when(mockedGameStateModifier.getGameState()).thenReturn(gameState);
    }

    @Test
    void setupWasCorrect() {
        assertEquals(oldHostId, mockedGameStateModifier.getGameState().getHostIndividualId());
    }

    @Test
    void executeChangesHostToNewHost() throws CommandFailedException {
        sut.execute(mockedGameStateModifier);

        assertEquals(newHostId, mockedGameStateModifier.getGameState().getHostIndividualId());
    }
}
