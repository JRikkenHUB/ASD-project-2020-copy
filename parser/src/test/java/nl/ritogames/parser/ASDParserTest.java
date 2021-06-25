package nl.ritogames.parser;

import nl.ritogames.parser.aliases.AliasReplacer;
import nl.ritogames.parser.command.Command;
import nl.ritogames.parser.command.CommandMapper;
import nl.ritogames.parser.event.EventDistributor;
import nl.ritogames.shared.AgentCompiler;
import nl.ritogames.shared.Chat;
import nl.ritogames.shared.EventSender;
import nl.ritogames.shared.dto.event.InteractionEvent;
import nl.ritogames.shared.exception.CommandNotFoundException;
import nl.ritogames.shared.exception.InvalidArgumentException;
import nl.ritogames.shared.exception.ParseInputException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ASDParserTest {

  private ASDParser sut;
  private CommandMapper commandMapper;
  private AliasReplacer aliasReplacer;
  private EventDistributor eventDistributor;
  private Command command;
  private Chat chat;
  private String individualId;

  @BeforeEach
  void setUp() {
    sut = new ASDParser();
    commandMapper = mock(CommandMapper.class);
    aliasReplacer = mock(AliasReplacer.class);
    EventSender eventSender = mock(EventSender.class);
    command = mock(Command.class);
    chat = mock(Chat.class);
    AgentCompiler agentCompiler = mock(AgentCompiler.class);
    eventDistributor = mock(EventDistributor.class);
    this.individualId = "any id";
    sut.setChat(chat);
    sut.setEventSender(eventSender);
    sut.setAgentCompiler(agentCompiler);
    sut.setEventDistributor(eventDistributor);
    sut.setAliasReplacer(aliasReplacer);
    sut.setCommandMapper(commandMapper);
  }

  @Test
  void parseInputShouldThrowCommandNotFoundException() throws ParseInputException {
    //arrange
    String input = "in";
    String output = "out";
    when(aliasReplacer.replaceAliases(anyString())).thenReturn(output);
    when(commandMapper.getCommand(output)).thenThrow(new CommandNotFoundException(anyString()));
    //act + assert
    assertThrows(CommandNotFoundException.class, () -> sut.parseInput(input, individualId));
  }

  @Test
  void parseInputShouldThrowInvalidArgumentException() throws ParseInputException {
    //arrange
    String input = "in";
    String output = "out";
    var expectedException = new InvalidArgumentException("", "", "");
    when(aliasReplacer.replaceAliases(anyString())).thenReturn(output);
    when(commandMapper.getCommand(output)).thenReturn(command);
    when(command.toInteractionEvent(input)).thenThrow(expectedException);
    //act + assert
    assertThrows(InvalidArgumentException.class, () -> sut.parseInput(input, individualId));

  }

  @Test
  void parseInputShouldCallEventSender() throws ParseInputException {
    //arrange
    String input = "in";
    String output = "out";
    InteractionEvent result = mock(InteractionEvent.class);
    when(aliasReplacer.replaceAliases(anyString())).thenReturn(output);
    when(commandMapper.getCommand(output)).thenReturn(command);
    when(command.toInteractionEvent(input)).thenReturn(result);

    //act
    sut.parseInput(input, individualId);

    //assert
    verify(eventDistributor, times(1)).distribute(result);
  }
}
