import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
//import com.sun.org.apache.xml.internal.serializer.ElemDesc;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

 /*} catch (Exception e) {
            e.printStackTrace();
        }*/

public class TSelect extends SQLCommand {
    private String[] columnNames;
    private String tableName;
    private String[] whereCond;
    private DOMUtility domUtil;
    private Document doc;
    private Path tablePath;
    private File outputFile;

    public TSelect(String database, String tableName, String[] columnNames, String[] whereCond) {
        super(database);
        domUtil= new DOMUtility();
        tablePath = Paths.get("tables", database, tableName+ ".xml");
        this.tableName  = tableName;
        this.columnNames = columnNames;
        this.whereCond = whereCond;
        columnNames = columnNames;

    }
    @Override
    public void executeCommand() {

            if(!fileExist(tablePath)){
                System.out.println("ERROR: File not found");
                return;
            }
       try {
            doc = domUtil.XMLtoDOM(new File(tablePath.toString()));
            //--------------------------------------------
            System.out.print("Table: ");
            System.out.println(doc.getDocumentElement().getNodeName());
            //will always be record!
            NodeList nList = doc.getElementsByTagName("record");
            System.out.println("----------------------------");
            //IF SELECT STATEMENT IS SELECT * FROM tablename;

            //SELECT columnName, ...,.. FROM TABLE// includes *
            if(whereCond == null) {
                int arrayCounter = 0;

                for (int temp = 0; temp < nList.getLength(); temp++) {
                    boolean timePrinted = false;
                    Node nNode = nList.item(temp);
                    System.out.println("----------------------------");
                    System.out.println("Current Record: ");
                    System.out.println("----------------------------");
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;

                        while (arrayCounter < columnNames.length) {

                            NodeList nodeList = eElement.getElementsByTagName(columnNames[arrayCounter]);
                            for (int count = 0; count < nodeList.getLength(); count++) {
                                Node node1 = nodeList.item(count);
                                if (node1.getNodeType() == node1.ELEMENT_NODE) {
                                    Element element = (Element) node1;
                                    if (columnNames[arrayCounter].equals("time")) {
                                        timePrinted = true;
                                    }
                                    System.out.print(columnNames[arrayCounter] + ": ");
                                    System.out.println(element.getTextContent());
                                }
                            }
                            arrayCounter++;
                        }
                        //if time was not printed, i.e. Select specificField FROM TABLE; then print!
                        if (!timePrinted) {
                            NodeList nodeList = eElement.getElementsByTagName("time");
                            Node node1 = nodeList.item(0);
                            Element element = (Element) node1;
                            System.out.print("time: ");
                            System.out.println(element.getTextContent());
                        }

                    }
                    //resets string column array for next record.
                    arrayCounter = 0;
                }
            }else{ // WHERE CONDITION

            int arrayCounter = 0;

            for (int temp = 0; temp < nList.getLength(); temp++) {
                boolean printCurrent = false;
                boolean timePrinted = true;
                //ITERATES THROUGH EACH RECORD
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    while (arrayCounter < columnNames.length) {
                        //SPECIFIC ATTRIBUTES/COLUMNNAMES ITERATEs THROUGH EACH COLUMN
                        NodeList nodeList = eElement.getElementsByTagName(columnNames[arrayCounter]);
                        for (int count = 0; count < nodeList.getLength(); count++) {
                            Node node1 = nodeList.item(count);
                            if (node1.getNodeType() == node1.ELEMENT_NODE) {
                                Element element = (Element) node1;


                                //SET CONDITIONS for where
                                //get where condition column name [0]
                                NodeList condList = eElement.getElementsByTagName(whereCond[0]);
                                Node condNode1 = condList.item(0);
                                Element condElement = (Element) condNode1;

                                // =, >, <, <=, >=, !=
                                boolean printIt = false;
                                // =, >, <, <=, >=, !=
                                String comparator = whereCond[1];
                                //CHECK if compare value is date------------------
                                String dateRegex = "^(0[0-9]||1[0-2])/([0-2][0-9]||3[0-1])/([0-9][0-9])?[0-9][0-9]$";
                                //mm/dd/yyy hh:mm:ss
                                String timeRegex ="^(0[0-9]||1[0-2])/([0-2][0-9]||3[0-1])/[0-9][0-9][0-9][0-9]\\s+([0-1]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$";
                                Date whereCondDate;
                                Date elementDate;
                                ///IF IT IS A TIME STAMP
                                if(whereCond[2].matches(timeRegex)|| whereCond[2].matches(dateRegex)) {
                                    if (whereCond[2].matches(timeRegex)) {
                                        SimpleDateFormat timeStamp = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                                        whereCondDate = timeStamp.parse(whereCond[2]);
                                        elementDate = timeStamp.parse(condElement.getTextContent());
                                    } else{
                                        //check which variation mm/dd/yy[yy]
                                        String yyyyRegex = "^(0[0-9]||1[0-2])/([0-2][0-9]||3[0-1])/[0-9][0-9][0-9][0-9]$";
                                        String yyRegex = "^(0[0-9]||1[0-2])/([0-2][0-9]||3[0-1])/[0-9][0-9]$";

                                        if (whereCond[2].matches(yyyyRegex)) {
                                            SimpleDateFormat fourYearDf = new SimpleDateFormat("MM/dd/yyyy");
                                            whereCondDate = fourYearDf.parse(whereCond[2]);
                                            elementDate = fourYearDf.parse(condElement.getTextContent());
                                        } else {
                                            SimpleDateFormat twoYearDf = new SimpleDateFormat("MM/dd/yy");
                                            whereCondDate = (twoYearDf.parse(whereCond[2]));
                                            elementDate = twoYearDf.parse(condElement.getTextContent());
                                        }
                                    }
                                    switch (comparator) {
                                        case "=":
                                            if (whereCondDate.equals(elementDate)) {
                                                printIt = true;
                                            }
                                            break;
                                        case ">":
                                            if (elementDate.after(whereCondDate)) {
                                                printIt = true;
                                            }
                                            break;
                                        case "<":
                                            if (elementDate.before(whereCondDate)) {
                                                printIt = true;
                                            }
                                            break;
                                        case "<=":
                                            if (elementDate.equals(whereCondDate) || elementDate.before(whereCondDate)) {
                                                printIt = true;
                                            }
                                            break;
                                        case ">=":
                                            if (elementDate.equals(whereCondDate) || elementDate.after(whereCondDate)) {
                                                printIt = true;
                                            }
                                            break;
                                        case "!=":
                                            if (!elementDate.equals(whereCondDate)) {
                                                printIt = true;
                                            }
                                            break;
                                        default:
                                            System.out.print("Error:compare value is not a valid comparator");

                                    }
                                } else {

                                    switch(comparator){
                                    case "=":if(condElement.getTextContent().equals(whereCond[2])){
                                        printIt = true;
                                    }
                                        break;
                                    case ">": if(Integer.parseInt(condElement.getTextContent()) > Integer.parseInt(whereCond[2])){
                                        printIt = true;
                                    }
                                        break;
                                    case "<":if(Integer.parseInt(condElement.getTextContent())< Integer.parseInt(whereCond[2])){
                                        printIt =true;
                                    }break;
                                    case "<=":if(Integer.parseInt(condElement.getTextContent())<= Integer.parseInt(whereCond[2])){
                                        printIt =true;
                                    }break;
                                    case ">=":if(Integer.parseInt(condElement.getTextContent()) > Integer.parseInt(whereCond[2])){
                                        printIt = true;
                                    }break;
                                    case "!=":if(!condElement.getTextContent().equals(whereCond[2])){
                                        printIt = true;
                                    }break;
                                    default: System.out.println("ERROR: this is not a valide comparator");
                                    }
                                }

                                if(printIt) {
                                    //for printing current element if not already printed.
                                    if(!printCurrent){
                                        System.out.println("----------------------------");
                                        System.out.println("Current Record: ");
                                        System.out.println("----------------------------");
                                        NodeList tnodeList = eElement.getElementsByTagName("time");
                                        Node tnode1 = tnodeList.item(0);
                                        Element telement = (Element) tnode1;
                                        System.out.print("time: ");
                                        System.out.println(telement.getTextContent());
                                        printCurrent = true;
                                    }
                                    System.out.print(columnNames[arrayCounter] + ": ");
                                    System.out.println(element.getTextContent());
                                }

                            }

                        }
                        arrayCounter++;
                    }

                }
                arrayCounter = 0;
            }
        }
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