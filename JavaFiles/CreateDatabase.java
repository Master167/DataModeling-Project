import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
/**
 * CreateDatabase
 * @author Sean Domingo, Michael Frederick, Megan Molumby, Mai Huong Nguyen, Richard Pratt
 */
public class CreateDatabase extends SQLCommand {
	
	private WriteDOMtoFile writer;
    private DOMUtility domUtil;
    private Path dbPath;
    private Path tablePath;
    private Document dbFileDOM;// DOM for database catalog file
    private File databaseFile;
    private File tableFile;
	
    public CreateDatabase(String databaseName) {
    	
        super(databaseName);
        domUtil = new DOMUtility(); 
	    writer = new WriteDOMtoFile();
        dbPath = Paths.get("databases", database + ".xml");// get "/databases/<databaseName>.xml"
        tablePath = Paths.get("tables", database);// get "/tables/<databaseName>/"
        databaseFile = new File(dbPath.toString());
        tableFile = new File(tablePath.toString());
        tableFile.mkdir();
    }
    
    @Override
    public void executeCommand() {
    		
    		dbFileDOM = domUtil.createDOM();//
		Node dbRoot = dbFileDOM.createElement("tables");
		dbFileDOM.appendChild(dbRoot);
		writer.write(dbFileDOM, databaseFile);
		System.out.println("Successfully create " + super.database + " database.");
    }
}