/**
 * CreateTable
 * @author Sean Domingo, Michael Frederick, Megan Molumby, Mai Huong Nguyen, Richard Pratt
 */
public class CreateTable extends SQLCommand {
    public String tableName;
    public String[] columnNames;
    public String[] columnTypes;
    public String[] columnLengths;
    public boolean[] Nullable;
    
    public CreateTable(String database, String tableName, String[] names, String[] types, String[] lengths, boolean[] nullable) {
        super(database);
        this.tableName = tableName;
        this.columnNames = names;
        this.columnTypes = types;
        this.columnLengths = lengths;
        this.Nullable = nullable;
    }
    
    @Override
    public void executeCommand() {
    }
}