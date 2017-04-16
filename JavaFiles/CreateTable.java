import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * CreateTable
 * @author Sean Domingo, Michael Frederick, Megan Molumby, Mai Huong Nguyen, Richard Pratt
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
    private Document tableFileDOM;// DOM to build table file
    private Document databaseFileDOM;// DOM for database catalog file
    private File tableFile;
    
    public CreateTable(String database, String tableName, String[] columnNames, String[] types, String[] columnLength, boolean[] isNullable) {
       
    		super(database);
    		domUtil = new DOMUtility(); 
    	    writer = new WriteDOMtoFile();
    	    tablePath = Paths.get("tables", database, tableName + ".xml");// get "tables/<databaseName>/<tableName>.xml"
    	    tableFile = new File(tablePath.toString());
        createFile(tableFile, tablePath);// create new table data file from Path object
        this.tableName = tableName;
        this.columnNames = columnNames;
        this.columnLength = columnLength;
        this.columnTypes = types;
        this.isNullable = isNullable;
    }
    
    @Override
    public void executeCommand() {
    		
    		tableFileDOM = domUtil.createDOM();//XMLtoDOM(new File(tablePath.toString()));// retrieve table file 
		Node root = tableFileDOM.createElement("tables"); 
    		//Element root = tableFileDOM.getDocumentElement();
		Element tableElem = tableFileDOM.createElement(tableName);// create table name tag
		Element time = tableFileDOM.createElement("time");// create time tag for insertion time
		Element timeType = tableFileDOM.createElement("type");// create nested time type tage
		Element timeFormat = tableFileDOM.createElement("format");// create nexted time format tag
		timeType.setTextContent("time");
		timeFormat.setTextContent("mm/dd/yyyy");
		time.appendChild(timeType);
		time.appendChild(timeFormat);
		tableElem.appendChild(time);
		for (int i = 0; i < columnNames.length; i++) {
			Element columnName = tableFileDOM.createElement(columnNames[i]);// create column name element
			// create column type element
			Element columnType = tableFileDOM.createElement("type");// create type element
			columnType.setTextContent(columnTypes[i]);// set type element text
			columnName.appendChild(columnType);// append type to column name element
			// create column length element
			Element columnLen = tableFileDOM.createElement("length");
			if(columnLength[i].contains(",")) {
				String[] numLengths = columnLength[i].split("\\,");
				Element numberWidth = tableFileDOM.createElement("width");
				numberWidth.setTextContent(numLengths[0]);
				Element numberDeci = tableFileDOM.createElement("decimal");
				numberDeci.setTextContent(numLengths[1]);
				columnName.appendChild(numberWidth);
				columnName.appendChild(numberDeci);
			} else if(columnLength[i].contains("\\/")) {
				// not sure I need to do anything special with date like instead of length call is format?
			} else if(columnLength[i].isEmpty()) {
				columnLen.setTextContent("255");
				columnName.appendChild(columnLen);// append length to column name element
			} else {
				columnLen.setTextContent(columnLength[i]);
				columnName.appendChild(columnLen);// append length to column name element
			}
			
			// create column isNullable element
			Element colNullType = tableFileDOM.createElement("isnullable");
			colNullType.setTextContent(String.valueOf(isNullable[i]));
			columnName.appendChild(colNullType);// append null type to column name element
			// append to table element
			tableElem.appendChild(columnName);
		}
		root.appendChild(tableElem);
		tableFileDOM.appendChild(root);
		writer.write(tableFileDOM, tableFile);
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
