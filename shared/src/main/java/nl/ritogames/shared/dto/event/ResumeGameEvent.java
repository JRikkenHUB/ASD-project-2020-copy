package nl.ritogames.shared.dto.event;


public class ResumeGameEvent extends MenuEvent {

    /**
     * Event used for when a player tries to resume a paused game
     */
    public ResumeGameEvent() {
        super();
    }

    public ResumeGameEvent(String individualId, String gameName) {
        super(individualId, gameName);
    }
}
