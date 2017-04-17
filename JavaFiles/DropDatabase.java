import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * DropDatabase
 * @author Sean Domingo, Michael Frederick, Megan Molumby, Mai Huong Nguyen, Richard Pratt
 */

public class DropDatabase extends SQLCommand {
	
	private Path tablePath;
	private Path dbPath;
	private File tableFile;
	private File databaseFile;
	
    public DropDatabase(String databaseName) {
    	
        super(databaseName);
        tablePath = Paths.get("tables", database);// get "/tables/<databaseName>/<tableName>.xml"
	    dbPath = Paths.get("databases", database + ".xml");// get "/databases/<databaseName>.xml"
	    tableFile = new File(tablePath.toString());
	    databaseFile = new File(dbPath.toString());
        
    }
    
    @Override
    public void executeCommand() {
    		
    		tableFile.delete();
    		databaseFile.delete();
    		System.out.println("Successfully dropped " + super.database + " database.");
    }
}