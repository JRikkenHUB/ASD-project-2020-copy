package nl.ritogames.intelligentagent;

import nl.ritogames.shared.AgentService;
import nl.ritogames.shared.EventProcessor;
import nl.ritogames.shared.GameStateContextProvider;
import nl.ritogames.shared.dto.gameobject.individual.Individual;
import nl.ritogames.shared.exception.AgentBuilderException;
import nl.ritogames.shared.logger.Logger;

import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.*;

/**
 * A thread pooling service that manages threads for all agents in the game.
 */
public class ASDAgentService implements AgentService {

    private static final int THREAD_COUNT = 10;
    private int updateInterval = 5000;
    private static final int TIME_BETWEEN_AGENT_UPDATES = 50;

    private final Map<String, AgentRunner> characterAgents;
    private final Map<String, Future<?>> runningAgents;
    private EventProcessor eventProcessor;
    private GameStateContextProvider contextProvider;
    private AgentBuilder agentBuilder;
    private ScheduledExecutorService executorService;

    @Inject
    public ASDAgentService(
            EventProcessor eventProcessor,
            GameStateContextProvider contextProvider,
            AgentBuilder agentBuilder) {
        characterAgents = new HashMap<>();
        runningAgents = new HashMap<>();
        this.setEventHandler(eventProcessor);
        this.setContextProvider(contextProvider);
        this.setAgentBuilder(agentBuilder);
        this.setExecutorService(Executors.newScheduledThreadPool(THREAD_COUNT));
    }

    @Override
    public void buildAgent(String agentJSON, Individual individual) throws AgentBuilderException {
        if (characterAgents.containsKey(individual.getIndividualID())) return;

        Agent agent;
        try {
            agent = agentBuilder.build(agentJSON, individual);
        } catch (AgentBuilderException e) {
            agentBuilder = new AgentBuilder();
            throw e;
        }

        agent.setEventProcessor(eventProcessor);
        agent.setContextProvider(contextProvider);
        AgentRunner runner = new AgentRunner(agent);
        characterAgents.put(individual.getIndividualID(), runner);

        agentBuilder = new AgentBuilder();
    }

    @Override
    public void startAgent(Individual individual) {
      Logger.logMethodCall(this);
        AgentRunner runner = characterAgents.get(individual.getIndividualID());
        ScheduledFuture<?> future = executorService.scheduleWithFixedDelay(runner,
            (long) characterAgents.size() * TIME_BETWEEN_AGENT_UPDATES, updateInterval, TimeUnit.MILLISECONDS);
        runningAgents.put(individual.getIndividualID(), future);
    }

    @Override
    public void stopAgent(Individual individual) {
      Logger.logMethodCall(this);
        Future<?> future = runningAgents.remove(individual.getIndividualID());
        future.cancel(true);
    }

    @Override
    public boolean isAgentActive(Individual individual) {
        return runningAgents.containsKey(individual.getIndividualID());
    }

    @Inject
    public void setEventHandler(EventProcessor eventProcessor) {
        this.eventProcessor = eventProcessor;
    }

    @Inject
    public void setContextProvider(GameStateContextProvider contextProvider) {
        this.contextProvider = contextProvider;
    }

    @Inject
    public void setAgentBuilder(AgentBuilder agentBuilder) {
        this.agentBuilder = agentBuilder;
    }

    public void setExecutorService(ScheduledExecutorService executorService) {
        this.executorService = executorService;
    }

    public void setUpdateInterval(int updateInterval) {
        this.updateInterval = updateInterval;
    }

    Map<String, AgentRunner> getCharacterAgents() {
        return this.characterAgents;
    }
}
