package nl.ritogames.generator.entity;

import nl.ritogames.shared.dto.gameobject.attribute.Armour;
import nl.ritogames.shared.dto.gameobject.attribute.Attribute;
import nl.ritogames.shared.dto.gameobject.attribute.Potion;
import nl.ritogames.shared.dto.gameobject.attribute.Weapon;
import nl.ritogames.shared.dto.gameobject.world.tile.AccessibleWorldTile;
import nl.ritogames.shared.exception.AbsentEntityException;
import nl.ritogames.shared.exception.UnknownAttributeException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class AttributeBuilder extends EntityBuilder {

    public AttributeBuilder() {
        super("attribute");
    }

    /**
     * Spawns an attribute on the given tile.
     *
     * @param tile                      The tile on which to spawn the attribute.
     * @param totalRarity               The rarity of the attribute that has to be spawned.
     * @throws AbsentEntityException    Thrown when the attribute doesn't exist.
     */
    @Override
    protected void spawnEntity(AccessibleWorldTile tile, double totalRarity) throws AbsentEntityException {
            tile.addAttribute(generate(totalRarity));
    }

    /**
     * Generates an attribute of the given rarity.
     *
     * @param totalRarity   The rarity of the attribute.
     */
    private Attribute generate(double totalRarity) {
        Attribute attribute = null;
        double rarity = totalRarity * random.nextDouble();
        double currentRarity = 0;
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) node;
                currentRarity += Double.parseDouble(eElement.getElementsByTagName("spawnrate").item(0).getTextContent());
                if (currentRarity >= rarity) {
                    String name = eElement.getAttribute("name");
                    attribute = switch (eElement.getElementsByTagName("type").item(0).getTextContent()) {
                        case "armour" -> new Armour(name, Integer.parseInt(eElement.getElementsByTagName("defense").item(0).getTextContent()));
                        case "potion" -> new Potion(name, eElement.getElementsByTagName("characterField").item(0).getTextContent(), Integer.parseInt(eElement.getElementsByTagName("effect").item(0).getTextContent()));
                        case "weapon" -> new Weapon(name, Integer.parseInt(eElement.getElementsByTagName("damage").item(0).getTextContent()));
                        default -> throw new UnknownAttributeException("Unexpected type: " + eElement.getElementsByTagName("type").item(0).getTextContent());
                    };
                    break;
                }
            }
        }
        return attribute;
    }
}
