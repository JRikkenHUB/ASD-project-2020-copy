package nl.ritogames.parser.command;

import nl.ritogames.parser.command.annotation.CommandParam;
import nl.ritogames.shared.exception.InvalidArgumentException;
import nl.ritogames.shared.logger.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ArgumentParser {

  public Command mapArguments(String input, Class<? extends Command> commandClass) throws InvalidArgumentException {
    Logger.logMethodCall(this);
    // get a list of arguments
    List<String> arguments = Arrays.asList(input.split(" "));
    Method[] methods = commandClass.getMethods();
    //make sure the order of parameters is maintained for consistency
    Arrays.sort(methods, Comparator.comparingInt(this::getMethodOrderValue));

    Command command = getCommandInstance(commandClass);

    // check which setters of the command accept this argument.
    for (Method method : methods) {
      if (containsAnnotation(method)) {
        parseAnnotation(method, arguments, command);
      }
    }
    return command;
  }

  private Command getCommandInstance(Class<? extends Command> commandClass) {
    try {
      return commandClass.getConstructor().newInstance();
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }

  private boolean containsAnnotation(Method method) {
    return Arrays.stream(method.getAnnotations())
        .anyMatch(a ->
            a.annotationType().equals(CommandParam.class))
        && method.getReturnType().equals(Void.TYPE)
        && method.getParameterCount() == 1
        && method.getParameterTypes()[0].equals(String.class);
  }

  private void parseAnnotation(Method method, List<String> input, Command src)
      throws InvalidArgumentException {
    try {
      CommandParam commandParam = method.getAnnotation(CommandParam.class);
      // if the parameter is indexed, grab the input at the index
      if (input.size() > commandParam.index()) {
        method.invoke(src, input.get(commandParam.index()));
      } else if (!commandParam.optional()) {
        throw new InvalidArgumentException(input.get(0), "",
            String.format("Expected to get more parameters for this command, usage: %s",
                src.getUsage()));
      }
    } catch (IllegalAccessException e) {
      throw new IllegalArgumentException(
          "Method needs to be a void method with 1 String parameter");
    } catch (InvocationTargetException e) {
      if (e.getCause() instanceof InvalidArgumentException) {
        throw (InvalidArgumentException) e.getCause();
      } else {
        throw new RuntimeException(e.getCause());
      }
    }
  }

  private int getMethodOrderValue(Method method) {
    CommandParam annotation = method.getAnnotation(CommandParam.class);
    if (annotation != null) {
      return annotation.index();
    }
    return 0;
  }

}
