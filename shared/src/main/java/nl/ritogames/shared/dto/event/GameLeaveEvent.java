package nl.ritogames.shared.dto.event;

public class GameLeaveEvent extends LobbyEvent {
    public GameLeaveEvent() {
        super();
    }

    public GameLeaveEvent(String individualId) {
        super(individualId);
    }
}
