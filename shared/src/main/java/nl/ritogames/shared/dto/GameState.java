package nl.ritogames.shared.dto;

import nl.ritogames.shared.dto.gameobject.individual.Individual;
import nl.ritogames.shared.dto.gameobject.world.World;
import nl.ritogames.shared.enums.Direction;
import nl.ritogames.shared.enums.GameStatus;

import java.util.HashMap;

public class GameState {
    private HashMap<String, Individual> individuals;
    private World world;
    private String name;
    private GameStatus status;
    private long seed;
    private String hostIndividualId;

    public GameState() {
    }

    /**
     * Creates the gamestate and sets the status to CREATED
     * @param name
     */
    public GameState(String name) {
        this.name = name;
        individuals = new HashMap<>();
        this.status = GameStatus.CREATED;
    }

    /**
     * Adds an individual to the gameState
     * @param uuid
     * @param individual
     */
    public void addIndividual(String uuid, Individual individual) {
        individual.setIndividualID(uuid);
        individuals.put(uuid, individual);
    }

    public World getWorld() {
        return world;
    }

    /**
     * Gets an individual based on the identifier
     * @param id
     * @return
     */
    public Individual getIndividual(String id) {
        return individuals.get(id);
    }

    /**
     * Checks if in individual exists based on the identifier
     * @param uuid
     * @return
     */
    public boolean hasIndividual(String uuid) {
        return individuals.containsKey(uuid);
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public HashMap<String, Individual> getIndividuals() {
        return individuals;
    }

    public void setIndividuals(HashMap<String, Individual> individuals) {
        this.individuals = individuals;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public GameStatus getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSeed(long generateSeed) {
        this.seed = generateSeed;
    }

    public long getSeed() {
        return seed;
    }

    public String getHostIndividualId() {
        return hostIndividualId;
    }

    public void setHostIndividualId(String hostIndividualId) {
        this.hostIndividualId = hostIndividualId;
    }

    public void getIndividuals(String individualId, Direction north) {
    }

    @Override
    public String toString() {
        return "GameState{" +
                "individuals=" + individuals +
                ", world=" + world +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", seed=" + seed +
                '}';
    }

}
