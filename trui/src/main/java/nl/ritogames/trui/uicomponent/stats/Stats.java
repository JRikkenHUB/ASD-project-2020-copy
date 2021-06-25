package nl.ritogames.trui.uicomponent.stats;

import com.indvd00m.ascii.render.api.ICanvas;
import com.indvd00m.ascii.render.elements.Rectangle;
import com.indvd00m.ascii.render.elements.Text;
import nl.ritogames.shared.dto.gameobject.individual.Individual;
import nl.ritogames.shared.exception.AbsentEntityException;
import nl.ritogames.trui.render.UIContext;
import nl.ritogames.trui.uicomponent.UIComponent;
import nl.ritogames.trui.uicomponent.Vector;

import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>Draws the player stats on the command line interface</p>
 */
public class Stats extends UIComponent {

  public Stats(Vector position, Vector size) {
    super(position, size);
  }


  @Override
  public ICanvas draw(UIContext uiContext) {
    super.draw(uiContext);
    Optional<Individual> self = Optional.empty();
    try {
      self = Optional.of(uiContext.getGameStateContext().getSelf());
    } catch (AbsentEntityException e) {
      Logger.getAnonymousLogger().log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
    }

    this.builder.element(new Rectangle());
    this.builder.element(new Text("STATS", getSize().getX() / 2 - 3, 1, getSize().getX(), 1));

    int textX = 2;//X location for the stat text
    int statX = 11;//X location for the stat value
    int lineNr = 3;//Y location of the current line

    this.builder.element(new Text("HEALTH", textX, lineNr, getSize().getX(), 1));
    this.builder
        .element(new Text(self.map(Individual::getHp).orElse(-1).toString(), statX, lineNr,
            getSize().getX(), 1));

    lineNr++;
    this.builder.element(new Text("ATTACK", textX, lineNr, getSize().getX(), 1));
    this.builder
        .element(new Text(self.map(Individual::getAttack).orElse(-1).toString(), statX, lineNr,
            getSize().getX(), 1));

    lineNr++;
    this.builder.element(new Text("DEFENCE", textX, lineNr, getSize().getX(), 1));
    this.builder
        .element(new Text(self.map(Individual::getDefense).orElse(-1).toString(), statX, lineNr,
            getSize().getX(), 1));

    return this.render.render(this.builder.build());
  }

}
