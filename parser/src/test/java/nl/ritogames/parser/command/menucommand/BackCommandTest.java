package nl.ritogames.parser.command.menucommand;

import nl.ritogames.shared.dto.event.BackEvent;
import nl.ritogames.shared.exception.InvalidArgumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class BackCommandTest {

  private BackCommand sut;

  @BeforeEach
  void setUp() {
    sut = new BackCommand();
  }

  @Test
  void toInteractionEventShouldReturnBackEvent() throws InvalidArgumentException {
    var actual = sut.toInteractionEvent("");

    assertTrue(actual instanceof BackEvent);
  }
}
