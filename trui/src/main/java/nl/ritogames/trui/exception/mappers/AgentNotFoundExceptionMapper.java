package nl.ritogames.trui.exception.mappers;

import nl.ritogames.shared.exception.AgentNotFoundException;
import nl.ritogames.trui.exception.ExceptionMapper;
import nl.ritogames.trui.render.ScreenRenderer;

public class AgentNotFoundExceptionMapper implements ExceptionMapper<AgentNotFoundException> {
    private final ScreenRenderer renderer;


    public AgentNotFoundExceptionMapper(ScreenRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public void handle(AgentNotFoundException exception) {
        renderer.setError(exception.getMessage());
    }
}
