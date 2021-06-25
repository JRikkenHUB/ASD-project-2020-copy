package nl.ritogames.intelligentagent;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;
import java.util.function.Predicate;
import nl.ritogames.intelligentagent.behavior.ActionNode;
import nl.ritogames.intelligentagent.exception.UnknownActionException;
import nl.ritogames.intelligentagent.sensor.Sensor;
import nl.ritogames.shared.GameStateContextProvider;
import nl.ritogames.shared.dto.GameStateContext;
import nl.ritogames.shared.dto.agent.expression.ArtifactDTO;
import nl.ritogames.shared.dto.agent.expression.DualExpression;
import nl.ritogames.shared.dto.agent.expression.StateDTO;
import nl.ritogames.shared.dto.agent.expression.UnitDTO;
import nl.ritogames.shared.dto.agent.statement.ActionDTO;
import nl.ritogames.shared.dto.agent.statement.BehaviorCallDTO;
import nl.ritogames.shared.dto.gameobject.individual.Character;
import nl.ritogames.shared.dto.gameobject.individual.Individual;
import nl.ritogames.shared.dto.gameobject.individual.Monster;
import nl.ritogames.shared.enums.Direction;
import nl.ritogames.shared.exception.AgentBuilderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

class AgentBuilderTest {

  private final Agent agentMock = mock(Agent.class);
  private final GameStateContextProvider contextProviderMock = mock(GameStateContextProvider.class);
  private final GameStateContext contextMock = mock(GameStateContext.class);
  private final Character characterMock = mock(Character.class);
  private final String TEST_FILE_NAME = "ruurd.json";
  private AgentBuilder sut;
  private ActionDTO moveAction;

  @BeforeEach
  void setUp() {
    sut = new AgentBuilder();
    sut.setAgent(agentMock);
    moveAction = new ActionDTO();
    when(agentMock.getGameStateContext()).thenReturn(contextMock);
    agentMock.setContextProvider(contextProviderMock);
    when(contextProviderMock.getContext(anyString())).thenReturn(contextMock);
  }

  @Test
  void buildAgentShouldMapJSONToAgent()
      throws URISyntaxException, IOException, AgentBuilderException {
    Individual mockUnit = mock(Individual.class);
    Sensor<Individual> unitSensor = mock(Sensor.class);
    when(unitSensor.getOutput()).thenReturn(mockUnit);

    Agent agent = new Agent();
    Agent spyAgent = spy(agent);
    doNothing().when(spyAgent).createSensor(anyString());
    doReturn(contextMock).when(spyAgent).getGameStateContext();
    doReturn(unitSensor).when(spyAgent).getSensor(anyString());
    sut.setAgent(spyAgent);

    String agentJSON =
        Files.readString(Path.of(getClass().getClassLoader().getResource(TEST_FILE_NAME).toURI()));
    assertDoesNotThrow(() -> sut.build(agentJSON, characterMock));
  }

  @Test
  void mapMoveUpShouldReturnMoveNorthAction() {
    moveAction.setAction("move up");

    ActionNode result = sut.mapAction(moveAction);
    result.execute(contextMock);
    verify(agentMock, times(1)).move(Direction.NORTH);
  }

  @Test
  void mapMoveRightShouldReturnMoveEastAction() {
    moveAction.setAction("move right");

    ActionNode result = sut.mapAction(moveAction);
    result.execute(contextMock);
    verify(agentMock, times(1)).move(Direction.EAST);
  }

  @Test
  void mapMoveDownShouldReturnMoveSouthAction() {
    moveAction.setAction("move down");

    ActionNode result = sut.mapAction(moveAction);
    result.execute(contextMock);
    verify(agentMock, times(1)).move(Direction.SOUTH);
  }

  @Test
  void mapMoveLeftShouldReturnMoveWestAction() {
    moveAction.setAction("move left");

    ActionNode result = sut.mapAction(moveAction);
    result.execute(contextMock);
    verify(agentMock, times(1)).move(Direction.WEST);
  }

  @Test
  void mapMoveRandomlyShouldReturnMoveRandomAction() {
    moveAction.setAction("move randomly");

    ActionNode result = sut.mapAction(moveAction);
    result.execute(contextMock);
    verify(agentMock, times(1)).move(Direction.RANDOM);
  }

  @Test
  void invalidMoveShouldThrowUnknownActionException() {
    ActionDTO invalidMove = new ActionDTO("move", "downwards");

    assertThrows(UnknownActionException.class, () -> sut.mapAction(invalidMove));
  }

  @Test
  void mapFightShouldFightSensorOutput() {
    ActionDTO fightEnemy = new ActionDTO("attack", "enemy");
    Sensor<Individual> enemySensorMock = mock(Sensor.class);
    Individual nearestEnemy = mock(Individual.class);
    when(enemySensorMock.getOutput()).thenReturn(nearestEnemy);
    when(agentMock.getSensor("enemy")).thenReturn(enemySensorMock);


    ActionNode result = sut.mapAction(fightEnemy);
    result.execute(contextMock);

    verify(agentMock).fight(nearestEnemy);
  }

  @Test
  void invalidFightShouldThrowUnknownActionException() {
    ActionDTO invalidFight = new ActionDTO("fight", "boar");
    when(agentMock.getSensor("boar")).thenReturn(null);

    assertThrows(UnknownActionException.class, () -> sut.mapAction(invalidFight));
  }

  @Test
  void invalidPickupShouldThrowUnknownActionException() {
    ActionDTO invalidPickup = new ActionDTO("pickup", "drink");
    when(agentMock.getSensor("drink")).thenReturn(null);

    assertThrows(UnknownActionException.class, () -> sut.mapAction(invalidPickup));
  }

  @Test
  void invalidActionShouldThrowUnknownActionException() {
    ActionDTO invalidAction = new ActionDTO("fly", "up");

    assertThrows(UnknownActionException.class, () -> sut.mapAction(invalidAction));
  }

  @Test
  void mapBehaviorCallShouldSetBehaviorOfAgent() {
    BehaviorCallDTO behaviorCall = new BehaviorCallDTO();
    final String BEHAVIOR_NAME = "agressive";
    behaviorCall.setBehaviorCall(BEHAVIOR_NAME);

    ActionNode result = sut.mapBehaviorCall(behaviorCall);
    result.execute(contextMock);

    verify(agentMock).setBehavior(BEHAVIOR_NAME);
  }

  // mock individualsensor
  // mock individual
  // test health attribute for individual (should call getHp())
  @Test
  void getIndividualHealthAttributeShouldCallGetHp() {
    final String UNIT_NAME = "unit";
    final String ATTRIBUTE_NAME = "health";
    UnitDTO unit = new UnitDTO(UNIT_NAME, ATTRIBUTE_NAME);
    Sensor<Individual> individualSensor = mock(Sensor.class);
    Individual individual = mock(Individual.class);
    when(individualSensor.getOutput()).thenReturn(individual);
    when(agentMock.getSensor(UNIT_NAME)).thenReturn(individualSensor);

    Function<GameStateContext, Integer> result = sut.getUnitAttribute(unit);
    result.apply(contextMock);

    verify(individual).getHp();
  }

  // test armor attribute for character (should call getDefense())
  @Test
  void getCharacterArmorShouldCallGetDefense() {
    final String UNIT_NAME = "enemy";
    final String ATTRIBUTE_NAME = "armor";
    UnitDTO unit = new UnitDTO(UNIT_NAME, ATTRIBUTE_NAME);
    Sensor<Character> enemySensor = mock(Sensor.class);
    Character enemy = mock(Character.class);
    when(enemySensor.getOutput()).thenReturn(enemy);
    when(agentMock.getSensor(UNIT_NAME)).thenReturn(enemySensor);

    Function<GameStateContext, Integer> result = sut.getUnitAttribute(unit);
    result.apply(contextMock);

    verify(enemy).getDefense();
  }

  // test armor attribute for monster (should call getDefense())
  @Test
  void getMonsterArmorShouldCallGetDefense() {
    final String UNIT_NAME = "monster";
    final String ATTRIBUTE_NAME = "armor";
    UnitDTO unit = new UnitDTO(UNIT_NAME, ATTRIBUTE_NAME);
    Sensor<Monster> monsterSensor = mock(Sensor.class);
    Monster monster = mock(Monster.class);
    when(monsterSensor.getOutput()).thenReturn(monster);
    when(agentMock.getSensor(UNIT_NAME)).thenReturn(monsterSensor);

    Function<GameStateContext, Integer> result = sut.getUnitAttribute(unit);
    result.apply(contextMock);

    verify(monster).getDefense();
  }

  @ParameterizedTest
  @CsvSource({
    "unit, armor",
    "unit, strength",
    "unit, charisma",
  })
  void testUnknownAction(String unitName, String attributeName) {
    UnitDTO unit = new UnitDTO(unitName, attributeName);
    Sensor<Individual> unitSensor = mock(Sensor.class);
    Individual individual = mock(Individual.class);
    when(unitSensor.getOutput()).thenReturn(individual);
    when(agentMock.getSensor(unitName)).thenReturn(unitSensor);

    assertThrows(UnknownActionException.class, () -> sut.getUnitAttribute(unit));
  }

  // test strength attribute for character (should call getAttack())
  @Test
  void getCharacterStrengthShouldCallGetAttack() {
    final String UNIT_NAME = "enemy";
    final String ATTRIBUTE_NAME = "strength";
    UnitDTO unit = new UnitDTO(UNIT_NAME, ATTRIBUTE_NAME);
    Sensor<Character> enemySensor = mock(Sensor.class);
    Character enemy = mock(Character.class);
    when(enemySensor.getOutput()).thenReturn(enemy);
    when(agentMock.getSensor(UNIT_NAME)).thenReturn(enemySensor);

    Function<GameStateContext, Integer> result = sut.getUnitAttribute(unit);
    result.apply(contextMock);

    verify(enemy).getAttack();
  }

  // test strength attribute for monster (should call getAttack())
  @Test
  void getMonsterStrengthShouldCallGetAttack() {
    final String UNIT_NAME = "monster";
    final String ATTRIBUTE_NAME = "strength";
    UnitDTO unit = new UnitDTO(UNIT_NAME, ATTRIBUTE_NAME);
    Sensor<Monster> monsterSensor = mock(Sensor.class);
    Monster monster = mock(Monster.class);
    when(monsterSensor.getOutput()).thenReturn(monster);
    when(agentMock.getSensor(UNIT_NAME)).thenReturn(monsterSensor);

    Function<GameStateContext, Integer> result = sut.getUnitAttribute(unit);
    result.apply(contextMock);

    verify(monster).getAttack();
  }

  @Test
  void mapAndExpressionShouldReturnTrueWhenBothOperandsAreTrue() {
    AgentBuilder builderSpy = spy(AgentBuilder.class);

    DualExpression trueExpressionMock = mock(DualExpression.class);
    when(trueExpressionMock.getExpressionType()).thenReturn("lt");
    Predicate<GameStateContext> trueFunction = context -> true;
    Mockito.doReturn(trueFunction).when(builderSpy).mapComparison(trueExpressionMock);
    DualExpression andExpression =
        new DualExpression("and", trueExpressionMock, trueExpressionMock);

    Predicate<GameStateContext> result = builderSpy.mapExpression(andExpression);
    assertTrue(result.test(contextMock));
  }

  @Test
  void mapAndExpressionShouldReturnFalseWhenBothOperandsAreFalse() {
    AgentBuilder builderSpy = spy(AgentBuilder.class);

    DualExpression falseExpressionMock = mock(DualExpression.class);
    when(falseExpressionMock.getExpressionType()).thenReturn("lt");
    Predicate<GameStateContext> falseFunction = context -> false;
    Mockito.doReturn(falseFunction).when(builderSpy).mapComparison(falseExpressionMock);
    DualExpression andExpression =
        new DualExpression("and", falseExpressionMock, falseExpressionMock);

    Predicate<GameStateContext> result = builderSpy.mapExpression(andExpression);
    assertFalse(result.test(contextMock));
  }

  @Test
  void mapAndExpressionShouldReturnFalseWhenBothOperandsAreDifferent() {
    AgentBuilder builderSpy = spy(AgentBuilder.class);

    DualExpression falseExpressionMock = mock(DualExpression.class);
    when(falseExpressionMock.getExpressionType()).thenReturn("lt");
    Predicate<GameStateContext> falseFunction = context -> false;
    Mockito.doReturn(falseFunction).when(builderSpy).mapComparison(falseExpressionMock);

    DualExpression trueExpressionMock = mock(DualExpression.class);
    when(trueExpressionMock.getExpressionType()).thenReturn("eq");
    Predicate<GameStateContext> trueFunction = context -> true;
    Mockito.doReturn(trueFunction).when(builderSpy).mapComparison(trueExpressionMock);

    DualExpression andExpression =
        new DualExpression("and", trueExpressionMock, falseExpressionMock);

    Predicate<GameStateContext> result = builderSpy.mapExpression(andExpression);
    assertFalse(result.test(contextMock));
  }

  @Test
  void mapOrExpressionShouldReturnTrueWhenBothOperandsTrue() {
    AgentBuilder builderSpy = spy(AgentBuilder.class);

    DualExpression trueExpressionMock = mock(DualExpression.class);
    when(trueExpressionMock.getExpressionType()).thenReturn("lt");
    Predicate<GameStateContext> trueFunction = context -> true;
    Mockito.doReturn(trueFunction).when(builderSpy).mapComparison(trueExpressionMock);
    DualExpression andExpression = new DualExpression("or", trueExpressionMock, trueExpressionMock);

    Predicate<GameStateContext> result = builderSpy.mapExpression(andExpression);
    assertTrue(result.test(contextMock));
  }

  @Test
  void mapOrExpressionShouldReturnFalseWhenBothOperandsFalse() {
    AgentBuilder builderSpy = spy(AgentBuilder.class);

    DualExpression falseExpressionMock = mock(DualExpression.class);
    when(falseExpressionMock.getExpressionType()).thenReturn("lt");
    Predicate<GameStateContext> trueFunction = context -> false;
    Mockito.doReturn(trueFunction).when(builderSpy).mapComparison(falseExpressionMock);
    DualExpression andExpression =
        new DualExpression("or", falseExpressionMock, falseExpressionMock);

    Predicate<GameStateContext> result = builderSpy.mapExpression(andExpression);
    assertFalse(result.test(contextMock));
  }

  @Test
  void mapOrExpressionShouldReturnTrueWhenBothOperandsAreDifferent() {
    AgentBuilder builderSpy = spy(AgentBuilder.class);

    DualExpression falseExpressionMock = mock(DualExpression.class);
    when(falseExpressionMock.getExpressionType()).thenReturn("lt");
    Predicate<GameStateContext> falseFunction = context -> false;
    Mockito.doReturn(falseFunction).when(builderSpy).mapComparison(falseExpressionMock);

    DualExpression trueExpressionMock = mock(DualExpression.class);
    when(trueExpressionMock.getExpressionType()).thenReturn("eq");
    Predicate<GameStateContext> trueFunction = context -> true;
    Mockito.doReturn(trueFunction).when(builderSpy).mapComparison(trueExpressionMock);

    DualExpression andExpression =
        new DualExpression("or", trueExpressionMock, falseExpressionMock);

    Predicate<GameStateContext> result = builderSpy.mapExpression(andExpression);
    assertTrue(result.test(contextMock));
  }

  @Test
  void mapStateExpressionWithValidSensor() {
    Sensor<Character> enemySensor = mock(Sensor.class);
    when(agentMock.getSensor("enemy")).thenReturn(enemySensor);

    DualExpression stateExpression =
        new DualExpression("state", new UnitDTO("enemy", null), new StateDTO("near"));

    assertDoesNotThrow(
        () -> {
          sut.mapExpression(stateExpression);
        });
  }

  @Test
  void mapStateExpressionWithInvalidSensorShouldCreateSensor() {
    when(agentMock.getSensor("boar")).thenReturn(null);

    DualExpression stateExpression =
        new DualExpression("state", new UnitDTO("boar", null), new StateDTO("near"));

    sut.mapExpression(stateExpression);
    verify(agentMock).createSensor("boar");
  }

  @Test
  void mapHasExpressionWhenUnitHasFlag() {
    Character enemyWithFlag = mock(Character.class);
    Sensor<Character> enemySensor = mock(Sensor.class);
    when(agentMock.getSensor("enemy")).thenReturn(enemySensor);
    when(enemySensor.getOutput()).thenReturn(enemyWithFlag);
    when(enemyWithFlag.getHasFlag()).thenReturn(true);

    DualExpression stateExpression =
        new DualExpression("has", new UnitDTO("enemy", null), new ArtifactDTO("flag"));
    Predicate<GameStateContext> result = sut.mapExpression(stateExpression);

    assertTrue(result.test(contextMock));
  }

  @Test
  void mapHasExpressionWhenUnitDoesNotHaveFlag() {
    Character enemyWithFlag = mock(Character.class);
    Sensor<Character> enemySensor = mock(Sensor.class);
    when(agentMock.getSensor("enemy")).thenReturn(enemySensor);
    when(enemySensor.getOutput()).thenReturn(enemyWithFlag);
    when(enemyWithFlag.getHasFlag()).thenReturn(false);

    DualExpression stateExpression =
        new DualExpression("has", new UnitDTO("enemy", null), new ArtifactDTO("flag"));
    Predicate<GameStateContext> result = sut.mapExpression(stateExpression);

    assertFalse(result.test(contextMock));
  }

  @Test
  void mapHasExpressionWithInvalidSensor() {
    when(agentMock.getSensor("bug")).thenReturn(null);

    DualExpression stateExpression =
        new DualExpression("has", new UnitDTO("bug", null), new ArtifactDTO("flag"));
    assertThrows(
        UnknownActionException.class,
        () -> {
          sut.mapExpression(stateExpression);
        });
  }

  @Test
  void mapHasExpressionWithValidSensor() {
    Sensor mockedSensor = mock(Sensor.class);
    when(agentMock.getSensor("enemy")).thenReturn(mockedSensor);
    DualExpression hasExpression =
        new DualExpression("has", new UnitDTO("enemy", null), new ArtifactDTO("flag"));

    assertDoesNotThrow(
        () -> {
          sut.mapExpression(hasExpression);
        });
  }
}
