package nl.ritogames.shared.dto.command.individual;

import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.exception.CommandFailedException;
import nl.ritogames.shared.exception.ModificationException;

public class WinCommand extends IndividualCommand {

    /**
     * A command used when the game is won.
     * @param initiatingIndividualId
     */
    public WinCommand(String initiatingIndividualId) {
        super(initiatingIndividualId);
    }
    
    public WinCommand() {
        //Standard constructor
    }
    
    @Override
    public void execute(GameStateModifier gameStateModifier) throws CommandFailedException {
        try {
            gameStateModifier.endGame();
        } catch (ModificationException e) {
            throw new CommandFailedException("WinCommand failed.", e);
        }
    }

    @Override
    public String generateUIText() {
        return String.format("%S won the game!", initiatingIndividualId);
    }
}
