package nl.ritogames.shared.dto.command.individual;

public abstract class RelevantForOwnContext extends IndividualCommand {
    protected RelevantForOwnContext(String initiatingIndividualId) {
        super(initiatingIndividualId);
    }

    /**
     * When a command extends from RelevantForOwnContext the command will be added to the commandQueue in the gameStateContextProvider
     * This queue will then be showed in the TRUI. This way all commands that are 'relevant for 
     */
    protected RelevantForOwnContext() {

    }
}
