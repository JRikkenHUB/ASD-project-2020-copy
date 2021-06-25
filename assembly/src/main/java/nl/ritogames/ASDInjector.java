package nl.ritogames;

import com.google.inject.AbstractModule;
import nl.ritogames.agentcompiler.ASDAgentCompiler;
import nl.ritogames.chat.ASDChat;
import nl.ritogames.filehandler.ASDFileHandler;
import nl.ritogames.generator.ASDGenerator;
import nl.ritogames.intelligentagent.ASDAgentService;
import nl.ritogames.networkhandler.NetworkHandler;
import nl.ritogames.parser.ASDParser;
import nl.ritogames.ruleengine.ASDEventProcessor;
import nl.ritogames.shared.*;

public class ASDInjector extends AbstractModule {
    @Override
    protected void configure() {
        bind(Parser.class).to(ASDParser.class);
        bind(Chat.class).to(ASDChat.class);
        bind(AgentCompiler.class).to(ASDAgentCompiler.class);
        bind(EventProcessor.class).to(ASDEventProcessor.class);
        bind(EventSender.class).to(NetworkHandler.class);
        bind(Generator.class).to(ASDGenerator.class);
        bind(FileRepository.class).to(ASDFileHandler.class);
        bind(GameStateModifier.class).toInstance(new ASDGameStateModifier());
        bind(CommandExecutor.class).to(ASDCommandExecutor.class);
        bind(CommandSender.class).to(NetworkHandler.class);
        bind(GameStateContextProvider.class).toInstance(new ASDGameStateContextProvider());
        bind(CommandHandler.class).to(ASDCommandHandler.class);
        bind(AgentService.class).to(ASDAgentService.class);

    }
}
