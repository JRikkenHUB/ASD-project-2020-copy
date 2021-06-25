//package nl.ritogames;
//
//import nl.ritogames.filehandler.ASDFileHandler;
//import nl.ritogames.generator.ASDGenerator;
//import nl.ritogames.shared.AgentService;
//import nl.ritogames.shared.FileRepository;
//import nl.ritogames.shared.GameStateContextProvider;
//import nl.ritogames.shared.Generator;
//import nl.ritogames.shared.dto.GameState;
//import nl.ritogames.shared.dto.gameobject.ASDVector;
//import nl.ritogames.shared.dto.gameobject.attribute.Potion;
//import nl.ritogames.shared.dto.gameobject.individual.Character;
//import nl.ritogames.shared.dto.gameobject.individual.Individual;
//import nl.ritogames.shared.dto.gameobject.trap.Trap;
//import nl.ritogames.shared.dto.gameobject.world.World;
//import nl.ritogames.shared.dto.gameobject.world.tile.AccessibleWorldTile;
//import nl.ritogames.shared.dto.gameobject.world.tile.TrapTile;
//import nl.ritogames.shared.dto.gameobject.world.tile.WorldTile;
//import nl.ritogames.shared.enums.Direction;
//import nl.ritogames.shared.enums.GameStatus;
//import nl.ritogames.shared.exception.AbsentEntityException;
//import nl.ritogames.shared.exception.AgentBuilderException;
//import nl.ritogames.shared.exception.ModificationException;
//import org.codehaus.jackson.map.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//
//import java.io.IOException;
//import java.nio.file.Path;
//import java.util.Base64;
//import java.util.HashMap;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class ASDGameStateModifierTest {
//    private ASDGameStateModifier sut;
//    private GameState mockedGameState;
//    private Character mockedCharacter;
//    private Generator mockedGenerator;
//    private GameStateContextProvider mockedProvider;
//    private AgentService mockedAgentService;
//    private FileRepository fileHandlerMock;
//    private FileRepository realFileHandler;
//    private String gameName;
//
//
//    @BeforeEach
//    void setUp() {
//        gameName = "game";
//        mockedGameState = mock(GameState.class);
//        mockedGenerator = mock(ASDGenerator.class);
//        mockedCharacter = mock(Character.class);
//        fileHandlerMock = mock(FileRepository.class);
//        mockedAgentService = mock(AgentService.class);
//        mockedProvider = mock(GameStateContextProvider.class);
//
//        when(mockedGameState.getStatus()).thenReturn(GameStatus.CREATED);
//
//        sut = new ASDGameStateModifier();
//        sut.setGameState(mockedGameState);
//        sut.setGenerator(mockedGenerator);
//        sut.setAgentService(mockedAgentService);
//        sut.setFileHandler(fileHandlerMock);
//        sut.setContextProvider(mockedProvider);
//        when(mockedGameState.getName()).thenReturn(gameName);
//        when(mockedGameState.getWorld()).thenReturn(new World());
//    }
//
//    @Test
//    void testAddCharacterToAccessibleTileHappyPath() throws ModificationException {
//        // Arrange
//
//        mockedGameState.getWorld().setTiles(ASDGameStateContextProviderTest.getWorldTiles());
//
//        ASDVector location = new ASDVector(1,1);
//        mockedCharacter.setLocation(location);
//
//        AccessibleWorldTile target = sut.getAccessibleTile(location);
//
//        // Act
//        sut.addIndividualToAccessibleTile(mockedCharacter, target);
//
//        // Assert
//        assertEquals(target.getIndividual(), mockedCharacter);
//    }
//
//    @Test
//    void testAddCharacterToAccessibleTileTileOccupied() throws ModificationException {
//        // Arrange
//
//        mockedGameState.getWorld().setTiles(ASDGameStateContextProviderTest.getWorldTiles());
//
//        ASDVector location = new ASDVector(1,1);
//        mockedCharacter.setLocation(location);
//
//        AccessibleWorldTile target = sut.getAccessibleTile(location);
//        target.setIndividual(new Character());
//        // Act
//
//        // Assert
//        assertThrows(ModificationException.class, () -> sut.addIndividualToAccessibleTile(mockedCharacter, target));
//    }
//
//    @Test
//    void testRemoveIndividualFromTileHappyPath() throws ModificationException {
//        // Arrange
//
//        mockedGameState.getWorld().setTiles(ASDGameStateContextProviderTest.getWorldTiles());
//
//        ASDVector location = new ASDVector(1,1);
//        mockedCharacter.setLocation(location);
//
//        AccessibleWorldTile target = sut.getAccessibleTile(location);
//        target.setIndividual(mockedCharacter);
//        // Act
//        sut.removeIndividualFromAccessibleTile(target);
//
//        // Assert
//        assertFalse(target.hasIndividual());
//
//    }
//
//    @Test
//    void testDamageIndividual() throws ModificationException {
//        // Arrange
//        mockedGameState.getWorld().setTiles(ASDGameStateContextProviderTest.getWorldTiles());
//        Individual individual = new Character();
//        individual.setLocation(new ASDVector(1,1));
//        individual.setHp(100);
//        int fakeDamageAmount = 50;
//        int expected = individual.getHp() + individual.getDefense() - fakeDamageAmount;
//
//        // Act
//        sut.damageIndividual(individual, fakeDamageAmount);
//
//        //verify
//        assertEquals(individual.getHp(), expected);
//    }
//
//    @Test
//    void testKillRemovesIndividual() throws ModificationException {
//        // Arrange
//        HashMap<String, Individual> hashMap = new HashMap<>();
//        Individual individual = new Character();
//        individual.setIndividualID("test");
//        hashMap.put(individual.getIndividualID(),individual);
//        mockedGameState.getWorld().setTiles(ASDGameStateContextProviderTest.getWorldTiles());
//        when(mockedGameState.getIndividuals()).thenReturn(hashMap);
//        individual.setLocation(new ASDVector(1,1));
//        individual.setHp(100);
//        int fakeDamageAmount = 150;
//
//        // Act
//        sut.damageIndividual(individual, fakeDamageAmount);
//
//        //verify
//        assertEquals(0,hashMap.size());
//    }
//
//    @Test
//    void testKillUnsubscribesContextListener() throws ModificationException {
//        // Arrange
//        HashMap<String, Individual> hashMap = new HashMap<>();
//        Individual individual = new Character();
//        individual.setIndividualID("test");
//        hashMap.put(individual.getIndividualID(),individual);
//        mockedGameState.getWorld().setTiles(ASDGameStateContextProviderTest.getWorldTiles());
//        when(mockedGameState.getIndividuals()).thenReturn(hashMap);
//        when(mockedProvider.isSubscribed(anyString())).thenReturn(true);
//        when(mockedAgentService.isAgentActive(any())).thenReturn(true);
//        individual.setLocation(new ASDVector(1,1));
//        individual.setHp(100);
//        int fakeDamageAmount = 150;
//
//        // Act
//        sut.damageIndividual(individual, fakeDamageAmount);
//
//        //verify
//        verify(mockedProvider).isSubscribed(anyString());
//        verify(mockedProvider).unsubscribe(anyString());
//        verify(mockedProvider).processCommand(any());
//        verify(mockedAgentService).isAgentActive(any());
//        verify(mockedAgentService).stopAgent(any());
//    }
//
//    @Test
//    void testAddCharacter() {
//        // Arrange
//        String uuid = UUID.randomUUID().toString();
//        // Act
//        sut.addCharacter(uuid);
//        // Assert
//        verify(mockedGameState).addIndividual(Mockito.any(), Mockito.any(Character.class));
//    }
//
//    @Test
//    void createGameSetsGameStatusToCreated() throws ModificationException, IOException {
//        // Arrange
//        String gameName = "game";
//        // Act
//        sut = new ASDGameStateModifier();
//        sut.setGenerator(mockedGenerator);
//        sut.setFileHandler(fileHandlerMock);
//        sut = spy(sut);
//        doNothing().when(sut).addSelf(anyString(), anyString());
//
//        sut.createGame(gameName, "individualid", "agentName");
//        // Assert
//
//        assertEquals(GameStatus.CREATED, sut.getGameState().getStatus());
//    }
//
//    @Test
//    void createGameSetsGameName() throws ModificationException, IOException {
//        sut = new ASDGameStateModifier();
//        sut.setGenerator(mockedGenerator);
//        sut.setFileHandler(fileHandlerMock);
//        sut = spy(sut);
//        doNothing().when(sut).addSelf(anyString(), anyString());
//
//        String gameName = "game";
//        String individualid = "individualid";
//        sut.createGame(gameName, individualid, "agentName");
//
//        assertEquals(gameName, sut.getGameState().getName());
//    }
//
//    @Test
//    void createGameSetsCorrectHost() throws ModificationException, IOException {
//        sut = new ASDGameStateModifier();
//        sut.setGenerator(mockedGenerator);
//        sut.setFileHandler(fileHandlerMock);
//        sut = spy(sut);
//        doNothing().when(sut).addSelf(anyString(), anyString());
//
//        String gameName = "game";
//        String hostId = "individualid";
//        sut.createGame(gameName, hostId, "agentName");
//
//        assertEquals(hostId, sut.getGameState().getHostIndividualId());
//    }
//
//    @Test
//    void createGameSetsSeed() throws ModificationException, IOException {
//        sut = new ASDGameStateModifier();
//        sut.setGenerator(mockedGenerator);
//        sut.setFileHandler(fileHandlerMock);
//        sut = spy(sut);
//        doNothing().when(sut).addSelf(anyString(), anyString());
//
//        String gameName = "game";
//        String individualid = "individualid";
//        sut.createGame(gameName, individualid, "agentName");
//
//        verify(mockedGenerator).generateSeed();
//    }
//
//    @Test
//    void createGameAddsIndividualId() throws ModificationException, IOException {
//        sut = new ASDGameStateModifier();
//        sut.setGenerator(mockedGenerator);
//        sut.setFileHandler(fileHandlerMock);
//        when(fileHandlerMock.readFile(any())).thenReturn("");
//
//        String gameName = "game";
//        String individualid = "individualid";
//        sut.createGame(gameName, individualid, "agentName");
//
//        assertTrue(sut.getGameState().getIndividuals().containsKey(individualid));
//    }
//
//    @Test
//    void createGameAddsIndividual() throws ModificationException, IOException {
//        sut = new ASDGameStateModifier();
//        sut.setGenerator(mockedGenerator);
//        sut.setFileHandler(fileHandlerMock);
//        when(fileHandlerMock.readFile(any())).thenReturn("");
//
//        String gameName = "game";
//        String individualid = "individualid";
//        sut.createGame(gameName, individualid, "agentName");
//
//        assertNotNull(sut.getGameState().getIndividuals().get(individualid));
//    }
//
//    @Test
//    void testStartGame() throws ModificationException, AbsentEntityException, IOException, AgentBuilderException {
//        // Arrange
//        sut.setGameState(mockedGameState);
//
//        ASDGameStateModifier sutSpy = spy(sut);
//        doNothing().when(sutSpy).startAgent(anyString());
//
//        when(mockedGameState.getIndividuals()).thenReturn(new HashMap<>());
//        World world = mock(World.class);
//        when(mockedGameState.getWorld()).thenReturn(world);
//        when(world.getTiles()).thenReturn(new WorldTile[][]{});
//        // Act
//        sutSpy.initGame(1, 1);
//
//        sutSpy.startGame();
//        GameState gameState = sutSpy.getGameState();
//        // Assert
//        verify(mockedGenerator).generateWorld(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt(), Mockito.any()); // gets called with atleast 5 bots
//    }
//
//    @Test
//    void testStartAgent() throws ModificationException, AgentBuilderException {
//        // Arrange
//        String id = "id";
//        Character character = mock(Character.class);
//        when(mockedGameState.hasIndividual(id)).thenReturn(true);
//        when(mockedGameState.getIndividual(id)).thenReturn(character);
//        when(character.getAgentJson()).thenReturn(Base64.getEncoder().encodeToString("agentJSON".getBytes()));
//        when(mockedAgentService.isAgentActive(character)).thenReturn(false);
//        // Act
//        sut.startAgent(id);
//        // Assert
//        verify(mockedGameState).getIndividual(id);
//        verify(mockedAgentService).isAgentActive(character);
//        verify(mockedAgentService).startAgent(any());
//    }
//
//    @Test
//    void testStartAgentWithNoIndividualThrowsException() {
//        // Arrange
//        // Act
//        // Assert
//        assertThrows(ModificationException.class, () -> {
//            sut.startAgent("");
//        });
//    }
//
//    @Test
//    void testStartAgentWithAgentEnabled() throws ModificationException, AgentBuilderException {
//        // Arrange
//        String id = "id";
//        Character character = mock(Character.class);
//        when(mockedGameState.hasIndividual(id)).thenReturn(true);
//        when(mockedGameState.getIndividual(id)).thenReturn(character);
//        when(character.getAgentJson()).thenReturn(Base64.getEncoder().encodeToString("agentJSON".getBytes()));
//        when(mockedAgentService.isAgentActive(character)).thenReturn(false);
//        // Act
//        sut.startAgent(id);
//        // Assert
//        verify(mockedAgentService).startAgent(any(Individual.class));
//    }
//
//
//    @Test
//    void testStopAgent() throws ModificationException {
//        // Arrange
//        String id = "id";
//        Character character = mock(Character.class);
//        when(mockedGameState.hasIndividual(id)).thenReturn(true);
//        when(mockedGameState.getIndividual(id)).thenReturn(character);
//        when(mockedAgentService.isAgentActive(character)).thenReturn(true);
//        // Act
//        sut.stopAgent(id);
//        // Assert
//        verify(mockedAgentService).stopAgent(character);
//        verify(mockedGameState).getIndividual(id);
//    }
//
//    @Test
//    void testStopAgentWithNoIndividualThrowsException() {
//        // Arrange
//        // Act
//        // Assert
//        assertThrows(ModificationException.class, () -> {
//            sut.stopAgent("");
//        });
//    }
//
//    @Test
//    void testStopAgentWithAgentDisabled() throws ModificationException {
//        // Arrange
//        String id = "id";
//        Character character = mock(Character.class);
//        when(mockedGameState.hasIndividual(id)).thenReturn(true);
//        when(mockedGameState.getIndividual(id)).thenReturn(character);
//        when(mockedAgentService.isAgentActive(character)).thenReturn(false);
//        // Act
//        // Assert
//        assertThrows(ModificationException.class, () -> {
//            sut.stopAgent(id);
//        });
//    }
//
//    @Test
//    void saveGameSavesTheGameToFile() throws ModificationException, IOException {
//        // Arrange
//
//        // Act
//        sut.saveGame();
//        // Assert
//        verify(fileHandlerMock).createFile(Mockito.any(), Mockito.any());
//    }
//
//    //<p>Integration test
//    //Relies on the <bold>@FileHandler</bold> to work</p>
//    GameState saveGameSetup() {
//        //Set the file handler and create a gamestate with a world, a character and an attribute
//
//        realFileHandler = new ASDFileHandler();
//        sut.setFileHandler(realFileHandler);
//        WorldTile[][] tiles = new WorldTile[1][1];
//        TrapTile trapTile = new TrapTile();
//        trapTile.setTrap(new Trap());
//        trapTile.setAttribute(new Potion("potion", "maxHp", 10));
//        tiles[0][0] = trapTile;
//        GameState gameState = new GameState(gameName);
//        World world = new World(tiles);
//        gameState.setWorld(world);
//        HashMap<String, Individual> individuals = new HashMap<>();
//        individuals.put("uuid", new Character(0, 0));
//        individuals.put("uuid2", new Character(1, 1));
//        gameState.setIndividuals(individuals);
//        sut.setGameState(gameState);
//        return gameState;
//    }
//
//    @Test
//    void saveGameThrowsModifcationExceptionWhenIOException() throws IOException {
//        doThrow(new IOException()).when(fileHandlerMock).createFile(any(), anyString());
//
//        assertThrows(ModificationException.class, () -> {
//            sut.saveGame();
//        });
//    }
//
//    @Test
//    void resumeGameThrowsModificationExceptionWhenIOException() throws IOException {
//        doThrow(new IOException()).when(fileHandlerMock).readFile(any());
//
//        assertThrows(ModificationException.class, () -> {
//            sut.resumeGame("test");
//        });
//    }
//
//    @Test
//    void checkIfSaveGameExistsReturnsFalseOnIOException() throws IOException {
//        doThrow(new IOException()).when(fileHandlerMock).readFile(any());
//
//        assertEquals(false, sut.saveGameExists("test"));
//    }
//
//    @Test
//    void checkIfSaveGameExistsReturnTrueIfExists() throws IOException {
//        when(fileHandlerMock.readFile(any())).thenReturn("save");
//
//        assertEquals(true, sut.saveGameExists("test"));
//    }
//
//    @Test
//    void savedGameStaysPersistentAfterSave() throws IOException, ModificationException {
//        // Arrange
//        GameState gameStateToCheck = saveGameSetup();
//        // Act
//        sut.saveGame();
//
//        final String result = realFileHandler.readFile(Path.of(
//                String.format("data/%s.gss", gameName)));
//        // Assert
//        assertEquals(new ObjectMapper().enableDefaultTyping().writeValueAsString(gameStateToCheck), result);
//    }
//
////    @Test
////    void resumeGameIsSameAsSavedGame() throws IOException, ModificationException {
////        // Arrange
////        GameState gameStateToCheck = saveGameSetup();
////        // Act
////        sut.saveGame();
////        sut.resumeGame(gameName);
////        // Assert
////        //assert if the gamestate made before the save, is the same as after resuming it by converting them both to json
////        assertEquals(new ObjectMapper().enableDefaultTyping().writeValueAsString(gameStateToCheck), new ObjectMapper().enableDefaultTyping().writeValueAsString(sut.getGameState()));
////    }
//
//    @Test
//    void testEndGameSetsStatus() {
//        // Arrange
//
//        // Act
//        sut.endGame();
//        // Assert
//        verify(mockedGameState).setStatus(GameStatus.FINISHED);
//    }
//
//    @Test
//    void testNextLocation(){
//        ASDVector asdVector = new ASDVector(0,0);
//        ASDVector expectedNorth = new ASDVector(0, -1);
//        ASDVector expectedEast = new ASDVector(1, 0);
//        ASDVector expectedSouth = new ASDVector(0, 1);
//        ASDVector expectedWest = new ASDVector(-1, 0);
//        assertEquals(sut.getNextLocation(asdVector, Direction.NORTH), expectedNorth);
//        assertEquals(sut.getNextLocation(asdVector, Direction.EAST), expectedEast);
//        assertEquals(sut.getNextLocation(asdVector, Direction.SOUTH), expectedSouth);
//        assertEquals(sut.getNextLocation(asdVector, Direction.WEST), expectedWest);
//
//    }
//}
