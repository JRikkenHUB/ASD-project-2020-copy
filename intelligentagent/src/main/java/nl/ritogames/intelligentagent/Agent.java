package nl.ritogames.intelligentagent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import javax.inject.Inject;
import nl.ritogames.intelligentagent.behavior.Behavior;
import nl.ritogames.intelligentagent.exception.TargetNotNearException;
import nl.ritogames.intelligentagent.sensor.ArtifactSensor;
import nl.ritogames.intelligentagent.sensor.MonsterSensor;
import nl.ritogames.intelligentagent.sensor.Sensor;
import nl.ritogames.intelligentagent.sensor.UnitSensor;
import nl.ritogames.shared.EventProcessor;
import nl.ritogames.shared.GameStateContextProvider;
import nl.ritogames.shared.dto.GameStateContext;
import nl.ritogames.shared.dto.event.AttackEvent;
import nl.ritogames.shared.dto.event.MoveEvent;
import nl.ritogames.shared.dto.event.PickUpEvent;
import nl.ritogames.shared.dto.gameobject.ASDVector;
import nl.ritogames.shared.dto.gameobject.attribute.Attribute;
import nl.ritogames.shared.dto.gameobject.individual.Individual;
import nl.ritogames.shared.dto.gameobject.world.World;
import nl.ritogames.shared.enums.Direction;
import nl.ritogames.shared.exception.AbsentEntityException;
import nl.ritogames.shared.exception.EventFailedException;
import nl.ritogames.shared.exception.OutOfMapException;

/**
 * An agent is an AI that is programmed by a player or by a developer. An agent has several
 * behaviors that can be cycled through. Every agent has a default behavior it falls back to when
 * it's idling. The behavior of the agent can be changed within another behavior.
 */
public class Agent {


  private final List<Behavior> behaviors;
  private final List<Sensor> sensors;
  private final Random random = new Random();
  private Behavior currentBehavior;
  private EventProcessor eventProcessor;
  private GameStateContextProvider contextProvider;
  private Individual individual;

  private Direction previousDirection;

  public Agent() {
    this.behaviors = new ArrayList<>();
    this.sensors = new ArrayList<>();
  }

  public Agent(List<Behavior> behaviors, EventProcessor eventProcessor) {
    this.behaviors = behaviors;
    this.eventProcessor = eventProcessor;
    this.sensors = new ArrayList<>();
  }

  /**
   * Gets behavior from behavior name and sets it.
   *
   * @param behaviorName the behavior name
   * @see java.util.stream.Stream
   * @see Behavior
   */
  void setBehavior(String behaviorName) {
    this.currentBehavior = behaviors.stream()
        .filter(behavior -> behavior.getBehaviorName().equals(behaviorName))
        .findFirst()
        .orElseThrow();
  }

  /** Move one step towards a direction. Does nothing if the direction is inaccessible.*/
  void move(Direction direction) {
    if (direction == Direction.RANDOM) {
      direction = createRandomDirection();
    } else if (!directionAvailable(direction)) {
      return;
    }
    try {
      MoveEvent moveEvent = new MoveEvent(this.individual.getIndividualID(), direction);
      moveEvent.setGameName(this.getGameStateContext().getGameName());
      eventProcessor.handleEvent(moveEvent);
    } catch (EventFailedException ignored) {
      // No action required
    }
    previousDirection = direction;
  }

  /**
   * Attack a target. This method determines the direction the agent must attack in to hit the target.
   * If the target is too far away, it will move towards the target.
   */
  void fight(Individual target) {
    ASDVector individualLocation = getLocation(this.individual);
    ASDVector targetLocation = target.getLocation();
    try {
      Direction direction = getDirection(individualLocation, targetLocation);
      AttackEvent fightEvent = new AttackEvent(this.individual.getIndividualID(), direction, target.getIndividualID());
      fightEvent.setGameName(this.getGameStateContext().getGameName());
      eventProcessor.handleEvent(fightEvent);
    } catch (TargetNotNearException | EventFailedException e) {
      moveTowards(target);
    }
  }

  /**
   * Pick up an artifact. This method determines the direction the agent must pick up to get the artifact.
   * If the artifact is too far away, it will move towards the artifact.
   */
  void pickup(Attribute artifact) {
    ASDVector individualLocation = getLocation(this.individual);
    ASDVector artifactLocation = artifact.getLocation();
    try {
      Direction direction = getDirection(individualLocation, artifactLocation);
      PickUpEvent pickUpEvent = new PickUpEvent(this.individual.getIndividualID(),
          this.getGameStateContext().getGameName(), direction);
      eventProcessor.handleEvent(pickUpEvent);
    } catch (TargetNotNearException | EventFailedException e) {
      moveTowards(artifact);
    }
  }

  /** Determine the direction towards the target location. */
  Direction getDirection(ASDVector fromLocation, ASDVector targetLocation) {
    if (targetLocation.getX() - fromLocation.getX() == 1) {
      return Direction.EAST;
    } else if (targetLocation.getX() - fromLocation.getX() == -1) {
      return Direction.WEST;
    } else if (targetLocation.getY() - fromLocation.getY() == -1) {
      return Direction.NORTH;
    } else if (targetLocation.getY() - fromLocation.getY() == 1) {
      return Direction.SOUTH;
    } else {
      throw new TargetNotNearException();
    }
  }

  /* Move one step towards a target individual */
  public void moveTowards(Individual targetIndividual) {
    moveTowards(targetIndividual.getLocation());
  }

  /* Move one step towards a target artifact */
  public void moveTowards(Attribute targetArtifact) {
    moveTowards(targetArtifact.getLocation());
  }

  /* Move one step towards a target location */
  public void moveTowards(ASDVector target) {
    ASDVector currentLocation = this.individual.getLocation();
    List<ASDVector> pathList = PathFinder.findPath(getGameStateContext().getWorldContext(),
        currentLocation, target);
    if (!(pathList.isEmpty())) {
      Direction targetDirection = getDirection(currentLocation, pathList.get(0));
      move(targetDirection);
    } else {
      move(Direction.RANDOM);
    }

  }

  private ASDVector getLocation(Individual individual) {
    return individual.getLocation();
  }

  private Direction createRandomDirection() {
    List<Direction> availableDirections = Arrays.stream(Direction.values())
        .filter(this::directionAvailable)
        .collect(Collectors.toCollection(ArrayList::new));

    if (availableDirections.size() > 1 && previousDirection != null) availableDirections.remove(previousDirection.opposite());
    return availableDirections.get(random.nextInt(availableDirections.size()));

  }

  public GameStateContext getGameStateContext() {
    return contextProvider
        .getContext(String.valueOf(individual.getIndividualID()));
  }

  public void addBehavior(Behavior behavior) {
    behaviors.add(behavior);
  }

  /**
   * Handles the current behavior with the new GameStateContext.
   *
   * @see GameStateContext
   * @see Behavior
   */
  public void update() {
    GameStateContext context = contextProvider
        .getContext(individual.getIndividualID());
    scan(context);
    currentBehavior.handle(context);
    clearSensors();
  }

  /**
   * Clears all sensors.
   *
   * @see Sensor
   */
  public void clearSensors() {
    for (Sensor sensor : sensors) {
      sensor.clearOutput();
    }
  }

  /**
   * Scans the visible map with the available sensors.
   *
   * @param context the GameStateContext
   * @see GameStateContext
   */
  private void scan(GameStateContext context) {
    PathFinder.scanMap(context.getWorldContext(), individual.getLocation(), sensors);
  }

  private boolean directionAvailable(Direction direction) {
    if (direction == Direction.RANDOM) {
      return false;
    }

    final GameStateContext gameStateContext = contextProvider
        .getContext(this.individual.getIndividualID());
    final ASDVector selfLocation;
    try {
      selfLocation = gameStateContext.getSelf().getLocation();
    } catch (AbsentEntityException e) {
      return false;
    }

    World world;

    try {
      world = new World(gameStateContext.getWorldContext());
    } catch (OutOfMapException exception) {
      return false;
    }

    return switch (direction) {
      case NORTH -> world.getTile(selfLocation.getX(), selfLocation.getY() - 1).tileAccessible();
      case EAST -> world.getTile(selfLocation.getX() + 1, selfLocation.getY()).tileAccessible();
      case SOUTH -> world.getTile(selfLocation.getX(), selfLocation.getY() + 1).tileAccessible();
      case WEST -> world.getTile(selfLocation.getX() - 1, selfLocation.getY()).tileAccessible();
      default -> false;
    };
  }

  public Individual getIndividual() {
    return this.individual;
  }

  public void setIndividual(Individual individual) {
    this.individual = individual;
  }

  @Inject
  public void setContextProvider(GameStateContextProvider contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Inject
  public void setEventProcessor(EventProcessor eventProcessor) {
    this.eventProcessor = eventProcessor;
  }

  public Direction getPreviousDirection() {
    return previousDirection;
  }

  public void addSensor(Sensor sensor) {
    sensors.add(sensor);
  }

  public List<Sensor> getSensors() {
    return sensors;
  }

  public List<Behavior> getBehaviors() {
    return behaviors;
  }

  public Behavior getCurrentBehavior() {
    return currentBehavior;
  }

  public void setCurrentBehavior(Behavior currentBehavior) {
    this.currentBehavior = currentBehavior;
  }

  public Sensor getSensor(String sensorName) {
    Optional<Sensor> foundSensor = sensors.stream()
        .filter(sensor -> sensor.getSensorName().equals(sensorName))
        .findFirst();

    return foundSensor.orElse(null);
  }

  public void createSensor(String sensorName) {
    switch (sensorName) {
      case "unit", "enemy":
        sensors.add(new UnitSensor(sensorName));
        break;
      case "artifact":
        sensors.add(new ArtifactSensor(sensorName));
        break;
      case "monster":
        sensors.add(new MonsterSensor(sensorName));
        break;
      default:
        break;

    }
  }
}
