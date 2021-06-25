package nl.ritogames.ruleengine.rules.general;

import nl.ritogames.ASDGameStateModifier;
import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.event.ResumeGameEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static nl.ritogames.ruleengine.ASDEventProcessorTest.GAME;
import static nl.ritogames.ruleengine.ASDEventProcessorTest.INDIVIDUAL_ID;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SavedGameExistsTest {

    private SavedGameExists<ResumeGameEvent> sut;
    private GameStateModifier mockedModifier;

    @BeforeEach
    void setUp() {
        sut = new SavedGameExists<>();
        mockedModifier = mock(ASDGameStateModifier.class);
    }

    @Test
    void testValidate() {
        // Setup
        final ResumeGameEvent event = new ResumeGameEvent(INDIVIDUAL_ID,GAME);
        when(mockedModifier.saveGameExists(GAME)).thenReturn(true);

        // Run the test
        final boolean result = sut.validate(event, mockedModifier);

        // Verify the results
        assertTrue(result);
    }
}
