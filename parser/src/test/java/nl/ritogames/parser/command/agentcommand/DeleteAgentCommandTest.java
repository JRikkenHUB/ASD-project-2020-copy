package nl.ritogames.parser.command.agentcommand;

import nl.ritogames.shared.dto.event.DeleteAgentEvent;
import nl.ritogames.shared.exception.InvalidArgumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DeleteAgentCommandTest {

  private DeleteAgentCommand sut;

  @BeforeEach
  void setUp() {
    sut = new DeleteAgentCommand();
  }

  @Test
  void setNameShouldSetNameOfTheCommand() {
    String expected = "expected";

    sut.setName(expected);

    assertEquals(expected, sut.name);
  }

  @Test
  void toInteractionEventShouldUseCorrectName() throws InvalidArgumentException {
    String expected = "expected";
    sut.name = expected;

    DeleteAgentEvent event = (DeleteAgentEvent) sut.toInteractionEvent("");

    assertEquals(expected, event.getAgentName());
  }

  @Test
  void toInteractionEventShouldThrowWithoutName() throws InvalidArgumentException {
    var expected = InvalidArgumentException.class;

    assertThrows(expected, () -> sut.toInteractionEvent(""));
  }
}
