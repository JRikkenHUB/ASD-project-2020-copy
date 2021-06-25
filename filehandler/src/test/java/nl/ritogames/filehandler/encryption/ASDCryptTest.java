package nl.ritogames.filehandler.encryption;

import nl.ritogames.filehandler.TestResourceProvider;
import nl.ritogames.shared.exception.ASDCryptoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The unit tests for ASDCrypt
 */
class ASDCryptTest implements TestResourceProvider {

  private static final String originalString = "this is a test string. short, but fine for testing";
  private static final String encryptedString = "AAAADNum9qoci10+D2GrIPycrqv58jEWHCJECw7F59IDQVirObyiesB900Le8TnDtFuGVj5ifUtgyABV0wg4IUA4tsdOetB/0HRJ/OWUBnpTKA==";

  /**
   * Reset all ASDCrypt attributes to their original values.
   */
  @BeforeEach
  void reset() {
    ASDCrypt.key = "noegnudsasdungeon";
    ASDCrypt.transform = "AES/GCM/NoPadding";
  }

  @Test
  void testEncryption() throws ASDCryptoException {
    assertDoesNotThrow(() -> ASDCrypt.encrypt(originalString));
  }

  @Test
  void testDecryption() throws ASDCryptoException {
    final String actualDecryptedString = ASDCrypt.decrypt(encryptedString);

    assertEquals(originalString, actualDecryptedString);
  }

  @Test
  void testDecryptingWrongEncryptionType() {
    final String wrongEncryptionString = "k74HKrhtQ4ZU698vL9AmE+F9WT3ciLRWtPF0oF4jz4Q=";
    assertThrows(ASDCryptoException.class, () -> ASDCrypt.decrypt(wrongEncryptionString));
  }

  @Test
  void testWrongSecretKey() {
    ASDCrypt.key = "wrong";

    assertThrows(ASDCryptoException.class, () -> ASDCrypt.decrypt(encryptedString));
  }

  @Test
  void testInvalidTransform() {
    ASDCrypt.transform = "invalid";

    assertThrows(ASDCryptoException.class, () -> ASDCrypt.encrypt(originalString));
  }

}
