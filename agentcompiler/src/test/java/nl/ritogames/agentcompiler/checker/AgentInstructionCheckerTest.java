package nl.ritogames.agentcompiler.checker;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import nl.ritogames.agentcompiler.ast.ASTNode;
import nl.ritogames.agentcompiler.ast.Artifact;
import nl.ritogames.agentcompiler.ast.Instruction;
import nl.ritogames.agentcompiler.ast.InstructionParameter;
import nl.ritogames.agentcompiler.ast.Unit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AgentInstructionCheckerTest {

    private AgentInstructionChecker sut;
    private List<ASTNode> SENSOR_LIST;

    private static final String INVALID_PARAMETER = "teammate";

    @BeforeEach
    void setUp() {
        sut = new AgentInstructionChecker();
        SENSOR_LIST = (List<ASTNode>) mock(List.class);
        when(SENSOR_LIST.contains(any(ASTNode.class))).thenReturn(true);
        assertTrue(sut.getErrorMessages().isEmpty());
    }

    @Test
    void validInstructionShouldNotAddErrorMessage() {
        Instruction instruction = new Instruction("move up", null);
        sut.check(instruction, SENSOR_LIST);

        assertTrue(sut.getErrorMessages().isEmpty());
    }

    @Test
    void invalidInstructionShouldAddErrorMessage() {
        Artifact artifact = new Artifact("flag");
        Instruction instruction = new Instruction("pick up ", artifact);
        sut.check(instruction, SENSOR_LIST);

        assertFalse(sut.getErrorMessages().isEmpty());
    }

    @Test
    void validParameterShouldNotAddErrorMessage() {
        InstructionParameter enemy = new Unit("enemy");
        Instruction instruction = new Instruction("move towards", enemy);
        sut.check(instruction, SENSOR_LIST);

        assertTrue(sut.getErrorMessages().isEmpty());
    }

    @Test
    void invalidParameterShouldAddErrorMessage() {
        InstructionParameter teammate = new Unit(INVALID_PARAMETER);
        Instruction instruction = new Instruction("attack", teammate);
        sut.check(instruction, SENSOR_LIST);

        assertFalse(sut.getErrorMessages().isEmpty());
    }

    @Test
    void sensorDeclaredInUnitInstruction() {
        InstructionParameter enemy = new Unit("enemy");
        Instruction instruction = new Instruction("move towards", enemy);
        List<ASTNode> localSensorList = new ArrayList<>();
        localSensorList.add(new Unit("enemy"));

        sut.check(instruction, localSensorList);

        assertTrue(sut.getErrorMessages().isEmpty());
    }

    @Test
    void sensorNotDeclaredInUnitInstruction() {
        InstructionParameter enemy = new Unit("enemy");
        Instruction instruction = new Instruction("move towards", enemy);
        List<ASTNode> localSensorList = new ArrayList<>();
        localSensorList.add(new Unit("teammate"));

        sut.check(instruction, localSensorList);

        assertFalse(sut.getErrorMessages().isEmpty());
    }

    @Test
    void sensorDeclaredInArtifactInstruction() {
        InstructionParameter sword = new Artifact("weapon");
        Instruction instruction = new Instruction("pickup", sword);
        List<ASTNode> localSensorList = new ArrayList<>();
        localSensorList.add(new Artifact("weapon"));

        sut.check(instruction, localSensorList);

        assertTrue(sut.getErrorMessages().isEmpty());
    }

    @Test
    void sensorNotDeclaredInArtifactInstruction() {
        InstructionParameter sword = new Artifact("weapon");
        Instruction instruction = new Instruction("pickup", sword);
        List<ASTNode> localSensorList = new ArrayList<>();
        localSensorList.add(new Artifact("armor"));

        sut.check(instruction, localSensorList);

        assertFalse(sut.getErrorMessages().isEmpty());
    }

    @Test
    void emptySensorListReturnsError() {
        InstructionParameter sword = new Artifact("weapon");
        Instruction instruction = new Instruction("pickup", sword);
        List<ASTNode> localSensorList = new ArrayList<>();

        sut.check(instruction, localSensorList);

        assertFalse(sut.getErrorMessages().isEmpty());
    }

    @Test
    void clearErrorMessagesTestWorking() {
        InstructionParameter sword = new Artifact("weapon");
        Instruction instruction = new Instruction("pickup", sword);
        List<ASTNode> localSensorList = new ArrayList<>();

        sut.check(instruction, localSensorList);

        assertFalse(sut.getErrorMessages().isEmpty());
        sut.clearErrorMessages();
        assertTrue(sut.getErrorMessages().isEmpty());
    }

    @Test
    void agentCantAttackTeammate() {
        Unit teammate = new Unit("teammate");
        Instruction instruction = new Instruction("attack", teammate);
        sut.check(instruction, SENSOR_LIST);

        assertFalse(sut.getErrorMessages().isEmpty());
    }
}