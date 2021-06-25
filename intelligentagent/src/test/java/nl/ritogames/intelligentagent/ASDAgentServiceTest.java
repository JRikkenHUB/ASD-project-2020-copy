package nl.ritogames.intelligentagent;

import nl.ritogames.shared.EventProcessor;
import nl.ritogames.shared.GameStateContextProvider;
import nl.ritogames.shared.dto.gameobject.individual.Character;
import nl.ritogames.shared.exception.AgentBuilderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class ASDAgentServiceTest {

    private static final int UPDATE_INTERVAL = 500;
    private final AgentBuilder agentBuilderMock = mock(AgentBuilder.class);
    private final EventProcessor eventProcessorMock = mock(EventProcessor.class);
    private final ScheduledExecutorService executorServiceMock = mock(ScheduledExecutorService.class);
    private final GameStateContextProvider contextProviderMock = mock(GameStateContextProvider.class);

    private final String CHARACTER_ID = "5234534";
    private final String agentJSON = "";
    private final Character characterMock = mock(Character.class);
    private final Agent agent = mock(Agent.class);
    private ASDAgentService sut;

    @BeforeEach
    void setUp() {
        sut = new ASDAgentService(eventProcessorMock, contextProviderMock, agentBuilderMock);
        sut.setExecutorService(executorServiceMock);
    }

    @Test
    void buildAgentShouldDelegateToAgentBuilder() throws AgentBuilderException, IOException {
        when(agentBuilderMock.build(agentJSON, characterMock)).thenReturn(agent);

        sut.buildAgent(agentJSON, characterMock);

        verify(agentBuilderMock).build(agentJSON, characterMock);
    }

    @Test
    void startAgentShouldExecuteRunnerDirectly() throws AgentBuilderException, IOException {
        when(agentBuilderMock.build(agentJSON, characterMock)).thenReturn(agent);
        sut.buildAgent(agentJSON, characterMock);

        sut.startAgent(characterMock);

        verify(executorServiceMock).scheduleWithFixedDelay(any(AgentRunner.class), anyLong(), anyLong(), any(TimeUnit.class));
    }

    @Test
    void stopAgentShouldStopRunnerDirectly() throws InterruptedException {
        Character characterMock = mock(Character.class);
        when(characterMock.getIndividualID()).thenReturn(CHARACTER_ID);
        Agent agentMock = mock(Agent.class);
        AgentRunner runner = new AgentRunner(agentMock);
        sut.getCharacterAgents().put(CHARACTER_ID, runner);
        ScheduledExecutorService realExecutor = Executors.newScheduledThreadPool(10);
        sut.setExecutorService(realExecutor);

        sut.startAgent(characterMock);
        sut.stopAgent(characterMock);
        TimeUnit.MILLISECONDS.sleep(UPDATE_INTERVAL + 10);
        verify(agentMock, times(0)).update();
    }

    @Test
    void isAgentActiveShouldReturnTrueIfAgentIsActive() throws AgentBuilderException {
        when(agentBuilderMock.build(agentJSON, characterMock)).thenReturn(agent);
        when(characterMock.getIndividualID()).thenReturn(CHARACTER_ID);
        sut.buildAgent(agentJSON, characterMock);

        sut.startAgent(characterMock);

        boolean result = sut.isAgentActive(characterMock);
        assertTrue(result);
    }

    @Test
    void isAgentActiveShouldReturnFalseIfAgentIsNotActive() throws AgentBuilderException {
        when(agentBuilderMock.build(agentJSON, characterMock)).thenReturn(agent);
        when(characterMock.getIndividualID()).thenReturn(CHARACTER_ID);
        sut.buildAgent(agentJSON, characterMock);

        boolean result = sut.isAgentActive(characterMock);
        assertFalse(result);
    }
}
