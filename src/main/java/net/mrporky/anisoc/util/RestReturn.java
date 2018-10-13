package net.mrporky.anisoc.util;

import net.mrporky.anisoc.members.Member;
import net.mrporky.anisoc.members.Members;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;

/*
    TODO: Re-generalise this class to allow for all possible RestAPIs to be supported
    This class takes an XMLFile input (was generalised but now only for SU-API) and returns a members object for this value
 */
public class RestReturn {

    public JSONArray getRest(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONArray json = new JSONArray(jsonText);


            return json;
        } finally {
            is.close();
        }
    }

    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public Members getXML(String url) {
        Members members = new Members();

        try {

            // Create a new XML Document Builder and a factory to build this
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder;

            dBuilder = dbFactory.newDocumentBuilder();

            // Get the document from the specified URL
            Document doc = dBuilder.parse(url);
            doc.getDocumentElement().normalize();

            XPath xPath = XPathFactory.newInstance().newXPath();

            // XML path to where we wish to scan through
            String expression = "/MembershipAPI/Member";

            // Generate nodes from the XML document
            NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(
                    doc, XPathConstants.NODESET);

            // Loop through all of the nodes and get out the values, adding them to the member object
            // TODO: Simplify this expression
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node nNode = nodeList.item(i);
                String firstName = "", lastName = "", uniqueID = "", email = "";
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    firstName = eElement
                            .getElementsByTagName("FirstName")
                            .item(0)
                            .getTextContent();
                    lastName = eElement
                            .getElementsByTagName("LastName")
                            .item(0)
                            .getTextContent();
                    uniqueID = eElement
                            .getElementsByTagName("UniqueID")
                            .item(0)
                            .getTextContent();
                    email = eElement
                            .getElementsByTagName("EmailAddress")
                            .item(0)
                            .getTextContent();
                }
                Member memeber = new Member(firstName, lastName, uniqueID, email);

                // Add this member to the members class
                members.add(memeber);
            }
        } catch (ParserConfigurationException | IOException | XPathExpressionException | SAXException e) {
            e.printStackTrace();
        }
        return members;
    }
}
