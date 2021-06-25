package nl.ritogames.shared.dto.event;



public class StartAgentEvent extends GameEvent {

    /**
     * Event used when attempting to start the AI of an agent
     */
    public StartAgentEvent() {
        super();
    }

    public StartAgentEvent(String individualId) {
        super(individualId);
    }

}
