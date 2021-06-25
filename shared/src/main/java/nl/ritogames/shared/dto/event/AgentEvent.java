package nl.ritogames.shared.dto.event;

import java.util.Objects;

public abstract class AgentEvent extends Event{
  private final String agentName;

  protected AgentEvent(String name) {
    this.agentName = name;
  }

  public String getAgentName() {
    return agentName;
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
    AgentEvent that = (AgentEvent) o;
    return Objects.equals(agentName, that.agentName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), agentName);
  }
}
