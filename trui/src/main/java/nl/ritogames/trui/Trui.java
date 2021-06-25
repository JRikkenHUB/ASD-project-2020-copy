package nl.ritogames.trui;

import nl.ritogames.shared.Chat;
import nl.ritogames.shared.GameStateContextListener;
import nl.ritogames.shared.GameStateContextProvider;
import nl.ritogames.shared.Parser;
import nl.ritogames.shared.dto.GameStateContext;
import nl.ritogames.shared.dto.event.*;
import nl.ritogames.shared.exception.AgentIsNotActiveException;
import nl.ritogames.shared.exception.CommandNotFoundException;
import nl.ritogames.shared.exception.CompilationException;
import nl.ritogames.shared.exception.InvalidArgumentException;
import nl.ritogames.shared.exception.AgentNotFoundException;
import nl.ritogames.trui.enums.ScreenState;
import nl.ritogames.trui.exception.ExceptionHandler;
import nl.ritogames.trui.exception.mappers.AgentIsNotActiveExceptionMapper;
import nl.ritogames.trui.exception.mappers.AgentNotFoundExceptionMapper;
import nl.ritogames.trui.exception.mappers.CommandNotFoundExceptionMapper;
import nl.ritogames.trui.exception.mappers.CompilationExceptionMapper;
import nl.ritogames.trui.exception.mappers.InvalidArgumentExceptionMapper;
import nl.ritogames.trui.input.InputHandler;
import nl.ritogames.trui.input.ScannerImpl;
import nl.ritogames.trui.render.OnCommandRenderStrategy;
import nl.ritogames.trui.render.RealtimeRenderStrategy;
import nl.ritogames.trui.render.RenderStrategy;
import nl.ritogames.trui.render.RenderStrategy.Strategy;
import nl.ritogames.trui.render.ScreenRenderer;
import nl.ritogames.trui.render.message.Message;
import nl.ritogames.trui.render.message.MessageFactory;

import javax.inject.Inject;
import java.io.InputStreamReader;


/**
 * The runnable app of the command line interface
 */
public class Trui implements GameStateContextListener {

  private static final String SENDER_AGENT = "Agent";

  GameStateContextProvider contextProvider;
  Chat chat;
  Parser parser;
  ScreenRenderer renderer;
  InputHandler inputHandler;
  RenderStrategy renderStrategy;
  ExceptionHandler exceptionHandler;
  MessageFactory messageFactory;

  boolean running;

  String individualId = "";

  public Trui() {
    this.renderer = new ScreenRenderer();
    this.inputHandler = new InputHandler(new ScannerImpl(new InputStreamReader(System.in)));
    this.setExceptionHandler(new ExceptionHandler());
    this.setMessageFactory(new MessageFactory());
  }

  /**
   * <p>Sets the strategy and puts up the interface</p>
   */
  public void start() {
    this.running = true;
    while (running) {
      run();
    }
  }

  /**
   * <p>Keeps running during the game it will make sure the game is handled correctly and can be
   * exited</p>
   */
  void run() {
    if (this.individualId.isEmpty()) {
      requestIndividualId();
    } else {
      renderGame();
    }
  }

  private boolean inputIsExit() {
    return inputHandler.peek().equalsIgnoreCase("exit");
  }

  private void closeGame() {
    checkSubscriptionAndUnsubscribe();
    running = false;
    renderer.renderExitScreen();
  }

  private void renderGame() {
    this.inputHandler.requestInput();
    if (inputIsExit()) {
      closeGame();
    } else {
      handleSubscribedState();
      try {
        this.renderStrategy.update(this.individualId);
      } catch (Exception e) {
        exceptionHandler.handleException(e);
//        e.printStackTrace();
        this.renderer.render(getContext());
      }
    }
  }

  private void handleSubscribedState() {
    if (renderer.isAtState(ScreenState.GAME) || renderer.isAtState(ScreenState.LOBBY)) {
      if (!contextProvider.isSubscribed(this.individualId)) {
        this.contextProvider.subscribe(this, this.individualId);
      }
    } else checkSubscriptionAndUnsubscribe();
  }

  private void checkSubscriptionAndUnsubscribe() {
    if (this.contextProvider.isSubscribed(this.individualId)) {
      this.contextProvider.unsubscribe(this.individualId);
    }
  }

  private void requestIndividualId() {
    this.renderer.render(getContext());
    this.inputHandler.requestInput();
    if (!this.inputHandler.isEmpty()) {
      this.individualId = this.inputHandler.removeAndFetchInput();
      this.renderer.setState(ScreenState.MENU);
      this.renderer.render(getContext());
      this.renderStrategy = new OnCommandRenderStrategy(this, parser, renderer, inputHandler,
          contextProvider);
    }
  }

  private GameStateContext getContext() {
    return this.contextProvider.getContext(this.individualId);
  }

  /**
   * <p>Sets it to the corresponding implementation of {@link RenderStrategy}</p>
   */
  public void toggleRenderStrategy() {
    if (this.renderStrategy.getStrategy() == Strategy.ONCOMMAND) {
      this.renderStrategy = new RealtimeRenderStrategy(this.renderer, this.inputHandler, this);
    } else {
      this.renderStrategy = new OnCommandRenderStrategy(this, parser, renderer, inputHandler,
          this.contextProvider);
    }
    this.renderStrategy.displayToggleMessage(this.individualId);
  }

  @Inject
  public void setContextProvider(GameStateContextProvider contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Inject
  public void setChat(Chat chat) {
    this.chat = chat;
  }

  @Inject
  public void setParser(Parser parser) {
    this.parser = parser;
    this.registerListeners();
  }

  public String getIndividualId() {
    return this.individualId;
  }

  public void setMessageFactory(MessageFactory messageFactory) {
    this.messageFactory = messageFactory;
  }

  public void setExceptionHandler(ExceptionHandler exceptionHandler) {
    this.exceptionHandler = exceptionHandler;
    this.registerExceptions();
  }

  private void registerExceptions() {
    this.exceptionHandler.addExceptionMapper(CommandNotFoundException.class,
        new CommandNotFoundExceptionMapper(this.renderer));
    this.exceptionHandler.addExceptionMapper(InvalidArgumentException.class,
        new InvalidArgumentExceptionMapper(renderer));
    this.exceptionHandler.addExceptionMapper(AgentIsNotActiveException.class,
            new AgentIsNotActiveExceptionMapper(this.renderer));
    this.exceptionHandler.addExceptionMapper(CompilationException.class,
            new CompilationExceptionMapper(this.renderer));
    this.exceptionHandler.addExceptionMapper(AgentNotFoundException.class,
            new AgentNotFoundExceptionMapper(this.renderer));
  }

  private void registerListeners() {
    this.parser.registerEventListener(BackEvent.class, this::onBackEvent);
    this.parser.registerEventListener(AgentConfigureEvent.class, this::onAgentConfigureEvent);
    this.parser.registerEventListener(AgentsListedEvent.class, this::onListedAgentEvent);
    this.parser.registerEventListener(CreateAgentEvent.class, this::onCreateAgentEvent);
    this.parser.registerEventListener(SelectAgentEvent.class, this::onSelectAgentEvent);
  }

  private void onBackEvent(BackEvent backEvent) {
    this.renderer.setState(ScreenState.MENU);
  }

  private void onAgentConfigureEvent(AgentConfigureEvent event) {
    this.renderer.setState(ScreenState.AGENTMENU);
  }

  private void onListedAgentEvent(AgentsListedEvent agentsListedEvent) {
    this.renderer.addMessage(new Message("Found the following agents: ", SENDER_AGENT));
    agentsListedEvent.getAgents().forEach(agent -> this.renderer.addMessage(new Message(agent, SENDER_AGENT)));
  }

  private void onCreateAgentEvent(CreateAgentEvent createAgentEvent) {
    this.renderer.addMessage(new Message(String.format(
        "Created agent with name %s. Open the file in a text editor to start editing your agent",
        createAgentEvent.getAgentName()),
        SENDER_AGENT));
  }

  private void onSelectAgentEvent(SelectAgentEvent selectAgentEvent) {
    this.renderer.addMessage(new Message(String
        .format("Selected agent with name %s as your active agent!",
            selectAgentEvent.getAgentName()), SENDER_AGENT));
  }

  @Override
  public void updateContext(GameStateContext context) {
    //cue message
    renderer.addMessage(this.messageFactory.fromCommand(context.lastCommand));
    //tell realtime render strategy to render.
    if (renderStrategy instanceof RealtimeRenderStrategy) {
      ((RealtimeRenderStrategy) renderStrategy).updateContext(context);
    }
  }

  public Parser getParser() {
    return parser;
  }

  @Override
  public void unsubscribedSignal() {
    renderer.setState(ScreenState.MENU);
    renderer.addMessage(new Message("You died and have returned to the main menu!", "Game"));
  }
}
