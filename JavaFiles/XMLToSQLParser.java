/**
 * XMLtoSQLParser - Takes in a XML and XSD and translates it into SQL statements
 * @author Sean Domingo, Michael Frederick, Megan Molumby, Mai Huong Nguyen, Richard Pratt
 *
 * c2 Parse XML
 */
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;

import org.w3c.dom.NodeList;
import java.io.*;
public class XMLToSQLParser {
    public static void main(String args[]){
        try{
            //get input file
            File inputFile = new File("xmlTestFile.txt");
            //Create a DocumentBuilder
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(inputFile);
            doc.getDocumentElement().normalize();
            System.out.println("root element:" + doc.getDocumentElement().getNodeName());

            //Create a Document from XML input file
            /*StringBuilder xmlStringBuilder = new StringBulder();
            xmlStringBuilder.append("<?xml version ="1.0"?> <class> </class>");//THis is will be pulled from XSd.
            ByteArrayInputStream input = new ByteArrayInputStream(xmlStringBuilder.toString().getBytes("UTF-8"));
*/

            //Extract root element
           // Element root = document.getDocumentElement();

            //get Specific attributes
            //returns specific attribute
            //getAttributes("attributeName");
            //returns  a Map(table) of names/values
            // getAttributes();

        }catch(Exception e){
            e.printStackTrace();
        }


    }

}
