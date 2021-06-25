package nl.ritogames.shared.dto.command.individual;

public abstract class AttributeCommand extends IndividualCommand {
    private String attributeId;

    /**
     * A abstract command involving an attribute and an individual
     */
    protected AttributeCommand(String initiatingIndividualId, String attributeId) {
        super(initiatingIndividualId);
        this.attributeId = attributeId;
    }

    public String getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(String attributeId) {
        this.attributeId = attributeId;
    }
}
