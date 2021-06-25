package nl.ritogames.intelligentagent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import nl.ritogames.intelligentagent.behavior.ActionNode;
import nl.ritogames.intelligentagent.behavior.Behavior;
import nl.ritogames.intelligentagent.behavior.BehaviorNode;
import nl.ritogames.intelligentagent.behavior.BehaviorTree;
import nl.ritogames.intelligentagent.sensor.ArtifactSensor;
import nl.ritogames.shared.EventProcessor;
import nl.ritogames.shared.GameStateContextProvider;
import nl.ritogames.shared.dto.GameStateContext;
import nl.ritogames.shared.dto.event.MoveEvent;
import nl.ritogames.shared.dto.gameobject.ASDVector;
import nl.ritogames.shared.dto.gameobject.attribute.Weapon;
import nl.ritogames.shared.dto.gameobject.individual.Character;
import nl.ritogames.shared.dto.gameobject.world.tile.ASDTile;
import nl.ritogames.shared.dto.gameobject.world.tile.AccessibleWorldTile;
import nl.ritogames.shared.dto.gameobject.world.tile.InaccessibleWorldTile;
import nl.ritogames.shared.enums.Direction;
import nl.ritogames.shared.exception.AbsentEntityException;
import nl.ritogames.shared.exception.AgentBuilderException;
import nl.ritogames.shared.exception.EventFailedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

class AgentTestIT {

  private static final String CHARACTER_ID = "342342";
  private final Character characterMock = Mockito.mock(Character.class);
  private final EventProcessor eventProcessorMock = Mockito.mock(EventProcessor.class);
  private final GameStateContextProvider contextProviderMock =
      Mockito.mock(GameStateContextProvider.class);
  private final GameStateContext contextMock = Mockito.mock(GameStateContext.class);
  private Agent sut;

  @BeforeEach
  void setUp() throws AbsentEntityException {
    this.sut = spy(new Agent(new LinkedList<>(), eventProcessorMock));
    sut.setIndividual(characterMock);
    sut.setContextProvider(contextProviderMock);

    when(characterMock.getIndividualID()).thenReturn(CHARACTER_ID);
    when(contextMock.getSelf()).thenReturn(characterMock);
    when(characterMock.getLocation()).thenReturn(new ASDVector(1, 1));
    when(contextProviderMock.getContext(CHARACTER_ID)).thenReturn(contextMock);
  }

  @Test
  @Tag("integration-test")
  void agentShouldMoveUp() throws EventFailedException, AgentBuilderException {
    BehaviorNode moveUpNode = new ActionNode(context -> sut.move(Direction.NORTH));
    BehaviorTree behaviorTree = new BehaviorTree();
    behaviorTree.setFirstBehaviorNode(moveUpNode);
    Behavior moveUpBehavior = new Behavior("default", behaviorTree);
    sut.addBehavior(moveUpBehavior);
    sut.setBehavior("default");

    ASDTile[][] worldContext =
        new ASDTile[][]{
            {
                new InaccessibleWorldTile(0, 0),
                new InaccessibleWorldTile(0, 1),
                new InaccessibleWorldTile(0, 2)
            },
            {
                new AccessibleWorldTile(1, 0),
                new AccessibleWorldTile(1, 1),
                new InaccessibleWorldTile(1, 2)
            },
            {
                new InaccessibleWorldTile(2, 0),
                new InaccessibleWorldTile(2, 1),
                new InaccessibleWorldTile(2, 2)
            }
        };

    when(contextMock.getWorldContext()).thenReturn(worldContext);

    sut.update();

    MoveEvent expected = new MoveEvent(CHARACTER_ID, Direction.NORTH);
    expected.setGameName("");

    final ArgumentCaptor<MoveEvent> eventCaptor = ArgumentCaptor.forClass(MoveEvent.class);
    verify(eventProcessorMock, times(1)).handleEvent(eventCaptor.capture());
    MoveEvent result = eventCaptor.getValue();

    assertEquals(expected.getIndividualId(), result.getIndividualId());
    assertEquals(expected.getDirection(), result.getDirection());
  }

  @Test
  void testScanFindsClosestArtifact() {
    Weapon weapon = new Weapon("testwapen", 10);
    Weapon weapon1 = new Weapon("teswapen1", 10);

    AccessibleWorldTile artifactTile = new AccessibleWorldTile(0, 1);
    artifactTile.setAttribute(weapon);

    AccessibleWorldTile artifactTile1 = new AccessibleWorldTile(0, 1);
    artifactTile1.setAttribute(weapon1);

    ASDTile[][] worldContext =
        new ASDTile[][]{
            {artifactTile, artifactTile1, new AccessibleWorldTile(0, 2)},
            {
                new AccessibleWorldTile(1, 0),
                new AccessibleWorldTile(1, 1),
                new AccessibleWorldTile(1, 2)
            },
            {
                new AccessibleWorldTile(2, 0),
                new AccessibleWorldTile(2, 1),
                new AccessibleWorldTile(2, 2)
            }
        };

    Behavior behaviorMock = mock(Behavior.class);
    sut.setCurrentBehavior(behaviorMock);

    sut.addSensor(new ArtifactSensor("artifactSensor"));

    when(contextMock.getWorldContext()).thenReturn(worldContext);
    when(characterMock.getLocation()).thenReturn(new ASDVector(2, 2));
    doNothing().when(behaviorMock).handle(any());
    doNothing().when(sut).clearSensors();

    sut.update();

    assertEquals(weapon1, sut.getSensor("artifactSensor").getOutput());
  }
}
