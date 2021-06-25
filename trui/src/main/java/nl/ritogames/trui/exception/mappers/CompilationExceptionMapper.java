package nl.ritogames.trui.exception.mappers;

import nl.ritogames.shared.exception.CompilationException;
import nl.ritogames.trui.exception.ExceptionMapper;
import nl.ritogames.trui.render.ScreenRenderer;

public class CompilationExceptionMapper implements ExceptionMapper<CompilationException> {
    private final ScreenRenderer renderer;

    public CompilationExceptionMapper(ScreenRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public void handle(CompilationException exception) {
        renderer.setError(exception.getMessage());
    }
}
