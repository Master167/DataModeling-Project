/**
 * Select - Remember this one cannot use the implicit time column
 * @author Sean Domingo, Michael Frederick, Megan Molumby, Mai Huong Nguyen, Richard Pratt
 */
public class Select extends SQLCommand {
    public String tableName;
    public String[] columnNames;
    public String[] whereColumns;
    public String[] whereValues;
    
    public Select(String database, String tableName, String[] columns, String[] whereColumns, String[] values) {
        super(database);
        this.tableName = tableName;
        this.columnNames = columns;
        this.whereColumns = whereColumns;
        this.whereValues = values;
    }
    
    @Override
    public void executeCommand() {
    }
}