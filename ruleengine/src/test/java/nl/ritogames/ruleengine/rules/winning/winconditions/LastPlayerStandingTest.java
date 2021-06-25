package nl.ritogames.ruleengine.rules.winning.winconditions;

import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.GameState;
import nl.ritogames.shared.dto.gameobject.individual.Character;
import nl.ritogames.shared.dto.gameobject.individual.Individual;
import nl.ritogames.shared.dto.gameobject.individual.Monster;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LastPlayerStandingTest {

    private GameState gameState;
    private LastPlayerStanding sut;

    @BeforeEach
    void setUp() {
        GameStateModifier mockModifier = mock(GameStateModifier.class);
        sut = new LastPlayerStanding(mockModifier);
        sut.setModifier(mockModifier);
        gameState = mock(GameState.class);
        when(mockModifier.getGameState()).thenReturn(gameState);
    }

    @Test
    void testIsMetGameWon() {
        // Setup
        HashMap<String, Individual> individuals = new HashMap<>();
        individuals.put("P1", new Character());
        individuals.put("Monster1", new Monster());
        when(gameState.getIndividuals()).thenReturn(individuals);
        // Run the test
        final boolean result = sut.isMet();

        // Verify the results
        assertTrue(result);
    }

    @Test
    void testIsMetGameNotWon() {
        // Setup
        HashMap<String, Individual> individuals = new HashMap<>();
        individuals.put("P1", new Character());
        individuals.put("P2", new Character());
        individuals.put("Monster1", new Monster());
        when(gameState.getIndividuals()).thenReturn(individuals);
        // Run the test
        final boolean result = sut.isMet();

        // Verify the results
        assertFalse(result);
    }
}
