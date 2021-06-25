package nl.ritogames.shared.dto.command.menu;

import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.GameState;
import nl.ritogames.shared.enums.GameStatus;
import nl.ritogames.shared.exception.CommandFailedException;
import nl.ritogames.shared.exception.ModificationException;
import nl.ritogames.shared.exception.NotImplementedException;

import java.io.IOException;

public class StartGameCommand extends MenuCommand {
    private GameState gameState;
    private int tilesPerPlayer;
    private int difficulty;

    /**
     * Command used to start the game and generate the world so that players can play on it
     */
    public StartGameCommand() {
    }

    public StartGameCommand(int tilesPerPlayer, int difficulty, GameState gameState) {
        this.tilesPerPlayer = tilesPerPlayer;
        this.difficulty = difficulty;
        this.gameState = gameState;
    }

    @Override
    public void execute(GameStateModifier gameStateModifier) throws CommandFailedException {
        try {
            if (gameState.getStatus() != GameStatus.CREATED) throw new ModificationException("Game is already started");
            gameStateModifier.setGameState(gameState);
            gameStateModifier.initGame(tilesPerPlayer, difficulty);
            gameStateModifier.startGame();
        } catch (ModificationException | IOException e) {
            throw new CommandFailedException("StartGameCommand failed.", e);
        }
    }

    @Override
    public String generateUIText() {
        throw new NotImplementedException();
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

}
