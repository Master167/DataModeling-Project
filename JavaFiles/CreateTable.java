/**
 * CreateTable
 * @author Sean Domingo, Michael Frederick, Megan Molumby, Mai Huong Nguyen, Richard Pratt
 */
public class CreateTable extends SQLCommand {
    public String tableName;
    public String[] columnNames;
    public String[][] columnTypes;
    public boolean[] Nullable;
    
    public CreateTable(String database, String tableName, String[] names, String[][] types, boolean[] nullable) {
        super(database);
        this.tableName = tableName;
        this.columnNames = names;
        this.columnTypes = types;
        this.Nullable = nullable;
    }
    
    @Override
    public void executeCommand() {
    }
}