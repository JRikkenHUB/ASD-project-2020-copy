package nl.ritogames.shared.dto.command.individual;

import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.exception.CommandFailedException;
import nl.ritogames.shared.exception.NotImplementedException;

public class AttackCommand extends TwoIndividualCommand {
    /**
     * The attack command is used when one individual attacks another individual
     */
    public AttackCommand(String initiatingIndividualId, String interactingIndividualId) {
        super(initiatingIndividualId, interactingIndividualId);
    }
    
    @Override
    public void execute(GameStateModifier gameStateModifier) throws CommandFailedException {
        throw new NotImplementedException();
    }

    @Override
    public String generateUIText() {
        throw new NotImplementedException();
    }
}
