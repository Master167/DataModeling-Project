import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Element;
/*catches catch (ParserConfigurationException e)
        { 
         System.out.print("ERROR:Parser Configuration error");
            //e.printStackTrace();
            return;       
            
        }

        catch (org.xml.sax.SAXException e)
        { //System.out.print("ERROR:Parser Configuration error");
            //e.printStackTrace();
            return;        }
        catch (IOException ed )
        {
             System.out.print("ERROR:IO Error");
            //e.printStackTrace();
            return;        }
        //return table;
    }
    @Michael CHECK XMLTOSQLPARSER for its catches!! 
    */

/**
 * Created by Megan on 4/3/2017.
 */
public class XSDParser {
       public void parseXSD(String xmlFilename, String xsdFilename, String outputFilename, XMLToSQLParser xmlToSQLParser) throws ParserConfigurationException, org.xml.sax.SAXException, IOException {
    
        //File inputFile = new File(xsdFilename);
        ArrayList<Schema> table = new ArrayList<>(1);
       // try{
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder;
            docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse (new File(xsdFilename));
            NodeList list = doc.getElementsByTagName("xsd:element");

            //RETRIEVE TABLE NAME FROM DOC
            NodeList tableNames = doc.getElementsByTagName("xsd:complexType");
            Element temp = (Element) tableNames.item(0);
            String tableName = temp.getAttribute("name");

            //loop to print data
            for(int i = 0 ; i < list.getLength(); i++)
            {
                Schema attribute = new Schema();
                attribute.setTableName(tableName);
                Element first = (Element)list.item(i) ;
                if( first.hasAttributes())
                {
                    //set Table attributes
                    attribute.setName(first.getAttribute("name"));
                    attribute.setDataType(first.getAttribute("type"));
                    //may need to use .getNextSibling if not specific
                    attribute.setLength(first.getAttribute("maxOccurs"));
                    attribute.setMin(first.getAttribute("minOccurs"));
                    if(first.hasAttribute("fraction")) {
                        attribute.setFraction(first.getAttribute("fraction"));
                    }
                    if(first.hasAttribute("date")){
                        attribute.setDate(first.getAttribute("date"));
                    }
                    table.add(attribute);
                }

               }
               
               xmlToSQLParser.parse(table, xmlFilename, outputFilename);
               
       /* }
        catch (ParserConfigurationException e)
        { 
         System.out.print("ERROR:Parser Configuration error");
            //e.printStackTrace();
            return;       
            
        }

        catch (org.xml.sax.SAXException e)
        { //System.out.print("ERROR:Parser Configuration error");
            //e.printStackTrace();
            return;        }
        catch (IOException ed )
        {
             System.out.print("ERROR:IO Error");
            //e.printStackTrace();
            return;        }
        //return table;
        */
    }

}
