package nl.ritogames.generator.grid;

import com.raylabz.opensimplex.OpenSimplexNoise;
import nl.ritogames.generator.world.WorldBench;
import nl.ritogames.generator.xml.XMLParser;
import nl.ritogames.generator.xml.XMLRetriever;
import nl.ritogames.shared.dto.gameobject.trap.Trap;
import nl.ritogames.shared.dto.gameobject.world.tile.AccessibleWorldTile;
import nl.ritogames.shared.dto.gameobject.world.tile.TrapTile;
import nl.ritogames.shared.dto.gameobject.world.tile.WorldTile;
import nl.ritogames.shared.exception.XMLLoadException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The TrapBuilder retrieves all the traptypes from the XML file and generates traps using a noise map
 */
public class TrapBuilder {
    public static final double TRAP_SPARSITY = 0.55;
    public static final String XML_PATH = "traps.xml";

    private XMLRetriever xmlRetriever = new XMLRetriever();
    private List<Trap> trapTypes;
    private double totalSpawnRate;
    private Random random;
    private WorldBench bench;

    /**
     * addTraps generates traps based on noise and adds them to the WorldBench
     *
     * @param bench            The worldbench that contains the world
     * @param seed             The seed that is used to generate the noise
     * @param noiseFeatureSize The scale of the noise map
     * @return the WorldBench containing the traps
     */
    public WorldBench addTraps(WorldBench bench, long seed, int noiseFeatureSize) {
        trapTypes = getTrapTypes();
        this.random = new Random(seed);
        this.bench = bench;
        OpenSimplexNoise noise = getNoise(seed, noiseFeatureSize);
        placeTrapsWhenSuited(noise);
        return bench;
    }

    /**
     * Gets a list with all trap types.
     *
     * @return list with all trap types.
     */
    private List<Trap> getTrapTypes() {
        trapTypes = new ArrayList<>();

        XMLParser xmlParser = new XMLParser();

        String tag = "trap";
        NodeList nlist;
        totalSpawnRate = 0;

        try {
            final String xml = xmlRetriever.getXMLString(XML_PATH);
            nlist = xmlParser.parseXML(xml, tag);

            for (int i = 0; i < nlist.getLength(); i++) {
                Node node = nlist.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                    trapTypes.add(new Trap(eElement.getAttribute("name"), Boolean.parseBoolean(eElement.getElementsByTagName("visible").item(0).getTextContent()),
                            Integer.parseInt(eElement.getElementsByTagName("damage").item(0).getTextContent()), Integer.parseInt(eElement.getElementsByTagName("spawnrate").item(0).getTextContent()), (eElement.getElementsByTagName("texture").item(0).getTextContent()).charAt(0)));
                    totalSpawnRate += Double.parseDouble(eElement.getElementsByTagName("spawnrate").item(0).getTextContent());
                }
            }

        } catch (IOException | SAXException | ParserConfigurationException | NullPointerException e) {
            throw new XMLLoadException("Cannot load tag: " + tag + " into node list");
        }

        return trapTypes;
    }

    /**
     * Generates noise based on a seed and the feature size of the noise.
     *
     * @param seed              The seed to use when generating the noise.
     * @param noiseFeatureSize  The feature size of the noise.
     */
    private OpenSimplexNoise getNoise(long seed, int noiseFeatureSize) {
        OpenSimplexNoise noise = new OpenSimplexNoise(seed);
        noise.setFeatureSize(noiseFeatureSize);
        return noise;
    }

    /**
     * Places traps on the world based on the noise.
     *
     * @param noise The noise to base the placements of the traps off
     */
    private void placeTrapsWhenSuited(OpenSimplexNoise noise) {
        for (WorldTile[] tile : bench.getTiles()) {
            for (WorldTile worldTile : tile) {
                placeTrapWhenSuited(noise, worldTile);
            }
        }
    }

    /**
     * Places a trop on a world tile based on the noise.
     *
     * @param noise The noise to base the placements of the traps off.
     * @param tile  The tile to try to place the trap on.
     */
    private void placeTrapWhenSuited(OpenSimplexNoise noise, WorldTile tile) {
        double noiseValue = noise.getNoise2D(tile.getCoordinates().getX(), tile.getCoordinates().getY()).getValue();
        if (noiseValue > TrapBuilder.TRAP_SPARSITY) {
            placeTrap(tile.getCoordinates().getX(), tile.getCoordinates().getY());
        }
    }

    /**
     * Places a trop on a world tile with given coordinates.
     *
     * @param x The x coordinate of the tile.
     * @param y The y coordinate of the tile.
     */
    private void placeTrap(int x, int y) {
        if (bench.getTile(x, y) instanceof AccessibleWorldTile) {
            bench.setTile(new TrapTile(x, y, generateTrap()));
        }
    }

    /**
     * Generate a trap based on the spawnrate.
     *
     * @return the generated trap.
     */
    private Trap generateTrap() {
        Trap trap = new Trap("default", true, 1, 1, 'd');
        double currentSpawnRate = 0.0;
        double spawnRate = totalSpawnRate * random.nextDouble();

        for (Trap trapType : trapTypes) {
            currentSpawnRate += trapType.getSpawnRate();
            if (currentSpawnRate >= spawnRate) {
                trap = new Trap(trapType.getName(), trapType.isVisible(), trapType.getDamage(), trapType.getSpawnRate(), trapType.getTexture());
                break;
            }
        }
        return trap;
    }

    public void setXmlRetriever(XMLRetriever xmlRetriever) {
        this.xmlRetriever = xmlRetriever;
    }
}
