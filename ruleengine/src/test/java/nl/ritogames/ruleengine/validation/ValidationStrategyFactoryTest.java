package nl.ritogames.ruleengine.validation;

import nl.ritogames.shared.dto.GameState;
import nl.ritogames.shared.dto.event.*;
import nl.ritogames.shared.enums.Direction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static nl.ritogames.ruleengine.ASDEventProcessorTest.GAME;
import static nl.ritogames.ruleengine.ASDEventProcessorTest.INDIVIDUAL_ID;

class ValidationStrategyFactoryTest {

    @Test
    void testGetStrategyReturnsCreateValidator() {
        // Setup
        final CreateGameEvent event = new CreateGameEvent(INDIVIDUAL_ID, GAME);

        // Run the test
        final ValidationStrategy<?> result = ValidationStrategyFactory.getStrategy(event);

        // Verify the results
        Assertions.assertEquals(CreateEventValidator.class, result.getClass());
    }

    @Test
    void testGetStrategyReturnsMoveValidator() {
        // Setup
        final MoveEvent event = new MoveEvent(INDIVIDUAL_ID, Direction.SOUTH);

        // Run the test
        final ValidationStrategy<?> result = ValidationStrategyFactory.getStrategy(event);

        // Verify the results
        Assertions.assertEquals(MoveEventValidator.class, result.getClass());
    }

    @Test
    void testGetStrategyReturnsStartValidator() {
        // Setup
        final StartGameEvent event = new StartGameEvent(INDIVIDUAL_ID, GAME, new GameState());

        // Run the test
        final ValidationStrategy<?> result = ValidationStrategyFactory.getStrategy(event);

        // Verify the results
        Assertions.assertEquals(StartGameEventValidator.class, result.getClass());
    }

    @Test
    void testGetStrategyReturnsSaveValidator() {
        // Setup
        final InteractionEvent event = new GameSaveEvent(INDIVIDUAL_ID);

        // Run the test
        final ValidationStrategy<?> result = ValidationStrategyFactory.getStrategy(event);

        // Verify the results
        Assertions.assertEquals(GameSaveEventValidator.class, result.getClass());
    }


    @Test
    void testGetStrategyReturnsStartAgentValidator() {
        // Setup
        final StartAgentEvent event = new StartAgentEvent(INDIVIDUAL_ID);

        // Run the test
        final ValidationStrategy<?> result = ValidationStrategyFactory.getStrategy(event);

        // Verify the results
        Assertions.assertEquals(StartAgentValidator.class, result.getClass());
    }

}
