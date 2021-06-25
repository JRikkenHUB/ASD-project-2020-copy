package nl.ritogames.filehandler.encryption;

import nl.ritogames.shared.exception.ASDCryptoException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

/**
 * The ASDCrypt encryption/decryption utility class.
 *
 * @author Dani Hengeveld
 */
public final class ASDCrypt {

  /**
   * The Secret key.
   */
  static String key = "noegnudsasdungeon";

  /**
   * The Transform.
   */
  static String transform = "AES/GCM/NoPadding";

  private ASDCrypt() {
  }

  /**
   * Encrypts the given string.
   *
   * @param strToEncrypt the string to encrypt.
   * @return the encrypted string.
   * @throws ASDCryptoException asd crypto exception when something goes wrong.
   * @see Cipher
   * @see Base64
   */
  public static String encrypt(String strToEncrypt) throws ASDCryptoException {
    try {
      SecureRandom secureRandom = new SecureRandom();
      byte[] iv = new byte[12];
      secureRandom.nextBytes(iv);

      SecretKey secretKey = generateSecretKey(iv);

      Cipher cipher = Cipher.getInstance(transform);
      GCMParameterSpec parameterSpec = new GCMParameterSpec(128, iv);
      cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);

      byte[] encryptedData = cipher.doFinal(strToEncrypt.getBytes());

      ByteBuffer byteBuffer = ByteBuffer.allocate(4 + iv.length + encryptedData.length);
      byteBuffer.putInt(iv.length);
      byteBuffer.put(iv);
      byteBuffer.put(encryptedData);

      return Base64.getEncoder().encodeToString(byteBuffer.array());
    } catch (Exception e) {
      throw new ASDCryptoException("Error while encrypting", e);
    }
  }

  /**
   * Decrypts the given string.
   *
   * @param strToDecrypt the string to decrypt.
   * @return the decrypted string.
   * @throws ASDCryptoException asd crypto exception when something goes wrong.
   * @see Cipher
   * @see Base64
   */
  public static String decrypt(String strToDecrypt) throws ASDCryptoException {
    try {
      ByteBuffer byteBuffer = ByteBuffer.wrap(Base64.getDecoder().decode(strToDecrypt));

      int nonceSize = byteBuffer.getInt();

      if (nonceSize < 12 || nonceSize >= 16) {
        throw new IllegalArgumentException(
            "Nonce size is incorrect. Make sure that the incoming data is an AES encrypted file");
      }
      byte[] iv = new byte[nonceSize];
      byteBuffer.get(iv, 0, nonceSize);

      SecretKey secretKey = generateSecretKey(iv);

      byte[] cipherBytes = new byte[byteBuffer.remaining()];
      byteBuffer.get(cipherBytes);

      Cipher cipher = Cipher.getInstance(transform);
      GCMParameterSpec parameterSpec = new GCMParameterSpec(128, iv);

      cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);

      return new String(cipher.doFinal(cipherBytes));
    } catch (Exception e) {
      throw new ASDCryptoException("Error while decrypting", e);
    }
  }

  /**
   * Function to generate a 128 bit key from the given password and iv
   *
   * @param iv
   * @return Secret key
   * @throws NoSuchAlgorithmException
   * @throws InvalidKeySpecException
   */
  private static SecretKey generateSecretKey(byte[] iv)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    KeySpec spec = new PBEKeySpec(key.toCharArray(), iv, 65536, 128); // AES-128
    SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    byte[] key = secretKeyFactory.generateSecret(spec).getEncoded();
    return new SecretKeySpec(key, "AES");
  }
}
