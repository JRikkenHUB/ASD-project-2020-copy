package nl.ritogames.shared.dto.command.individual;

import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.exception.CommandFailedException;
import nl.ritogames.shared.exception.ModificationException;
import nl.ritogames.shared.exception.NotImplementedException;

public class StopAgentCommand extends RelevantForOwnContext {

    public StopAgentCommand() {
        super();
    }

    public StopAgentCommand(String individualId) {
        super(individualId);
    }

    @Override
    public void execute(GameStateModifier gameStateModifier) throws CommandFailedException {
        try {
            gameStateModifier.stopAgent(getInitiatingIndividualId());
        } catch (ModificationException e) {
            throw new CommandFailedException("StopAgentCommand failed.", e);
        }
    }

    @Override
    public String generateUIText() {
        throw new NotImplementedException();
    }
}
