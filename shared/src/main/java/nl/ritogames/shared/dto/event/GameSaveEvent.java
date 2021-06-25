package nl.ritogames.shared.dto.event;

import java.util.Objects;

public class GameSaveEvent extends LobbyEvent {
    private Long gameDate;

    public GameSaveEvent(){
        super();
    }

    public GameSaveEvent(String individualId) {
        super(individualId);
    }

    public GameSaveEvent(String individualId, String gameName) {
        super(individualId, gameName);
    }

    /**
     * Event used when a player tries to save the current game to a save file
     * @param individualId
     * @param gameDate
     */
    public GameSaveEvent(String individualId, Long gameDate) {
        super(individualId);
        this.gameDate = gameDate;
    }


    public Long getGameDate() {
        return gameDate;
    }

    public void setGameDate(Long gameDate) {
        this.gameDate = gameDate;
    }


    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(),  gameDate);
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public String toString() {
        return "GameSaveEvent{" +
                ", GameDate=" + gameDate +
                '}';
    }
}