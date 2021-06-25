package nl.ritogames.parser.command.agentcommand;


import nl.ritogames.shared.dto.event.CreateAgentEvent;
import nl.ritogames.shared.exception.InvalidArgumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CreateAgentCommandTest {

  private CreateAgentCommand sut;

  @BeforeEach
  void setUp() {
    sut = new CreateAgentCommand();
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

    CreateAgentEvent event = (CreateAgentEvent) sut.toInteractionEvent("");

    assertEquals(expected, event.getAgentName());
  }

  @Test
  void toInteractionEventShouldThrowWithoutName() throws InvalidArgumentException {
    var expected = InvalidArgumentException.class;

    assertThrows(expected, () -> sut.toInteractionEvent(""));
  }
}
