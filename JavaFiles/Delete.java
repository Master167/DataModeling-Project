/**
 * Delete
 * @author Sean Domingo, Michael Frederick, Megan Molumby, Mai Huong Nguyen, Richard Pratt
 */
public class Delete extends SQLCommand {
    public String tableName;
    public String[] whereColumns;
    public String[] whereValues;
    
    public Delete(String database, String tableName, String[] columns, String[] values) {
        super(database);
        this.tableName = tableName;
        this.whereColumns = columns;
        this.whereValues = values;
    }
    
    @Override
    public void executeCommand() {
    }
}
