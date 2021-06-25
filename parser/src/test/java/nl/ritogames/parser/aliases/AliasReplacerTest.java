package nl.ritogames.parser.aliases;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AliasReplacerTest {

  private AliasReplacer sut;

  @BeforeEach
  void setUp() {
    sut = new AliasReplacer();
  }

  @Test
  void replaceAliasesShouldReplaceOccurrences() {
    //arrange
    sut.registerAlias("alias", "command");
    var expected = "my command needs replacing";
    var input = "my alias needs replacing";

    //act
    var actual = sut.replaceAliases(input);

    //assert
    assertEquals(expected, actual);
  }

  @Test
  void replaceAliasesShouldNotReplacePartialOccurrences() {
    //arrange
    sut.registerAlias("alias", "command");
    var expected = "my aliases needs replacing";
    var input = "my aliases needs replacing";

    //act
    var actual = sut.replaceAliases(input);

    //assert
    assertEquals(expected, actual);
  }
}
