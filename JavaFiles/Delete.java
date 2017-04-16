
/**
 * Delete
 * @author Sean Domingo, Michael Frederick, Megan Molumby, Mai Huong Nguyen, Richard Pratt
 */
 
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.io.File;
import java.nio.file.Files;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Delete extends SQLCommand {
    private  String tableName;
    private String[] whereCondition;
    private  String[] columnNames;
    private DOMUtility domUtil;
    private Path tablePath;
    private Document doc;
    private WriteDOMtoFile writer;
   

   public Delete(String database, String tableName, String[] whereCondition) {
        super(database);
        domUtil = new DOMUtility();
        tablePath = Paths.get("tables", database, tableName+".xml");
        this.tableName = tableName;
        this.whereCondition = whereCondition;
       writer = new WriteDOMtoFile();
    }

    @Override
    public void executeCommand() {

           if(!fileExist(tablePath)){
                System.out.println("ERROR: File not found");
                return;
            }
        try {


      //------------------------------------

            doc = domUtil.XMLtoDOM(new File(tablePath.toString()));
            //--------------------------------------------
            System.out.println("----------------------------");
            //will always be record!
            Node table = doc.getFirstChild();
            NodeList records = table.getChildNodes();
            int recordDeletionCounter =0;
            NodeList nList = doc.getElementsByTagName("record");
            for(int temp = 0; temp < nList.getLength(); temp++){
                      Node nNode = nList.item(temp);
             
                    Element element = (Element) nNode;
                NodeList columnList = element.getElementsByTagName(whereCondition[0]);
                //always going to be one column
                Node column = columnList.item(0);
                if(column.getNodeType() == column.ELEMENT_NODE){
                    Element cElement = (Element) column;
                   
                     
                    String comparator = whereCondition[1];
                    boolean removeIt = false;
                    switch(comparator){
                    
                        case "=":if(whereCondition[2].equals(cElement.getTextContent())){
                                    removeIt = true;
                                }
                                break;
                        case ">": if(Integer.parseInt(cElement.getTextContent()) > Integer.parseInt(whereCondition[2])){
                                removeIt = true;
                                }
                                break;
                        case "<":if(Integer.parseInt(cElement.getTextContent())< Integer.parseInt(whereCondition[2])){
                            removeIt = true;
                        }
                            break;
                        case "<=":if(Integer.parseInt(cElement.getTextContent())<= Integer.parseInt(whereCondition[2])){
                            removeIt = true;
                        }
                            break;
                        case ">=":if(Integer.parseInt(cElement.getTextContent()) > Integer.parseInt(whereCondition[2])){
                            removeIt = true;
                        }
                            break;
                        case "!=":if(!cElement.getTextContent().equals(whereCondition[2])){
                            removeIt = true;
                        }
                            break;
                        default: System.out.println("ERROR: this is not a valid comparator");
                                    return;
                    }
                    if(removeIt){
                        table.removeChild(nNode);
                        recordDeletionCounter++;
                    }
                }

            }/*
            writer.write(doc, new File(tablePath.toString()));
            TransformerFactory transformerFactory =
                    TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            System.out.println("-----------Modified File-----------");
            StreamResult consoleResult = new StreamResult(System.out);
            transformer.transform(source, consoleResult);
            */
        System.out.println("Successfully deleted " + recordDeletionCounter + " records.");
     
     } catch (Exception e) {
            e.printStackTrace();
     }
 }

    private boolean fileExist(Path tablePath) {

        if(Files.notExists(tablePath)) {
            return false;
        } else {
            return true;
        }
    }
}