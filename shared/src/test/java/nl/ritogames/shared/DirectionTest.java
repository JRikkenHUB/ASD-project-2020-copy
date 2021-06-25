package nl.ritogames.shared;

import nl.ritogames.shared.enums.Direction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.NoSuchElementException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DirectionTest {

  @ParameterizedTest
  @MethodSource("directionStringsWithCorrespondingEnums")
  void fromStringShouldReturnCorrectEnum(String str, Direction direction) {
    assertEquals(direction, Direction.fromString(str));
  }

  @Test
  void fromStringShouldThrow() {
    assertThrows(IllegalArgumentException.class, () -> Direction.fromString("invalidEnum"));
  }

  @ParameterizedTest
  @MethodSource("directionsWithOpposites")
  void oppositeMethodShouldReturnCorrectOpposite(Direction direction, Direction opposite) {
    assertEquals(opposite, direction.opposite());
  }

  @Test
  void oppositeMethodShouldThrowWhenCastOnRandom() {
    assertThrows(NoSuchElementException.class, Direction.RANDOM::opposite);
  }

  private static Stream<Arguments> directionsWithOpposites() {
    return Stream.of(
        Arguments.of(Direction.NORTH, Direction.SOUTH),
        Arguments.of(Direction.EAST, Direction.WEST),
        Arguments.of(Direction.SOUTH, Direction.NORTH),
        Arguments.of(Direction.WEST, Direction.EAST)
    );
  }

  private static Stream<Arguments> directionStringsWithCorrespondingEnums() {
    return Stream.of(
        Arguments.of("north", Direction.NORTH),
        Arguments.of("east", Direction.EAST),
        Arguments.of("south", Direction.SOUTH),
        Arguments.of("west", Direction.WEST),
        Arguments.of("random", Direction.RANDOM),
        Arguments.of("raNdoM", Direction.RANDOM)
    );
  }

}
