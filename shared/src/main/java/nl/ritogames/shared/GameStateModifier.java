package nl.ritogames.shared;

import nl.ritogames.shared.dto.GameState;
import nl.ritogames.shared.dto.event.GameJoinEvent;
import nl.ritogames.shared.dto.gameobject.ASDVector;
import nl.ritogames.shared.dto.gameobject.individual.Individual;
import nl.ritogames.shared.dto.gameobject.world.tile.AccessibleWorldTile;
import nl.ritogames.shared.enums.Direction;
import nl.ritogames.shared.exception.AgentBuilderException;
import nl.ritogames.shared.exception.ModificationException;
import nl.ritogames.shared.exception.PlayerHasDiedException;

import java.io.IOException;

public interface GameStateModifier {
    /**
     * Damages an individual
     *
     * @param individual Individual being damaged
     * @param amount     Amount of damage applied
     * @throws ModificationException
     */
    void damageIndividual(Individual individual, int amount) throws ModificationException, PlayerHasDiedException;

    /**
     * Adds a character to the gameState
     *
     * @param uuid identifier of the character
     * @throws ModificationException
     */
    void addCharacter(String uuid) throws ModificationException;

    /**
     * Creates a game (basically opening a lobby)
     *
     * @param gameName     Name of the game
     * @param individualId Id of the individual creating the game
     * @throws ModificationException
     */
    void createGame(String gameName, String individualId, String agentName) throws ModificationException, IOException;

    /**
     * This method starts an initialised game. Should always be called AFTER initGame()
     *
     * @throws ModificationException Thrown when the bots or monsters cannot be started
     */
    void startGame() throws ModificationException;

    /**
     * Initialises a game to be started.
     *
     * @param tilesPerPlayer The number of tiles that should be generated per player
     * @param difficulty     The difficulty of the game to be initialised. This influences number of mobs and items.
     * @throws ModificationException Thrown when the world cannot be populated
     */
    void initGame(int tilesPerPlayer, int difficulty) throws ModificationException, IOException;

    /**
     * Activates the IA of an Individual.
     *
     * @param individualId Identifier of the agent
     * @throws ModificationException
     */
    void startAgent(String individualId) throws ModificationException, AgentBuilderException;

    /**
     * Deactivates the IA of an Individual.
     *
     * @param individualId Identifier of the agent
     * @throws ModificationException
     */
    void stopAgent(String individualId) throws ModificationException;

    void resumeGame(String gameName) throws ModificationException;

    boolean saveGameExists(String gameName);

    void saveGame() throws ModificationException;

    void setGameState(GameState gameState);

    GameState getGameState();

    /**
     * Recieves a location, checks the direction, creates a new direction, moves and returns it
     *
     * @param location  The starting location
     * @param direction Direction, such as NOTH, SOUTH, EAST and WEST
     * @return
     */
    ASDVector getNextLocation(ASDVector location, Direction direction);

    void endGame() throws ModificationException;

    /**
     * Places an individual on an accessible worldTile
     *
     * @param individual Individual being placed
     * @param target     Target tile
     * @throws ModificationException
     */
    void addIndividualToAccessibleTile(Individual individual, AccessibleWorldTile target) throws ModificationException;

    /**
     * Clears the individual from a target tile
     *
     * @param target tile
     */
    void removeIndividualFromAccessibleTile(AccessibleWorldTile target);

    /**
     * Returns a worldTile based on a location
     *
     * @param location Location the tile is being requested from
     * @return
     * @throws ModificationException
     */
    AccessibleWorldTile getAccessibleTile(ASDVector location) throws ModificationException;

    AgentService getAgentService();

    void playerJoin(GameJoinEvent event);
}
