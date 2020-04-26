package Server;

import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import Model.Power;
import Model.TurnPhase;
import  org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

/**
 * PowerParser class is used for read from the configuration file for powers and create an ArrayList of Power
 */

public class PowerParser {

    /**
     * This method builds a new DOM Document object using the configuration file parsed
     * @return the DOM document created
     */
    public static Document buildPowerDocument() {

        try{

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder pBuilder = factory.newDocumentBuilder();
            Document pDocument = pBuilder.parse("./Resources/configurationfilepower.xml");
            return pDocument;

        } catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * This method
     * @param document is the DOM Document that will be parsed
     * @return an ArrayList of Power objects created parsing the XML file
     */
    public static ArrayList<Power> parsePower(Document document) {

        document.getDocumentElement().normalize();
        NodeList pList = document.getElementsByTagName("Power");
        ArrayList<Power> powerList = new ArrayList<Power>();

        for (int i = 0; i < pList.getLength(); i++) {

            Node powerNode = pList.item(i);

            if (powerNode.getNodeType() == Node.ELEMENT_NODE) {

                Element power = (Element) powerNode;

                Node node1 = power.getElementsByTagName("powerID").item(0);
                String ID = node1.getTextContent();
                int powerID = Integer.parseInt(ID);

                Node node2 = power.getElementsByTagName("isActive").item(0);
                String active = node2.getTextContent();
                boolean isActive = Boolean.parseBoolean(active);

                Node node3 = power.getElementsByTagName("usableOnPlayerTurn").item(0);
                String usable = node3.getTextContent();
                boolean usableOnPlayerTurn = Boolean.parseBoolean(usable);

                Node node4 = power.getElementsByTagName("validOnOpponentTurn").item(0);
                String valid = node4.getTextContent();
                boolean validOnOpponentTurn = Boolean.parseBoolean(valid);

                Node node5 = power.getElementsByTagName("turnPhase").item(0);
                String phase = node5.getTextContent();
                TurnPhase turnPhase = Enum.valueOf(TurnPhase.class, phase);


                //add currentPower to PowerList
                Power currentPower = new Power(powerID, isActive, usableOnPlayerTurn, validOnOpponentTurn, turnPhase);
                powerList.add(currentPower);
            }
        }
        return powerList;
    }


    /**
     * This method initialize the DOM document and return the ArrayList obtained after the parsing
     * @return an ArrayList of Powers
     */
    public ArrayList<Power> readPower(){
        Document pDocument = PowerParser.buildPowerDocument();
        ArrayList<Power> powers = PowerParser.parsePower(pDocument);
        return powers;
    }

}

