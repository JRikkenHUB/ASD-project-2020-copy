package nl.ritogames.shared.dto.command.individual;

import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.exception.CommandFailedException;
import nl.ritogames.shared.exception.ModificationException;
import nl.ritogames.shared.exception.NotImplementedException;

import java.io.IOException;


public class CreateGameCommand extends IndividualCommand {
    private String gameName;
    private String agentName;

    public CreateGameCommand() {
    }

    /**
     * A command used to create a game (lobby)
     */
    public CreateGameCommand(String gameName, String initiatingIndividualId, String agentName) {
        super(initiatingIndividualId);
        this.gameName = gameName;
        this.agentName = agentName;
    }
    
    @Override
    public void execute(GameStateModifier gameStateModifier) throws CommandFailedException {
        try {
            gameStateModifier.createGame(gameName, initiatingIndividualId, agentName);
        } catch (ModificationException | IOException e) {
            throw new CommandFailedException("CreateGameCommand failed.", e);
        }
    }

    @Override
    public String generateUIText() {
        throw new NotImplementedException();
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

}
