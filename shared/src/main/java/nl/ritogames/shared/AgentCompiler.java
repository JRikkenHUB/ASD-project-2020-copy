package nl.ritogames.shared;

import nl.ritogames.shared.exception.AgentAlreadyExistsException;
import nl.ritogames.shared.exception.AgentNotFoundException;
import nl.ritogames.shared.exception.CompilationException;

import java.io.IOException;
import java.util.List;

public interface AgentCompiler {

    /**
     * Compile the agent with the given name.
     * The compiled agent is saved into the data/agents folder as a .json file.
     * Throws a CompilationException if the source code does not compile. This exception provides
     * a list of compilation errors.
     * @param agentName The name of the agent to be compiled.
     */
    void compile(String agentName) throws CompilationException;

    /**
     * Get the compiled active agent in JSON format.
     */
    String getActiveAgent();

    /**
     * Set the current active agent. This is the agent that will be sent to the host.
     */
    void setActiveAgent(String agentName) throws AgentNotFoundException;

    /**
     * Get the names of all agents in the agent directory.
     */
    List<String> getAgents();

    /**
     * Create an empty agent file for the agent with the given name.
     */
    void createAgent(String name)
        throws AgentAlreadyExistsException, IOException;

    /**
     * Completely delete the agent with the given name. This is irreversible.
     * Does nothing when the agent does not exist.
     */
    void deleteAgent(String name);
}
