package nl.ritogames.filehandler;

import nl.ritogames.shared.exception.ASDCryptoException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


/**
 * The Integration tests for the ASDFileHandler.
 *
 * @author Dani Hengeveld
 */
class ASDFileHandlerIT implements TestResourceProvider {

  private static ASDFileHandler sut;

  @BeforeAll
  static void setUp() {
    sut = new ASDFileHandler();
    sut.setCurrentDir(Path.of(""));
  }

  @Test
  void testCRUDFile(@TempDir Path tempDir) throws IOException {
    Path filePath = tempDir.resolve(RESOURCE_1_FILE_NAME);

    // Create
    sut.createFile(filePath, FILE_1_CONTENT);
    final String actual1 = sut.readFile(filePath);

    // Update
    sut.createFile(filePath, FILE_1_CONTENT_ALT);
    final String actual2 = sut.readFile(filePath);

    // Delete
    boolean deleted = sut.deleteFile(filePath);

    assertAll(
        () -> assertEquals(FILE_1_CONTENT, actual1),
        () -> assertEquals(FILE_1_CONTENT_ALT, actual2),
        () -> assertTrue(deleted),
        () -> assertFalse(Files.exists(filePath))
    );
  }

  @Test
  void testCreateEncryptedFile(@TempDir Path tempDir) throws Exception {
    Path filePath = tempDir.resolve(RESOURCE_1_FILE_NAME);
    sut.createEncryptedFile(filePath, FILE_1_CONTENT);

    assertTrue(Files.exists(filePath));
  }

  @Test
  void testReadEncryptedFile() throws Exception {
    ClassLoader classLoader = getClass().getClassLoader();
    URL testFile = classLoader.getResource(RESOURCE_2_FILE_NAME);

    final String actualDecryptedContent = sut.readEncryptedFile(Path.of(testFile.toURI()));

    assertEquals(FILE_1_CONTENT, actualDecryptedContent);
  }

  @Test
  void testCRUDEncryptedFile(@TempDir Path tempDir) throws IOException, ASDCryptoException {
    Path filePath = tempDir.resolve(RESOURCE_1_FILE_NAME);

    // Create
    sut.createEncryptedFile(filePath, FILE_1_CONTENT);
    final String actual1 = sut.readEncryptedFile(filePath);

    // Update
    sut.createEncryptedFile(filePath, FILE_1_CONTENT_ALT);
    final String actual2 = sut.readEncryptedFile(filePath);

    // Delete
    boolean deleted = sut.deleteFile(filePath);

    assertAll(
        () -> assertEquals(FILE_1_CONTENT, actual1),
        () -> assertEquals(FILE_1_CONTENT_ALT, actual2),
        () -> assertTrue(deleted),
        () -> assertFalse(Files.exists(filePath))
    );
  }

  @Test
  void testGetFilesInDirectoryShouldReturnListOfAllFiles(@TempDir Path tempDir) throws IOException {
    List<String> fileNames = Arrays.asList("test1.asdia", "test2.asdia");
    for (String fileName : fileNames) {
      sut.createFile(tempDir.resolve(fileName), "");
    }

    List<String> result = sut.getFilesInDirectory(tempDir);

    assertTrue(result.containsAll(fileNames));
  }
}
