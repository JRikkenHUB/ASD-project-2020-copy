package nl.ritogames.ruleengine.validation;

import nl.ritogames.ASDGameStateModifier;
import nl.ritogames.ruleengine.rules.Rule;
import nl.ritogames.ruleengine.rules.general.SavedGameExists;
import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.event.ResumeGameEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static nl.ritogames.ruleengine.ASDEventProcessorTest.GAME;
import static nl.ritogames.ruleengine.ASDEventProcessorTest.INDIVIDUAL_ID;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ResumeGameEventValidatorTest {

    private ResumeGameEventValidator sut;
    private GameStateModifier mockedModifier;
    private Rule<ResumeGameEvent> mockedRule;

    @BeforeEach
    void setUp() {
        sut = new ResumeGameEventValidator();
        mockedModifier = mock(ASDGameStateModifier.class);
        mockedRule = mock(SavedGameExists.class);
        ArrayList<Rule<ResumeGameEvent>> rules = new ArrayList<>();
        rules.add(mockedRule);
        sut.setRules(rules);
    }

    @Test
    void testValidateEvent() {
        // Setup
        final ResumeGameEvent event = new ResumeGameEvent(INDIVIDUAL_ID, GAME);
        when(mockedRule.validate(event, mockedModifier)).thenReturn(true);
        // Run the test
        final boolean result = sut.validateEvent(event, mockedModifier);

        // Verify the results
        assertTrue(result);
    }
}
