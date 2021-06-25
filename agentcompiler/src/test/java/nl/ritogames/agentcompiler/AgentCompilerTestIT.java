package nl.ritogames.agentcompiler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import nl.ritogames.agentcompiler.checker.AgentChecker;
import nl.ritogames.agentcompiler.checker.AgentInstructionChecker;
import nl.ritogames.agentcompiler.generator.AgentGenerator;
import nl.ritogames.agentcompiler.parser.AgentLexer;
import nl.ritogames.agentcompiler.parser.AgentParser;
import nl.ritogames.agentcompiler.parser.AgentPreprocessor;
import nl.ritogames.shared.FileRepository;
import nl.ritogames.shared.exception.AgentAlreadyExistsException;
import nl.ritogames.shared.exception.AgentIsNotActiveException;
import nl.ritogames.shared.exception.AgentNotFoundException;
import nl.ritogames.shared.exception.CompilationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;

@Tag("integration-test")
class AgentCompilerTestIT {

    private final FileRepository fileHandlerMock = mock(FileRepository.class);

    private static final String AGENT_NAME = "megabot2000";

    private final String sourcePath = String.format(ASDAgentCompiler.AGENT_SOURCE_FORMAT, AGENT_NAME);
    private final String targetPath = String.format(ASDAgentCompiler.AGENT_TARGET_FORMAT, AGENT_NAME);

    private ASDAgentCompiler sut;

    @BeforeEach
    void setUp() throws CompilationException {
        Stage<String, String> pipeline = (new AgentPreprocessor())
            .pipe(new AgentLexer())
            .pipe(new AgentParser())
            .pipe(new AgentChecker(new AgentInstructionChecker()))
            .pipe(new AgentGenerator());
        sut = new ASDAgentCompiler(pipeline);
        sut.setFileRepository(fileHandlerMock);
    }

    @ParameterizedTest
    @MethodSource("validSourceProvider")
    void testSuccessfulCompilation(String source, String target)
            throws IOException, CompilationException {
        when(fileHandlerMock.readFile(Paths.get(sourcePath))).thenReturn(source);

        sut.compile(AGENT_NAME);

        final ArgumentCaptor<String> targetCaptor = ArgumentCaptor.forClass(String.class);
        final ArgumentCaptor<Path> pathCaptor = ArgumentCaptor.forClass(Path.class);
        verify(fileHandlerMock).createFile(pathCaptor.capture(), targetCaptor.capture());
        assertEquals(target, targetCaptor.getValue());
        assertEquals(targetPath, pathCaptor.getValue().toString().replace("\\", "/"));
    }

    @ParameterizedTest
    @MethodSource("invalidSourceProvider")
    void testLevel0WithoutDefaultBehaviorShouldFail(String source) throws IOException {
        when(fileHandlerMock.readFile(Paths.get(sourcePath))).thenReturn(source);

        assertThrows(CompilationException.class, () -> sut.compile(AGENT_NAME));
    }

    @Test
    void fileHandlerReadErrorShouldBeMappedToCompilationException() throws IOException {
        when(fileHandlerMock.readFile(any())).thenThrow(IOException.class);

        assertThrows(CompilationException.class, () -> sut.compile(""));
    }

    @Test
    void fileHandlerWriteErrorShouldBeMappedToCompilationException()
        throws IOException, CompilationException {
        Stage<String, String> pipelineMock = mock(Stage.class);
        sut = new ASDAgentCompiler(pipelineMock);
        sut.setFileRepository(fileHandlerMock);
        when(pipelineMock.execute(any())).thenReturn("");
        doThrow(IOException.class).when(fileHandlerMock).createFile(any(), any());
        assertThrows(CompilationException.class, () -> sut.compile(""));
    }

    @Test
    void getActiveAgentShouldReturnAgentJSONOfActiveAgent()
        throws AgentNotFoundException, IOException, AgentIsNotActiveException {
        ASDAgentCompiler agentCompilerSpy = spy(sut);
        doReturn(Collections.singletonList(AGENT_NAME)).when(agentCompilerSpy).getAgents();

        agentCompilerSpy.setActiveAgent(AGENT_NAME);
        String result = agentCompilerSpy.getActiveAgent();

        assertEquals(AGENT_NAME, result);
    }

    @Test
    void setActiveAgentShouldThrowAgentNotFoundExceptionWhenAgentDoesNotExist() throws IOException {
        when(fileHandlerMock.getFilesInDirectory(Path.of(ASDAgentCompiler.AGENT_DIRECTORY_NAME)))
            .thenReturn(new ArrayList<>());

        assertThrows(AgentNotFoundException.class, () -> sut.setActiveAgent(AGENT_NAME));
    }

    @Test
    void createAgentShouldCallCreateFile() throws AgentAlreadyExistsException, IOException {
        sut.createAgent(AGENT_NAME);

        verify(fileHandlerMock, times(1)).createFile(any(), any());
    }

    @Test
    void createAgentShouldThrowAgentAlreadyExistsExceptionIfFileAlreadyExists() throws IOException {
        doThrow(new FileAlreadyExistsException("")).when(fileHandlerMock).createFile(any(), any());

        assertThrows(AgentAlreadyExistsException.class, () -> sut.createAgent(AGENT_NAME));
    }

    @Test
    void deleteAgentShouldCallDeleteFile() throws IOException {
        Path sourcePath = Path.of(String.format(ASDAgentCompiler.AGENT_SOURCE_FORMAT, AGENT_NAME));
        Path targetPath = Path.of(String.format(ASDAgentCompiler.AGENT_TARGET_FORMAT, AGENT_NAME));
        sut.deleteAgent(AGENT_NAME);

        verify(fileHandlerMock, times(1)).deleteFile(sourcePath);
        verify(fileHandlerMock, times(1)).deleteFile(targetPath);
    }

    @Test
    void getAgentsShouldBeEmptyIfIOExceptionOccurs() throws IOException {
        when(fileHandlerMock.getFilesInDirectory(any())).thenThrow(IOException.class);

        List<String> result = sut.getAgents();

        assertTrue(result.isEmpty());
    }

    private static Stream<Arguments> validSourceProvider() {
        final String LEVEL_0_SOURCE = "default do move up end behavior test do move down end";
        final String LEVEL_0_TARGET = "{\"behaviors\":[{\"behaviorName\":\"default\",\"body\":[{\"type\":\"action\",\"action\":\"move up\"}]},{\"behaviorName\":\"test\",\"body\":[{\"type\":\"action\",\"action\":\"move down\"}]}]}";

        final String LEVEL_1_SOURCE = "default do move up set test end behavior test do move down set default end";
    final String LEVEL_1_TARGET = "{\"behaviors\":[{\"behaviorName\":\"default\",\"body\":[{\"type\":\"action\",\"action\":\"move up\"},{\"type\":\"behaviorCall\",\"behaviorCall\":\"test\"}]},{\"behaviorName\":\"test\",\"body\":[{\"type\":\"action\",\"action\":\"move down\"},{\"type\":\"behaviorCall\",\"behaviorCall\":\"default\"}]}]}";

    final String LEVEL_2_SOURCE = "default do if my health is lower than 20% do set flee end else do set patrol end end behavior flee do if teammate is near do move towards teammate end else do move randomly end end behavior patrol do move randomly if enemy is near do attack enemy end end";
    final String LEVEL_2_TARGET = "{\"behaviors\":[{\"behaviorName\":\"default\",\"body\":[{\"type\":\"decision\",\"condition\":{\"type\":\"dualExpression\",\"expressionType\":\"lt\",\"lhs\":{\"type\":\"unit\",\"unit\":\"my\",\"attribute\":\"health\"},\"rhs\":{\"type\":\"percentage\",\"percentage\":\"20\"}},\"ifTrue\":[{\"type\":\"behaviorCall\",\"behaviorCall\":\"flee\"}],\"ifFalse\":[{\"type\":\"behaviorCall\",\"behaviorCall\":\"patrol\"}]}]},{\"behaviorName\":\"flee\",\"body\":[{\"type\":\"decision\",\"condition\":{\"type\":\"dualExpression\",\"expressionType\":\"state\",\"lhs\":{\"type\":\"unit\",\"unit\":\"teammate\"},\"rhs\":{\"type\":\"state\",\"state\":\"near\"}},\"ifTrue\":[{\"type\":\"action\",\"action\":\"move towards\",\"parameter\":\"teammate\"}],\"ifFalse\":[{\"type\":\"action\",\"action\":\"move randomly\"}]}]},{\"behaviorName\":\"patrol\",\"body\":[{\"type\":\"action\",\"action\":\"move randomly\"},{\"type\":\"decision\",\"condition\":{\"type\":\"dualExpression\",\"expressionType\":\"state\",\"lhs\":{\"type\":\"unit\",\"unit\":\"enemy\"},\"rhs\":{\"type\":\"state\",\"state\":\"near\"}},\"ifTrue\":[{\"type\":\"action\",\"action\":\"attack\",\"parameter\":\"enemy\"}]}]}]}";

    return Stream.of(
        Arguments.of(LEVEL_0_SOURCE, LEVEL_0_TARGET),
        Arguments.of(LEVEL_1_SOURCE, LEVEL_1_TARGET),
        Arguments.of(LEVEL_2_SOURCE, LEVEL_2_TARGET)
    );
  }

  private static Stream<Arguments> invalidSourceProvider() {
    final String INVALID_LEVEL_0_SOURCE = "behavior test do move down end";

    return Stream.of(
        Arguments.of(INVALID_LEVEL_0_SOURCE)
    );
  }
}
