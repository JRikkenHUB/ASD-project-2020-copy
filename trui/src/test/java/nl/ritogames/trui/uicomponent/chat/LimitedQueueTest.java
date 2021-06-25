package nl.ritogames.trui.uicomponent.chat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

class LimitedQueueTest {

  private final int maxSize = 2;
  private LimitedQueue<String> sut;

  @BeforeEach
  void setUp() {
    sut = new LimitedQueue<>(maxSize);
  }

  @Test
  void addLowerThanMaxSize() {
    sut.add("Hello");

    String[] expected = {"Hello"};
    String[] result = sut.toArray(new String[0]);

    Assertions.assertTrue(Arrays.equals(expected, result));
  }

  @Test
  void addAtMaxSize() {

    sut.add("Hello");
    sut.add("There");

    String[] expected = {"Hello", "There"};
    String[] result = sut.toArray(new String[sut.size()]);

    Assertions.assertTrue(Arrays.equals(expected, result));

  }

  @Test
  void addHigherThanMaxSize() {

    sut.add("Hello");
    sut.add("There");
    sut.add("General");
    sut.add("Kenobi");

    String[] expected = {"General", "Kenobi"};
    String[] result = sut.toArray(new String[sut.size()]);

    Assertions.assertTrue(Arrays.equals(expected, result));

  }

  @Test
  void addAllLowerThanMaxSize() {
    ArrayList<String> list = new ArrayList<>();
    list.add("Hello");

    sut.addAll(list);

    String[] expected = {"Hello"};
    String[] result = sut.toArray(new String[sut.size()]);

    Assertions.assertTrue(Arrays.equals(expected, result));
  }

  @Test
  void addAllAtThanMaxSize() {
    ArrayList<String> list = new ArrayList<>();
    list.add("Hello");
    list.add("There");

    sut.addAll(list);

    String[] expected = {"Hello", "There"};
    String[] result = sut.toArray(new String[sut.size()]);

    Assertions.assertTrue(Arrays.equals(expected, result));
  }

  @Test
  void addAllHigherThanMaxSize() {
    ArrayList<String> list = new ArrayList<>();
    list.add("Hello");
    list.add("There");
    list.add("General");
    list.add("Kenobi");

    sut.addAll(list);

    String[] expected = {"General", "Kenobi"};
    String[] result = sut.toArray(new String[sut.size()]);

    Assertions.assertTrue(Arrays.equals(expected, result));
  }

  @Test
  void addFirstLowerThanMaxSize() {
    sut.addFirst("Hello");

    String[] expected = {"Hello"};
    String[] result = sut.toArray(new String[sut.size()]);

    Assertions.assertTrue(Arrays.equals(expected, result));

  }

  @Test
  void addFirstAtThanMaxSize() {

    sut.addFirst("Hello");
    sut.addFirst("There");

    String[] expected = {"There", "Hello"};
    String[] result = sut.toArray(new String[sut.size()]);

    Assertions.assertTrue(Arrays.equals(expected, result));

  }

  @Test
  void addFirstHigherThanMaxSize() {

    sut.addFirst("Hello");
    sut.addFirst("There");
    sut.addFirst("General");
    sut.addFirst("Kenobi");

    String[] expected = {"There", "Hello"};
    String[] result = sut.toArray(new String[sut.size()]);

    Assertions.assertTrue(Arrays.equals(expected, result));

  }

  @Test
  void addLastLowerThanMaxSize() {
    sut.addLast("Hello");

    String[] expected = {"Hello"};
    String[] result = sut.toArray(new String[sut.size()]);

    Assertions.assertTrue(Arrays.equals(expected, result));
  }

  @Test
  void addLastAtThanMaxSize() {

    sut.addLast("Hello");
    sut.addLast("There");

    String[] expected = {"Hello", "There"};
    String[] result = sut.toArray(new String[sut.size()]);

    Assertions.assertTrue(Arrays.equals(expected, result));

  }

  @Test
  void addLastHigherThanMaxSize() {
    sut.addLast("Hello");
    sut.addLast("There");
    sut.addLast("General");
    sut.addLast("Kenobi");

    String[] expected = {"General", "Kenobi"};
    String[] result = sut.toArray(new String[sut.size()]);

    Assertions.assertTrue(Arrays.equals(expected, result));

  }

}
