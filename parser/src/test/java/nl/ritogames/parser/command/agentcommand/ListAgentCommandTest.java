package nl.ritogames.parser.command.agentcommand;

import nl.ritogames.shared.dto.event.Event;
import nl.ritogames.shared.dto.event.ListAgentEvent;
import nl.ritogames.shared.exception.InvalidArgumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ListAgentCommandTest {

  private ListAgentCommand sut;

  @BeforeEach
  void setUp() {
    sut = new ListAgentCommand();
  }

  @Test
  void toInteractionEventShouldReturnAnEventWithoutName() throws InvalidArgumentException {
    String expected = "expected";

    Event event = sut.toInteractionEvent("");

    assertTrue(event instanceof ListAgentEvent);
  }

}
