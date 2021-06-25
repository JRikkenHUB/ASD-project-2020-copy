package nl.ritogames.generator.xml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.MissingResourceException;
import java.util.UUID;

/** The XMLRetriever. Provides a method to retrieve XML from resource files. */
public class XMLRetriever {
  /**
   * Retrieves XML string from specific resource file.
   *
   * @param fileName the name of the resource file
   * @return the xml string
   * @throws IOException when string cannot be copied to the tempFile.
   */
  public String getXMLString(String fileName) throws IOException {
    final InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
    if (inputStream == null) {
      throw new MissingResourceException(
          String.format("Could not find resource: %s", fileName),
          XMLRetriever.class.getName(),
          fileName);
    }
    Path tempFile = Files.createTempDirectory("").resolve(UUID.randomUUID().toString() + ".tmp");
    Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
    return new String(Files.readAllBytes(tempFile));
  }
}
