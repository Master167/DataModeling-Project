import java.io.File;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
        domUtil = new DOMUtility(); 
        WriteDOMtoFile writer = new WriteDOMtoFile();
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
    		
    		tableDOM = domUtil.XMLtoDOM(new File(tablePath.toString()));
    		Element root = tableDOM.getDocumentElement();
    		Element tableElem = tableDOM.createElement(tableName);
    		Element time = tableDOM.createElement("TIME");
    		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    		Date date = new Date();
    		time.setTextContent(dateFormat.format(date));
    		tableElem.appendChild(time);
    		for (int i = 0; i < columnNames.length; i++) {
    			Element columnElem = tableDOM.createElement(columnNames[i]);
    			columnElem.setTextContent(columnValues[i]);
    			tableElem.appendChild(columnElem);
    		}
    		root.appendChild(tableElem);
    		writer.write(tableDOM, new File(tablePath.toString()));
    		System.out.println("Succesfully inserted into " + tableName + " table. ");
  
    }
    
    private boolean fileExist(Path tablePath) {
    	
    		if(Files.notExists(tablePath)) {
			return false;
		} else {
			return true;
		}
	}
}