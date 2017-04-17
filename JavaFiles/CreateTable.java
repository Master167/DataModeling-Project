import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * CreateTable
 * 
 * @author Sean Domingo, Michael Frederick, Megan Molumby, Mai Huong Nguyen, Richard Pratt
 *CreateTable is a class and holds the data field description relating to database name 
 *from its extension to SQLCommand. Additionally, CreateTable also contains data field 
 *description relating to the table name, an array of field names (columnNames), an array 
 *of field types (columnTypes), an array of field max lengths (columnLengths), and array 
 *that determines whether a field can be null (Nullable). The arrays “columnNames”, 
 *“columnTypes”, and “columnLengths” are of type String, while array “Nullable” is of 
 *type boolean. Other objects within CreateTable includes a WriteDOMtoFile method, 
 *DOMUtility constructor, Path class (tablePath and dbPath), Document interface 
 *(tableFileDOM and dbFileDOM), and File class (tableFile and databaseFile). 
 *WriteDOMtoFile is a method within DOMUtility. It’s primary function is to write
 * a DOM document to a file. DOMUtility is a constructor with various methods relating 
 * to parsing an XML document. Path is an instance used to  specify file location or
 *  directory and locate a file. The Document interface serves as the root of a document 
 *  tree and as the primary access to the data within the XML document. The class File is
 *   used for the creation of files and 
 *
 */
public class CreateTable extends SQLCommand {
    
	private String tableName;
    private String[] columnNames;
    private String[] columnTypes;
    private String[] columnLength;
    private boolean[] isNullable;
    private WriteDOMtoFile writer;
    private DOMUtility domUtil;
    private Path tablePath;
    private Path dbPath;
    private Document tableFileDOM;// DOM to build table file
    private Document dbFileDOM;// DOM for database catalog file
    private File tableFile;
    private File databaseFile;
    
    public CreateTable(String database, String tableName, String[] columnNames, String[] types, String[] columnLength, boolean[] isNullable) {
       
    		super(database);
    		domUtil = new DOMUtility(); 
    	    writer = new WriteDOMtoFile();
    	    tablePath = Paths.get("tables", database, tableName + ".xml");// get "/tables/<databaseName>/<tableName>.xml"
    	    dbPath = Paths.get("databases", database + ".xml");// get "/databases/<databaseName>.xml"
    	    tableFile = new File(tablePath.toString());
    	    databaseFile = new File(dbPath.toString());
        createFile(tableFile, tablePath);// create new table data file from Path object
        this.tableName = tableName;
        this.columnNames = columnNames;
        this.columnLength = columnLength;
        this.columnTypes = types;
        this.isNullable = isNullable;
    }
    
    @Override
    public void executeCommand() {
    		
    		tableFileDOM = domUtil.createDOM();//
    		Node tblRoot = tableFileDOM.createElement(tableName);
    		tableFileDOM.appendChild(tblRoot);
    		writer.write(tableFileDOM, tableFile);
    		dbFileDOM = domUtil.XMLtoDOM(databaseFile);
    		Node dbRoot = dbFileDOM.getDocumentElement();//tableFileDOM.createElement("tables"); 
		Element tableElem = dbFileDOM.createElement(tableName);// create table name tag
		Element time = dbFileDOM.createElement("time");// create time tag for insertion time
		Element timeType = dbFileDOM.createElement("type");// create nested time type tag
		Element timeFormat = dbFileDOM.createElement("format");// create nested time format tag
		timeType.setTextContent("time");
		timeFormat.setTextContent("mm/dd/yyyy");
		time.appendChild(timeType);
		time.appendChild(timeFormat);
		tableElem.appendChild(time);
		for (int i = 0; i < columnNames.length; i++) {
			Element columnName = dbFileDOM.createElement(columnNames[i]);// create column name element
			// create column type element
			Element columnType = dbFileDOM.createElement("type");// create type element
			columnType.setTextContent(columnTypes[i]);// set type element text
			columnName.appendChild(columnType);// append type to column name element
			// create column length element
			Element columnLen = dbFileDOM.createElement("length");
			if(columnLength[i].contains(",")) {
				String[] numLengths = columnLength[i].split("\\,");
				Element numberWidth = dbFileDOM.createElement("digits");
				numberWidth.setTextContent(numLengths[0]);
				Element numberDeci = dbFileDOM.createElement("decimals");
				numberDeci.setTextContent(numLengths[1]);
				columnName.appendChild(numberWidth);
				columnName.appendChild(numberDeci);
				columnLen.appendChild(numberWidth);
				columnLen.appendChild(numberDeci);
			} else if(columnLength[i].contains("\\/")) {
				// not sure I need to do anything special with date like instead of length call is format?
			} else if(columnLength[i].isEmpty()) {
				columnLen.setTextContent("255");
			} else {
				columnLen.setTextContent(columnLength[i]);
			}
			columnName.appendChild(columnLen);// append length to column name element
			// create column isNullable element
			Element colNullType = dbFileDOM.createElement("isnullable");
			colNullType.setTextContent(String.valueOf(isNullable[i]));
			columnName.appendChild(colNullType);// append null type to column name element
			// append to table element
			tableElem.appendChild(columnName);
		}
		dbRoot.appendChild(tableElem);
		writer.write(dbFileDOM, databaseFile);
		System.out.println("Succesfully inserted into " + tableName + " table.");
    }
    
    private void createFile(File table, Path path) {
	    	
    		table = new File(path.toString());
		try {
			table.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
}
