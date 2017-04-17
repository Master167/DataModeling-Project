/**
 * XMLtoSQLParser - Takes in a XML and XSD and translates it into SQL statements
 * @author Sean Domingo, Michael Frederick, Megan Molumby, Mai Huong Nguyen, Richard Pratt
 *
 * c2 Parse XML
 */
 
 /*CATCHES
/* 
catch (ParserConfigurationException e) {
            System.out.print("ERROR:Parser Configuration error");
            //e.printStackTrace();
            return;
        } catch (UnsupportedEncodingException e) {
            System.out.print("ERROR:Unsupported Encoding");
            //e.printStackTrace();
            return;
        } catch (IOException e) {
             System.out.print("ERROR:IO error");
            //e.printStackTrace();
            return;
        } catch (SAXException e) {
            return;
            //e.printStackTrace();
    }
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

    public void parse(ArrayList<Schema> table, String xmlFilename, String outputFilename) throws IOException, UnsupportedEncodingException, ParserConfigurationException, SAXException {
        boolean error = false;
        //try {
            //CREATE file to write SQL commands to
            if(!outputFilename.contains(".txt")){
                System.out.print("Error: please specify the output file extension type(ex: output.txt)");
            }else {
                File sqlCommandFile = new File(outputFilename);
                //when interface is created will insert choice filename variable here
                PrintWriter writer = new PrintWriter(sqlCommandFile, "UTF-8");

                //get input file
                File inputFile = new File(xmlFilename);

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
                            if (i + 1 == table.size()) {
                                insertCommand = insertCommand.concat(")");
                            } else {//add comma if followed by another field
                                insertCommand = insertCommand.concat(", ");
                            }
                        }
                        insertCommand = insertCommand.concat(" VALUES (");
                        //add in literals to insert command
                        for (int j = 0; j < table.size(); j++) {
                            boolean qualified = false;
                            String value = eElement.getElementsByTagName(table.get(j)
                                    .getName()).item(0).getTextContent();
                            String attributeName = table.get(j).getDataType();
                            String regex;
                            //CHECKS WITH XSD TO VERIFY DATA TYPE AND LENGTH
                            switch (attributeName) {
                                //check if string length min and max
                                case "string":
                                    if (value.length() <= table.get(j).getLength() && value.length() >= table.get(j).getMin()) {
                                        qualified = true;
                                    }
                                    break;
                                //check if decimal fraction
                                case "decimal":
                                    regex = "^\\d{0," + table.get(j).getLength() + "}(\\.\\d{" + table.get(j).getFraction() + "})?$";
                                    if (value.matches(regex)) {//checks for decimal length/fraction and if not null
                                        qualified = true;
                                    }
                                    break;
                                //check if integer length is correct
                                case "integer":
                                    regex = "^\\d{0," + table.get(j).getLength() + "}$";
                                    if (value.matches(regex)) {
                                        qualified = true;
                                    }
                                    break;
                                //CHeck date format//ASSUMING DATE FORMAT IS (mm/dd/[yy]yy)
                                case "date":
                                    regex = "^(0[0-9]||1[0-2])/([0-2][0-9]||3[0-1])/([0-9][0-9])?[0-9][0-9]$";
                                    if (value.matches(regex)) {
                                        qualified = true;
                                    }
                                    break;
                                default:
                                    System.out.println("Error: data type is not a subset of required data types");
                                    error = true;
                            }
                            if (qualified) {
                                insertCommand = insertCommand.concat(value);
                                if (j + 1 == table.size()) {
                                    insertCommand = insertCommand.concat(");");
                                } else {
                                    insertCommand = insertCommand.concat(", ");
                                }
                            } else {
                                System.out.println("ERROR: XML Document does not match specifications in XSD document");
                                error = true;
                            }
                        }

                        writer.println(insertCommand);
                    }


                }
                //if error remove file from directory? Option
                if (error) {
                     return;
                }
                writer.close();
                System.out.println("File has been converted to SQL Insert Command File");
            }
       /* } catch (ParserConfigurationException e) {
            System.out.print("ERROR:Parser Configuration error");
            //e.printStackTrace();
            return;
        } catch (UnsupportedEncodingException e) {
            System.out.print("ERROR:Unsupported Encoding");
            //e.printStackTrace();
            return;
        } catch (IOException e) {
             System.out.print("ERROR:IO error");
            //e.printStackTrace();
            return;
        } catch (SAXException e) {
            return;
            //e.printStackTrace();
        }*/

    }

}
