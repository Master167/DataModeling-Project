/**
 *
 * @author Michael
 */
public abstract class SQLCommand {
    public String database;
    
    public SQLCommand(String databaseName) {
        this.database = databaseName;
    }
    
    public abstract void executeCommand();
}
