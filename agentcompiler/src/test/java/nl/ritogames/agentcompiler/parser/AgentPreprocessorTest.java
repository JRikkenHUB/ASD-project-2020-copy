package nl.ritogames.agentcompiler.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import nl.ritogames.shared.exception.CompilationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AgentPreprocessorTest {

  private AgentPreprocessor sut;

  @BeforeEach
  void setUp() {
    sut = new AgentPreprocessor();
  }

  @Test
  void testReplaceBehaviour() throws CompilationException {
    String expected = "behavior";
    String actual = sut.execute("behaviour");
    assertEquals(expected, actual);
  }

  @Test
  void testReplaceBehaviourHappy() throws CompilationException {
    String expected = "behavior";
    String actual = sut.execute("behavior");
    assertEquals(expected, actual);
  }

  @Test
  void testReplaceOpenBracket() throws CompilationException {
    String expected = "{";
    String actual = sut.execute("do");
    assertEquals(expected, actual);
  }

  @Test
  void testReplaceOpenBracketHappy() throws CompilationException {
    String expected = "{";
    String actual = sut.execute("{");
    assertEquals(expected, actual);
  }

  @Test
  void testReplaceCloseBracket() throws CompilationException {
    String expected = "}";
    String actual = sut.execute("end");
    assertEquals(expected, actual);
  }

  @Test
  void testReplaceCloseBracketHappy() throws CompilationException {
    String expected = "}";
    String actual = sut.execute("}");
    assertEquals(expected, actual);
  }

  @Test
  void testReplaceGreaterThan() throws CompilationException {
    String expected = ">";
    String actual = sut.execute("is higher than");
    assertEquals(expected, actual);
  }

  @Test
  void testReplaceGreaterThanHappy() throws CompilationException {
    String expected = ">";
    String actual = sut.execute(">");
    assertEquals(expected, actual);
  }


  @Test
  void testReplaceLowerThan() throws CompilationException {
    String expected = "<";
    String actual = sut.execute("is lower than");
    assertEquals(expected, actual);
  }

  @Test
  void testReplaceLowerThanHappy() throws CompilationException {
    String expected = "<";
    String actual = sut.execute("<");
    assertEquals(expected, actual);
  }

  @Test
  void testReplaceAndSymbol() throws CompilationException {
    String expected = "&";
    String actual = sut.execute("and");
    assertEquals(expected, actual);
  }

  @Test
  void testReplaceAndSymbolHappy() throws CompilationException {
    String expected = "&";
    String actual = sut.execute("&");
    assertEquals(expected, actual);
  }

  @Test
  void testReplaceOrSymbol() throws CompilationException {
    String expected = "|";
    String actual = sut.execute("or");
    assertEquals(expected, actual);
  }

  @Test
  void testReplaceOrSymbolHappy() throws CompilationException {
    String expected = "|";
    String actual = sut.execute("|");
    assertEquals(expected, actual);
  }

  @Test
  void testReplaceIsSymbol() throws CompilationException {
    String expected = "=";
    String actual = sut.execute("is");
    assertEquals(expected, actual);
  }

  @Test
  void testReplaceIsSymbolHappy() throws CompilationException {
    String expected = "=";
    String actual = sut.execute("=");
    assertEquals(expected, actual);
  }

  @Test
  void testStringToLowercase() throws CompilationException {
    String expected = "abcdef";
    String actual = sut.execute("aBcDeF");
    assertEquals(expected, actual);
  }

  @Test
  void testStringToLowercaseHappy() throws CompilationException {
    String expected = "abcdef";
    String actual = sut.execute("abcdef");
    assertEquals(expected, actual);
  }


  @Test
  void testSourceCode() throws CompilationException {
    String expected = "default { if enemy = near { if enemy health < my health { move towards enemy set agressive } } } behavior agressive { } behavior passive { }";
//    String expected = "{ default: [ { sensor: { enemynear: true, sensor: { enemyhealthlowerthan: } }, actions: [ { type: movetowards, parameter: enemy }, { type: behavior, parameter: agressive } ] } ], agressive: { }, passive: { } }";
    String input = "default do if enemy is near do if enemy health is lower than my health do move towards enemy set agressive end end end behavior agressive do end behavior passive do end";

    String actual = sut.execute(input);
    assertEquals(expected, actual);
  }
}
