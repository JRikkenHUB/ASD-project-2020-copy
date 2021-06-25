package nl.ritogames.shared.dto.command;

import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.Packageable;
import nl.ritogames.shared.exception.CommandFailedException;
import nl.ritogames.shared.exception.PlayerHasDiedException;

public interface Command extends Packageable {
    /**
     * Execute this command.
     *
     * @param gameStateModifier The GameStateModifier used to execute the command on.
     * @throws CommandFailedException Thrown when the command cannot be executed.
     */
     void execute(GameStateModifier gameStateModifier) throws CommandFailedException, PlayerHasDiedException;

}
