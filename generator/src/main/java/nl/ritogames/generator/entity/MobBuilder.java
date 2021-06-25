package nl.ritogames.generator.entity;

import nl.ritogames.shared.dto.gameobject.individual.Monster;
import nl.ritogames.shared.dto.gameobject.world.tile.AccessibleWorldTile;
import nl.ritogames.shared.exception.AbsentEntityException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.text.MessageFormat;
import java.util.UUID;

public class MobBuilder extends EntityBuilder {

    public MobBuilder() {
        super("mob");
    }


    /**
     * Spawns a mob on the given tile.
     *
     * @param tile                      The tile on which to spawn the mob.
     * @param totalRarity               The rarity of the mob that has to be spawned.
     * @throws AbsentEntityException    Thrown when the mob doesn't exist.
     */
    @Override
    protected void spawnEntity(AccessibleWorldTile tile, double totalRarity) throws AbsentEntityException {
        tile.addIndividual(generateMob(tile, totalRarity));
    }

    /**
     * Generates a mob of the given rarity on the tile.
     *
     * @param tile          The tile on which to generate the mob.
     * @param totalRarity   The rarity of the attribute.
     * @return              The mob that has been generated.
     */
    private Monster generateMob(AccessibleWorldTile tile, double totalRarity) {
        Monster mob = null;
        double rarity = totalRarity * random.nextDouble();
        double currentRarity = 0;
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) node;
                currentRarity += Double.parseDouble(eElement.getElementsByTagName("spawnrate").item(0).getTextContent());
                if (currentRarity >= rarity) {
                    String name = eElement.getAttribute("name");
                    int maxHp = Integer.parseInt(eElement.getElementsByTagName("max_health").item(0).getTextContent());
                    int def = Integer.parseInt(eElement.getElementsByTagName("defense").item(0).getTextContent());
                    int dmg = Integer.parseInt(eElement.getElementsByTagName("damage").item(0).getTextContent());
                    char texture = eElement.getElementsByTagName("texture").item(0).getTextContent().charAt(0);
                    mob = new Monster(tile.getCoordinates().getX(), tile.getCoordinates().getY(), maxHp, dmg, def, name, texture);
                    mob.setIndividualID(MessageFormat.format("asd_dungeon_game_{0}_{1}", mob.getName(), i));
                    break;
                }
            }
        }
        return mob;
    }
}
