/**
 * XMLtoSQLParser - Takes in a XML and XSD and translates it into SQL statements
 * @author Sean Domingo, Michael Frederick, Megan Molumby, Mai Huong Nguyen, Richard Pratt
 *
 * c2 Parse XML
 */
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;

import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.*;
import java.util.ArrayList;

public class XMLToSQLParser {

    public void parse(ArrayList<Schema> table) {
        try {
            //get input file
            File inputFile = new File("xmlTestFile.txt");

            //Create a DocumentBuilder
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(inputFile);
            doc.getDocumentElement().normalize();


            //Create a Document from XML input file
            StringBuilder xmlStringBuilder = new StringBuilder();
            //UNKnOWN ON HOW THIS WILL IMPACT LATER ON BUT CURRENTLY GIVING ME ERROR 4/7/2017
            // xmlStringBuilder.append("<?xml version="1.0"?> <class> </class>"); //THis is will be pulled from XSd.
            ByteArrayInputStream input = new ByteArrayInputStream(xmlStringBuilder.toString().getBytes("UTF-8"));


            //Extract root element
            Element root = doc.getDocumentElement();
            System.out.println("Root element :"
                    + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName(table.get(0).getTableName());
            System.out.println("----------------------------");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                System.out.println("\nCurrent Element :"
                        + nNode.getNodeName());
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    System.out.println("Student roll no : "
                            + eElement
                            .getElementsByTagName(table.get(0).getName())
                            .item(0)
                            .getTextContent());
                    System.out.println("First Name : "
                            + eElement
                            .getElementsByTagName(table.get(1).getName())
                            .item(0)
                            .getTextContent());
                    System.out.println("Last Name : "
                            + eElement
                            .getElementsByTagName(table.get(2).getName())
                            .item(0)
                            .getTextContent());
                    System.out.println("Nick Name : "
                            + eElement
                            .getElementsByTagName(table.get(3).getName())
                            .item(0)
                            .getTextContent());
                    System.out.println("Marks : "
                            + eElement
                            .getElementsByTagName(table.get(4).getName())
                            .item(0)
                            .getTextContent());
                }



            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

    }
}
