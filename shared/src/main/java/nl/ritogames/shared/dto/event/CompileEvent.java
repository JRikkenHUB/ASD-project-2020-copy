package nl.ritogames.shared.dto.event;

import java.util.Objects;

public class CompileEvent extends Event {
    private String agentName;

    public CompileEvent() {
        super();
    }

    
    public CompileEvent(String individualId, String agentName) {
        super(individualId);
        this.agentName = agentName;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompileEvent that = (CompileEvent) o;
        return Objects.equals(agentName, that.agentName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(agentName);
    }

    @Override
    public String toString() {
        return "CompileEvent{" +
                "AgentName='" + agentName + '\'' +
                '}';
    }
}
