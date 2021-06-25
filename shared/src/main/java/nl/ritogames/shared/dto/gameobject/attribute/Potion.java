package nl.ritogames.shared.dto.gameobject.attribute;

import nl.ritogames.shared.dto.gameobject.individual.Character;
import nl.ritogames.shared.exception.UnknownCharacterFieldException;

import java.beans.Expression;
import java.beans.Statement;

public class Potion extends Attribute {

    private String field;
    private int effect;


    /**
     * The Potion attribute is used to apply special effects to characters
     * @param name
     * @param field
     * @param effect
     */
    public Potion(String name, String field, int effect) {
        super(name);
        this.field = field;
        this.effect = effect;
    }

    public Potion(){
        super("default");
    }

    @Override
    public void apply(Character character) {
        try {
            String methodNameGet = "get"+ field.substring(0, 1).toUpperCase() + field.substring(1);
            Expression expression = new Expression(character, methodNameGet, new Object[] {});
            expression.execute();
            String methodNameSet = "set"+ field.substring(0, 1).toUpperCase() + field.substring(1);
            Statement statement = new Statement(character, methodNameSet, new Object[] {effect + (int) expression.getValue()});
            statement.execute();
        } catch (Exception e) {
            throw new UnknownCharacterFieldException("Potion effect applied on invalid or non-existing field", e);
        }
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public int getEffect() {
        return effect;
    }

    public void setEffect(int effect) {
        this.effect = effect;
    }
}
