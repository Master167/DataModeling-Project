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
            //CREATE file to write SQL commands to
            //when interface is created will insert choice filename variable here
            PrintWriter writer = new PrintWriter("filename.txt", "UTF-8");

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

            NodeList nList = doc.getElementsByTagName(table.get(0).getTableName());

            //For the number of records
            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node record = nList.item(temp);
                //RECORD TYPE NAME

                if (record.getNodeType() == Node.ELEMENT_NODE) {
                    //create an element
                    Element eElement = (Element) record;
                    String insertCommand = "INSERT INTO " + record.getNodeName() + " (";
                    //add all fields to insert command
                    for (int i = 0; i < table.size(); i++) {
                        insertCommand = insertCommand.concat(table.get(i).getName());
                        //if last item in field list
                        if(i+1 == table.size()){
                           insertCommand = insertCommand.concat(")");
                        }else{//add comma if followed by another field
                            insertCommand=insertCommand.concat(", ");
                        }
                    }
                    insertCommand=insertCommand.concat(" VALUES (");
                    //add in literals to insert command
                    for(int j = 0; j<table.size(); j++){
                        //NEED TO ADD CHECKS!
                        insertCommand=insertCommand.concat(eElement
                                .getElementsByTagName(table.get(j)
                                        .getName()).item(0).getTextContent());
                        if(j+1 == table.size()){
                            insertCommand=insertCommand.concat(");");
                        }else{
                            insertCommand=insertCommand.concat(", ");
                        }
                    }

                    writer.println(insertCommand);

                }



            }
            writer.close();
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
