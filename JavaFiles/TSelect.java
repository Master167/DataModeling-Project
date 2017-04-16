import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import com.sun.org.apache.xml.internal.serializer.ElemDesc;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class tSelect extends SQLCommand {
    private String[] columnNames;
    private String tableName;
    private String[] whereCond;
    private DOMUtility domUtil;
    private Document doc;
    private Path tablePath;
    private File outputFile;

    public tSelect(String database, String tableName, String[] columnNames, String[] whereCond) {
        super(database);
        domUtil= new DOMUtility();
        tablePath = Paths.get("tables", database, tableName, +".xml");
        this.tableName  = tableName;
        this.columnNames = columnNames;
        this.whereCond = whereCond;
        columnNames = names;

    }
    @Override
    public void executeCommand() {

            if(!fileExist(tablePath)){
                Systme.out.println("ERROR: File not found");
                return;
            }
        //try {
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
                                String comparator = whereCond[1];
                                boolean printIt = false;
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
        /*} catch (Exception e) {
            e.printStackTrace();
        }*/
    }
    private boolean fileExist(Path tablePath) {

        if(Files.notExists(tablePath)) {
            return false;
        } else {
            return true;
        }
    }
}