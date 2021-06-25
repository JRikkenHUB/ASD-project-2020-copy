package nl.ritogames.trui.uicomponent.screen;

import nl.ritogames.trui.render.UIContext;

public class StartupScreen implements Screen {

  @Override
  public void draw(UIContext context) {
    System.out.println("Welcome to ASDungeon");
    System.out.println("Enter a username to begin: ");
  }
}
