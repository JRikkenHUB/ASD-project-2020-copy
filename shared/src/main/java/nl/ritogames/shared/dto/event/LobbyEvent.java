package nl.ritogames.shared.dto.event;


public abstract class LobbyEvent extends InteractionEvent {
    
    protected LobbyEvent() {
        super();
    }

    protected LobbyEvent(String individualId) {
        super(individualId);
    }

    protected LobbyEvent(String individualId, String gameName) {
        super(individualId, gameName);
    }
}
