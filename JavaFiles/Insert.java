import java.io.File;
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
 * 
 * Insert is an object class and holds the data field description relating to database name
 * from its extension to SQLCommand. Additionally, Insert also contains data field description 
 * relating to the table name, an array of field names (columnNames) of type String, an array 
 * of field values (columnValues) of type String, a WriteDOMtoFile object, a method called 
 * “writer”, a DOMUtility object called domUtil, a Path object called tablePath, a Document
 * interface called tableDOM and a File object called outputFile. The WriteDOMtoFile object’s 
 * primary function is to write a DOM document to a file. DOMUtility is a class with various 
 * methods relating to parsing an XML document. Path is an instance used to  specify file 
 * location or directory and locate a file. The Document interface serves as the root of a 
 * document tree and as the primary access to the data within the XML document. The class
 * File is used for the creation of files and directories.
 *
 * 
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
        writer = new WriteDOMtoFile();
        tablePath = Paths.get("tables", database, tableName + ".xml");
        this.tableName = tableName;
        this.columnNames = names;
        this.columnValues = values;
    }
    
    @Override
    public void executeCommand() {
    		
    		tableDOM = domUtil.XMLtoDOM(new File(tablePath.toString()));
    		Element root = tableDOM.getDocumentElement();
    		Element tableElem = tableDOM.createElement("record");// create record tag
    		Element time = tableDOM.createElement("time");// create time tag for insertion time
    		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");// form
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
    
}