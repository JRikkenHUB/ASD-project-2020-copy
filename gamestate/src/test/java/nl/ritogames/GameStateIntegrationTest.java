package nl.ritogames;

/**
 * This class contains the integration test for the GameState component
 */
class GameStateIntegrationTest {
//    @Test
//    void integrationTest() throws CommandFailedException {
//        // Arrange
//        ASDCommandExecutor executor = new ASDCommandExecutor();
//        GameStateModifier modifier = new ASDGameStateModifier();
//        executor.setGameStateModifier(modifier);
//        ASDGameStateContextProvider contextProvider = new ASDGameStateContextProvider(5,5);
//        executor.setContextProvider(contextProvider);
//        //set up commands for the executor to execute
//        String game = "INTEGRATION_GAME";
//        String[] players = {"HOST", "SomePlayer", "SomeOtherPlayer"};
//        ArrayList<Command> commands = new ArrayList<>();
//        commands.add(new CreateGameCommand(game,players[0]));
//        commands.add(new JoinGameCommand(players[0]));
//        commands.add(new StartGameCommand(game));
//        commands.add(new MoveCommand(players[0], Direction.SOUTH));
//        // Act
//        //execute the commands
//        for (Command command: commands) {
//            executor.executeCommand(command);
//        }
//        // Assert
//        //check if the gamestate was correctly changed
//        assertEquals(game, modifier.getGameState().getName());
//        assertTrue(modifier.getGameState().hasIndividual(players[0]));
//        assertEquals(STARTED,modifier.getGameState().getStatus());
//    }
}
