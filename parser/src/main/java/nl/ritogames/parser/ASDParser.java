package nl.ritogames.parser;

import nl.ritogames.parser.aliases.AliasReplacer;
import nl.ritogames.parser.command.Command;
import nl.ritogames.parser.command.CommandMapper;
import nl.ritogames.parser.command.agentcommand.*;
import nl.ritogames.parser.command.interactioncommand.*;
import nl.ritogames.parser.command.menucommand.BackCommand;
import nl.ritogames.parser.command.menucommand.ConfigureAgentCommand;
import nl.ritogames.parser.event.EventDistributor;
import nl.ritogames.parser.event.MultiListenerEventDistributor;
import nl.ritogames.parser.event.listener.*;
import nl.ritogames.shared.*;
import nl.ritogames.shared.dto.event.*;
import nl.ritogames.shared.exception.ParseInputException;
import nl.ritogames.shared.logger.Logger;

import javax.inject.Inject;

public class ASDParser implements Parser {

  public static final String CREATEAGENT = "createagent";
  private EventSender eventSender;
  private Chat chat;
  private AgentCompiler agentCompiler;
  private CommandMapper commandMapper;
  private AliasReplacer aliasReplacer;
  private EventDistributor eventDistributor;

  public ASDParser() {
    this.setAliasReplacer(new AliasReplacer());
    this.setCommandMapper(new CommandMapper());
  }

  @Override
  public void parseInput(String input, String individualId) throws ParseInputException {
    Logger.logMethodCall(this);
    String lowered = input.toLowerCase();
    String replaced = aliasReplacer.replaceAliases(lowered);
    Command command = commandMapper.getCommand(replaced);
    Event event = command.toInteractionEvent(input);
    event.setIndividualId(individualId);
    eventDistributor.distribute(event);
  }

  public void registerEventDistributor() {
    if (this.eventSender != null && this.chat != null && this.agentCompiler != null) {
      this.setEventDistributor(new MultiListenerEventDistributor());
    }
  }

  @Inject
  public void setEventSender(EventSender eventSender) {
    this.eventSender = eventSender;
    this.registerEventDistributor();
  }

  public void setCommandMapper(CommandMapper commandMapper) {
    this.commandMapper = commandMapper;
    registerCommands();
  }

  public void setAliasReplacer(AliasReplacer aliasReplacer) {
    this.aliasReplacer = aliasReplacer;
    registerAlternatives();
  }

  public void setEventDistributor(EventDistributor eventDistributor) {
    this.eventDistributor = eventDistributor;
    this.registerEventListeners();
  }

  private void registerEventListeners() {
    this.registerEventListener(MoveEvent.class, new GameEventListener<>(eventSender));
    this.registerEventListener(AttackEvent.class, new GameEventListener<>(eventSender));
    this.registerEventListener(PickUpEvent.class, new GameEventListener<>(eventSender));
    this.registerEventListener(StartAgentEvent.class, new GameEventListener<>(eventSender));
    this.registerEventListener(StopAgentEvent.class, new GameEventListener<>(eventSender));
    this.registerEventListener(StartGameEvent.class, new StartGameEventListener(eventSender));
    this.registerEventListener(CreateGameEvent.class,
        new CreateGameEventListener(eventSender, agentCompiler));
    this.registerEventListener(GameJoinEvent.class,
        new GameJoinEventListener(eventSender, agentCompiler));
    this.registerEventListener(CreateAgentEvent.class, new CreateAgentEventListener(agentCompiler));
    this.registerEventListener(SelectAgentEvent.class, new SelectAgentEventListener(agentCompiler));
    this.registerEventListener(DeleteAgentEvent.class, new DeleteAgentEventListener(agentCompiler));
    this.registerEventListener(ListAgentEvent.class,
        new ListAgentEventListener(agentCompiler, this.eventDistributor));
    this.registerEventListener(ChatEvent.class, new ChatEventListener(chat));
  }


  private void registerAlternatives() {
    aliasReplacer.registerAlias("new game", "game create");
    aliasReplacer.registerAlias("agent create", CREATEAGENT);
    aliasReplacer.registerAlias("agent select", "selectagent");
    aliasReplacer.registerAlias("agent list", "listagent");
    aliasReplacer.registerAlias("grab", "pickup");
    aliasReplacer.registerAlias("up", "north");
    aliasReplacer.registerAlias("down", "south");
    aliasReplacer.registerAlias("left", "west");
    aliasReplacer.registerAlias("right", "east");
    aliasReplacer.registerAlias("hit", "attack");
    aliasReplacer.registerAlias("newagent", CREATEAGENT);
    aliasReplacer.registerAlias("afk", "away");
  }

  @Inject
  public void setChat(Chat chat) {
    this.chat = chat;
    this.registerEventDistributor();
  }

  @Inject
  public void setAgentCompiler(AgentCompiler agentCompiler) {
    this.agentCompiler = agentCompiler;
    this.registerEventDistributor();
  }

  private void registerCommands() {
    commandMapper.addCommand("game", GameCommand.class);
    commandMapper.addCommand("attack", AttackInteractionCommand.class);
    commandMapper.addCommand("pickup", PickupInteractionCommand.class);
    commandMapper.addCommand("move", MoveInteractionCommand.class);
    commandMapper.addCommand("agent", ConfigureAgentCommand.class);
    commandMapper.addCommand(CREATEAGENT, CreateAgentCommand.class);
    commandMapper.addCommand("back", BackCommand.class);
    commandMapper.addCommand("away", AwayInteractionCommand.class);
    commandMapper.addCommand("listagent", ListAgentCommand.class);
    commandMapper.addCommand("editagent", EditAgentCommand.class);
    commandMapper.addCommand("deleteagent", DeleteAgentCommand.class);
    commandMapper.addCommand("selectagent", SelectAgentCommand.class);
    commandMapper.addCommand("present", StopAgentCommand.class);
  }

  /**
   * method to register a handler to a type of event. Supports multiple events.
   *
   * @param eventType
   * @param eventListener
   * @param <T>
   */
  public <T extends Event> void registerEventListener(Class<T> eventType,
      EventListener<T> eventListener) {
    this.eventDistributor.registerListener(eventType, eventListener);
  }

}
