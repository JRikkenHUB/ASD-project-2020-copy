package nl.ritogames.shared;

import nl.ritogames.shared.exception.ASDCryptoException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * The interface FileRepository. Provides CRUD operation methods for file access.
 *
 * @author Dani Hengeveld
 */
public interface FileRepository {

  /**
   * Create a file in given directory with given name and data.
   *
   * @param filePath the path to the new file (including the name of the file and the file
   *                 extension).
   * @param data     the contents of the file as a string.
   * @throws IOException the IOException for when IO operations fail.
   * @see Path
   */
  void createFile(Path filePath, String data) throws IOException;

  /**
   * Read the file on given path.
   *
   * @param filePath the path to the file.
   * @return the contents of the file as a string.
   * @throws IOException the IOException for when IO operations fail.
   */
  String readFile(Path filePath) throws IOException;

  /**
   * Delete the file on given path.
   *
   * @param filePath the path to the file.
   * @return true if file existed and was deleted. false if file does not exist and was not deleted.
   * @throws IOException the IOException for when IO operations fail.
   */
  boolean deleteFile(Path filePath) throws IOException;

  /**
   * Creates an encrypted file in given directory with given name and data.
   *
   * @param filePath the path to the new file (including the name of the file and the file *
   *                 extension).
   * @param data     the contents of the file as a string.
   * @throws ASDCryptoException the asd crypto exception
   * @throws IOException        the io exception
   */
  void createEncryptedFile(Path filePath, String data)
      throws ASDCryptoException, IOException;

  /**
   * Read and decrypt the file on given path.
   *
   * @param filePath the path to the file.
   * @return the contents of the file as a string.
   * @throws IOException        the io exception
   * @throws ASDCryptoException the asd crypto exception
   */
  String readEncryptedFile(Path filePath)
      throws IOException, ASDCryptoException;

  /**
   * Get the names of all files in a directory.
   */
  List<String> getFilesInDirectory(Path directoryPath) throws IOException;
}
