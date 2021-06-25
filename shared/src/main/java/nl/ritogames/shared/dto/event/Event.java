package nl.ritogames.shared.dto.event;

import nl.ritogames.shared.dto.Packageable;

import java.util.Objects;

public abstract class Event implements Packageable {
    protected long timeStamp;
    protected String individualId;
    protected String gameName;

    protected Event() {
    }

    /**
     * Generic event. An Event is used when any instance (players, monsters, etc) tries to do something in the game.
     * Events are always checked by the RuleEngine to see if they are valid and meet all the conditions.
     * @param individualId
     * @param gameName
     */
    protected Event(String individualId, String gameName){
        this.individualId = individualId;
        this.gameName = gameName;
    }

    protected Event(String individualId){
        this.individualId = individualId;
    }

    public String getIndividualId() {
        return individualId;
    }

    public void setIndividualId(String individualId) {
        this.individualId = individualId;
    }


    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return individualId.equals(event.individualId) && gameName.equals(event.gameName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(individualId, gameName);
    }
}
