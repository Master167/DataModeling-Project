/**
 * SQLCommnad
 * @author Sean Domingo, Michael Frederick, Megan Molumby, Mai Huong Nguyen, Richard Pratt
 */
public abstract class SQLCommand {
    public String database;
    
    public SQLCommand(String databaseName) {
        this.database = databaseName;
    }
    
    public String getDatabaseName(){
      return database;
    }
    
    public abstract void executeCommand();
}