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
    private StringBuilder outputFile;
    private StringBuilder tablePath;
    private File tableDir;
    private Path dbPath;
    
    public Insert(String database, String tableName, String[] names, String[] values) {
        super(database);
        outputFile = new StringBuilder();
        outputFile.append(".xml");
        this.tableName = tableName;
        this.columnNames = names;
        this.columnValues = values;
    }
    
    @Override
    public void executeCommand() {
    		dbPath = Paths.get(super.database + "\\");
    		DOMUtility domUtil = new DOMUtility();
    		Document tableFile;// load 
    		//Document tableInfo// table info to verify types 
    		/*
    		 * Find table by element name
    		 * Insert stuff in loop
    		 * Append to table 
    		 */
    		//writer.write(doc, outputFile);// write altered DOM object to file
    		
    }
}