package nl.ritogames.ruleengine;

import nl.ritogames.ASDGameStateModifier;
import nl.ritogames.ruleengine.rules.winning.WinningConditionsMet;
import nl.ritogames.ruleengine.validation.MoveEventValidator;
import nl.ritogames.ruleengine.validation.ValidationStrategyFactory;
import nl.ritogames.shared.CommandHandler;
import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.event.MoveEvent;
import nl.ritogames.shared.enums.Direction;
import nl.ritogames.shared.exception.AgentBuilderException;
import nl.ritogames.shared.exception.EventFailedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;


public class ASDEventProcessorTest {
    public static final String INDIVIDUAL_ID = "TestUser";
    public static final String GAME = "GameName";

    private ASDEventProcessor sut;
    private CommandHandler mockedCommandHandler;
    private GameStateModifier mockedModifier;
    private MoveEventValidator mockedValidator;
    final MoveEvent event = new MoveEvent(INDIVIDUAL_ID, Direction.SOUTH);

    @BeforeEach
    void setUp() {
        sut = new ASDEventProcessor();
        mockedCommandHandler = mock(CommandHandler.class);
        mockedModifier = mock(ASDGameStateModifier.class);
        WinningConditionsMet mockedWinningConditions = mock(WinningConditionsMet.class);
        sut.setCommandHandler(mockedCommandHandler);
        sut.setModifier(mockedModifier);
        sut.setWinningConditionsMet(mockedWinningConditions);
        mockedValidator = mock(MoveEventValidator.class);
    }

    @Test
    void testHandleEvent() throws EventFailedException {
        try (MockedStatic<ValidationStrategyFactory> staticFactory = mockStatic(ValidationStrategyFactory.class)) {
            //arrange
            staticFactory.when(() -> ValidationStrategyFactory.getStrategy(event)).thenReturn(mockedValidator);
            // Arrange
            when(mockedValidator.validateEvent(event, mockedModifier)).thenReturn(true);
            // Act
            sut.handleEvent(event);
            // Assert
            Mockito.verify(mockedCommandHandler).handleEventIntoCommand(Mockito.any());
        }
    }

    @Test
    void testEventSentTooFastDoesntCallHandler() throws EventFailedException, AgentBuilderException {
        try (MockedStatic<ValidationStrategyFactory> staticFactory = mockStatic(ValidationStrategyFactory.class)) {
            //arrange
            staticFactory.when(() -> ValidationStrategyFactory.getStrategy(event)).thenReturn(mockedValidator);
            // Arrange
            when(mockedValidator.validateEvent(event, mockedModifier)).thenReturn(true);
            // Act
            sut.handleEvent(event);
            sut.handleEvent(event);//call it again too fast
            // Assert
            Mockito.verify(mockedCommandHandler, times(1)).handleEventIntoCommand(Mockito.any());
        }
    }

    @Test
    void testEventSentWithCorrectDelayCallsHandler() throws EventFailedException, InterruptedException {
        try (MockedStatic<ValidationStrategyFactory> staticFactory = mockStatic(ValidationStrategyFactory.class)) {
            //arrange
            staticFactory.when(() -> ValidationStrategyFactory.getStrategy(event)).thenReturn(mockedValidator);
            // Arrange
            when(mockedValidator.validateEvent(event, mockedModifier)).thenReturn(true);
            // Act
            sut.handleEvent(event);
            TimeUnit.SECONDS.sleep(2); // wait for 2 seconds
            sut.handleEvent(event);
            // Assert
            Mockito.verify(mockedCommandHandler, times(2)).handleEventIntoCommand(Mockito.any());
        }
    }
}
