package nl.ritogames.ruleengine.rules.winning;

import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.GameState;
import nl.ritogames.shared.dto.event.InteractionEvent;
import nl.ritogames.shared.dto.event.StartAgentEvent;
import nl.ritogames.shared.dto.event.WinConditionMetEvent;
import nl.ritogames.shared.dto.gameobject.individual.Character;
import nl.ritogames.shared.dto.gameobject.individual.Individual;
import nl.ritogames.shared.dto.gameobject.individual.Monster;
import nl.ritogames.shared.enums.GameStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WinningConditionsMetTest {

    private WinningConditionsMet sut;
    private GameState gameState;

    @BeforeEach
    void setUp() {
        GameStateModifier mockModifier = mock(GameStateModifier.class);
        sut = new WinningConditionsMet();
        sut.setModifier(mockModifier);
        gameState = mock(GameState.class);
        when(mockModifier.getGameState()).thenReturn(gameState);
    }

    @Test
    void testIsMetReturnsTrue() {
        // Setup
        HashMap<String, Individual> individuals = new HashMap<>();
        individuals.put("P1", new Character());
        individuals.put("Monster1", new Monster());
        when(gameState.getIndividuals()).thenReturn(individuals);
        when(gameState.getStatus()).thenReturn(GameStatus.IN_PROGRESS);
        // Run the test
        final boolean result = sut.isMet();

        // Verify the results
        assertTrue(result);
    }

    @Test
    void testIsMetReturnsFalse() {
        // Setup
        HashMap<String, Individual> individuals = new HashMap<>();
        individuals.put("P1", new Character());
        individuals.put("P2", new Character());
        individuals.put("Monster1", new Monster());
        when(gameState.getIndividuals()).thenReturn(individuals);
        when(gameState.getStatus()).thenReturn(GameStatus.IN_PROGRESS);
        // Run the test
        final boolean result = sut.isMet();

        // Verify the results
        assertFalse(result);
    }

    @Test
    void testIsMetReturnsFalseDueToStatus() {
        // Setup
        HashMap<String, Individual> individuals = new HashMap<>();
        individuals.put("P1", new Character());
        individuals.put("P2", new Character());
        individuals.put("Monster1", new Monster());
        when(gameState.getIndividuals()).thenReturn(individuals);
        when(gameState.getStatus()).thenReturn(GameStatus.CREATED);
        // Run the test
        final boolean result = sut.isMet();

        // Verify the results
        assertFalse(result);
    }

    @Test
    void testGetEventReturnsWinningConditionsMetEvent() {
        // Setup
        final InteractionEvent event = new StartAgentEvent("P1");
        final InteractionEvent expectedResult = new WinConditionMetEvent("P1", "ThisGame");

        // Run the test
        final InteractionEvent result = sut.getEvent(event);

        // Verify the results
        assertEquals(expectedResult.getIndividualId(), result.getIndividualId());
    }
}
