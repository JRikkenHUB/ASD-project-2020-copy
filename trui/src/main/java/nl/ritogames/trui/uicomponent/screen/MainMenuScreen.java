package nl.ritogames.trui.uicomponent.screen;

import nl.ritogames.trui.render.UIContext;

/**
 * The screen of the command line interface during the state {@link nl.ritogames.trui.enums.ScreenState#MENU}
 */
public class MainMenuScreen extends MenuScreen {

  @Override
  public void draw(UIContext context) {
    this.renderError(context);
    System.out.println("== Main Menu ==");
    System.out.println("- To manage your agents write 'agent configure'");
    System.out.println("- To start a game write 'game create <gamename>'");
    System.out.println("- To exit the game write 'exit'");
    this.renderMessages(context.getMessages());

  }
}
