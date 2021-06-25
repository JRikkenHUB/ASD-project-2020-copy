package nl.ritogames.shared.dto.command.individual;

import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.exception.CommandFailedException;
import nl.ritogames.shared.exception.NotImplementedException;

public class DeathCommand extends IndividualCommand {
    /**
     * A command created if a player dies so that it is displayed in the TRUI
     *
     * @param initiatingIndividualId
     */
    public DeathCommand(String initiatingIndividualId) {
        super(initiatingIndividualId);
    }

    @Override
    public void execute(GameStateModifier gameStateModifier) throws CommandFailedException {
        
    }

    @Override
    public String generateUIText() {
        throw new NotImplementedException();
    }
}
