import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Element;


/**
 * Created by Megan on 4/3/2017.
 */
public class XSDParser {

    public void parseXSD(File xsdFile, XMLToSQLParser xmlToSQLParser ){
        ArrayList<Schema> table = new ArrayList<>(1);
        try{
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder;
            docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse (new File("xsdTestFile.xsd"));
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
                    attribute.setLength(first.getAttribute("maxOccurs"));
                    attribute.setMin(first.getAttribute("minOccurs"));
                    if(first.hasAttribute("fraction")) {
                        attribute.setFraction(first.getAttribute("fraction"));
                    }
                    table.add(attribute);
                }

            }
            xmlToSQLParser.parse(table);

        }
        catch (ParserConfigurationException e)
        {
            e.printStackTrace();
        }

        catch (org.xml.sax.SAXException e)
        {
            e.printStackTrace();
        }
        catch (IOException ed )
        {
            ed.printStackTrace();
        }
        //return table;
    }

}

