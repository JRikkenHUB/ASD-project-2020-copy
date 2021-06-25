package nl.ritogames.agentcompiler.checker;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nl.ritogames.agentcompiler.ast.ASTNode;
import nl.ritogames.agentcompiler.ast.Artifact;
import nl.ritogames.agentcompiler.ast.Instruction;
import nl.ritogames.agentcompiler.ast.InstructionParameter;
import nl.ritogames.agentcompiler.ast.Unit;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * This class checks Instructions for Errors. -Checks if sensors are already defined in the current
 * Scope -Checks if the Instructions is allowed (using instructions.json as reference)
 */
public class AgentInstructionChecker {

    private final Map<String, List<String>> validInstructionParameters;
    private final List<String> errorMessages;

    private static final String INSTRUCTION_FILE_NAME = "instructions.json";
    private static final String PARAMETER_KEY = "parameters";
    private static final String ALIAS_KEY = "alias";

    private static final String INVALID_INSTRUCTION = "Instruction '%s' does not exist.";
    private static final String INVALID_PARAMETER = "Invalid parameter '%s' for instruction '%s'. Valid parameters are: %s";
    private static final String UNDEFINED_SENSOR = "The sensor %s hasn't been defined in this scope.";

    /**
     * Instantiates a new Agent instruction checker.
     */
    public AgentInstructionChecker() {
        this(new HashMap<>(), new ArrayList<>());
    }

    /**
     * Instantiates a new Agent instruction checker.
     *
     * @param validInstructionParameters the valid instruction parameters
     * @param errorMessages              the error messages
     */
    AgentInstructionChecker(Map<String, List<String>> validInstructionParameters,
        List<String> errorMessages) {
        this.validInstructionParameters = validInstructionParameters;
        this.errorMessages = errorMessages;
        loadInstructions();
    }

    private void loadInstructions() {
        InputStream instructionStream = getClass().getClassLoader()
            .getResourceAsStream(INSTRUCTION_FILE_NAME);
        if (instructionStream == null) {
            errorMessages.add("Couldn't load the predefined instructions from file.");
            return;
        }
        JSONObject instructions = new JSONObject(new JSONTokener(instructionStream));

        for (String instruction : instructions.keySet()) {
            JSONObject instructionObject = instructions.getJSONObject(instruction);
            JSONArray parameterArray = instructionObject.getJSONArray(PARAMETER_KEY);
            JSONArray aliasesArray = instructionObject.getJSONArray(ALIAS_KEY);

            List<String> parameters = new ArrayList<>();
            for (int p = 0; p < parameterArray.length(); p++) {
                parameters.add(parameterArray.getString(p));
            }

            validInstructionParameters.put(instruction, parameters);
            for (int a = 0; a < aliasesArray.length(); a++) {
                validInstructionParameters.put(aliasesArray.getString(a), parameters);
            }
        }
    }

    /**
     * Check.
     *
     * @param instruction    the Instruction
     * @param definedSensors the defined sensors in the current scope
     */
    void check(Instruction instruction, List<ASTNode> definedSensors) {
        InstructionParameter param = instruction.getParameter();
        String instructionName = instruction.getInstructionText();
        if (!validInstructionParameters.containsKey(instructionName)) {
            errorMessages.add(String.format(INVALID_INSTRUCTION, instructionName));
        } else if (param != null) {
            checkInstructionParameter(instructionName, param, definedSensors);
        }
    }

    private void checkInstructionParameter(String instructionName, InstructionParameter parameter,
        List<ASTNode> definedSensors) {
        List<String> validParams = validInstructionParameters.get(instructionName);
        if (!validParams.contains(parameter.getParameterName())) {
            String errorMessage = String.format(INVALID_PARAMETER, parameter, instructionName,
                String.join(", ", validParams));
            errorMessages.add(errorMessage);
        }

        if (((parameter instanceof Unit && !((Unit) parameter).isSelf())
            || parameter instanceof Artifact) && !definedSensors.contains(parameter)) {
            String errorMessage = String.format(UNDEFINED_SENSOR, parameter.toString());
            errorMessages.add(errorMessage);
        }
    }

    /**
     * Returns the list of error messages.
     *
     * @return the error messages
     */
    List<String> getErrorMessages() {
        return this.errorMessages;
    }

    /**
     * Clear all current error messages.
     */
    void clearErrorMessages() {
        this.errorMessages.clear();
    }
}
