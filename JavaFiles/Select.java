import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.nio.file.Files;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Select extends SQLCommand {
    private String[] columnNames;
    private String tableName;
    private String[] whereCond;
    private DOMUtility domUtil;
    private Path tablePath;
    private Document doc;

    public void Select(String database, String tableName, String[] names,String[] whereCond){
            super(database);
            domUtil = DOMUtility();
            tablePath = Paths.get("tables", database, tableName+".xml");
            this.tableName = tableName;
            this.columnNames = names;
            this.whereCond = whereCond;
    }
    @Override
    public void executeCommand() {
        if(!fileExist(tablePath)){
            System.out.println("Error:File not found");
            return;
        }
        doc = domUtil.XMLtoDOM(new File(tablePath.toString()));

        try {
            //--------------------------------------------
            System.out.print("Table: ");
            System.out.println(doc.getDocumentElement().getNodeName());
            //will always be record!
            NodeList nList = doc.getElementsByTagName("record");
            System.out.println("----------------------");

            if(whereCond == null) {
            
                //SELECT columnName, ...,.. FROM TABLE// includes * without time
                int arrayCounter = 0;
                for (int temp = 0; temp < nList.getLength(); temp++) {
                    Node nNode = nList.item(temp);
                    System.out.print("\nCurrent Element: ");
                    System.out.println(nNode.getNodeName());
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;
                        //System.out.print("record");
                        // System.out.println(eElement.getAttribute(columnNames[arrayCounter]));

                        while (arrayCounter < columnNames.length) {
                            NodeList nodeList = eElement.getElementsByTagName(columnNames[arrayCounter]);

                            for (int count = 0; count < nodeList.getLength(); count++) {
                                Node node1 = nodeList.item(count);
                                if (node1.getNodeType() == node1.ELEMENT_NODE) {
                                    Element element = (Element) node1;
                                    if (!columnNames[arrayCounter].equals("time")) {
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
            }else{ // WHERE CONDITION
                int arrayCounter = 0;

                for (int temp = 0; temp < nList.getLength(); temp++) {
                    boolean printCurrent = false;
                    //ITERATES THROUGH EACH RECORD
                    Node nNode = nList.item(temp);
                   // System.out.print("\nCurrent Element: ");
                   // System.out.println(nNode.getNodeName());
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;

                        while (arrayCounter < columnNames.length) {
                            //SPECIFIC ATTRIBUTES/COLUMNNAMES ITERATEs THROUGH EACH COLUMN
                            NodeList nodeList = eElement.getElementsByTagName(columnNames[arrayCounter]);
                            for (int count = 0; count < nodeList.getLength(); count++) {
                                Node node1 = nodeList.item(count);
                                if (node1.getNodeType() == node1.ELEMENT_NODE) {
                                    Element element = (Element) node1;

                                    if (!columnNames[arrayCounter].equals("time")) {
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
                                                    System.out.println("----------------------");
                                                    System.out.println("Current Record: ");
                                                    System.out.println("----------------------");

                                                    printCurrent = true;
                                                }
                                                System.out.print(columnNames[arrayCounter] + ": ");
                                                System.out.println(element.getTextContent());
                                            }
                                        }
                                }
                            }
                            arrayCounter++;
                        }

                    }
                    arrayCounter = 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private boolean fileExist(Path tablePath){
        if(Files.notExists(tablePath)){
            return false;
        }else{
            return true;
        }
    }
}