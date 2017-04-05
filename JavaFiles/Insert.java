/**
 * Insert
 * @author Sean Domingo, Michael Frederick, Megan Molumby, Mai Huong Nguyen, Richard Pratt
 */
public class Insert extends SQLCommand {
    public String tableName;
    public String[] columnNames;
    public String[] columnValues;
    
    public Insert(String database, String tableName, String[] names, String[] values) {
        super(database);
        this.tableName = tableName;
        this.columnNames = names;
        this.columnValues = values;
    }
    
    @Override
    public void executeCommand() {
    }
}
