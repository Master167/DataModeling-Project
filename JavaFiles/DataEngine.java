/**
 * DataEngine - Main Class for the application, Use this to generate our objects in the global scope
 * @author Sean Domingo, Michael Frederick, Megan Molumby, Mai Huong Nguyen, Richard Pratt
 */
import java.util.ArrayList;

public class DataEngine {
    private SQLParser sqlParser;
    private FileReader fileReader;
    private UserInterface userInterface;
    private XMLToSQLGenerator xmlToSqlGenerator;
    private XMLToSQLParser xmlToSqlParser;
    
    public DataEngine() {
        this.sqlParser = new SQLParser();
        this.fileReader = new FileReader();
        this.userInterface = new UserInterface();
        this.xmlToSqlGenerator = new XMLToSQLGenerator();
        this.xmlToSqlParser = new XMLToSQLParser();
    }
    
    /**
     * Actual entry point
     */
    public void runEngine() {
        System.out.println("Running");
        //calls XmlParser and parses given textfile
        xmlToSQLParser();

    }
    public void xmlToSQLParser(){
        fileReader = new FileReader();
        fileReader.setInputFile();
        xmlToSqlParser = new XMLToSQLParser();
        xmlToSqlParser.parse(fileReader.getInputFile());

    }
}
