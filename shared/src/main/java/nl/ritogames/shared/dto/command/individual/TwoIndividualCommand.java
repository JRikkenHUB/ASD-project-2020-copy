package nl.ritogames.shared.dto.command.individual;

public abstract class TwoIndividualCommand extends IndividualCommand {
    private String interactingIndividualId;

    /**
     * A command involving 2 individuals.
     * @param initiatingIndividualId The individual initiating the command, for example the attacker
     * @param interactingIndividualId The individual being interacted with
     */
    protected TwoIndividualCommand(String initiatingIndividualId, String interactingIndividualId) {
        super(initiatingIndividualId);
        this.interactingIndividualId = interactingIndividualId;
    }

    public String getInteractingIndividualId() {
        return interactingIndividualId;
    }

    public void setInteractingIndividualId(String interactingIndividualId) {
        this.interactingIndividualId = interactingIndividualId;
    }
}
