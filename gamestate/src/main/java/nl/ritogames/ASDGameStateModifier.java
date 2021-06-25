package nl.ritogames;

import nl.ritogames.shared.*;
import nl.ritogames.shared.dto.GameState;
import nl.ritogames.shared.dto.command.individual.DeathCommand;
import nl.ritogames.shared.dto.event.GameJoinEvent;
import nl.ritogames.shared.dto.gameobject.ASDVector;
import nl.ritogames.shared.dto.gameobject.individual.Character;
import nl.ritogames.shared.dto.gameobject.individual.Individual;
import nl.ritogames.shared.dto.gameobject.individual.Monster;
import nl.ritogames.shared.dto.gameobject.world.tile.AccessibleWorldTile;
import nl.ritogames.shared.dto.gameobject.world.tile.WorldTile;
import nl.ritogames.shared.enums.Direction;
import nl.ritogames.shared.enums.GameStatus;
import nl.ritogames.shared.exception.AbsentEntityException;
import nl.ritogames.shared.exception.AgentBuilderException;
import nl.ritogames.shared.exception.ModificationException;
import nl.ritogames.shared.exception.PlayerHasDiedException;
import org.codehaus.jackson.map.ObjectMapper;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;

public class ASDGameStateModifier implements GameStateModifier {
    public static final String SAVE_GAME_FILE_EXTENSION = ".gss";
    public static final String DATA = "data/%s%s";
    private GameState gameState;
    private Generator generator;
    private FileRepository fileHandler;
    private AgentService agentService;
    private GameStateContextProvider contextProvider;
    private List<Character> bots;

    public void addCharacter(String uuid) {
        Individual individual = new Character();
        gameState.addIndividual(uuid, individual);
    }
    public void addIndividual(Individual individual) {
        gameState.addIndividual(individual.getIndividualID(), individual);
    }

    /**
     * Creates a new game with a given gameName and adds the given player to this game.
     *
     * @param gameName     The name of the game to create.
     * @param individualId The Id of the individual that needs to be added to the game.
     * @throws ModificationException Thrown when there is already a created game.
     */
    @Override
    public void createGame(String gameName, String individualId, String agentName) throws ModificationException, IOException {
        if (gameState != null) throw new ModificationException("There is already a created game.");
        this.gameState = new GameState(gameName);
        addSelf(individualId, agentName);
        gameState.setSeed(generator.generateSeed());
        gameState.setHostIndividualId(individualId);
        gameState.setStatus(GameStatus.CREATED);
    }

    public void addSelf(String individualId, String agentFileName) throws IOException {
        Individual self = new Character();
        self.setIndividualID(individualId);
        String AGENT_DIRECTORY_NAME = "/data/agents/";
        String AGENT_SOURCE_FORMAT = AGENT_DIRECTORY_NAME + "%s.json";
        String jSON = fileHandler.readFile(Paths.get(String.format(AGENT_SOURCE_FORMAT, agentFileName)));
        self.setAgentJson(Base64.getEncoder().encodeToString(jSON.getBytes()));
        addIndividual(self);
    }

    /**
     * This method starts an initialised game. Should always be called AFTER initGame()
     *
     * @throws ModificationException Thrown when the bots or monsters cannot be started
     */
    @Override
    public void startGame() throws ModificationException {
        gameState.setStatus(GameStatus.IN_PROGRESS);
        startBots();
        startMonsters();
    }

    /**
     * Initialises a game to be started.
     *
     * @param tilesPerPlayer The number of tiles that should be generated per player
     * @param difficulty     The difficulty of the game to be initialised. This influences number of mobs and items.
     * @throws ModificationException Thrown when the world cannot be populated
     */
    @Override
    public void initGame(int tilesPerPlayer, int difficulty) throws ModificationException, IOException {
        if(this.gameState == null) this.gameState = new GameState();
        fillPlayerSlotsWithAgents();

        try {
            gameState.setWorld(generator.generateWorld(gameState.getSeed(), tilesPerPlayer, difficulty, getCharactersFromIndividuals(gameState.getIndividuals().values())));
        } catch (AbsentEntityException e) {
            throw new ModificationException("Could not populate world.", e);
        }

        collectMonsters();
    }

    private void fillPlayerSlotsWithAgents() throws IOException {
        this.bots = new ArrayList<>();
        int minimumPlayers = 3;
        List<Character> characters = getCharactersFromIndividuals(gameState.getIndividuals().values());
        for (int i = characters.size() + 1; i <= minimumPlayers; i++) {
            Character tempIndividual = new Character();
            tempIndividual.setIndividualID(String.format("Player %s", i));
            String jSON = ResourceHelper.getResourceFileAsString("agent.json");
            tempIndividual.setAgentJson(jSON);
            gameState.addIndividual(tempIndividual.getIndividualID(), tempIndividual);
            this.bots.add(tempIndividual);
        }
    }

    private void collectMonsters() {
        for (WorldTile[] tiles : gameState.getWorld().getTiles()) {
            for (WorldTile tile : tiles) {
                if (tile.hasIndividual() && tile instanceof AccessibleWorldTile) {
                    if (((AccessibleWorldTile) tile).getIndividual() instanceof Monster) {
                        gameState.addIndividual(((AccessibleWorldTile) tile).getIndividual().getIndividualID(), ((AccessibleWorldTile) tile).getIndividual());
                    }
                }
            }
        }
    }

    private void startBots() throws ModificationException {
        try {
            for (Character character : bots) {
                agentService.buildAgent(character.getAgentJson(), character);
                agentService.startAgent(character);
            }
        } catch (AgentBuilderException e) {
            throw new ModificationException("Building the agent failed!", e);
        }
    }

    private void startMonsters() throws ModificationException {
        for (Individual individual : gameState.getIndividuals().values()) {
            if (individual instanceof Monster) {
                try {
                    agentService.buildAgent(ResourceHelper.getResourceFileAsString(((Monster) individual).getName() + ".json"), individual);
                    agentService.startAgent(individual);
                } catch (AgentBuilderException | IOException e) {
                    throw new ModificationException("Building the agent failed!", e);
                }
            }
        }
    }

    /**
     * Returns all the characters from a list of individuals.
     *
     * @param individuals A list of individuals.
     * @return A list of all the characters in the given list.
     */
    private ArrayList<Character> getCharactersFromIndividuals(Collection<Individual> individuals) {
        ArrayList<Character> characters = new ArrayList<>();
        for (Individual individual : individuals) {
            if (individual instanceof Character) {
                characters.add((Character) individual);
            }
        }
        return characters;
    }

    /**
     * Starts the agent of an individual.
     *
     * @param individualId The individualId of the individual to start an agent for.
     * @throws ModificationException Thrown when there is no individual with the given Id in the gamestate.
     * @throws AgentBuilderException Thrown when the agent can't be build from the json of the individual.
     */
    @Override
    public void startAgent(String individualId) throws ModificationException, AgentBuilderException {
        if (individualId == null || !gameState.hasIndividual(individualId)) {
            throw new ModificationException("Individual does not exist.");
        }
        Individual characterInGameState = gameState.getIndividual(individualId);
        agentService.buildAgent(new String(Base64.getDecoder().decode(characterInGameState.getAgentJson())), characterInGameState);

        if (!agentService.isAgentActive(characterInGameState)) {
//            throw new ModificationException("Individual already has agent enabled.");
            agentService.startAgent(characterInGameState);
        }
    }

    /**
     * Stops the agent of an individual.
     *
     * @param individualId The individualId of the individual to stop an agent for.
     */
    @Override
    public void stopAgent(String individualId) throws ModificationException {
        if (individualId == null || !gameState.hasIndividual(individualId))
            throw new ModificationException("Individual does not exist.");
        Individual characterInGameState = gameState.getIndividual(individualId);

        if (!agentService.isAgentActive(characterInGameState)) {
            throw new ModificationException("Individual does not have agent enabled.");
        }
        agentService.stopAgent(characterInGameState);
    }

    /**
     * Saves the game to a savefile.
     *
     * @throws ModificationException Thrown when the file cannot be created, usually due to an IOException.
     */
    @Override
    public void saveGame() throws ModificationException {
        try {
            fileHandler.createFile(Path.of(
                    String.format(DATA, gameState.getName(), SAVE_GAME_FILE_EXTENSION)),
                    new ObjectMapper().enableDefaultTyping().writeValueAsString(gameState));
        } catch (IOException e) {
            throw new ModificationException("Failure during save game file creation.");
        }
    }

    /**
     * Resumes the most recent save.
     *
     * @param gameName The name of the game to resume.
     * @throws ModificationException Thrown when game could not be loaded.
     */
    @Override
    public void resumeGame(String gameName) throws ModificationException {
        try {
            String savedGame = fileHandler.readFile(Path.of(String.format(DATA, gameName, SAVE_GAME_FILE_EXTENSION)));
            this.gameState = new ObjectMapper().enableDefaultTyping().readValue(savedGame, GameState.class);
        } catch (IOException e) {
            throw new ModificationException("Failure during resume game. Could not load ".concat(gameName + SAVE_GAME_FILE_EXTENSION));
        }
    }

    /**
     * Checks the availability of the requested save.
     *
     * @param gameName The name of the save game that needs to be checked.
     * @return A boolean whether the requested save game file is present.
     */
    @Override
    public boolean saveGameExists(String gameName) {
        try {
            String savedGame = fileHandler.readFile(Path.of(String.format(DATA, gameName, SAVE_GAME_FILE_EXTENSION)));
            return !savedGame.isEmpty();
        } catch (IOException e) {
            return false;
        }
    }

    public void addIndividualToAccessibleTile(Individual individual, AccessibleWorldTile target) throws ModificationException {
        if (!target.hasIndividual()) {
            target.setIndividual(individual);
            individual.setLocation(target.getCoordinates());
        } else {
            throw new ModificationException("Tile is occupied");
        }
    }

    /**
     * Removes an individual from a given accessibleTile.
     *
     * @param target The target tile to remove an individual from.
     */
    @Override
    public void removeIndividualFromAccessibleTile(AccessibleWorldTile target) {
        target.setIndividual(null);
    }


    @Override
    public GameState getGameState() {
        return gameState;
    }

    /**
     * Checks if the tile on the given location is an accessibleTile and then returns it.
     *
     * @param location The location of requested tile.
     * @return The accessibleTile on the given location.
     * @throws ModificationException Thrown when the tile is not accessible or the location doesn't exist.
     */
    @Override
    public AccessibleWorldTile getAccessibleTile(ASDVector location) throws ModificationException {
        try {
            WorldTile tile = gameState.getWorld().getTile(location);
            if (tile instanceof AccessibleWorldTile) {
                return (AccessibleWorldTile) tile;
            } else {
                throw new ModificationException("Tile is not accessible");
            }
        } catch (NullPointerException e) {
            throw new ModificationException("Location does not exist");
        }
    }

    /**
     * Damages the individual by the given amount.
     *
     * @param individual The individual to damage.
     * @param amount     The ammount of damage.
     * @throws ModificationException Thrown when the hp of the player gets below and the game tries
     *                               to remove the individual.
     */
    @Override
    public void damageIndividual(Individual individual, int amount) throws ModificationException, PlayerHasDiedException {
        amount = Math.max(amount - individual.getDefense(), 0);
        individual.lowerHp(amount);
        PlayerHasDiedException exception = null;
        if (individual.getHp() <= 0) {
            if (agentService.isAgentActive(individual)) agentService.stopAgent(individual);

            if (contextProvider.isSubscribed(individual.getIndividualID())) {
                contextProvider.processCommand(new DeathCommand(individual.getIndividualID()));
                contextProvider.unsubscribe(individual.getIndividualID());
                exception = new PlayerHasDiedException();
                exception.setIndividualId(individual.getIndividualID());
            }

            removeIndividualFromAccessibleTile(getAccessibleTile(individual.getLocation()));
            gameState.getIndividuals().remove(individual.getIndividualID());
        }
        if (exception != null) {
            throw exception;
        }
    }

    /**
     * Ends the game by setting the gameStatus to finished
     */
    @Override
    public void endGame() {
        gameState.setStatus(GameStatus.FINISHED);
    }

    @Override
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    @Inject
    public void setGenerator(Generator generator) {
        this.generator = generator;
    }

    @Inject
    public void setFileHandler(FileRepository fileHandler) {
        this.fileHandler = fileHandler;
    }

    /**
     * Gets the ASDVector next to the given location in the given direction
     *
     * @param location  The base location from which to get the adjacent tile.
     * @param direction The direction relative from the base location to get the adjacent tile from.
     * @return The ASDVector adjacent to the base location.
     */
    @Override
    public ASDVector getNextLocation(ASDVector location, Direction direction) {
        ASDVector nextLocation = new ASDVector(location.getX(), location.getY());
        switch (direction) {
            case NORTH -> nextLocation.setY(nextLocation.getY() - 1);
            case EAST -> nextLocation.setX(nextLocation.getX() + 1);
            case SOUTH -> nextLocation.setY(nextLocation.getY() + 1);
            case WEST -> nextLocation.setX(nextLocation.getX() - 1);
            default -> throw new IllegalArgumentException();
        }
        return nextLocation;
    }

    @Inject
    public void setAgentService(AgentService agentService) {
        this.agentService = agentService;
    }
    @Override
    public AgentService getAgentService() {
        return agentService;
    }

    @Override
    public void playerJoin(GameJoinEvent event) {
        Individual newPlayer = new Character();
        newPlayer.setIndividualID(event.getIndividualId());
        newPlayer.setAgentJson(event.getAgentJSON());
        gameState.addIndividual(newPlayer.getIndividualID(), newPlayer);
    }

    @Inject
    public void setContextProvider(GameStateContextProvider contextProvider) {
        this.contextProvider = contextProvider;
    }
}
