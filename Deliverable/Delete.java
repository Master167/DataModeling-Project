
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
import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
 
 /* } catch (Exception e) {
            e.printStackTrace();
     }*/


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
                    // =, >, <, <=, >=, !=
                   
                    //CHECK if compare value is date------------------
                    String dateRegex = "^(0[0-9]||1[0-2])/([0-2][0-9]||3[0-1])/([0-9][0-9])?[0-9][0-9]$";
                    //mm/dd/yyy hh:mm:ss
                    String timeRegex ="^(0[0-9]||1[0-2])/([0-2][0-9]||3[0-1])/[0-9][0-9][0-9][0-9]\\s+([0-1]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$";
                    Date whereCondDate;
                    Date elementDate;
                    ///IF IT IS A TIME STAMP
                    if(whereCondition[2].matches(timeRegex)|| whereCondition[2].matches(dateRegex)) {
                        if (whereCondition[2].matches(timeRegex)) {
                            SimpleDateFormat timeStamp = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                            whereCondDate = timeStamp.parse(whereCondition[2]);
                            elementDate = timeStamp.parse(cElement.getTextContent());
                        } else{
                            //check which variation mm/dd/yy[yy]
                            String yyyyRegex = "^(0[0-9]||1[0-2])/([0-2][0-9]||3[0-1])/[0-9][0-9][0-9][0-9]$";
                            String yyRegex = "^(0[0-9]||1[0-2])/([0-2][0-9]||3[0-1])/[0-9][0-9]$";

                            if (whereCondition[2].matches(yyyyRegex)) {
                                SimpleDateFormat fourYearDf = new SimpleDateFormat("MM/dd/yyyy");
                                whereCondDate = fourYearDf.parse(whereCondition[2]);
                                elementDate = fourYearDf.parse(cElement.getTextContent());
                            } else {
                                SimpleDateFormat twoYearDf = new SimpleDateFormat("MM/dd/yy");
                                whereCondDate = (twoYearDf.parse(whereCondition[2]));
                                elementDate = twoYearDf.parse(cElement.getTextContent());
                            }
                        }
                        switch (comparator) {
                            case "=":
                                if (whereCondDate.equals(elementDate)) {
                                    removeIt = true;
                                }
                                break;
                            case ">":
                                if (elementDate.after(whereCondDate)) {
                                    removeIt = true;
                                }
                                break;
                            case "<":
                                if (elementDate.before(whereCondDate)) {
                                    removeIt = true;
                                }
                                break;
                            case "<=":
                                if (elementDate.equals(whereCondDate) || elementDate.before(whereCondDate)) {
                                    removeIt = true;
                                }
                                break;
                            case ">=":
                                if (elementDate.equals(whereCondDate) || elementDate.after(whereCondDate)) {
                                    removeIt = true;
                                }
                                break;
                            case "!=":
                                if (!elementDate.equals(whereCondDate)) {
                                    removeIt = true;
                                }
                                break;
                            default:
                                System.out.print("Error:compare value is not a valid comparator");

                        }
                    } else {


                        switch (comparator) {

                            case "=":
                                if (whereCondition[2].equals(cElement.getTextContent())) {
                                    removeIt = true;
                                }
                                break;
                            case ">":
                                if (Integer.parseInt(cElement.getTextContent()) > Integer.parseInt(whereCondition[2])) {
                                    removeIt = true;
                                }
                                break;
                            case "<":
                                if (Integer.parseInt(cElement.getTextContent()) < Integer.parseInt(whereCondition[2])) {
                                    removeIt = true;
                                }
                                break;
                            case "<=":
                                if (Integer.parseInt(cElement.getTextContent()) <= Integer.parseInt(whereCondition[2])) {
                                    removeIt = true;
                                }
                                break;
                            case ">=":
                                if (Integer.parseInt(cElement.getTextContent()) > Integer.parseInt(whereCondition[2])) {
                                    removeIt = true;
                                }
                                break;
                            case "!=":
                                if (!cElement.getTextContent().equals(whereCondition[2])) {
                                    removeIt = true;
                                }
                                break;
                            default:
                                System.out.println("ERROR: this is not a valid comparator");
                                return;
                        }
                    }
                    if(removeIt){
                        table.removeChild(nNode);
                        recordDeletionCounter++;
                    }
                }

            }
            writer.write(doc, new File(tablePath.toString()));
         System.out.println("Successfully deleted " + recordDeletionCounter + " records.");
     
    } catch (ParseException e) {
        
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