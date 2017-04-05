
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


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
    }

    /**
     * Actual entry point
     */
    public void runEngine() {
        //System.out.println("Running");
        //calls Xsd parser to parse file
        //xsdParser();

    }

    public void xsdParser() {
        this.fileReader.setInputFile();
        this.xsdParser.parseXSD(this.fileReader.getInputFile(), xmlToSqlParser);
    }

    private void checkFilesystem() {
        Path path = Paths.get("tables\\");
        if (Files.notExists(path)) {
            // Initialize Tables directory
        }
        
        path = Paths.get("databases\\");
        if (Files.notExists(path)) {
            // Initialize database directory
        }
    }
}
