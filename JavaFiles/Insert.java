import java.io.File;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.w3c.dom.Document;

/**
 * Insert
 * @author Sean Domingo, Michael Frederick, Megan Molumby, Mai Huong Nguyen, Richard Pratt
 */
public class Insert extends SQLCommand {
	
    private String tableName;
    private String[] columnNames;
    private String[] columnValues;
    private WriteDOMtoFile writer;
    private DOMUtility domUtil;
    private Path tablePath;
    private Document tableDOM;
    
    public Insert(String database, String tableName, String[] names, String[] values) {
          	
        super(database);
        tablePath = Paths.get("tables", database, tableName + ".xml");
        this.tableName = tableName;
        this.columnNames = names;
        this.columnValues = values;
        
    }
    
    @Override
    public void executeCommand() {
    		
    		if(!fileExist(tablePath)) {
    			System.out.println("ERROR: File found");
    			return;
    		}
    		DOMUtility domUtil = new DOMUtility();
    		//System.out.print(tablePath.getFileName());
    		tableDOM = domUtil.XMLtoDOM(new File(tablePath.toString()));// load 
    		//Document tableInfo// table info to verify types 
    		/*
    		 * Find table by element name
    		 * Insert stuff in loop
    		 * Append to table 
    		 */
    		//writer.write(doc, outputFile);// write altered DOM object to file
    }
    
    private boolean fileExist(Path tablePath) {
    	
    		if(Files.notExists(tablePath)) {
			return false;
		} else {
			return true;
		}
	}
}