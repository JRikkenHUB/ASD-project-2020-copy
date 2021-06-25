package nl.ritogames.shared.dto.event;


public abstract class MenuEvent extends InteractionEvent {

    /**
     * An abstract Event used for interactions inside menu screens
     */
    protected MenuEvent() {}

    protected MenuEvent(String individualId) {
        super(individualId);
    }

    protected MenuEvent(String individualId, String gameName) {
        super(individualId, gameName);
    }
}
