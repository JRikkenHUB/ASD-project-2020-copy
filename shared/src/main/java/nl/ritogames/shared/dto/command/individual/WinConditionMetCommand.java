package nl.ritogames.shared.dto.command.individual;

import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.exception.CommandFailedException;

public class WinConditionMetCommand extends IndividualCommand {

    /**
     * A command called when the win condintions of the game are met
     * @param initiatingIndividualId
     */
    public WinConditionMetCommand(String initiatingIndividualId) {
        super(initiatingIndividualId);
    }

    public WinConditionMetCommand() {
    }

    @Override
    public void execute(GameStateModifier gameStateModifier) throws CommandFailedException {

    }

    @Override
    public String generateUIText() {
        return String.format("Game over, %s won the game", initiatingIndividualId);
    }
}
