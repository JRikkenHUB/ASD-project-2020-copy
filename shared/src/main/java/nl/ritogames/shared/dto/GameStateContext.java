package nl.ritogames.shared.dto;

import nl.ritogames.shared.dto.command.Command;
import nl.ritogames.shared.dto.gameobject.individual.Character;
import nl.ritogames.shared.dto.gameobject.individual.Individual;
import nl.ritogames.shared.dto.gameobject.world.tile.ASDTile;
import nl.ritogames.shared.dto.gameobject.world.tile.AccessibleWorldTile;
import nl.ritogames.shared.enums.GameStatus;
import nl.ritogames.shared.exception.AbsentEntityException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class GameStateContext {
    public final ASDTile[][] worldContext;
    public final int vision;
    private Map<String, Character> activePlayers;
    public final Command lastCommand;
    public final GameStatus currentGameStatus;
    public final String gameName;

    /**
     * The GameContext contain the Tiles surrounding a character or monster.
     * The context contains everything that is inside of their viewing distance (vision)
     *
     * @param worldContext
     * @param vision            The viewing distance (how large the context is)
     * @param lastCommand
     * @param activePlayers
     * @param currentGameStatus
     * @param gameName
     */
    public GameStateContext(ASDTile[][] worldContext, int vision, Command lastCommand, Map<String, Character> activePlayers, GameStatus currentGameStatus, String gameName) {
        this.worldContext = worldContext;
        this.vision = vision;
        this.lastCommand = lastCommand;
        this.activePlayers = activePlayers;
        this.currentGameStatus = currentGameStatus;
        this.gameName = gameName;
    }

    public GameStateContext(ASDTile[][] worldContext) {
        this.worldContext = worldContext;
        this.vision = (int) Math.ceil(worldContext.length / 2.0);
        this.lastCommand = null;
        this.gameName = null;
        this.activePlayers = new HashMap<>();
        this.currentGameStatus = GameStatus.MENU;
    }

    /**
     * Returns the character that the context belongs to
     *
     * @return returns the individual the context is based upon.
     * @throws AbsentEntityException
     */
    public Individual getSelf() throws AbsentEntityException {
        return Optional.ofNullable(((AccessibleWorldTile) worldContext[vision][vision]).getIndividual())
                .orElseThrow(() -> new AbsentEntityException("No individual available"));
    }

    public ASDTile[][] getWorldContext() {
        return worldContext;
    }


    public String getGameName() {
        return gameName;
    }

    public Map<String, Character> getActivePlayers() {
        return activePlayers;
    }

    public void setActivePlayers(Map<String, Character> activePlayers) {
        this.activePlayers = activePlayers;
    }

    @Override
    public String toString() {
        return "GameStateContext{" +
                "\n worldContext=" + Arrays.toString(worldContext) +
                "\n activePlayers=" + activePlayers +
                "\n lastCommand=" + lastCommand +
                "\n currentGameStatus=" + currentGameStatus +
                "\n}";
    }
}