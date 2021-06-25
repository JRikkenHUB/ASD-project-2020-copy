package nl.ritogames.filehandler;

import nl.ritogames.filehandler.encryption.ASDCrypt;
import nl.ritogames.shared.FileRepository;
import nl.ritogames.shared.exception.ASDCryptoException;
import nl.ritogames.shared.exception.AgentNotFoundException;
import nl.ritogames.shared.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The ASDFileHandler. Provides CRUD operations for files.
 *
 * @author Dani Hengeveld
 */
public class ASDFileHandler implements FileRepository {

  private Path currentDir;

  public ASDFileHandler() {
    currentDir = Paths.get(".").toAbsolutePath().normalize();
  }

  public ASDFileHandler(Path currentDir) {
    this.currentDir = currentDir;
  }

  @Override
  public void createFile(Path filePath, String data) throws IOException {
    Logger.logMethodCall(this);
    checkAndCreateDirectories(filePath);
    Files.writeString(Paths.get(currentDir.toString(), filePath.toString()), data);
  }

  @Override
  public String readFile(Path filePath) throws IOException {
    Logger.logMethodCall(this);
    try {
      final String fileData = Files.readString(Paths.get(currentDir.toString(), filePath.toString()));
      return replaceLineEndings(fileData);
    } catch (OutOfMemoryError err) {
      throw new IOException("File was too large to be read", err);
    }
  }

  @Override
  public boolean deleteFile(Path filePath) throws IOException {
    Logger.logMethodCall(this);
    return Files.deleteIfExists(Paths.get(currentDir.toString(), filePath.toString()));
  }

  @Override
  public void createEncryptedFile(Path filePath, String data)
      throws ASDCryptoException, IOException {
    final String encryptedFileData = ASDCrypt.encrypt(data);
    checkAndCreateDirectories(filePath);
    Files.writeString(Paths.get(currentDir.toString(), filePath.toString()), encryptedFileData);
  }

  @Override
  public String readEncryptedFile(Path filePath)
      throws IOException, ASDCryptoException {
    try {
      final String encryptedFileData = Files.readString(Paths.get(currentDir.toString(), filePath.toString()));
      final String decryptedFileData = ASDCrypt.decrypt(encryptedFileData);
      return replaceLineEndings(decryptedFileData);
    } catch (OutOfMemoryError err) {
      throw new IOException("File was too large to be read", err);
    }
  }

  @Override
  public List<String> getFilesInDirectory(Path directoryPath) throws IOException {
    Logger.logMethodCall(this);
    checkAndCreateDirectories(directoryPath);
    Path dir = Paths.get(currentDir.toString(), directoryPath.toString());
    File directory = dir.toFile();
    ArrayList<String> agentList = new ArrayList<>();

    try {
      agentList.addAll(Arrays.asList(directory.list()));
    } catch (NullPointerException e) {
       throw new AgentNotFoundException("No agents were found, please create one first");
    }

    return agentList;
  }

  private void checkAndCreateDirectories(Path filePath) throws IOException {
    Path parentDir = Paths.get(currentDir.toString(), filePath.toString()).getParent();
    if (!Files.exists(parentDir)) {
      Files.createDirectories(parentDir);
    }
  }

  private String replaceLineEndings(String string) {
    return string.replaceAll("\\r\\n?", "\n");
  }

  public void setCurrentDir(Path currentDir) {
    this.currentDir = currentDir;
  }
}
