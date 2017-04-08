/**
 * CreateTable
 * @author Sean Domingo, Michael Frederick, Megan Molumby, Mai Huong Nguyen, Richard Pratt
 */
public class CreateTable extends SQLCommand {
    public String tableName;
    public String[] columnNames;
    public String[] columnTypes;
    
    public CreateTable(String database, String tableName, String[] names, String[] types) {
        super(database);
        this.tableName = tableName;
        this.columnNames = names;
        this.columnTypes = types;
    }
    
    @Override
    public void executeCommand() {
    }
}