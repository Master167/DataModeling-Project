import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
/**
 * DropTable
 * @author Sean Domingo, Michael Frederick, Megan Molumby, Mai Huong Nguyen, Richard Pratt
 */
public class DropTable extends SQLCommand {
    
	public String tableName;
	private WriteDOMtoFile writer;
    private DOMUtility domUtil;
    private Path dbPath;
    private Path tablePath;
    private Document dbFileDOM;// DOM for database catalog file
    private File databaseFile;
    private File tableFile;
    private NodeList nodeList;
	
    public DropTable(String database, String tableName) {
    	
        super(database);
        domUtil = new DOMUtility(); 
	    writer = new WriteDOMtoFile();
        dbPath = Paths.get("databases", database + ".xml");// get "/databases/<databaseName>.xml"
        tablePath = Paths.get("tables", database, tableName + ".xml");// get "/tables/<databaseName>/"
        databaseFile = new File(dbPath.toString());
        tableFile = new File(tablePath.toString());
        this.tableName = tableName;
    }
    
    @Override
    public void executeCommand() {
    	
    		tableFile.delete();
    		dbFileDOM = domUtil.XMLtoDOM(databaseFile);
    		Element root = dbFileDOM.getDocumentElement();
    		nodeList = root.getChildNodes();
    		Node tableNode = root.getFirstChild();
    		while(tableNode != null) {
    			if(tableName.equals(tableNode.getNodeName())) {
    				tableNode.getParentNode().removeChild(tableNode);
    				break;
    			} else {
    				tableNode = tableNode.getNextSibling();
    			}
    		}
    		System.out.println("Successfully dropped " + tableName + " table.");
    		writer.write(dbFileDOM, databaseFile);
    }
}