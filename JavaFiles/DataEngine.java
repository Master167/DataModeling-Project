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
    
    /**
     * Actual entry point
     */
    public void runEngine() {
        System.out.println("Running");
    }
}
