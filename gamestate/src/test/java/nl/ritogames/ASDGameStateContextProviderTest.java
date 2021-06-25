package nl.ritogames;

import nl.ritogames.shared.GameStateContextListener;
import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.GameState;
import nl.ritogames.shared.dto.GameStateContext;
import nl.ritogames.shared.dto.command.Command;
import nl.ritogames.shared.dto.command.individual.AttackCommand;
import nl.ritogames.shared.dto.command.individual.CreateGameCommand;
import nl.ritogames.shared.dto.command.individual.RelevantForOwnContext;
import nl.ritogames.shared.dto.command.menu.ResumeGameCommand;
import nl.ritogames.shared.dto.gameobject.ASDVector;
import nl.ritogames.shared.dto.gameobject.individual.Character;
import nl.ritogames.shared.dto.gameobject.individual.Individual;
import nl.ritogames.shared.dto.gameobject.world.World;
import nl.ritogames.shared.dto.gameobject.world.tile.*;
import nl.ritogames.shared.exception.UnknownCharacterException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ASDGameStateContextProviderTest {
    private final ASDVector CHARACTER_STARTING_LOCATION = new ASDVector(5, 5);
    private final int WORLD_SIZE = 12;
    private final int VISION = 4;

    private ASDGameStateContextProvider sut;
    private GameStateModifier mockedModifier;
    private ContextListener contextListener;
    private GameState gameState;
    private String individualId;
    private Individual individual;
    private RelevantForOwnContext relevantCommand;
    private Command irrelevantCommand;

    @BeforeEach
    void setUp() {
        World world = mock(World.class);
        individualId = "individual_id";
        individual = new Character(CHARACTER_STARTING_LOCATION.getX(), CHARACTER_STARTING_LOCATION.getY());
        individual.setIndividualID(individualId);
        gameState = new GameState("name");
        gameState.addIndividual(individualId, individual);

        gameState.setWorld(world);
        when(world.getTiles()).thenReturn(getWorldTiles());
        when(world.calcWorldSize()).thenReturn(WORLD_SIZE);

        mockedModifier = mock(GameStateModifier.class);
        when(mockedModifier.getGameState()).thenReturn(gameState);

        contextListener = new ContextListener();
        relevantCommand = Mockito.mock(RelevantForOwnContext.class);
        irrelevantCommand = Mockito.mock(Command.class);

        when(relevantCommand.getInitiatingIndividualId()).thenReturn(individualId);

        sut = new ASDGameStateContextProvider(VISION);
        sut.setGameStateProvider(mockedModifier);
    }

    @Test
    void processRelevantForContextCommandSetsExecutedCommand() {
        Command command = new ResumeGameCommand();
        sut.processCommand(command);

        assertNotNull(sut.getExecutedCommand());
    }

    @Test
    void processCommandRelevantForOwnContectOnlyUpdatesSelf() {
        gameState.addIndividual("1", new Character(-100, 0));
        GameStateContextListener listerNotToUpdate = mock(GameStateContextListener.class);
        sut.subscribe(listerNotToUpdate, "1");

        gameState.addIndividual("2", new Character(0, 0));
        GameStateContextListener listnerToUpdate = mock(GameStateContextListener.class);
        sut.subscribe(listnerToUpdate, "2");

        RelevantForOwnContext command = mock(RelevantForOwnContext.class);
        when(command.getInitiatingIndividualId()).thenReturn("2");
        sut.processCommand(command);

        verify(listerNotToUpdate, never()).updateContext(any());
        verify(listnerToUpdate).updateContext(any());
    }

    @Test
    void processCommandWithoutLocationUpdatesAllListeners() {
        GameStateContextListener listener = mock(GameStateContextListener.class);
        sut.subscribe(listener, "individual_id");

        Command command = new ResumeGameCommand();
        sut.processCommand(command);

        verify(listener).updateContext(any());
    }

    @Test
    void processCommandWithLocationUpdatesListenersInVision() {
        gameState.addIndividual("1", new Character(-100, 0));
        GameStateContextListener listerNotToUpdate = mock(GameStateContextListener.class);
        sut.subscribe(listerNotToUpdate, "1");

        gameState.addIndividual("2", new Character(0, 0));
        GameStateContextListener listnerToUpdate = mock(GameStateContextListener.class);
        sut.subscribe(listnerToUpdate, "2");

        Command command = new AttackCommand("2", "1");
        sut.processCommand(command);

        verify(listerNotToUpdate, never()).updateContext(any());
        verify(listnerToUpdate).updateContext(any());
    }

    @Test
    void subscribeWithUnknownIndividualIdThrowsUnkownCharacterException() {
        GameStateContextListener listerNotToUpdate = mock(GameStateContextListener.class);
        Assertions.assertThrows(UnknownCharacterException.class, () -> sut.subscribe(listerNotToUpdate, "1"));
    }

    @Test
    void getContextReturnsContextWhenVisionIsLargerThanWorld() {
        sut = new ASDGameStateContextProvider(10);
        sut.setGameStateProvider(mockedModifier);
        gameState.addIndividual(individualId, individual);

        sut.subscribe(contextListener, individualId);
        sut.processCommand(relevantCommand);
        GameStateContext result = contextListener.context;
        assertNotNull(result);
    }

    @Test
    void getContextReturnsCorrectContextWhenInCenterOfMap() {
        sut.subscribe(contextListener, "individual_id");
        sut.processCommand(relevantCommand);

        GameStateContext result = contextListener.context;
        int vision = sut.vision;

        ASDTile[][] contextTiles = getContextTilesInaccessible();

        GameStateContext expected = new GameStateContext(contextTiles, vision, null, null, null, "game");

        for (int i = 0; i < expected.worldContext.length; i++) {
            for (int j = 0; j < expected.worldContext.length; j++) {
                ASDTile expectedTile = expected.worldContext[i][j];
                ASDTile resultTile = result.worldContext[i][j];
                assertEquals(expectedTile.getClass(), resultTile.getClass());

                if (expectedTile instanceof WorldTile)
                    assertEquals(((WorldTile) expectedTile).getCoordinates(), ((WorldTile) resultTile).getCoordinates());
            }
        }
    }

    class ContextListener implements GameStateContextListener {
        GameStateContext context;


        @Override
        public void updateContext(GameStateContext context) {
            this.context = context;
        }

        @Override
        public void unsubscribedSignal() {
            
        }
    }

    private ASDTile[][] getContextTiles() {
        return new ASDTile[][]{
                {
                        new AccessibleWorldTile(4, 4),
                        new AccessibleWorldTile(4, 5),
                        new InaccessibleWorldTile(4, 6),
                        new AccessibleWorldTile(4, 7),
                        new InaccessibleWorldTile(4, 8),
                        new InaccessibleWorldTile(4, 9),
                        new InaccessibleWorldTile(4, 10),
                        new AccessibleWorldTile(4, 11),
                        new EdgeTile()
                },
                {
                        new AccessibleWorldTile(5, 4),
                        new AccessibleWorldTile(5, 5),
                        new AccessibleWorldTile(5, 6),
                        new InaccessibleWorldTile(5, 7),
                        new AccessibleWorldTile(5, 8),
                        new InaccessibleWorldTile(5, 9),
                        new InaccessibleWorldTile(5, 10),
                        new InaccessibleWorldTile(5, 11),
                        new EdgeTile()
                },
                {
                        new AccessibleWorldTile(6, 4),
                        new AccessibleWorldTile(6, 5),
                        new AccessibleWorldTile(6, 6),
                        new InaccessibleWorldTile(6, 7),
                        new AccessibleWorldTile(6, 8),
                        new InaccessibleWorldTile(6, 9),
                        new InaccessibleWorldTile(6, 10),
                        new InaccessibleWorldTile(6, 11),
                        new EdgeTile()
                },
                {
                        new AccessibleWorldTile(7, 4),
                        new AccessibleWorldTile(7, 5),
                        new AccessibleWorldTile(7, 6),
                        new InaccessibleWorldTile(7, 7),
                        new AccessibleWorldTile(7, 8),
                        new InaccessibleWorldTile(7, 9),
                        new InaccessibleWorldTile(7, 10),
                        new InaccessibleWorldTile(7, 11),
                        new EdgeTile()
                },
                {
                        new AccessibleWorldTile(8, 4),
                        new AccessibleWorldTile(8, 5),
                        new AccessibleWorldTile(8, 6),
                        new InaccessibleWorldTile(8, 7),
                        new AccessibleWorldTile(8, 8),
                        new InaccessibleWorldTile(8, 9),
                        new InaccessibleWorldTile(8, 10),
                        new InaccessibleWorldTile(8, 11),
                        new EdgeTile()
                },
                {
                        new AccessibleWorldTile(9, 4),
                        new AccessibleWorldTile(9, 5),
                        new AccessibleWorldTile(9, 6),
                        new InaccessibleWorldTile(9, 7),
                        new AccessibleWorldTile(9, 8),
                        new InaccessibleWorldTile(9, 9),
                        new InaccessibleWorldTile(9, 10),
                        new InaccessibleWorldTile(9, 11),
                        new EdgeTile()
                },
                {
                        new AccessibleWorldTile(10, 4),
                        new AccessibleWorldTile(10, 5),
                        new AccessibleWorldTile(10, 6),
                        new InaccessibleWorldTile(10, 7),
                        new AccessibleWorldTile(10, 8),
                        new InaccessibleWorldTile(10, 9),
                        new InaccessibleWorldTile(10, 10),
                        new InaccessibleWorldTile(10, 11),
                        new EdgeTile()
                },
                {
                        new AccessibleWorldTile(11, 4),
                        new AccessibleWorldTile(11, 5),
                        new AccessibleWorldTile(11, 6),
                        new InaccessibleWorldTile(11, 7),
                        new AccessibleWorldTile(11, 8),
                        new InaccessibleWorldTile(11, 9),
                        new InaccessibleWorldTile(11, 10),
                        new InaccessibleWorldTile(11, 11),
                        new EdgeTile()
                }
        };
    }

    public static WorldTile[][] getWorldTiles() {
        return new WorldTile[][]{
                {
                        new AccessibleWorldTile(0, 0),
                        new AccessibleWorldTile(0, 1),
                        new AccessibleWorldTile(0, 2),
                        new AccessibleWorldTile(0, 3),
                        new AccessibleWorldTile(0, 4),
                        new AccessibleWorldTile(0, 5),
                        new InaccessibleWorldTile(0, 6),
                        new AccessibleWorldTile(0, 7),
                        new InaccessibleWorldTile(0, 8),
                        new InaccessibleWorldTile(0, 9),
                        new InaccessibleWorldTile(0, 10),
                        new AccessibleWorldTile(0, 11)
                },
                {
                        new AccessibleWorldTile(1, 0),
                        new AccessibleWorldTile(1, 1),
                        new AccessibleWorldTile(1, 2),
                        new AccessibleWorldTile(1, 3),
                        new AccessibleWorldTile(1, 4),
                        new AccessibleWorldTile(1, 5),
                        new InaccessibleWorldTile(1, 6),
                        new AccessibleWorldTile(1, 7),
                        new InaccessibleWorldTile(1, 8),
                        new InaccessibleWorldTile(1, 9),
                        new InaccessibleWorldTile(1, 10),
                        new AccessibleWorldTile(1, 11)
                },
                {
                        new AccessibleWorldTile(2, 0),
                        new AccessibleWorldTile(2, 1),
                        new AccessibleWorldTile(2, 2),
                        new AccessibleWorldTile(2, 3),
                        new AccessibleWorldTile(2, 4),
                        new AccessibleWorldTile(2, 5),
                        new InaccessibleWorldTile(2, 6),
                        new AccessibleWorldTile(2, 7),
                        new InaccessibleWorldTile(2, 8),
                        new InaccessibleWorldTile(2, 9),
                        new InaccessibleWorldTile(2, 10),
                        new AccessibleWorldTile(2, 11)
                },
                {
                        new AccessibleWorldTile(3, 0),
                        new AccessibleWorldTile(3, 1),
                        new AccessibleWorldTile(3, 2),
                        new AccessibleWorldTile(3, 3),
                        new AccessibleWorldTile(3, 4),
                        new AccessibleWorldTile(3, 5),
                        new InaccessibleWorldTile(3, 6),
                        new AccessibleWorldTile(3, 7),
                        new InaccessibleWorldTile(3, 8),
                        new InaccessibleWorldTile(3, 9),
                        new InaccessibleWorldTile(3, 10),
                        new AccessibleWorldTile(3, 11)
                },
                {
                        new AccessibleWorldTile(4, 0),
                        new AccessibleWorldTile(4, 1),
                        new AccessibleWorldTile(4, 2),
                        new AccessibleWorldTile(4, 3),
                        new AccessibleWorldTile(4, 4),
                        new AccessibleWorldTile(4, 5),
                        new InaccessibleWorldTile(4, 6),
                        new AccessibleWorldTile(4, 7),
                        new InaccessibleWorldTile(4, 8),
                        new InaccessibleWorldTile(4, 9),
                        new InaccessibleWorldTile(4, 10),
                        new AccessibleWorldTile(4, 11)
                },
                {
                        new AccessibleWorldTile(5, 0),
                        new AccessibleWorldTile(5, 1),
                        new AccessibleWorldTile(5, 2),
                        new AccessibleWorldTile(5, 3),
                        new AccessibleWorldTile(5, 4),
                        new AccessibleWorldTile(5, 5),
                        new AccessibleWorldTile(5, 6),
                        new InaccessibleWorldTile(5, 7),
                        new AccessibleWorldTile(5, 8),
                        new InaccessibleWorldTile(5, 9),
                        new InaccessibleWorldTile(5, 10),
                        new InaccessibleWorldTile(5, 11),
                },
                {
                        new AccessibleWorldTile(6, 0),
                        new AccessibleWorldTile(6, 1),
                        new AccessibleWorldTile(6, 2),
                        new AccessibleWorldTile(6, 3),
                        new AccessibleWorldTile(6, 4),
                        new AccessibleWorldTile(6, 5),
                        new AccessibleWorldTile(6, 6),
                        new InaccessibleWorldTile(6, 7),
                        new AccessibleWorldTile(6, 8),
                        new InaccessibleWorldTile(6, 9),
                        new InaccessibleWorldTile(6, 10),
                        new InaccessibleWorldTile(6, 11),
                },
                {
                        new AccessibleWorldTile(7, 0),
                        new AccessibleWorldTile(7, 1),
                        new AccessibleWorldTile(7, 2),
                        new AccessibleWorldTile(7, 3),
                        new AccessibleWorldTile(7, 4),
                        new AccessibleWorldTile(7, 5),
                        new AccessibleWorldTile(7, 6),
                        new InaccessibleWorldTile(7, 7),
                        new AccessibleWorldTile(7, 8),
                        new InaccessibleWorldTile(7, 9),
                        new InaccessibleWorldTile(7, 10),
                        new InaccessibleWorldTile(7, 11),
                },
                {
                        new AccessibleWorldTile(8, 0),
                        new AccessibleWorldTile(8, 1),
                        new AccessibleWorldTile(8, 2),
                        new AccessibleWorldTile(8, 3),
                        new AccessibleWorldTile(8, 4),
                        new AccessibleWorldTile(8, 5),
                        new AccessibleWorldTile(8, 6),
                        new InaccessibleWorldTile(8, 7),
                        new AccessibleWorldTile(8, 8),
                        new InaccessibleWorldTile(8, 9),
                        new InaccessibleWorldTile(8, 10),
                        new InaccessibleWorldTile(8, 11)
                },
                {
                        new AccessibleWorldTile(9, 0),
                        new AccessibleWorldTile(9, 1),
                        new AccessibleWorldTile(9, 2),
                        new AccessibleWorldTile(9, 3),
                        new AccessibleWorldTile(9, 4),
                        new AccessibleWorldTile(9, 5),
                        new AccessibleWorldTile(9, 6),
                        new InaccessibleWorldTile(9, 7),
                        new AccessibleWorldTile(9, 8),
                        new InaccessibleWorldTile(9, 9),
                        new InaccessibleWorldTile(9, 10),
                        new InaccessibleWorldTile(9, 11)
                },
                {
                        new AccessibleWorldTile(10, 0),
                        new AccessibleWorldTile(10, 1),
                        new AccessibleWorldTile(10, 2),
                        new AccessibleWorldTile(10, 3),
                        new AccessibleWorldTile(10, 4),
                        new AccessibleWorldTile(10, 5),
                        new AccessibleWorldTile(10, 6),
                        new InaccessibleWorldTile(10, 7),
                        new AccessibleWorldTile(10, 8),
                        new InaccessibleWorldTile(10, 9),
                        new InaccessibleWorldTile(10, 10),
                        new InaccessibleWorldTile(10, 11)
                },
                {
                        new AccessibleWorldTile(11, 0),
                        new AccessibleWorldTile(11, 1),
                        new AccessibleWorldTile(11, 2),
                        new AccessibleWorldTile(11, 3),
                        new AccessibleWorldTile(11, 4),
                        new AccessibleWorldTile(11, 5),
                        new AccessibleWorldTile(11, 6),
                        new InaccessibleWorldTile(11, 7),
                        new AccessibleWorldTile(11, 8),
                        new InaccessibleWorldTile(11, 9),
                        new InaccessibleWorldTile(11, 10),
                        new InaccessibleWorldTile(11, 11)
                }
        };
    }

    private ASDTile[][] getContextTilesInaccessible() {
        ASDTile[][] contextTiles = new ASDTile[][]{
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
                        new InaccessibleWorldTile(7, 7),
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
        return contextTiles;
    }

    @Test
    void testGetEventLocationOfCreateCommand(){
        Command createGameCommand = new CreateGameCommand();
        assertNull(sut.getEventLocation(createGameCommand));
    }
}
