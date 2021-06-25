package nl.ritogames;

import com.google.inject.Inject;
import nl.ritogames.shared.CommandExecutor;
import nl.ritogames.shared.GameStateContextProvider;
import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.command.Command;
import nl.ritogames.shared.exception.CommandFailedException;
import nl.ritogames.shared.exception.PlayerHasDiedException;
import nl.ritogames.shared.logger.Logger;

public class ASDCommandExecutor implements CommandExecutor {
    private GameStateModifier gameStateModifier;
    private GameStateContextProvider contextProvider;

    /**
     * Executes a given command and adds the command to the gameStateContextProvider.
     *
     * @param command                   The command to be executed
     * @throws CommandFailedException   Thrown when the command failed to execute.
     */
    @Override
    public void executeCommand(Command command) throws CommandFailedException, PlayerHasDiedException {
        Logger.logMethodCall(this);
        command.execute(gameStateModifier);
        contextProvider.processCommand(command);
    }

    @Inject
    public void setGameStateModifier(GameStateModifier gameStateModifier) {
        this.gameStateModifier = gameStateModifier;
    }

    @Inject
    public void setContextProvider(GameStateContextProvider contextProvider) {
        this.contextProvider = contextProvider;
    }
}
