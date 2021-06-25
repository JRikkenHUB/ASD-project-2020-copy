package nl.ritogames.generator.xml;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;

public class XMLParser {
    /**
     * parses a xml string to an NodeList.
     *
     * @param rawXML xml string.
     * @param tag    the attributes to be placed in the NodeList
     * @return a NodeList with attributes of a certain tag.
     * @throws IOException                  Thrown when fileRepo encounters an error.
     * @throws SAXException                 Thrown when the xml syntax is incorrect
     * @throws ParserConfigurationException Thrown when the parser is incorrectly configured.
     */
    public NodeList parseXML(String rawXML, String tag) throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
        df.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        df.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

        DocumentBuilder builder = df.newDocumentBuilder();

        StringReader sr = new StringReader(rawXML);
        InputSource is = new InputSource(sr);
        Document doc = builder.parse(is);
        doc.getDocumentElement().normalize();

        return doc.getElementsByTagName(tag);
    }
}
