package nl.ritogames.shared.dto.command.individual;

import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.GameState;
import nl.ritogames.shared.exception.CommandFailedException;
import nl.ritogames.shared.exception.ModificationException;

public class JoinGameCommand extends IndividualCommand {

    public JoinGameCommand() {
        super();
    }

    public JoinGameCommand(String initiatingIndividualId) {
        super(initiatingIndividualId);
    }

    @Override
    public void execute(GameStateModifier gameStateModifier) throws CommandFailedException {
        try {
            if (gameStateModifier.getGameState() == null) {
                gameStateModifier.setGameState(new GameState(""));
            }

            gameStateModifier.addCharacter(initiatingIndividualId);
        } catch (ModificationException e) {
            throw new CommandFailedException("JoinGameCommand failed.", e);
        }
    }

    @Override
    public String generateUIText() {
        return null;
    }
}
