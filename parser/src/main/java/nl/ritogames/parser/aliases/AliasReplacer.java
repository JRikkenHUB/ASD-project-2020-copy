package nl.ritogames.parser.aliases;

import nl.ritogames.shared.logger.Logger;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class AliasReplacer {

  private final Map<String, String> aliases;

  public AliasReplacer() {
    /**
     * <p>LinkedHashMap is used because the order is necessary to prevent bugs
     * Also this Map will be looped many times so the performance is greater with a LinkedHashMap</p>
     */
    this.aliases = new LinkedHashMap<>();
  }

  public String replaceAliases(String input) {
    Logger.logMethodCall(this);
    for (Entry<String, String> entry : aliases.entrySet()) {
      String alias = entry.getKey();
      String result = entry.getValue();
      input = input.replaceAll("\\b" + alias + "\\b", result);
    }
    return input;
  }

  public void registerAlias(String alias, String result) {
    aliases.put(alias, result);
  }
}
