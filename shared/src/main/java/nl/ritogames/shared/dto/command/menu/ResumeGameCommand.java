package nl.ritogames.shared.dto.command.menu;

import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.exception.CommandFailedException;
import nl.ritogames.shared.exception.ModificationException;
import nl.ritogames.shared.exception.NotImplementedException;

public class ResumeGameCommand extends MenuCommand {
    private String gameName;

    /**
     * Command used to resume a paused game
     * @param gameName
     */
    public ResumeGameCommand(String gameName) {
        this.gameName = gameName;
    }

    public ResumeGameCommand() {
    }

    @Override
    public void execute(GameStateModifier gameStateModifier) throws CommandFailedException {
        try {
            gameStateModifier.resumeGame(gameName);
        } catch (ModificationException e) {
            throw new CommandFailedException("ResumeGameCommand failed", e);
        }
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }


    @Override
    public String generateUIText() {
        throw new NotImplementedException();
    }
}
