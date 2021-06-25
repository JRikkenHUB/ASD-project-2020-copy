package nl.ritogames.generator.xml;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class XMLParserTest {


    XMLParser sut;

    @BeforeEach
    void setUp() {
        sut = new XMLParser();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testCorrectXMLGetsParsedCorrectLength() throws ParserConfigurationException, SAXException, IOException {
        String xml = "<?xml version=\"1.0\"?>" +
                "<test>" +
                "<tag name=\"test\">" +
                "</tag>" +
                "<tag name=\"test\">" +
                "</tag>" +
                "</test>";
        NodeList nodeList = sut.parseXML(xml, "tag");

        assertEquals(2,nodeList.getLength());
    }

    @Test
    void testCorrectXMLGetsParsedCorrectNames() throws ParserConfigurationException, SAXException, IOException {
        String xml = "<?xml version=\"1.0\"?>" +
                "<test>" +
                "<tag name=\"test1\">" +
                "</tag>" +
                "<tag name=\"test2\">" +
                "</tag>" +
                "</test>";
        NodeList nodeList = sut.parseXML(xml, "tag");

        Element eElement1 = (Element) nodeList.item(0);
        Element eElement2 = (Element) nodeList.item(1);

        assertEquals("test1",eElement1.getAttribute("name"));
        assertEquals("test2",eElement2.getAttribute("name"));
    }

    @Test
    void testIncorrectXMLThrows() {
        String xml = "";

        assertThrows(SAXParseException.class, () -> sut.parseXML(xml, "tag"));
    }
}
