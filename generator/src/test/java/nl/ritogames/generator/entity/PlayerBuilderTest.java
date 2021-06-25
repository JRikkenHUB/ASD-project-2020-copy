package nl.ritogames.generator.entity;

import nl.ritogames.generator.world.Room;
import nl.ritogames.generator.world.WorldBench;
import nl.ritogames.shared.dto.gameobject.individual.Character;
import nl.ritogames.shared.dto.gameobject.world.tile.AccessibleWorldTile;
import nl.ritogames.shared.dto.gameobject.world.tile.InaccessibleWorldTile;
import nl.ritogames.shared.dto.gameobject.world.tile.WorldTile;
import nl.ritogames.shared.exception.AbsentEntityException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlayerBuilderTest {

    private PlayerBuilder sut;
    private WorldBench bench;
    private ArrayList<Character> players;
    private ArrayList<Room> rooms;
    private long seed;


    @BeforeEach
    void setUp() {
        players = new ArrayList<>();
        rooms = new ArrayList<>();
        players.add(new Character(0, 0));
        players.add(new Character(0, 0));
        players.add(new Character(0, 0));
        sut = new PlayerBuilder();
        seed = 2;
        bench = new WorldBench(9);
    }

    @Test
    void testBuild() throws AbsentEntityException {
        bench.setTiles(world());
        sut.build(bench, seed, players);

        int playersSpawned = 0;
        for (WorldTile[] tiles : bench.getTiles()) {
            for (WorldTile tile : tiles) {
                if (tile instanceof AccessibleWorldTile) {
                    if (((AccessibleWorldTile) tile).getIndividual() instanceof Character) {
                        playersSpawned++;
                    }
                }
            }
        }

        assertEquals(players.size(), playersSpawned);
}


    private WorldTile[][] world() {
        return new WorldTile[][]{
                {
                        new AccessibleWorldTile(1, 1),
                        new AccessibleWorldTile(1, 2),
                        new AccessibleWorldTile(1, 3),
                        new AccessibleWorldTile(1, 4),
                        new AccessibleWorldTile(1, 5),
                        new InaccessibleWorldTile(1, 6),
                        new AccessibleWorldTile(1, 7),
                        new InaccessibleWorldTile(1, 8),
                        new InaccessibleWorldTile(1, 9)
                },
                {
                        new AccessibleWorldTile(2, 1),
                        new AccessibleWorldTile(2, 2),
                        new AccessibleWorldTile(2, 3),
                        new AccessibleWorldTile(2, 4),
                        new AccessibleWorldTile(2, 5),
                        new InaccessibleWorldTile(2, 6),
                        new AccessibleWorldTile(2, 7),
                        new InaccessibleWorldTile(2, 8),
                        new InaccessibleWorldTile(2, 9)
                },
                {
                        new AccessibleWorldTile(3, 1),
                        new AccessibleWorldTile(3, 2),
                        new AccessibleWorldTile(3, 3),
                        new AccessibleWorldTile(3, 4),
                        new AccessibleWorldTile(3, 5),
                        new InaccessibleWorldTile(3, 6),
                        new AccessibleWorldTile(3, 7),
                        new InaccessibleWorldTile(3, 8),
                        new InaccessibleWorldTile(3, 9)
                },
                {
                        new AccessibleWorldTile(4, 1),
                        new AccessibleWorldTile(4, 2),
                        new AccessibleWorldTile(4, 3),
                        new AccessibleWorldTile(4, 4),
                        new AccessibleWorldTile(4, 5),
                        new InaccessibleWorldTile(4, 6),
                        new AccessibleWorldTile(4, 7),
                        new InaccessibleWorldTile(4, 8),
                        new InaccessibleWorldTile(4, 9)
                },
                {
                        new AccessibleWorldTile(5, 1),
                        new AccessibleWorldTile(5, 2),
                        new AccessibleWorldTile(5, 3),
                        new AccessibleWorldTile(5, 4),
                        new AccessibleWorldTile(5, 5),
                        new AccessibleWorldTile(5, 6),
                        new InaccessibleWorldTile(5, 7),
                        new AccessibleWorldTile(5, 8),
                        new InaccessibleWorldTile(5, 9)
                },
                {
                        new AccessibleWorldTile(6, 1),
                        new AccessibleWorldTile(6, 2),
                        new AccessibleWorldTile(6, 3),
                        new AccessibleWorldTile(6, 4),
                        new AccessibleWorldTile(6, 5),
                        new AccessibleWorldTile(6, 6),
                        new InaccessibleWorldTile(6, 7),
                        new AccessibleWorldTile(6, 8),
                        new InaccessibleWorldTile(6, 9)
                },
                {
                        new AccessibleWorldTile(7, 1),
                        new AccessibleWorldTile(7, 2),
                        new AccessibleWorldTile(7, 3),
                        new AccessibleWorldTile(7, 4),
                        new AccessibleWorldTile(7, 5),
                        new AccessibleWorldTile(7, 6),
                        new AccessibleWorldTile(7, 7),
                        new AccessibleWorldTile(7, 8),
                        new InaccessibleWorldTile(7, 9)
                },
                {
                        new AccessibleWorldTile(8, 1),
                        new AccessibleWorldTile(8, 2),
                        new AccessibleWorldTile(8, 3),
                        new AccessibleWorldTile(8, 4),
                        new AccessibleWorldTile(8, 5),
                        new AccessibleWorldTile(8, 6),
                        new InaccessibleWorldTile(8, 7),
                        new AccessibleWorldTile(8, 8),
                        new InaccessibleWorldTile(8, 9)
                },
                {
                        new AccessibleWorldTile(9, 1),
                        new AccessibleWorldTile(9, 2),
                        new AccessibleWorldTile(9, 3),
                        new AccessibleWorldTile(9, 4),
                        new AccessibleWorldTile(9, 5),
                        new AccessibleWorldTile(9, 6),
                        new InaccessibleWorldTile(9, 7),
                        new AccessibleWorldTile(9, 8),
                        new InaccessibleWorldTile(9, 9)
                }
        };
    }
}