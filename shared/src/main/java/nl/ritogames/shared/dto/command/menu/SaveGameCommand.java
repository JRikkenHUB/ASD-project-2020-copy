package nl.ritogames.shared.dto.command.menu;

import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.exception.CommandFailedException;
import nl.ritogames.shared.exception.ModificationException;
import nl.ritogames.shared.exception.NotImplementedException;

public class SaveGameCommand extends MenuCommand {

    /**
     * A command called to save the current game to a save file
     */
    public SaveGameCommand() {
        //Standard constructor
    }

    @Override
    public void execute(GameStateModifier gameStateModifier) throws CommandFailedException {
        try {
            gameStateModifier.saveGame();
        } catch (ModificationException e) {
            throw new CommandFailedException("SaveGameCommand failed", e);
        }
    }

    @Override
    public String generateUIText() {
        throw new NotImplementedException();
    }
}
