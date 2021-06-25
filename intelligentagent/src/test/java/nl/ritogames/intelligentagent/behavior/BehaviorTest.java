package nl.ritogames.intelligentagent.behavior;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import nl.ritogames.shared.dto.GameStateContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BehaviorTest {

  private final BehaviorTree treeMock = mock(BehaviorTree.class);
  private Behavior sut;

  @BeforeEach
  void setUp() {
    sut = new Behavior("default", treeMock);
  }

  @Test
  void handleShouldDelegateToBehaviorTree() {
    GameStateContext contextMock = mock(GameStateContext.class);
    BehaviorNode firstBehaviorNode = mock(BehaviorNode.class);
    when(treeMock.getFirstBehaviorNode()).thenReturn(firstBehaviorNode);

    sut.handle(contextMock);

    verify(firstBehaviorNode).execute(contextMock);
  }
}
