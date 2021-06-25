package nl.ritogames.intelligentagent;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

class AgentRunnerTest {

    private AgentRunner sut;
    private Agent agentMock;

    @BeforeEach
    void setUp() {
        agentMock = mock(Agent.class);
        sut = new AgentRunner(agentMock);
    }

    @Test
    void agentRunnerShouldUpdateAgentOnThreadStart() throws InterruptedException {
        Thread testThread = new Thread(sut);
        testThread.start();
        verify(agentMock, timeout(20)).update();
    }
}
