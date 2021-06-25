package nl.ritogames;

import com.github.blindpirate.extensions.CaptureSystemOutput;
import com.google.inject.Guice;
import nl.ritogames.shared.exception.ParseInputException;
import nl.ritogames.shared.logger.Logger;
import nl.ritogames.trui.Trui;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;
@CaptureSystemOutput
public class ASDungeonTest {
ASDungeon asDungeon;
ArrayList<String> expectedLogs;

    @BeforeEach
    void setUp() {
        expectedLogs = new ArrayList<>();
        asDungeon = new ASDungeon();
        asDungeon.injector = Guice.createInjector(new ASDInjector());
        asDungeon.trui = asDungeon.injector.getInstance(Trui .class);
        Logger.setActive(true);
    }

    
    @Test
    void selectAgentTest(CaptureSystemOutput.OutputCapture outputCapture) throws ParseInputException {
        addExpectedAgentSelectLogs();
        asDungeon.trui.getParser().parseInput("agent select endtoend", "testUser");

        assertTrue(LogChecker.checkLogs(expectedLogs, outputCapture));
    }

    @Test
    void createGameTest(CaptureSystemOutput.OutputCapture outputCapture) throws ParseInputException {
        addExpectedAgentSelectLogs();
        addExpectedCreateGameLogs();

        asDungeon.trui.getParser().parseInput("agent select endtoend", "testUser");
        asDungeon.trui.getParser().parseInput("game create test", "testUser");

        assertTrue(LogChecker.checkLogs(expectedLogs, outputCapture));
    }

    @Test
    void joinGameTest(CaptureSystemOutput.OutputCapture outputCapture) throws ParseInputException {
        addExpectedAgentSelectLogs();
        
        expectedLogs.add("class nl.ritogames.parser.ASDParser, function: parseInput");
        expectedLogs.add("class nl.ritogames.parser.aliases.AliasReplacer, function: replaceAliases");
        expectedLogs.add("class nl.ritogames.parser.command.CommandMapper, function: getCommand");
        expectedLogs.add("class nl.ritogames.parser.command.ArgumentParser, function: mapArguments");
        expectedLogs.add("class nl.ritogames.parser.command.interactioncommand.GameCommand, function: toInteractionEvent");
        expectedLogs.add("class nl.ritogames.networkhandler.NetworkHandler, function: isUsingSwitch");
        expectedLogs.add("class nl.ritogames.networkhandler.connection.Connection, function: start");
        expectedLogs.add("class nl.ritogames.networkhandler.NetworkHandler, function: addConnection");
        expectedLogs.add("class nl.ritogames.networkhandler.NetworkHandler, function: addPeer");
        expectedLogs.add("class nl.ritogames.networkhandler.NetworkHandler, function: addLatency");
        expectedLogs.add("class nl.ritogames.networkhandler.NetworkHandler, function: sendEvent");
        expectedLogs.add("class nl.ritogames.filehandler.ASDFileHandler, function: readFile");
        expectedLogs.add("class nl.ritogames.networkhandler.NetworkHandler, function: sendPacket");

        asDungeon.trui.getParser().parseInput("agent select endtoend", "testUser");
        asDungeon.trui.getParser().parseInput("game join 1.1.1.1", "testUser");

        assertTrue(LogChecker.checkLogs(expectedLogs, outputCapture));
    }

    @Test
    void configureAgentsTest(CaptureSystemOutput.OutputCapture outputCapture) throws ParseInputException {
        addExpectedAgentConfigureLogs();
        
        asDungeon.trui.getParser().parseInput("agent configure", "testUser");

        assertTrue(LogChecker.checkLogs(expectedLogs, outputCapture));
    }

    @Test
    void agentListTest(CaptureSystemOutput.OutputCapture outputCapture) throws ParseInputException {
        addExpectedAgentConfigureLogs();

        expectedLogs.add("class nl.ritogames.parser.ASDParser, function: parseInput");
        expectedLogs.add("class nl.ritogames.parser.aliases.AliasReplacer, function: replaceAliases");
        expectedLogs.add("class nl.ritogames.parser.command.CommandMapper, function: getCommand");
        expectedLogs.add("class nl.ritogames.parser.command.ArgumentParser, function: mapArguments");
        expectedLogs.add("class nl.ritogames.parser.command.agentcommand.ListAgentCommand, function: toInteractionEvent");
        expectedLogs.add("class nl.ritogames.filehandler.ASDFileHandler, function: getFilesInDirectory");
        
        asDungeon.trui.getParser().parseInput("agent configure", "testUser");
        asDungeon.trui.getParser().parseInput("agent list", "testUser");

        assertTrue(LogChecker.checkLogs(expectedLogs, outputCapture));
    }

    @Test
    void agentCreateAndDeleteTest(CaptureSystemOutput.OutputCapture outputCapture) throws ParseInputException {
        addExpectedAgentConfigureLogs();

        expectedLogs.add("class nl.ritogames.parser.ASDParser, function: parseInput");
        expectedLogs.add("class nl.ritogames.parser.aliases.AliasReplacer, function: replaceAliases");
        expectedLogs.add("class nl.ritogames.parser.command.CommandMapper, function: getCommand");
        expectedLogs.add("class nl.ritogames.parser.command.ArgumentParser, function: mapArguments");
        expectedLogs.add("class nl.ritogames.parser.command.agentcommand.CreateAgentCommand, function: toInteractionEvent");
        expectedLogs.add("class nl.ritogames.filehandler.ASDFileHandler, function: createFile");
        
        expectedLogs.add("class nl.ritogames.parser.ASDParser, function: parseInput");
        expectedLogs.add("class nl.ritogames.parser.aliases.AliasReplacer, function: replaceAliases");
        expectedLogs.add("class nl.ritogames.parser.command.CommandMapper, function: getCommand");
        expectedLogs.add("class nl.ritogames.parser.command.ArgumentParser, function: mapArguments");
        expectedLogs.add("class nl.ritogames.parser.command.agentcommand.DeleteAgentCommand, function: toInteractionEvent");
        expectedLogs.add("class nl.ritogames.filehandler.ASDFileHandler, function: deleteFile");
        expectedLogs.add("class nl.ritogames.filehandler.ASDFileHandler, function: deleteFile");
        
        asDungeon.trui.getParser().parseInput("agent configure", "testUser");
        asDungeon.trui.getParser().parseInput("agent create testagent", "testUser");
        asDungeon.trui.getParser().parseInput("deleteagent testagent", "testUser");
        
        assertTrue(LogChecker.checkLogs(expectedLogs, outputCapture));
    }

    @Test
    void agentConfigureExit(CaptureSystemOutput.OutputCapture outputCapture) throws ParseInputException {
        addExpectedAgentConfigureLogs();

        expectedLogs.add("class nl.ritogames.parser.ASDParser, function: parseInput");
        expectedLogs.add("class nl.ritogames.parser.aliases.AliasReplacer, function: replaceAliases");
        expectedLogs.add("class nl.ritogames.parser.command.CommandMapper, function: getCommand");
        expectedLogs.add("class nl.ritogames.parser.command.ArgumentParser, function: mapArguments");
        expectedLogs.add("class nl.ritogames.parser.command.menucommand.BackCommand, function: toInteractionEvent");

        asDungeon.trui.getParser().parseInput("agent configure", "testUser");
        asDungeon.trui.getParser().parseInput("back", "testUser");

        assertTrue(LogChecker.checkLogs(expectedLogs, outputCapture));
    }

    @Test
    void startGameTest(CaptureSystemOutput.OutputCapture outputCapture) throws ParseInputException {
        addExpectedAgentSelectLogs();
        addExpectedCreateGameLogs();
        addExpectedStartGameLogs();

        asDungeon.trui.getParser().parseInput("agent select endtoend", "testUser");
        asDungeon.trui.getParser().parseInput("game create test", "testUser");
        asDungeon.trui.getParser().parseInput("game start test", "testUser");

        assertTrue(LogChecker.checkLogs(expectedLogs, outputCapture));
    }

    @Test
    void attackEventTest(CaptureSystemOutput.OutputCapture outputCapture) throws ParseInputException {
        addExpectedAgentSelectLogs();
        addExpectedCreateGameLogs();
        addExpectedStartGameLogs();

        expectedLogs.add("class nl.ritogames.parser.ASDParser, function: parseInput");
        expectedLogs.add("class nl.ritogames.parser.aliases.AliasReplacer, function: replaceAliases");
        expectedLogs.add("class nl.ritogames.parser.command.CommandMapper, function: getCommand");
        expectedLogs.add("class nl.ritogames.parser.command.ArgumentParser, function: mapArguments");
        expectedLogs.add("class nl.ritogames.parser.command.interactioncommand.AttackInteractionCommand, function: toInteractionEvent");
        expectedLogs.add("class nl.ritogames.networkhandler.NetworkHandler, function: sendEvent");
        expectedLogs.add("class nl.ritogames.ruleengine.ASDEventProcessor, function: handleEvent");

        asDungeon.trui.getParser().parseInput("agent select endtoend", "testUser");
        asDungeon.trui.getParser().parseInput("game create test", "testUser");
        asDungeon.trui.getParser().parseInput("game start test", "testUser");
        asDungeon.trui.getParser().parseInput("attack north", "testUser");

        assertTrue(LogChecker.checkLogs(expectedLogs, outputCapture));
    }

    @Test
    void pickupEventTest(CaptureSystemOutput.OutputCapture outputCapture) throws ParseInputException {
        addExpectedAgentSelectLogs();
        addExpectedCreateGameLogs();
        addExpectedStartGameLogs();

        expectedLogs.add("class nl.ritogames.parser.ASDParser, function: parseInput");
        expectedLogs.add("class nl.ritogames.parser.aliases.AliasReplacer, function: replaceAliases");
        expectedLogs.add("class nl.ritogames.parser.command.CommandMapper, function: getCommand");
        expectedLogs.add("class nl.ritogames.parser.command.ArgumentParser, function: mapArguments");
        expectedLogs.add("class nl.ritogames.parser.command.interactioncommand.PickupInteractionCommand, function: toInteractionEvent");
        expectedLogs.add("class nl.ritogames.networkhandler.NetworkHandler, function: sendEvent");
        expectedLogs.add("class nl.ritogames.ruleengine.ASDEventProcessor, function: handleEvent");

        asDungeon.trui.getParser().parseInput("agent select endtoend", "testUser");
        asDungeon.trui.getParser().parseInput("game create test", "testUser");
        asDungeon.trui.getParser().parseInput("game start test", "testUser");
        asDungeon.trui.getParser().parseInput("pickup north", "testUser");

        assertTrue(LogChecker.checkLogs(expectedLogs, outputCapture));
    }

    @Test
    void moveEventTest(CaptureSystemOutput.OutputCapture outputCapture) throws ParseInputException {
        addExpectedAgentSelectLogs();
        addExpectedCreateGameLogs();
        addExpectedStartGameLogs();

        expectedLogs.add("class nl.ritogames.parser.ASDParser, function: parseInput");
        expectedLogs.add("class nl.ritogames.parser.aliases.AliasReplacer, function: replaceAliases");
        expectedLogs.add("class nl.ritogames.parser.command.CommandMapper, function: getCommand");
        expectedLogs.add("class nl.ritogames.parser.command.ArgumentParser, function: mapArguments");
        expectedLogs.add("class nl.ritogames.parser.command.interactioncommand.MoveInteractionCommand, function: toInteractionEvent");
        expectedLogs.add("class nl.ritogames.networkhandler.NetworkHandler, function: sendEvent");
        expectedLogs.add("class nl.ritogames.ruleengine.ASDEventProcessor, function: handleEvent");

        asDungeon.trui.getParser().parseInput("agent select endtoend", "testUser");
        asDungeon.trui.getParser().parseInput("game create test", "testUser");
        asDungeon.trui.getParser().parseInput("game start test", "testUser");
        asDungeon.trui.getParser().parseInput("move north", "testUser");

        assertTrue(LogChecker.checkLogs(expectedLogs, outputCapture));
    }

    @Test
    void afkTest(CaptureSystemOutput.OutputCapture outputCapture) throws ParseInputException {
        addExpectedAgentSelectLogs();
        addExpectedCreateGameLogs();
        addExpectedStartGameLogs();
        addExpectedAfkLogs();

        asDungeon.trui.getParser().parseInput("agent select endtoend", "testUser");
        asDungeon.trui.getParser().parseInput("game create test", "testUser");
        asDungeon.trui.getParser().parseInput("game start test", "testUser");
        asDungeon.trui.getParser().parseInput("afk", "testUser");

        assertTrue(LogChecker.checkLogs(expectedLogs, outputCapture));
    }

    @Test
    void presentTest(CaptureSystemOutput.OutputCapture outputCapture) throws ParseInputException {
        addExpectedAgentSelectLogs();
        addExpectedCreateGameLogs();
        addExpectedStartGameLogs();
        addExpectedAfkLogs();

        expectedLogs.add("class nl.ritogames.parser.ASDParser, function: parseInput");
        expectedLogs.add("class nl.ritogames.parser.aliases.AliasReplacer, function: replaceAliases");
        expectedLogs.add("class nl.ritogames.parser.command.CommandMapper, function: getCommand");
        expectedLogs.add("class nl.ritogames.parser.command.ArgumentParser, function: mapArguments");
        expectedLogs.add("class nl.ritogames.parser.command.agentcommand.StopAgentCommand, function: toInteractionEvent");
        expectedLogs.add("class nl.ritogames.networkhandler.NetworkHandler, function: sendEvent");
        expectedLogs.add("class nl.ritogames.ruleengine.ASDEventProcessor, function: handleEvent");

        asDungeon.trui.getParser().parseInput("agent select endtoend", "testUser");
        asDungeon.trui.getParser().parseInput("game create test", "testUser");
        asDungeon.trui.getParser().parseInput("game start test", "testUser");
        asDungeon.trui.getParser().parseInput("afk", "testUser");
        asDungeon.trui.getParser().parseInput("present", "testUser");

        assertTrue(LogChecker.checkLogs(expectedLogs, outputCapture));
    }
    
    void addExpectedAgentSelectLogs() {
        expectedLogs.add("class nl.ritogames.parser.ASDParser, function: parseInput");
        expectedLogs.add("class nl.ritogames.parser.aliases.AliasReplacer, function: replaceAliases");
        expectedLogs.add("class nl.ritogames.parser.command.CommandMapper, function: getCommand");
        expectedLogs.add("class nl.ritogames.parser.command.ArgumentParser, function: mapArguments");
        expectedLogs.add("class nl.ritogames.parser.command.agentcommand.SelectAgentCommand, function: toInteractionEvent");
        expectedLogs.add("class nl.ritogames.agentcompiler.ASDAgentCompiler, function: compile");
        expectedLogs.add("class nl.ritogames.filehandler.ASDFileHandler, function: readFile");
        expectedLogs.add("class nl.ritogames.filehandler.ASDFileHandler, function: createFile");
        expectedLogs.add("class nl.ritogames.filehandler.ASDFileHandler, function: getFilesInDirectory");
    }
    
    void addExpectedCreateGameLogs() {
        expectedLogs.add("class nl.ritogames.parser.ASDParser, function: parseInput");
        expectedLogs.add("class nl.ritogames.parser.aliases.AliasReplacer, function: replaceAliases");
        expectedLogs.add("class nl.ritogames.parser.command.CommandMapper, function: getCommand");
        expectedLogs.add("class nl.ritogames.parser.command.ArgumentParser, function: mapArguments");
        expectedLogs.add("class nl.ritogames.parser.command.interactioncommand.GameCommand, function: toInteractionEvent");
        expectedLogs.add("class nl.ritogames.parser.command.interactioncommand.GameCommand, function: createCreateGameEvent");
        expectedLogs.add("class nl.ritogames.filehandler.ASDFileHandler, function: readFile");
        expectedLogs.add("class nl.ritogames.networkhandler.NetworkHandler, function: createSession");
        expectedLogs.add("class nl.ritogames.networkhandler.NetworkHandler, function: isUsingSwitch");
        expectedLogs.add("class nl.ritogames.networkhandler.connection.Connection, function: start");
        expectedLogs.add("class nl.ritogames.networkhandler.NetworkHandler, function: addConnection");
        expectedLogs.add("class nl.ritogames.networkhandler.NetworkHandler, function: addPeer");
        expectedLogs.add("class nl.ritogames.networkhandler.NetworkHandler, function: addLatency");
        expectedLogs.add("class nl.ritogames.networkhandler.NetworkHandler, function: sendEvent");
        expectedLogs.add("class nl.ritogames.ruleengine.ASDEventProcessor, function: handleEvent");
        expectedLogs.add("class nl.ritogames.ASDCommandHandler, function: handleEventIntoCommand");
        expectedLogs.add("class nl.ritogames.ASDCommandHandler, function: commitCommand");
        expectedLogs.add("class nl.ritogames.networkhandler.NetworkHandler, function: sendPacket");
        expectedLogs.add("class nl.ritogames.ASDCommandExecutor, function: executeCommand");
        expectedLogs.add("class nl.ritogames.filehandler.ASDFileHandler, function: readFile");
        expectedLogs.add("class nl.ritogames.generator.ASDGenerator, function: generateSeed");
        expectedLogs.add("class nl.ritogames.ASDGameStateContextProvider, function: processCommand");
    }

    void addExpectedStartGameLogs() {
        expectedLogs.add("class nl.ritogames.parser.ASDParser, function: parseInput");
        expectedLogs.add("class nl.ritogames.parser.aliases.AliasReplacer, function: replaceAliases");
        expectedLogs.add("class nl.ritogames.parser.command.CommandMapper, function: getCommand");
        expectedLogs.add("class nl.ritogames.parser.command.ArgumentParser, function: mapArguments");
        expectedLogs.add("class nl.ritogames.parser.command.interactioncommand.GameCommand, function: toInteractionEvent");
        expectedLogs.add("class nl.ritogames.parser.command.interactioncommand.GameCommand, function: createStartGameEvent");
        expectedLogs.add("class nl.ritogames.networkhandler.NetworkHandler, function: sendEvent");
        expectedLogs.add("class nl.ritogames.ruleengine.ASDEventProcessor, function: handleEvent");
        expectedLogs.add("class nl.ritogames.ASDCommandHandler, function: handleEventIntoCommand");
        expectedLogs.add("class nl.ritogames.ASDCommandHandler, function: commitCommand");
        expectedLogs.add("class nl.ritogames.networkhandler.NetworkHandler, function: sendPacket");
        expectedLogs.add("class nl.ritogames.ASDCommandExecutor, function: executeCommand");
        expectedLogs.add("class nl.ritogames.generator.ASDGenerator, function: generateWorld");
        expectedLogs.add("class nl.ritogames.ASDGameStateContextProvider, function: processCommand");
    }
    
    void addExpectedAfkLogs() {
        expectedLogs.add("class nl.ritogames.parser.ASDParser, function: parseInput");
        expectedLogs.add("class nl.ritogames.parser.aliases.AliasReplacer, function: replaceAliases");
        expectedLogs.add("class nl.ritogames.parser.command.CommandMapper, function: getCommand");
        expectedLogs.add("class nl.ritogames.parser.command.ArgumentParser, function: mapArguments");
        expectedLogs.add("class nl.ritogames.parser.command.interactioncommand.AwayInteractionCommand, function: toInteractionEvent");
        expectedLogs.add("class nl.ritogames.networkhandler.NetworkHandler, function: sendEvent");
        expectedLogs.add("class nl.ritogames.ruleengine.ASDEventProcessor, function: handleEvent");
    }
    
    void addExpectedAgentConfigureLogs() {
        expectedLogs.add("class nl.ritogames.parser.ASDParser, function: parseInput");
        expectedLogs.add("class nl.ritogames.parser.aliases.AliasReplacer, function: replaceAliases");
        expectedLogs.add("class nl.ritogames.parser.command.CommandMapper, function: getCommand");
        expectedLogs.add("class nl.ritogames.parser.command.ArgumentParser, function: mapArguments");
        expectedLogs.add("class nl.ritogames.parser.command.menucommand.ConfigureAgentCommand, function: toInteractionEvent");
    }
}
