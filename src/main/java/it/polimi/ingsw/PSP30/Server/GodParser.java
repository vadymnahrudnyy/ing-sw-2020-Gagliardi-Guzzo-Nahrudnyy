package it.polimi.ingsw.PSP30.Server;

import it.polimi.ingsw.PSP30.Model.God;
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
     * Builds a new DOM Document object using the configuration file parsed.
     * @return the DOM document created
     */
    public Document buildGodDocument() {

        try{

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder gBuilder = factory.newDocumentBuilder();
            //return gBuilder.parse("./src/main/resources/configurationfilegod.xml");
            return gBuilder.parse(String.valueOf(this.getClass().getClassLoader().getResource("configurationfilegod.xml")));

        } catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Parses the document and creates an ArrayList of gods.
     * @param document is the DOM Document that will be parsed
     * @return an ArrayList of God objects created parsing the XML file
     */
    public static ArrayList<God> parseGod(Document document) {

        document.getDocumentElement().normalize();
        NodeList gList = document.getElementsByTagName("God");
        ArrayList<God> godList = new ArrayList<>();

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
     * Initializes the DOM document and return the ArrayList obtained after the parsing.
     * @return an ArrayList of Gods
     */
    public ArrayList<God> readGods() {
        Document document = buildGodDocument();
        return GodParser.parseGod(document);
    }

}
