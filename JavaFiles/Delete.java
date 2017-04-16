/**
 * Delete
 * @author Sean Domingo, Michael Frederick, Megan Molumby, Mai Huong Nguyen, Richard Pratt
 */
public class Delete extends SQLCommand {
    public String tableName;
    public String[] whereConditional;
    
    public Delete(String database, String tableName, String[] whereConditional) {
        super(database);
        this.tableName = tableName;
        this.whereConditional = whereConditional;
    }
    
    @Override
    public void executeCommand() {
    }
}
