//package nl.ritogames;
//
//import nl.ritogames.shared.GameStateModifier;
//import nl.ritogames.shared.dto.command.Command;
//import nl.ritogames.shared.exception.CommandFailedException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.verify;
//
//class ASDCommandExecutorTest {
//    private ASDCommandExecutor sut;
//    private Command mockedCommand;
//    private GameStateModifier mockedGameStateModifier;
//    private ASDGameStateContextProvider mockedASDGameStateContextProvider;
//
//    @BeforeEach
//    void setUp() {
//        mockedCommand = mock(Command.class);
//        mockedGameStateModifier = mock(ASDGameStateModifier.class);
//        mockedASDGameStateContextProvider = mock(ASDGameStateContextProvider.class);
//        sut = new ASDCommandExecutor();
//        sut.setGameStateModifier(mockedGameStateModifier);
//        sut.setContextProvider(mockedASDGameStateContextProvider);
//    }
//
//    @Test
//    void testExecuteCommand() throws CommandFailedException {
//        sut.executeCommand(mockedCommand);
//
//        verify(mockedCommand).execute(mockedGameStateModifier);
//        verify(mockedASDGameStateContextProvider).processCommand(mockedCommand);
//    }
//}
