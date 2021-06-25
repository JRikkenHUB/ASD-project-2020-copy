package nl.ritogames.shared.dto.event;

public class StopGameEvent extends InteractionEvent {

    /**
     * Event used for when a player tries to stop a game
     */
    public StopGameEvent() {
        //Standard constructor
    }

    public StopGameEvent(String individualId, String gameName) {
        super(individualId, gameName);
    }
}
