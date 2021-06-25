package nl.ritogames.shared;

import nl.ritogames.shared.dto.gameobject.individual.Character;
import nl.ritogames.shared.dto.gameobject.world.World;
import nl.ritogames.shared.exception.AbsentEntityException;

import java.util.ArrayList;

public interface Generator {

    /**
     * Generates a World using the seed
     *
     * @param seed           The seed is used to generate the world itself, different seed equals a different world
     * @param tilesPerPlayer The amount of accessible tiles there have to be for each player
     * @param difficulty     The game difficulty adjust the spawnrate of attributes and monsters
     * @param characters     The list of players in the game
     * @return
     */
    World generateWorld(long seed, int tilesPerPlayer, int difficulty, ArrayList<Character> characters) throws AbsentEntityException;

    long generateSeed();
}
