import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.StringTokenizer;


/**
 * DataEngine - Main Class for the application, Use this to generate our objects in the global scope
 *
 * @author Sean Domingo, Michael Frederick, Megan Molumby, Mai Huong Nguyen,
 * Richard Pratt
 */

public class DataEngine {

    private SQLParser sqlParser;
    private FileReader fileReader;
    private UserInterface userInterface;
    private XMLToSQLGenerator xmlToSqlGenerator;
    private XMLToSQLParser xmlToSqlParser;
    private XSDParser xsdParser;
    
    private String currentDatabase;
    private String currentTime;

    public DataEngine() {
        this.sqlParser = new SQLParser();
        this.fileReader = new FileReader();
        this.userInterface = new UserInterface();
        this.xmlToSqlGenerator = new XMLToSQLGenerator();
        this.xmlToSqlParser = new XMLToSQLParser();
        this.xsdParser = new XSDParser();
        checkFilesystem();
        
        // Get time somewhere here.
        this.currentTime = new SimpleDateFormat("MM/dd/yyyy kk:mm:ss").format(new Timestamp(System.currentTimeMillis()));
    }

    /**
     * Actual entry point
     */
    public void runEngine() {
        this.currentDatabase = "";
        boolean running = true;
        String userInput = "";
        String temp;
        
        this.userInterface.showProgramHeader();
        while (running) {
            userInput = this.userInterface.getInput();
            
            if (userInput.equalsIgnoreCase("exit")) {
                this.userInterface.showUser("Goodbye");
                running = false;
            }
            else if (userInput.equalsIgnoreCase("help")) {
                if (this.currentDatabase.equals("")) {
                    this.userInterface.showDatabaseHelp();
                }
                else {
                    this.userInterface.showTableHelp(currentDatabase);
                }
            }
            else {
                if (this.currentDatabase.equals("")) {
                    // Pass data to Parser
                    this.parseCommand(userInput);
                }
                else {
                    // Database Selected
                    // Check if convert or input
                    if (userInput.matches("CONVERT XML (\\w)+(.\\w+)*, XSD (\\w)+(.\\w+)* AS (\\w)+(.\\w+)*;")) {
                        //this.userInterface.showUser("VALID CONVERT");
                        temp = userInput.replace("CONVERT XML ", "");
                        temp = temp.replace(", XSD", "");
                        temp = temp.replace(" AS", "");
                        temp = temp.replace(";", "");
                        String[] filenames = temp.split(" ");
                        this.convertXmlToSql(filenames[0], filenames[1], filenames[2]);
                    }
                    else if (userInput.matches("INPUT (\\w)+(.\\w+)*;")) {
                        //this.userInterface.showUser("VALID INPUT");
                        temp = userInput.replace("INPUT ", "");
                        temp = temp.replace(";", "");
                        this.inputFileIntoDatabase(temp);
                    }
                    else {
                        // Pass data to Parser
                        this.parseCommand(userInput);
                    }
                }
            }
        }
    }
    
    private void convertXmlToSql(String xmlFilename, String xsdFilename, String outputFilename) {
        try {
            // Passing xml, xsd, and outputFilename
            // Make call here
            this.xsdParser.parseXSD(xmlFilename, xsdFilename, outputFilename, this.xmlToSqlParser);
            this.userInterface.showUser("Conversion completed: " + outputFilename + " was created for INPUT command");
        }
        catch (Exception e) {
            this.userInterface.showUser("Error:");
            this.userInterface.showUser(e.getMessage());
        }
        
        return;
    }
    
    private void parseCommand(String input) {
        SQLCommand command;
        try {
            command = this.sqlParser.executeSQLParser(input, currentDatabase);
            if (command.database == null ? this.currentDatabase != null : !command.database.equals(this.currentDatabase)) {
                this.currentDatabase = command.database;
            }
            command.executeCommand();
        }
        catch (IndexOutOfBoundsException e) {
            this.userInterface.showUser("Error:");
            this.userInterface.showUser("Missing unknown set of characters");
            //e.printStackTrace(System.out);
        }
        catch (Exception e) {
            this.userInterface.showUser("Error:");
            this.userInterface.showUser(e.getMessage());
            //e.printStackTrace(System.out);
        }
        
        return;
    }
    
    private void inputFileIntoDatabase(String inputFilename) {
        // Going to flush this out in integration
        this.userInterface.showUser(inputFilename);
    }

    /**
     * Checks if database and tables folders exist, creates them if necessary
     */
    private void checkFilesystem() {
        File temp;
        Path path = Paths.get("tables\\");
        if (Files.notExists(path)) {
            temp = new File("tables");
            temp.mkdir();
        }
        
        path = Paths.get("databases\\");
        if (Files.notExists(path)) {
            temp = new File("databases");
            temp.mkdir();
        }
    }
    
    
}
