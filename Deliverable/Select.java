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
import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
 /* } catch (Exception e) {
            e.printStackTrace();
        }*/

public class Select extends SQLCommand {
    private String[] columnNames;
    private String tableName;
    private String[] whereCond;
    private DOMUtility domUtil;
    private Path tablePath;
    private Document doc;

    public Select(String database, String tableName, String[] names,String[] whereCond){
            super(database);
            domUtil = new DOMUtility();
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
                   System.out.println("----------------------");
                   System.out.println("Current Record: ");
                   System.out.println("----------------------");
                    
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;

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
                                        boolean printIt = false;
                                        // =, >, <, <=, >=, !=
                                        String comparator = whereCond[1];
                                        //CHECK if compare value is date------------------
                                        String dateRegex = "^(0[0-9]||1[0-2])/([0-2][0-9]||3[0-1])/([0-9][0-9])?[0-9][0-9]$";
                                        Date whereCondDate;
                                        Date elementDate;
                                        if(whereCond[2].matches(dateRegex)){
                                            //check which variation mm/dd/yy[yy]
                                            String yyyyRegex ="^(0[0-9]||1[0-2])/([0-2][0-9]||3[0-1])/[0-9][0-9][0-9][0-9]$";
                                            String yyRegex ="^(0[0-9]||1[0-2])/([0-2][0-9]||3[0-1])/[0-9][0-9]$";

                                            if(whereCond[2].matches(yyyyRegex)){
                                                SimpleDateFormat fourYearDf = new SimpleDateFormat("MM/dd/yyyy");
                                                 whereCondDate = fourYearDf.parse(whereCond[2]);
                                                elementDate = fourYearDf.parse(condElement.getTextContent());
                                            }else{
                                                SimpleDateFormat twoYearDf = new SimpleDateFormat("MM/dd/yy");
                                                whereCondDate = (twoYearDf.parse(whereCond[2]));
                                                elementDate = twoYearDf.parse(condElement.getTextContent());
                                            }
                                            switch(comparator){
                                                case "=": if(whereCondDate.equals(elementDate)){
                                                                printIt=true;
                                                            }break;
                                                case ">": if(elementDate.after(whereCondDate)){
                                                            printIt = true;
                                                            }break;
                                                case "<": if(elementDate.before(whereCondDate)){
                                                            printIt =true;
                                                    }break;
                                                case "<=":if(elementDate.equals(whereCondDate) || elementDate.before(whereCondDate)){
                                                    printIt=true;
                                                }break;
                                                case ">=": if(elementDate.equals(whereCondDate) || elementDate.after(whereCondDate)){
                                                                printIt=true;
                                                        }break;
                                                case "!=":if(!elementDate.equals(whereCondDate)){
                                                        printIt=true;
                                                }break;
                                                default:
                                                        System.out.print("Error:compare value is not a valid comparator");

                                            }
                                        }else {

                                            //all other values besides date. 
                                            //-----------------------------------


                                            switch (comparator) {
                                                case "=":
                                                    if (condElement.getTextContent().equals(whereCond[2])) {
                                                        printIt = true;
                                                    }
                                                    break;
                                                case ">":
                                                    if (Integer.parseInt(condElement.getTextContent()) > Integer.parseInt(whereCond[2])) {
                                                        printIt = true;
                                                    }
                                                    break;
                                                case "<":
                                                    if (Integer.parseInt(condElement.getTextContent()) < Integer.parseInt(whereCond[2])) {
                                                        printIt = true;
                                                    }
                                                    break;
                                                case "<=":
                                                    if (Integer.parseInt(condElement.getTextContent()) <= Integer.parseInt(whereCond[2])) {
                                                        printIt = true;
                                                    }
                                                    break;
                                                case ">=":
                                                    if (Integer.parseInt(condElement.getTextContent()) > Integer.parseInt(whereCond[2])) {
                                                        printIt = true;
                                                    }
                                                    break;
                                                case "!=":
                                                    if (!condElement.getTextContent().equals(whereCond[2])) {
                                                        printIt = true;
                                                    }
                                                    break;
                                                default:
                                                    System.out.println("ERROR: this is not a valide comparator");
                                            }
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
        } catch (ParseException e) {
            
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