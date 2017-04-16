import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
/**
 * CreateDatabase
 * @author Sean Domingo, Michael Frederick, Megan Molumby, Mai Huong Nguyen, Richard Pratt
 */
public class CreateDatabase extends SQLCommand {
    public CreateDatabase(String databaseName) {
        super(databaseName);
    }
    
    @Override
    public void executeCommand() {
    }
}