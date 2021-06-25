package nl.ritogames.intelligentagent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;
import nl.ritogames.intelligentagent.behavior.Behavior;
import nl.ritogames.intelligentagent.exception.TargetNotNearException;
import nl.ritogames.intelligentagent.sensor.ArtifactSensor;
import nl.ritogames.intelligentagent.sensor.Sensor;
import nl.ritogames.intelligentagent.sensor.UnitSensor;
import nl.ritogames.shared.EventProcessor;
import nl.ritogames.shared.GameStateContextProvider;
import nl.ritogames.shared.dto.GameStateContext;
import nl.ritogames.shared.dto.event.AttackEvent;
import nl.ritogames.shared.dto.event.InteractionEvent;
import nl.ritogames.shared.dto.event.MoveEvent;
import nl.ritogames.shared.dto.event.PickUpEvent;
import nl.ritogames.shared.dto.gameobject.ASDVector;
import nl.ritogames.shared.dto.gameobject.attribute.Attribute;
import nl.ritogames.shared.dto.gameobject.attribute.Flag;
import nl.ritogames.shared.dto.gameobject.individual.Character;
import nl.ritogames.shared.dto.gameobject.individual.Individual;
import nl.ritogames.shared.dto.gameobject.world.tile.ASDTile;
import nl.ritogames.shared.dto.gameobject.world.tile.AccessibleWorldTile;
import nl.ritogames.shared.dto.gameobject.world.tile.InaccessibleWorldTile;
import nl.ritogames.shared.enums.Direction;
import nl.ritogames.shared.exception.AbsentEntityException;
import nl.ritogames.shared.exception.AgentBuilderException;
import nl.ritogames.shared.exception.EventFailedException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class AgentTest {

  private static final String CHARACTER_ID = "5234534";

  private final GameStateContextProvider contextProviderMock = mock(GameStateContextProvider.class);
  private final EventProcessor eventProcessorMock = mock(EventProcessor.class);
  private final GameStateContext gameStateContextMock = mock(GameStateContext.class);
  private final Character characterMock = mock(Character.class);
  private final Character characterMock2 = mock(Character.class);
  private final Character characterMock3 = mock(Character.class);
  private final Attribute artifactMock = mock(Attribute.class);
  private Agent sut;

  private static Stream<Arguments> provideVectorAndDirections() {
    final ASDVector INDIVIDUAL = new ASDVector(10, 10);

    final ASDVector NORTH_TARGET = new ASDVector(10, 9);
    final Direction NORTH_DIRECTION = Direction.NORTH;

    final ASDVector EAST_TARGET = new ASDVector(11, 10);
    final Direction EAST_DIRECTION = Direction.EAST;

    final ASDVector SOUTH_TARGET = new ASDVector(10, 11);
    final Direction SOUTH_DIRECTION = Direction.SOUTH;

    final ASDVector WEST_TARGET = new ASDVector(9, 10);
    final Direction WEST_DIRECTION = Direction.WEST;

    return Stream.of(
        Arguments.of(INDIVIDUAL, NORTH_TARGET, NORTH_DIRECTION),
        Arguments.of(INDIVIDUAL, EAST_TARGET, EAST_DIRECTION),
        Arguments.of(INDIVIDUAL, SOUTH_TARGET, SOUTH_DIRECTION),
        Arguments.of(INDIVIDUAL, WEST_TARGET, WEST_DIRECTION));
  }

  @BeforeEach
  void setup() {
    sut = spy(new Agent());
    sut.setContextProvider(contextProviderMock);
    sut.setIndividual(characterMock);

    when(contextProviderMock.getContext(any(String.class))).thenReturn(gameStateContextMock);
    when(characterMock.getIndividualID()).thenReturn(CHARACTER_ID);
  }

  @Test
  void testClearSensors() {
    AccessibleWorldTile asdTile = mock(AccessibleWorldTile.class);
    when(asdTile.getIndividual()).thenReturn(characterMock);

    Sensor sensor = new UnitSensor("unit");
    sensor.scan(asdTile);

    sut.addSensor(sensor);

    assertEquals(sensor.getOutput(), characterMock);
    sut.clearSensors();
    assertNull(sensor.getOutput());
  }

  @Test
  void testSetBehavior() {
    Behavior behavior = mock(Behavior.class);
    when(behavior.getBehaviorName()).thenReturn("coolname");
    sut.addBehavior(behavior);

    assertIterableEquals(sut.getBehaviors(), Collections.singletonList(behavior));
    sut.setBehavior("coolname");
    assertEquals("coolname", sut.getCurrentBehavior().getBehaviorName());
  }

  @Test
  void testSetBehaviorWrongName() {
    assertThrows(NoSuchElementException.class, () -> sut.setBehavior("wrongname"));
  }

  @Test
  void getSensorShouldReturnSensorIfPresent() {
    Sensor<Individual> sensorMock = mock(Sensor.class);
    when(sensorMock.getSensorName()).thenReturn("testsensor");
    sut.addSensor(sensorMock);

    Sensor result = sut.getSensor("testsensor");

    assertEquals(sensorMock, result);
  }

  @Test
  void getSensorShouldReturnNullIfNotPresent() {
    Sensor result = sut.getSensor("testsensor");

    assertNull(result);
  }

  @Test
  void createSensorShouldAddUnitSensor() {
    sut.createSensor("unit");

    Sensor<Individual> result = sut.getSensor("unit");

    assertEquals("unit", result.getSensorName());
    assertTrue(result instanceof UnitSensor);
  }

  @Test
  void createSensorShouldAddArtifactSensor() {
    sut.createSensor("artifact");

    Sensor<Attribute> result = sut.getSensor("artifact");

    assertEquals("artifact", result.getSensorName());
    assertTrue(result instanceof ArtifactSensor);
  }

  @Test
  void createSensorShouldNotAddSensorIfSensorIsUnknown() {
    sut.createSensor("unknownName");

    Sensor result = sut.getSensor("unknownName");

    assertNull(result);
  }

  @ParameterizedTest
  @MethodSource("provideVectorAndDirections")
  void fightShouldCallHandleEventWithCorrectAttackEvent(
      ASDVector individualVector, ASDVector targetVector, Direction direction)
          throws EventFailedException, AgentBuilderException {
    String gameName = "game";

    sut.setEventProcessor(eventProcessorMock);
    sut.setIndividual(characterMock);

    when(characterMock.getLocation()).thenReturn(individualVector);
    when(characterMock2.getLocation()).thenReturn(targetVector);
    when(gameStateContextMock.getGameName()).thenReturn(gameName);

    sut.fight(characterMock2);
    verify(eventProcessorMock)
        .handleEvent(new AttackEvent(characterMock.getIndividualID(), gameName, direction));
  }

  @ParameterizedTest
  @MethodSource("provideVectorAndDirections")
  void pickupShouldCallHandleEventWithCorrectPickUpEvent(
      ASDVector individualVector, ASDVector targetVector, Direction direction)
          throws EventFailedException, AgentBuilderException {
    String gameName = "game";
    sut.setEventProcessor(eventProcessorMock);
    sut.setIndividual(characterMock);

    when(characterMock.getLocation()).thenReturn(individualVector);
    when(artifactMock.getLocation()).thenReturn(targetVector);
    when(characterMock.getIndividualID()).thenReturn(CHARACTER_ID);
    when(gameStateContextMock.getGameName()).thenReturn(gameName);

    sut.pickup(artifactMock);
    verify(eventProcessorMock)
        .handleEvent(new PickUpEvent(characterMock.getIndividualID(), gameName, direction));
  }

  @Test
  void fightShouldCallMoveTowardsWhenTargetIsNotNear() {
    ASDVector individualVector = new ASDVector(10, 10);
    ASDVector targetVector = new ASDVector(10, 5);

    sut.setEventProcessor(eventProcessorMock);
    sut.setIndividual(characterMock);

    when(characterMock.getLocation()).thenReturn(individualVector);
    when(characterMock2.getLocation()).thenReturn(targetVector);
    when(characterMock.getIndividualID()).thenReturn(CHARACTER_ID);
    doNothing().when(sut).moveTowards(characterMock2);

    sut.fight(characterMock2);
    verify(sut).moveTowards(characterMock2);
  }

  @Test
  void pickupShouldCallMoveTowardsWhenTargetIsNotNear() {
    ASDVector individualVector = new ASDVector(10, 10);
    ASDVector targetVector = new ASDVector(10, 5);

    sut.setEventProcessor(eventProcessorMock);
    sut.setIndividual(characterMock);

    when(characterMock.getLocation()).thenReturn(individualVector);
    when(artifactMock.getLocation()).thenReturn(targetVector);
    when(characterMock.getIndividualID()).thenReturn(CHARACTER_ID);
    doNothing().when(sut).moveTowards(artifactMock);

    sut.pickup(artifactMock);
    verify(sut).moveTowards(artifactMock);
  }

  @Test
  void throwsTargetNotNearException() throws TargetNotNearException {
    ASDVector individualVector = new ASDVector(0, 0);
    ASDVector artifactVector = new ASDVector(0, 50);

    Assertions.assertThrows(
        TargetNotNearException.class,
        () -> {
          sut.getDirection(individualVector, artifactVector);
        });
  }

  @Test
  void moveTowardsArtifactShouldCallMoveTowards() {
    ASDVector individualVector = new ASDVector(10, 10);
    ASDVector targetVector = new ASDVector(14, 12);

    Attribute artifact = new Flag();
    artifact.setLocation(targetVector);

    sut.setEventProcessor(eventProcessorMock);
    sut.setIndividual(characterMock);

    List<ASDVector> path =
        Arrays.asList(
            new ASDVector(11, 10),
            new ASDVector(12, 10),
            new ASDVector(13, 10),
            new ASDVector(14, 10),
            new ASDVector(14, 11),
            new ASDVector(14, 12));

    when(characterMock.getLocation()).thenReturn(individualVector);
    doNothing().when(sut).move(Direction.EAST);

    try (MockedStatic<PathFinder> myMock = Mockito.mockStatic(PathFinder.class)) {
      myMock.when(() -> PathFinder.findPath(any(), any(), any())).thenReturn(path);
      sut.moveTowards(artifact);
      verify(sut).move(Direction.EAST);
    }
  }

  @Test
  void moveTowardsIndividualShouldCallMoveTowards() {
    ASDVector individualVector = new ASDVector(10, 10);
    ASDVector targetVector = new ASDVector(14, 12);

    Individual individual = new Character(targetVector);
    Attribute artifact = new Flag();
    artifact.setLocation(targetVector);

    sut.setEventProcessor(eventProcessorMock);
    sut.setIndividual(characterMock);

    List<ASDVector> path =
        Arrays.asList(
            new ASDVector(11, 10),
            new ASDVector(12, 10),
            new ASDVector(13, 10),
            new ASDVector(14, 10),
            new ASDVector(14, 11),
            new ASDVector(14, 12));

    when(characterMock.getLocation()).thenReturn(individualVector);
    doNothing().when(sut).move(Direction.EAST);

    try (MockedStatic<PathFinder> myMock = Mockito.mockStatic(PathFinder.class)) {
      myMock.when(() -> PathFinder.findPath(any(), any(), any())).thenReturn(path);
      sut.moveTowards(individual);
      verify(sut).move(Direction.EAST);
    }
  }

  @Test
  void moveTowardsShouldCallMoveWithRandomDirectionWhenPathfinderCantFindPath() {
    ASDVector individualVector = new ASDVector(10, 10);
    ASDVector targetVector = new ASDVector(14, 12);

    sut.setEventProcessor(eventProcessorMock);
    sut.setIndividual(characterMock);

    List<ASDVector> path = Collections.emptyList();

    when(characterMock.getLocation()).thenReturn(individualVector);
    doNothing().when(sut).move(Direction.RANDOM);

    try (MockedStatic<PathFinder> myMock = Mockito.mockStatic(PathFinder.class)) {
      myMock.when(() -> PathFinder.findPath(any(), any(), any())).thenReturn(path);
      sut.moveTowards(targetVector);
      verify(sut).move(Direction.RANDOM);
    }
  }

  @Nested
  class RandomDirection {

    ASDTile[][] worldContext =

        new ASDTile[][] {
          {
              new InaccessibleWorldTile(0, 0),
              new InaccessibleWorldTile(0, 1),
              new InaccessibleWorldTile(0, 2)
          },
            {
                new InaccessibleWorldTile(1, 0),
                new AccessibleWorldTile(1, 1),
                new AccessibleWorldTile(1, 2)
            },
            {
                new InaccessibleWorldTile(2, 0),
                new InaccessibleWorldTile(2, 1),
                new InaccessibleWorldTile(2, 2)
            }
        };

    @BeforeEach
    void setup() throws AbsentEntityException {
      sut.setEventProcessor(eventProcessorMock);
      sut.setIndividual(characterMock);

      when(gameStateContextMock.getWorldContext()).thenReturn(worldContext);
      when(gameStateContextMock.getSelf()).thenReturn(characterMock);
      when(characterMock.getLocation()).thenReturn(new ASDVector(1, 1));
    }

    @ParameterizedTest
    @EnumSource(
        value = Direction.class,
        names = {"NORTH", "EAST", "WEST"})
    void directionAvailableShouldReturnFalse(Direction direction) throws EventFailedException, AgentBuilderException {
      sut.move(direction);

      verify(eventProcessorMock, never()).handleEvent(any(MoveEvent.class));
    }

    @Test
    void directionAvailableShouldReturnTrue() throws EventFailedException, AgentBuilderException {
      sut.move(Direction.SOUTH);

      MoveEvent expected = new MoveEvent(CHARACTER_ID, Direction.SOUTH);
      expected.setGameName("");

      final ArgumentCaptor<MoveEvent> eventCaptor = ArgumentCaptor.forClass(MoveEvent.class);
      verify(eventProcessorMock).handleEvent(eventCaptor.capture());
      MoveEvent result = eventCaptor.getValue();

      assertEquals(expected.getIndividualId(), result.getIndividualId());
      assertEquals(expected.getDirection(), result.getDirection());
    }

    @Test
    void moveShouldCallHandleEventWithRandomAvailableMoveEvent() throws EventFailedException, AgentBuilderException {
      sut.move(Direction.RANDOM);
      MoveEvent expected = new MoveEvent(CHARACTER_ID, Direction.EAST);
      expected.setGameName("");

      final ArgumentCaptor<MoveEvent> eventCaptor = ArgumentCaptor.forClass(MoveEvent.class);
      verify(eventProcessorMock).handleEvent(eventCaptor.capture());
      MoveEvent result = eventCaptor.getValue();

      assertEquals(expected.getIndividualId(), result.getIndividualId());
      assertNotEquals(Direction.RANDOM, result.getDirection());
    }

    @Test
    void moveDirectionShouldNotBeOppositeOfPrevious() throws EventFailedException, AgentBuilderException {
      ArgumentCaptor<InteractionEvent> argumentCaptor =
          ArgumentCaptor.forClass(InteractionEvent.class);

      sut.move(Direction.RANDOM);

      verify(eventProcessorMock).handleEvent(argumentCaptor.capture());

      final Direction direction = ((MoveEvent) argumentCaptor.getValue()).getDirection();

      sut.move(direction.opposite());

      MoveEvent event = new MoveEvent(CHARACTER_ID, direction.opposite());
      event.setGameName("");
      verify(eventProcessorMock, never()).handleEvent(event);
    }
  }
}
