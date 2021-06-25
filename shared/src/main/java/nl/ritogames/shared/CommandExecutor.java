package nl.ritogames.shared;


import nl.ritogames.shared.dto.command.Command;
import nl.ritogames.shared.exception.AgentBuilderException;
import nl.ritogames.shared.exception.CommandFailedException;
import nl.ritogames.shared.exception.ModificationException;
import nl.ritogames.shared.exception.PlayerHasDiedException;

public interface CommandExecutor {
    /**
     * Executes a given command.
     *
     * @param command The command to be executed
     * @throws CommandFailedException Thrown when the command cannot be executed.
     */
    void executeCommand(Command command) throws CommandFailedException, ModificationException, AgentBuilderException, PlayerHasDiedException;
}
