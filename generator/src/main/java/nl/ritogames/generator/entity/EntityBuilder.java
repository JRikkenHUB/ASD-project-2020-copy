package nl.ritogames.generator.entity;

import com.raylabz.opensimplex.OpenSimplexNoise;
import java.io.IOException;
import java.util.Random;
import javax.xml.parsers.ParserConfigurationException;
import nl.ritogames.generator.world.WorldBench;
import nl.ritogames.generator.xml.XMLParser;
import nl.ritogames.generator.xml.XMLRetriever;
import nl.ritogames.shared.dto.gameobject.world.tile.AccessibleWorldTile;
import nl.ritogames.shared.dto.gameobject.world.tile.TrapTile;
import nl.ritogames.shared.dto.gameobject.world.tile.WorldTile;
import nl.ritogames.shared.exception.AbsentEntityException;
import nl.ritogames.shared.exception.XMLLoadException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public abstract class EntityBuilder {
    public static final String XML_PATH = "entities.xml";
    private XMLParser xmlParser = new XMLParser();
    private XMLRetriever xmlRetriever = new XMLRetriever();
    protected Random random;
    protected OpenSimplexNoise noise;
    protected NodeList nodeList;
    private final String tag;

    protected EntityBuilder(String tag) {
        this.tag = tag;
    }

    /**
     * Calculates the total rarity of all the entities in a NodeList.
     *
     * @param nlist The list of entities.
     * @return      The total rarity
     */
    private double calcTotalRarity(NodeList nlist) {
        double total = 0;
        for (int i = 0; i < nlist.getLength(); i++) {
            Node node = nlist.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) node;
                total += Double.parseDouble(eElement.getElementsByTagName("spawnrate").item(0).getTextContent());
            }
        }
        return total;
    }

    /**
     * Adds entities of a type to accessible tiles based on a noise map and spawn rate.
     *
     * @param bench         The bench contains the world as it is being generated.
     * @param seed          A unique long that is used to generate a persistent world.
     * @param spawnFactor   The amount of entities of a type to spawn.
     * @return              A bench with new entities placed on tiles.
     */
    public WorldBench build(WorldBench bench, long seed, double spawnFactor) throws AbsentEntityException {
        nodeList = getNodesFromFile(tag);
        double totalRarity = calcTotalRarity(nodeList);
        random = new Random(seed);
        noise = new OpenSimplexNoise(seed);
        noise.setFeatureSize(1);
        for (WorldTile[] tiles : bench.getTiles()) {
            for (WorldTile tile : tiles) {
                if (noise.getNoise2D(tile.getCoordinates().getX(), tile.getCoordinates().getY())
                    .getValue() >= spawnFactor && tileIsSuited(tile)) {
                    spawnEntity((AccessibleWorldTile) tile, totalRarity);
                }
            }
        }
        return bench;
    }

    /**
     * Spawns an entity on the given tile.
     *
     * @param tile                      The tile on which to spawn the entity.
     * @param totalRarity               The rarity of the attribute that has to be spawned.
     * @throws AbsentEntityException    Thrown when the attribute doesn't exist.
     */
    protected abstract void spawnEntity(AccessibleWorldTile tile, double totalRarity) throws AbsentEntityException;

    /**
     * Check if the given tile is an instance of an AccessibleWorldTile, but not a Traptile.
     *
     * @param tile  The tile to check.
     * @return      If the tile is an instance of an AccessibleWorldTile, but not a Traptile.
     */
    protected boolean tileIsSuited(WorldTile tile) {
        return (tile instanceof AccessibleWorldTile) && !(tile instanceof TrapTile) && (tile
            .tileAccessible());
    }

    /**
     * Reads the nodes from a file with a given tag in the given path.
     *
     * @param tag           The tag of which to get the elements from the file
     * @return NodeList     The list of nodes in the file.
     */
    private NodeList getNodesFromFile(String tag) {
        try {
            return xmlParser.parseXML(xmlRetriever.getXMLString(EntityBuilder.XML_PATH), tag);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new XMLLoadException("Cannot load tag: " + tag + " into node list");
        }
    }

    public OpenSimplexNoise getNoise() {
        return noise;
    }

    public void setNoise(OpenSimplexNoise noise) {
        this.noise = noise;
    }

    public void setXmlRetriever(XMLRetriever xmlRetriever) {
        this.xmlRetriever = xmlRetriever;
    }
}
