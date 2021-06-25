package nl.ritogames.agentcompiler.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import nl.ritogames.shared.exception.CompilationException;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AgentLexerTest {

  private AgentLexer sut;

  @BeforeEach
  void setUp() {
    sut = new AgentLexer();
  }

  @Test
  void testThatExecuteReturnsATokenStream() throws CompilationException {
    //arrange
    var input = "any string";
    var stream = mock(CharStream.class);
    var lexer = mock(ASDAgentLexer.class);
    var tokenStream = mock(CommonTokenStream.class);
    var sutSpy = spy(sut);
    doReturn(stream).when(sutSpy).createCharStream(input);
    doReturn(lexer).when(sutSpy).createAgentLexer(stream);
    doReturn(tokenStream).when(sutSpy).createCommonTokenStream(lexer);

    //act
    var result = sutSpy.execute(input);

    //assert
    assertEquals(tokenStream, result);
  }
}
