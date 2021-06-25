package nl.ritogames.trui.uicomponent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VectorTest {

  Vector sut;

  @BeforeEach
  void setup() {
    sut = new Vector(5, 10);
  }

  void testGetValues() {
    //act
    int actualX = sut.getX();
    int actualY = sut.getY();
    //assert
    assertEquals(actualX, 5);
    assertEquals(actualY, 10);
  }

  @Test
  void testEquals() {
    //arrange
    Vector actual = new Vector(6, 10);
    //act
    //assert
    assertFalse(sut.equals(actual));
    assertTrue(sut.equals(sut));
  }

  @Test
  void testHash() {
    //arrange
    Vector vector = new Vector(6, 10);

    //act
    int expected = vector.hashCode();
    int actual = sut.hashCode();

    //assert
    assertNotEquals(expected, actual);
  }


}
