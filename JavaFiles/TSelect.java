/**
 * TSelect - Remember this one can use the implicit time column
 * @author Sean Domingo, Michael Frederick, Megan Molumby, Mai Huong Nguyen, Richard Pratt
 */
public class TSelect extends Select {
    public TSelect(String database, String tableName, String[] columns, String[] whereColumns, String[] values) {
        super(database, tableName, columns, whereColumns, values);
    }
    
    @Override
    public void executeCommand() {
    }
}