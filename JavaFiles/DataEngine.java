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
        String firstToken;
        StringTokenizer tokenGenerator;
        
        // Show a program header?
        while (running) {
            userInput = this.userInterface.getInput();
            
            /*
            First: tokenGenerator.nextToken throws exception if no other token exists
            Second: you need to javadoc your methods.
            */
            
            if (userInput.equalsIgnoreCase("exit")) {
                this.userInterface.showUser("Goodbye");
                running = false;
            }
            else if (userInput.equalsIgnoreCase("help")) {
                if (this.currentDatabase == "") {
                    this.userInterface.showDatabaseHelp();
                }
                else {
                    this.userInterface.showTableHelp(currentDatabase);
                }
            }
            else {
                tokenGenerator = new StringTokenizer(userInput, " ");
                firstToken = tokenGenerator.nextToken();
                if (this.currentDatabase == "") {
                    // No Database selected
                    if (firstToken.equalsIgnoreCase("create")) {
                        firstToken = tokenGenerator.nextToken();
                        if (firstToken.equalsIgnoreCase("database")) {
                            // Pass userInput to sql parser
                            // Set database name when the object comes out
                            
                            // For now
                            this.userInterface.showUser("User entered");
                            this.userInterface.showUser(userInput);
                            firstToken = tokenGenerator.nextToken().replace(";", "");
                            this.currentDatabase = firstToken;
                        }
                        else {
                            this.userInterface.showUserError();
                        }
                    }
                    else if (firstToken.equalsIgnoreCase("load")) {
                        // Pass userInput to sql parser
                        // Set database name when the object comes out
                        
                        // For now
                        this.userInterface.showUser("User entered");
                        this.userInterface.showUser(userInput);
                        firstToken = tokenGenerator.nextToken().replace(";", "");
                        this.currentDatabase = firstToken;
                    }
                    else if (firstToken.equalsIgnoreCase("drop")) {
                        firstToken = tokenGenerator.nextToken();
                        if (firstToken.equalsIgnoreCase("database")) {
                            // Pass userInput to sql parser
                            // Set database name when the object comes out
                            
                            // For now
                            this.userInterface.showUser("User entered");
                            this.userInterface.showUser(userInput);
                            firstToken = tokenGenerator.nextToken().replace(";", "");
                            this.currentDatabase = firstToken;
                        }
                        else {
                            this.userInterface.showUserError();
                        }
                    }
                    else {
                        // No database selected and no recognized command at this level
                        this.userInterface.showUserError();
                    }
                }
                else {
                    // Database Selected
                    if (firstToken.equalsIgnoreCase("create")){
//                    this.showUser("CREATE TABLE tableName (fieldName fieldType(n)[ NOT NULL], ...);");
                        firstToken = tokenGenerator.nextToken();
                        if (firstToken.equalsIgnoreCase("table")) {
                              // For now
                              this.userInterface.showUser("User entered");
                              this.userInterface.showUser(userInput);
                        }
                        else {
                            this.userInterface.showUserError();
                        }
                    }
                    else if (firstToken.equalsIgnoreCase("drop")) {
//                    this.showUser("DROP TABLE tableName;");
                        firstToken = tokenGenerator.nextToken();
                        if (firstToken.equalsIgnoreCase("table")) {
                            // For now
                            this.userInterface.showUser("User entered");
                            this.userInterface.showUser(userInput);
                        }
                        else {
                            this.userInterface.showUserError();
                        }
                    }
                    else if (firstToken.equalsIgnoreCase("insert")) {
//                    this.showUser("INSERT INTO tableName[ (fieldName, ...)] VALUES (fieldValue, ...);");
                        firstToken = tokenGenerator.nextToken();
                        if (firstToken.equalsIgnoreCase("into")) {
                            // For now
                            this.userInterface.showUser("User entered");
                            this.userInterface.showUser(userInput);
                        }
                        else {
                            this.userInterface.showUserError();
                        }
                    }
                    else if (firstToken.equalsIgnoreCase("convert")) {
//                    this.showUser("CONVVERT XML xmlFilename, XSD filename AS sqlFile;");
                        firstToken = tokenGenerator.nextToken();
                        if (firstToken.equalsIgnoreCase("xml")) {
                            // For now
                            this.userInterface.showUser("User entered");
                            this.userInterface.showUser(userInput);
                        }
                        else {
                            this.userInterface.showUserError();
                        }
                    }
                    else if (firstToken.equalsIgnoreCase("input")) {
//                    this.showUser("INPUT sqlFile;");
                        if (tokenGenerator.hasMoreTokens()) {
                            // For now
                            this.userInterface.showUser("User entered");
                            this.userInterface.showUser(userInput);
                        }
                        else {
                            this.userInterface.showUserError();
                        }
                    }
                    else if (firstToken.equalsIgnoreCase("tselect") || firstToken.equalsIgnoreCase("select")) {
//                    this.showUser("[t]SELECT [*|(field, ...)] FROM tableName [WHERE (field = value)]");
                        // For now
                        this.userInterface.showUser("User entered");
                        this.userInterface.showUser(userInput);
                    }
                    else if (firstToken.equalsIgnoreCase("save")) {
                        // save database
                        firstToken = tokenGenerator.nextToken();
                        if (firstToken.equalsIgnoreCase("database")) {
                            // For now
                            this.userInterface.showUser("User entered");
                            this.userInterface.showUser(userInput);
                        }
                        else {
                            this.userInterface.showUserError();
                        }
                    }
                    else {
                        this.userInterface.showUserError();
                    }
                }
            }
        }
    }

    public void xsdParser() {
        this.fileReader.setInputFile();
        this.xsdParser.parseXSD(this.fileReader.getInputFile(), xmlToSqlParser);
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
