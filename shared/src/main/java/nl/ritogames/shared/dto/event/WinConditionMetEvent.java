package nl.ritogames.shared.dto.event;


public class WinConditionMetEvent extends InteractionEvent {

    /**
     * Event used for when the win conditions of a game are met
     */
    public WinConditionMetEvent() {
    }

    public WinConditionMetEvent(String individualId, String gameName) {
        super(individualId, gameName);
    }
}