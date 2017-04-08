/**
 * DropTable
 * @author Sean Domingo, Michael Frederick, Megan Molumby, Mai Huong Nguyen, Richard Pratt
 */
public class DropTable extends SQLCommand {
    public String tableName;
    
    public DropTable(String database, String tableName) {
        super(database);
        this.tableName = tableName;
    }
    
    @Override
    public void executeCommand() {
    }
}
