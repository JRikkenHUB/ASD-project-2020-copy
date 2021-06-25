package nl.ritogames.filehandler;

/**
 * The arguments and fixtures provider for the tests
 *
 * @author Dani Hengeveld
 */
public interface TestResourceProvider {

  String RESOURCE_1_FILE_NAME = "readFileTest.asdia";
  String RESOURCE_2_FILE_NAME = "readEncryptedFileTest.asdia";

  String FILE_1_CONTENT = "default do\n"
      + "    if gamemode is lms do\n"
      + "        set patrol\n"
      + "    end\n"
      + "    if gamemode is ctf do\n"
      + "        grab flag\n"
      + "    end\n"
      + "end";

  String FILE_1_CONTENT_ALT = "default do\n"
      + "    if gamemode is ctf do\n"
      + "        set patrol\n"
      + "    end\n"
      + "    if gamemode is lms do\n"
      + "        walk away\n"
      + "    end\n"
      + "end";
}
