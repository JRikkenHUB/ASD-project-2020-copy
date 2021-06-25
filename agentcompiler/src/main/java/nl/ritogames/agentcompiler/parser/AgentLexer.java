package nl.ritogames.agentcompiler.parser;

import nl.ritogames.agentcompiler.Stage;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

/**
 * The type Agent lexer.
 */
public class AgentLexer implements Stage<String, CommonTokenStream> {

  @Override
  public CommonTokenStream execute(String source) {
    var charStream = createCharStream(source);
    var lexer = createAgentLexer(charStream);
    return createCommonTokenStream(lexer);
  }

  CharStream createCharStream(String text) {
    return CharStreams.fromString(text);
  }

  ASDAgentLexer createAgentLexer(CharStream stream) {
    return new ASDAgentLexer(stream);
  }

  CommonTokenStream createCommonTokenStream(ASDAgentLexer asdAgentLexer) {
    return new CommonTokenStream(asdAgentLexer);
  }
}