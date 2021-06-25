package nl.ritogames.agentcompiler.parser;

import java.util.LinkedHashMap;
import java.util.Map;
import nl.ritogames.agentcompiler.Stage;

/**
 * The AgentPreprocessor converts the input to lowercase and replaces some words making it easier
 * for the lexer to convert the input to a tokenstream
 */
public class AgentPreprocessor implements Stage<String, String> {

  private static final Map<String, String> argumentFilter = new LinkedHashMap<>();

  static {
    argumentFilter.put("behaviour", "behavior");
    argumentFilter.put("do", "{");
    argumentFilter.put("end", "}");
    argumentFilter.put("is higher than", ">");
    argumentFilter.put("is lower than", "<");
    argumentFilter.put("is below", "<");
    argumentFilter.put("and", "&");
    argumentFilter.put("or", "|");
    argumentFilter.put("is", "=");
    argumentFilter.put("have", "has");
    argumentFilter.put("me", "my");
    argumentFilter.put("myself", "my");
    argumentFilter.put("i", "my");
    argumentFilter.put("self", "my");
    argumentFilter.put("object", "artifact");
  }

  private String processString(String str) {
    str = str.toLowerCase();
    for (Map.Entry<String, String> entry : argumentFilter.entrySet()) {
      str = str.replaceAll(getWordBoundary(entry.getKey()), entry.getValue());
    }
    return str;
  }

  @Override
  public String execute(String input) {
    return processString(input);
  }

  private String getWordBoundary(String word){
    return String.format("\\b%s\\b", word);
  }
}
