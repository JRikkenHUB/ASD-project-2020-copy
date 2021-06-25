package nl.ritogames.agentcompiler.ast;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nl.ritogames.agentcompiler.ast.expression.Expression;

/**
 * The type If statement.
 */
@Getter
@Setter
@AllArgsConstructor
public class IfStatement extends BodyElement {

    Expression condition;
    Body body;
    ElseClause elseClause;

    @Override
    public ASTNode addChild(ASTNode child) {
        body.addChild(child);
        return this;
    }

    @Override
    public List<ASTNode> getChildren() {
        return body.getChildren();
    }

    @Override
    public String toString() {
        return "IfStatement (" + condition + "): " + body.toString() + " " + elseClause.toString();
    }

    /**
     * Checks for the ElseClause
     *
     * @return whether this IfStatement has an ElseClause
     */
    public boolean hasElseClause() {
        return this.elseClause != null;
    }
}
