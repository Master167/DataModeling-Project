import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Element;


/**
 * Created by Megan on 4/3/2017.
 */
public class XSDParser {

    public void parseXSD(File inputFile, XMLToSQLParser xmlToSQLParser ){
        try{System.out.print("imhere");
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder;
            docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse (new File("xsdTestFile.xsd"));
            NodeList list = doc.getElementsByTagName("xsd:element");

//loop to print data
            for(int i = 0 ; i < list.getLength(); i++)
            {
                Element first = (Element)list.item(i) ;
                if( first.hasAttributes())
                {

                    String nam = first.getAttribute("name");
                    System.out.println(nam);
                    String nam1 = first.getAttribute("type");
                    System.out.println(nam1);
                }
            }
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
    }

}

