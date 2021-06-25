package nl.ritogames.trui.uicomponent.screen;

import nl.ritogames.trui.render.UIContext;

public class AgentMenuScreen extends MenuScreen {

  @Override
  public void draw(UIContext context) {
    System.out.println("== Agent Menu ==");
    System.out.println("- To select an active agent, write: agent select <agentname>");
    System.out.println("- To list all available agents, write: agent list");
    System.out.println("- To delete an agent, write: deleteagent <agentname>");
    this.renderMessages(context.getMessages());
    this.renderError(context);
  }
}
