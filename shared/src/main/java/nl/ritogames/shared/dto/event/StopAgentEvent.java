package nl.ritogames.shared.dto.event;

public class StopAgentEvent extends GameEvent {
    public StopAgentEvent() {
        super();
    }

    public StopAgentEvent(String individualId) {
        super(individualId);
    }
}
