package nl.ritogames.filehandler;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;

/**
 * The Unit tests for the ASDFileHandler.
 *
 * @author Dani Hengeveld
 */
class ASDFileHandlerTest implements TestResourceProvider {

  static ASDFileHandler sut;

  @BeforeAll
  static void setUp() {
    sut = new ASDFileHandler();
    sut.setCurrentDir(Path.of(""));
  }

  @Test
  void testCreateFile(@TempDir Path tempDir) throws Exception {
    Path filePath = tempDir.resolve(RESOURCE_1_FILE_NAME);
    Path filePath2 = tempDir.resolve("agents/" + RESOURCE_1_FILE_NAME);

    sut.createFile(filePath, FILE_1_CONTENT);
    sut.createFile(filePath2, FILE_1_CONTENT);

    assertAll(
        () -> assertTrue(Files.exists(filePath)),
        () -> assertTrue(Files.exists(filePath2)),
        () -> assertEquals(FILE_1_CONTENT, Files.readString(filePath)),
        () -> assertEquals(FILE_1_CONTENT, Files.readString(filePath2))
    );
  }

  @Test
  void testReadOutOfMemory() {
    try (MockedStatic<Files> theMock = mockStatic(Files.class)) {
      theMock.when(() -> Files.readString(any(Path.class))).thenThrow(OutOfMemoryError.class);
      assertAll(
          () -> assertThrows(IOException.class, () -> sut.readFile(Path.of(""))),
          () -> assertThrows(IOException.class, () -> sut.readEncryptedFile(Path.of("")))
      );
    }
  }

  @Test
  void testReadFile() throws Exception {
    ClassLoader classLoader = getClass().getClassLoader();
    URL testFile = classLoader.getResource(RESOURCE_1_FILE_NAME);

    String actual = sut.readFile(Path.of(testFile.toURI()));

    assertEquals(FILE_1_CONTENT, actual);
  }

  @Test
  void testReadFileNotExists() {
    assertThrows(NoSuchFileException.class, () -> sut.readFile(Path.of("iDoNotExists.oops")));
  }

  @Test
  void testDeleteFileSuccess(@TempDir Path tempDir) throws Exception {
    Path filePath = tempDir.resolve(RESOURCE_1_FILE_NAME);

    ClassLoader classLoader = getClass().getClassLoader();
    URL testFile = classLoader.getResource(RESOURCE_1_FILE_NAME);

    Files.copy(Path.of(testFile.toURI()), filePath);

    boolean deleted = sut.deleteFile(filePath);

    assertAll(
        () -> assertTrue(deleted),
        () -> assertTrue(Files.notExists(filePath))
    );
  }

  @Test
  void testDeleteFileNotExists(@TempDir Path tempDir) throws Exception {
    boolean deleted = sut.deleteFile(tempDir.resolve("randomdir/idonotexist.asdia"));
    assertFalse(deleted);
  }
}
