package nl.ritogames.shared.dto.command.individual;

import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.exception.CommandFailedException;
import nl.ritogames.shared.exception.ModificationException;

public class StopGameCommand extends IndividualCommand{

    /**
     * Command used to stop the game in progress
     */
    public StopGameCommand(String initiatingIndividualId) {
        super(initiatingIndividualId);
    }
    
    public StopGameCommand() {
        //Standard constructor
    }

    @Override
    public void execute(GameStateModifier gameStateModifier) throws CommandFailedException {
        try {
            gameStateModifier.endGame();
       } catch (ModificationException e) {
            throw new CommandFailedException("Stop game command failed.", e);
        }
    }

    @Override
    public String generateUIText() {
        return null;
    }
}
