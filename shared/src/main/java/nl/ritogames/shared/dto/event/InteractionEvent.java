package nl.ritogames.shared.dto.event;


public abstract class InteractionEvent extends Event {

    /**
     * Event used for when an individual tries to interact with something
     */
    protected InteractionEvent() {
        super();
    }

    protected InteractionEvent(String individualId, String gameName) {
        super(individualId, gameName);
    }

    protected InteractionEvent(String individualId) {
        super(individualId);
    }
}
