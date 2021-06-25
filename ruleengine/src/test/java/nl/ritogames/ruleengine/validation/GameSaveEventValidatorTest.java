package nl.ritogames.ruleengine.validation;

import nl.ritogames.ASDGameStateModifier;
import nl.ritogames.ruleengine.rules.Rule;
import nl.ritogames.ruleengine.rules.general.GameExists;
import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.event.GameSaveEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static nl.ritogames.ruleengine.ASDEventProcessorTest.INDIVIDUAL_ID;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GameSaveEventValidatorTest {
    private GameSaveEventValidator sut;
    private GameStateModifier mockedModifier;
    private Rule<GameSaveEvent> mockedRule;

    @BeforeEach
    void setUp() {
        sut = new GameSaveEventValidator();
        mockedModifier = mock(ASDGameStateModifier.class);
        mockedRule = mock(GameExists.class);
        ArrayList<Rule<GameSaveEvent>> rules = new ArrayList<>();
        rules.add(mockedRule);
        sut.setRules(rules);
    }

    @Test
    void testValidateEvent() {
        // Setup
        final GameSaveEvent event = new GameSaveEvent(INDIVIDUAL_ID);
        when(mockedRule.validate(event, mockedModifier)).thenReturn(true);

        // Run the test
        final boolean result = sut.validateEvent(event, mockedModifier);

        // Verify the results
        assertTrue(result);
    }


}
