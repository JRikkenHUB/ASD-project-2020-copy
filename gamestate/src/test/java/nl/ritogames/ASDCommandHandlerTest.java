package nl.ritogames;

import nl.ritogames.shared.CommandSender;
import nl.ritogames.shared.dto.command.Command;
import nl.ritogames.shared.dto.command.individual.MoveCommand;
import nl.ritogames.shared.dto.event.GameJoinEvent;
import nl.ritogames.shared.dto.event.InteractionEvent;
import nl.ritogames.shared.enums.Direction;
import nl.ritogames.shared.exception.CommandFailedException;
import nl.ritogames.shared.exception.CommandNotGeneratedException;
import nl.ritogames.shared.exception.EventFailedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


class ASDCommandHandlerTest {

    private ASDCommandHandler sut;
    final InteractionEvent event = new GameJoinEvent("user", "ip");
    private CommandSender mockedCommandSender;

    @BeforeEach
    void setUp() {
        mockedCommandSender = mock(CommandSender.class);
        sut = new ASDCommandHandler();
        sut.setCommandSender(mockedCommandSender);
    }


    @Test
    void testGenerateCommand() throws EventFailedException {
        Command command = new MoveCommand("UUID", Direction.SOUTH);
        try (MockedStatic<CommandFactory> staticFactory = mockStatic(CommandFactory.class)) {
            //arrange
            staticFactory.when(() -> CommandFactory.produceCommand(event)).thenReturn(command);
            //act
            sut.handleEventIntoCommand(event);
            //assert
            staticFactory.verify(times(1), () -> CommandFactory.produceCommand(event));
            verify(mockedCommandSender).sendCommand(command);
        }
    }

    @Test
    void testCommandNotGeneratedThrowsEvenFailedException() throws EventFailedException, CommandFailedException {
        InteractionEvent interactionEvent = mock(InteractionEvent.class);

        try (MockedStatic<CommandFactory> staticFactory = mockStatic(CommandFactory.class)) {
            staticFactory.when(() -> CommandFactory.produceCommand(event)).thenThrow(mock(CommandNotGeneratedException.class));
            assertThrows(EventFailedException.class, () -> sut.handleEventIntoCommand(event));
        }
    }
}
