package nl.ritogames;

import nl.ritogames.shared.dto.GameState;
import nl.ritogames.shared.dto.command.Command;
import nl.ritogames.shared.dto.command.individual.*;
import nl.ritogames.shared.dto.command.menu.StartGameCommand;
import nl.ritogames.shared.dto.event.*;
import nl.ritogames.shared.enums.Direction;
import nl.ritogames.shared.exception.CommandNotGeneratedException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class CommandFactoryTest {

    public static final String USER = "user";
    public static final String GAME = "game";

    @Test
    void testProduceCommandReturnsMoveCommand() throws Exception {
        // Setup
        final InteractionEvent event = new MoveEvent(USER, Direction.SOUTH);

        // Run the test
        final Command result = CommandFactory.produceCommand(event);

        // Verify the results
        assertEquals(MoveCommand.class, result.getClass());
    }

    @Test
    void testProduceCommandReturnsJoinCommand() throws Exception {
        // Setup
        final InteractionEvent event = new GameJoinEvent(USER, GAME);

        // Run the test
        final Command result = CommandFactory.produceCommand(event);

        // Verify the results
        assertEquals(JoinGameCommand.class, result.getClass());
    }

    @Test
    void testProduceCommandReturnsCreateCommand() throws Exception {
        // Setup
        final InteractionEvent event = new CreateGameEvent(USER, GAME);

        // Run the test
        final Command result = CommandFactory.produceCommand(event);

        // Verify the results
        assertEquals(CreateGameCommand.class, result.getClass());
    }

    @Test
    void testProduceCommandReturnsStartGameCommand() throws Exception {
        // Setup
        final InteractionEvent event = new StartGameEvent(USER, GAME, new GameState());

        // Run the test
        final Command result = CommandFactory.produceCommand(event);

        // Verify the results
        assertEquals(StartGameCommand.class, result.getClass());
    }

    @Test
    void testProduceCommandReturnsStartAgentCommand() throws Exception {
        // Setup
        final InteractionEvent event = new StartAgentEvent(USER);

        // Run the test
        final Command result = CommandFactory.produceCommand(event);

        // Verify the results
        assertEquals(StartAgentCommand.class, result.getClass());
    }

    @Test
    void testProduceCommandReturnsStopAgentCommand() throws Exception {
        // Setup
        final InteractionEvent event = new StopAgentEvent(USER);

        // Run the test
        final Command result = CommandFactory.produceCommand(event);

        // Verify the results
        assertEquals(StopAgentCommand.class, result.getClass());
    }


    @Test
    void generateGenericEventThrowsError() throws Exception {
        // Setup
        final InteractionEvent event = mock(InteractionEvent.class);

        // Run the test
        assertThrows(CommandNotGeneratedException.class,() -> {
            CommandFactory.produceCommand(event);
        });
    }
}
