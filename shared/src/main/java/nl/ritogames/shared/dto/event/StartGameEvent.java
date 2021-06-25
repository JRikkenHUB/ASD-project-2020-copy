package nl.ritogames.shared.dto.event;


import nl.ritogames.shared.dto.GameState;

import java.util.Objects;

public class StartGameEvent extends LobbyEvent {
    private int tilesPerPlayer;
    private int difficulty;
    private GameState gameState;

    /**
     * Event used for when a player tries to start a game
     */
    public StartGameEvent() {
    }

    public StartGameEvent(String individualId, String gameName, GameState gameState) {
        super(individualId, gameName);
        tilesPerPlayer = 30;
        difficulty = 2;
        this.gameState = gameState;
    }

    public StartGameEvent(String gameName) {
        this.setGameName(gameName);
    }

    public int getTilesPerPlayer() {
        return tilesPerPlayer;
    }

    public void setTilesPerPlayer(int tilesPerPlayer) {
        this.tilesPerPlayer = tilesPerPlayer;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        StartGameEvent that = (StartGameEvent) o;
        return tilesPerPlayer == that.tilesPerPlayer &&
            difficulty == that.difficulty &&
            Objects.equals(gameState, that.gameState);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), tilesPerPlayer, difficulty, gameState);
    }
}
