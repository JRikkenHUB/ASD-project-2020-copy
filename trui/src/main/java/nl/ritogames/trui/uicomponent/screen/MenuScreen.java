package nl.ritogames.trui.uicomponent.screen;

import nl.ritogames.trui.render.UIContext;
import nl.ritogames.trui.render.message.Message;

import java.util.List;

public abstract class MenuScreen implements Screen {

  private final String RED = "\033[0;31m";
  private final String RESET = "\033[0m";



  protected void renderError(UIContext uiContext) {
    if (uiContext.hasError()) {
      System.out.println(RED + uiContext.getError() + RESET);
    }
  }

  protected void renderMessages(List<Message> messages) {
    if (messages.size() > 0) {
      System.out.printf("Message Log: showing %s recent messages%n", messages.size());
      messages.forEach(message -> System.out.printf("%s > %s%n", message.getSender(), message.getContent()));
    }
  }
}
