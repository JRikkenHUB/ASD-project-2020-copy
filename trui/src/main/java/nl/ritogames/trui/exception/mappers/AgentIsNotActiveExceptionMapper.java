package nl.ritogames.trui.exception.mappers;

import nl.ritogames.shared.exception.AgentIsNotActiveException;
import nl.ritogames.shared.exception.AgentNotFoundException;
import nl.ritogames.trui.exception.ExceptionMapper;
import nl.ritogames.trui.render.ScreenRenderer;

public class AgentIsNotActiveExceptionMapper implements ExceptionMapper<AgentIsNotActiveException> {

    private final ScreenRenderer renderer;

    public AgentIsNotActiveExceptionMapper(ScreenRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public void handle(AgentIsNotActiveException exception) {
        renderer.setError(
                String.format("You must select an agent first! Use select agent <agentname>."));
//        renderer.setError(exception.getMessage());
    }
}
