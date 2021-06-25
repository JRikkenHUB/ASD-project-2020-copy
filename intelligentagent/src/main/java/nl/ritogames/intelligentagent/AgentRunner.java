package nl.ritogames.intelligentagent;

/**
 * A helper class for making agents respond to the game state on a certain interval. This can be
 * run. in a separate thread to make sure agents don't interrupt each other.
 */
public class AgentRunner implements Runnable {

  private final Agent agent;

  /**
   * Constructor.
   *
   * @param agent the agent.
   */
  public AgentRunner(Agent agent) {
    this.agent = agent;
  }

  /** Stars a thread to update an agent on an interval. */
  @Override
  public void run() {
      agent.update();
  }

  public Agent getAgent() {
        return agent;
    }
}
