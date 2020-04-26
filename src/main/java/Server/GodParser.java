package Server;

import Model.God;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;

/**
 * GodParser class is used for read from the configuration file for gods and create an ArrayList of God
 */

public class GodParser {

    /**
     * This method builds a new DOM Document object using the configuration file parsed
     * @return the DOM document created
     */
    public static Document buildGodDocument() {

        try{

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder gBuilder = factory.newDocumentBuilder();
            Document gDocument = gBuilder.parse("./Resources/configurationfilegod.xml");
            return gDocument;

        } catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * This method
     * @param document is the DOM Document that will be parsed
     * @return an ArrayList of God objects created parsing the XML file
     */
    public static ArrayList<God> parseGod(Document document) {

        document.getDocumentElement().normalize();
        NodeList gList = document.getElementsByTagName("God");
        ArrayList<God> godList = new ArrayList<God>();

        for (int i = 0; i < gList.getLength(); i++) {

            Node godNode = gList.item(i);

            if (godNode.getNodeType() == Node.ELEMENT_NODE) {

                Element god = (Element) godNode;

                Node node1 = god.getElementsByTagName("name").item(0);
                String name = node1.getTextContent();


                Node node2 = god.getElementsByTagName("numPowers").item(0);
                String num = node2.getTextContent();
                int numPowers = Integer.parseInt(num);

                Node node3 = god.getElementsByTagName("playersAllowed").item(0);
                String allowed = node3.getTextContent();
                int playersAllowed = Integer.parseInt(allowed);

                Node node4 = god.getElementsByTagName("description").item(0);
                String description = node4.getTextContent();

                Node node5 = god.getElementsByTagName("powers").item(0);
                String allPower = node5.getTextContent();
                int p = Integer.parseInt(allPower.trim());
                int[] powers = new int[1];
                powers[0] = p;

                //add currentGod to GodList
                God currentGod = new God(name,numPowers,playersAllowed, description, powers);
                godList.add(currentGod);
            }

        }
        return godList;
    }


    /**
     * This method initialize the DOM document and return the ArrayList obtained after the parsing
     * @return an ArrayList of Gods
     */
    public ArrayList<God> readGod() {
        Document document = GodParser.buildGodDocument();
        ArrayList<God> gods = GodParser.parseGod(document);
        return gods;
    }

}
