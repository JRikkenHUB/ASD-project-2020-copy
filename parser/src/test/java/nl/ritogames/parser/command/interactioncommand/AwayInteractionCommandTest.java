package nl.ritogames.parser.command.interactioncommand;

import nl.ritogames.shared.dto.event.StartAgentEvent;
import nl.ritogames.shared.exception.InvalidArgumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AwayInteractionCommandTest {

  private AwayInteractionCommand sut;

  @BeforeEach
  void setUp() {
    sut = new AwayInteractionCommand();
  }

  @Test
  void toInteractionEventShouldReturnBackEvent() throws InvalidArgumentException {
    var actual = sut.toInteractionEvent("");

    assertTrue(actual instanceof StartAgentEvent);
  }

}
