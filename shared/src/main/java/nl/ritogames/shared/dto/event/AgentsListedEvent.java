package nl.ritogames.shared.dto.event;

import java.util.List;
import java.util.Objects;

public class AgentsListedEvent extends MenuEvent {

  private final List<String> agents;

  public AgentsListedEvent(List<String> agents) {
    this.agents = agents;
  }

  public List<String> getAgents() {
    return agents;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    AgentsListedEvent that = (AgentsListedEvent) o;
    return Objects.equals(agents, that.agents);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), agents);
  }
}
