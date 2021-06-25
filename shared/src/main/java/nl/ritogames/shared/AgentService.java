package nl.ritogames.shared;

import nl.ritogames.shared.dto.gameobject.individual.Individual;
import nl.ritogames.shared.exception.AgentBuilderException;

public interface AgentService {

    /**
     * Builds an agent out of a JSON representation and makes it control a character.
     * The agent will not start playing immediately.
     *
     * @param agentJSON The compiled representation of an agent's behavior, in JSON format.
     * @param individual The character the agent will be controlling.
     */
    void buildAgent(String agentJSON, Individual individual) throws AgentBuilderException;

    /**
     * Starts the agent that controls this character. The agent will start responding to updates to
     * the gameState.
     *
     * @param individual The character the agent should be controlling.
     */
    void startAgent(Individual individual);

    /**
     * Stops the agent that controls this character.
     * The agent will stop responding to updates to the gameState.
     *
     * @param individual The character the agent should be controlling.
     */
    void stopAgent(Individual individual);

    /**
     * Checks whether an agent is active at the moment.
     * This can be used to find out whether an event is executed by an agent or by a player.
     *
     * @param individual The individual the agent is controlling.
     */
    boolean isAgentActive(Individual individual);
}
