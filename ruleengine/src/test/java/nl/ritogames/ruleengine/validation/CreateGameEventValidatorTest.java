package nl.ritogames.ruleengine.validation;

import nl.ritogames.ASDGameStateModifier;
import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.event.CreateGameEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static nl.ritogames.ruleengine.ASDEventProcessorTest.GAME;
import static nl.ritogames.ruleengine.ASDEventProcessorTest.INDIVIDUAL_ID;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class CreateGameEventValidatorTest {

    private CreateEventValidator sut;
    private GameStateModifier mockedModifier;

    @BeforeEach
    void setUp() {
        sut = new CreateEventValidator();
        mockedModifier = mock(ASDGameStateModifier.class);
    }

    @Test
    void testValidateEvent() {
        // Setup
        final CreateGameEvent event = new CreateGameEvent(INDIVIDUAL_ID, GAME);

        // Run the test
        final boolean result = sut.validateEvent(event, mockedModifier);

        // Verify the results
        assertTrue(result);
    }
}
