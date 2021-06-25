package nl.ritogames.agentcompiler;

import nl.ritogames.agentcompiler.checker.AgentChecker;
import nl.ritogames.agentcompiler.checker.AgentInstructionChecker;
import nl.ritogames.agentcompiler.generator.AgentGenerator;
import nl.ritogames.agentcompiler.parser.AgentLexer;
import nl.ritogames.agentcompiler.parser.AgentParser;
import nl.ritogames.agentcompiler.parser.AgentPreprocessor;
import nl.ritogames.agentcompiler.transformer.AgentTransformer;
import nl.ritogames.shared.AgentCompiler;
import nl.ritogames.shared.FileRepository;
import nl.ritogames.shared.exception.AgentAlreadyExistsException;
import nl.ritogames.shared.exception.AgentIsNotActiveException;
import nl.ritogames.shared.exception.AgentNotFoundException;
import nl.ritogames.shared.exception.CompilationException;
import nl.ritogames.shared.logger.Logger;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Standard implementation of the AgentCompiler interface. This implementation uses .asdia files as
 * source files and compiles to JSON. It stores both source and compiled files in the /data/agents
 * directory relative to the running JAR. This implementation of the agent compiler uses a
 * pipe-and-filter chain of compilation stages. If an error occurs during compilation, a
 * CompilationException is generated.
 */
public class ASDAgentCompiler implements AgentCompiler {

  private FileRepository fileRepository;
  public static final String AGENT_DIRECTORY_NAME = "/data/agents/";
  public static final String AGENT_SOURCE_FORMAT = AGENT_DIRECTORY_NAME + "%s.asdia";
  public static final String AGENT_TARGET_FORMAT = AGENT_DIRECTORY_NAME + "%s.json";

  private String activeAgent;

  private Stage<String, String> pipeline;

  /**
   * Instantiates a new Asd agent compiler.
   */
  public ASDAgentCompiler() {
    // Method looks like it's unused, but it is necessary for dependency injection (don't remove).
    clearPipeline();
  }

  public ASDAgentCompiler(Stage<String, String> pipeline) {
    this.pipeline = pipeline;
  }


  @Override
  public void compile(String agentName) {
    Logger.logMethodCall(this);
    String sourcePath = String.format(AGENT_SOURCE_FORMAT, agentName);
    String sourceCode = readSourceCode(sourcePath);

    String targetCode;
    String targetPath = String.format(AGENT_TARGET_FORMAT, agentName);
    try {
      targetCode = pipeline.execute(sourceCode);
      writeTargetCode(targetPath, targetCode);
    } finally {
      clearPipeline();
    }
  }

    @Override
    public String getActiveAgent() {
      if (activeAgent == null) throw new AgentIsNotActiveException("Select an agent first");
      return activeAgent;
    }

  @Override
  public void setActiveAgent(String agentName) {
    List<String> agents = getAgents();
    if (!agents.contains(agentName)) {
      String message = String.format("Agent '%s' does not exist. Create it first.", agentName);
      throw new AgentNotFoundException(message);
    } else {
      this.activeAgent = agentName;
    }
  }

  @Override
  public List<String> getAgents() {
    List<String> agentFiles;
    try {
      agentFiles = fileRepository.getFilesInDirectory(Path.of(AGENT_DIRECTORY_NAME));
    } catch (IOException e) {
      return new ArrayList<>();
    }

    return agentFiles.stream()
        .filter(fileName -> fileName.matches(".*\\.asdia"))
        .map(fileName -> fileName.substring(0, fileName.length() - ".asdia".length()))
        .collect(Collectors.toList());
  }

  @Override
  public void createAgent(String name) throws IOException {
    String fileName = String.format(AGENT_SOURCE_FORMAT, name);
    try {
      fileRepository.createFile(Paths.get(fileName), "");
    } catch (FileAlreadyExistsException e) {
      String message = String.format("Agent with name %s already exists.", name);
      throw new AgentAlreadyExistsException(message);
    }
  }

  @Override
  public void deleteAgent(String name) {
    String sourceFileName = String.format(AGENT_SOURCE_FORMAT, name);
    String targetFileName = String.format(AGENT_TARGET_FORMAT, name);
    try {
      fileRepository.deleteFile(Paths.get(sourceFileName));
      fileRepository.deleteFile(Paths.get(targetFileName));
    } catch (IOException ignored) {
      //Exception is ignored
    }
  }

  private void clearPipeline() {
    this.pipeline = new AgentPreprocessor()
        .pipe(new AgentLexer())
        .pipe(new AgentParser())
        .pipe(new AgentChecker(new AgentInstructionChecker()))
        .pipe(new AgentTransformer())
        .pipe(new AgentGenerator());
  }

  private void writeTargetCode(String targetPath, String targetCode) {
    try {
      fileRepository.createFile(Paths.get(targetPath), targetCode);
    } catch (IOException e) {
      throw new CompilationException("An error occurred while creating the agent file.");
    }
  }

  private String readSourceCode(String sourcePath) {
    try {
      return fileRepository.readFile(Paths.get(sourcePath));
    } catch (IOException e) {
      throw new CompilationException("An error occurred while reading the agent file.");
    }
  }

  @Inject
  public void setFileRepository(FileRepository fileRepository) {
    this.fileRepository = fileRepository;
  }
}
