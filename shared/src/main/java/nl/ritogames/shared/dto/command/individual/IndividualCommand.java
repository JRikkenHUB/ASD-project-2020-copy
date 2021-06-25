package nl.ritogames.shared.dto.command.individual;

import nl.ritogames.shared.dto.command.Command;
import nl.ritogames.shared.dto.command.RelevantForContext;

public abstract class IndividualCommand implements Command, RelevantForContext {
    protected String initiatingIndividualId;

    /**
     * A command relevant for an individual
     *
     * @param initiatingIndividualId The individual the command is relevant for.
     */
    protected IndividualCommand(String initiatingIndividualId) {
        this.initiatingIndividualId = initiatingIndividualId;
    }

    protected IndividualCommand() {
    }

    public String getInitiatingIndividualId() {
        return initiatingIndividualId;
    }

    public void setInitiatingIndividualId(String initiatingIndividualId) {
        this.initiatingIndividualId = initiatingIndividualId;
    }
}
