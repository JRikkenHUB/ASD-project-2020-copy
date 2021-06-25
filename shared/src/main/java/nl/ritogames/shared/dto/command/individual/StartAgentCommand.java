package nl.ritogames.shared.dto.command.individual;

import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.exception.AgentBuilderException;
import nl.ritogames.shared.exception.CommandFailedException;
import nl.ritogames.shared.exception.ModificationException;
import nl.ritogames.shared.exception.NotImplementedException;

public class StartAgentCommand extends RelevantForOwnContext {

    public StartAgentCommand() {
        super();
    }

    /**
     * Command used to activate the AI of an individual
     * @param individualId
     */
    public StartAgentCommand(String individualId) {
        super(individualId);
    }

    @Override
    public void execute(GameStateModifier gameStateModifier) throws CommandFailedException {
        try {
            gameStateModifier.startAgent(getInitiatingIndividualId());
        } catch (ModificationException | AgentBuilderException e) {
            throw new CommandFailedException("StartAgentCommand failed.", e);
        }
    }

    @Override
    public String generateUIText() {
        throw new NotImplementedException();
    }
}
