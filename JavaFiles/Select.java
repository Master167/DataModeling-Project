/**
 * Select - Remember this one cannot use the implicit time column
 * @author Sean Domingo, Michael Frederick, Megan Molumby, Mai Huong Nguyen, Richard Pratt
 */
public class Select extends SQLCommand {
    private String[] columnNames;
    private String tableName;
    private String[] whereCond;
    
    public Select(String database, String tableName, String[] names,String[] whereCond){
        super(database);
        this.tableName = tableName;
        this.columnNames = names;
        this.whereCond = whereCond;
    }
    
    @Override
    public void executeCommand() {
    }
}