/**
 * TSelect - Remember this one can use the implicit time column
 * @author Sean Domingo, Michael Frederick, Megan Molumby, Mai Huong Nguyen, Richard Pratt
 */
public class TSelect extends Select {
    public TSelect(String database, String tableName, String[] names,String[] whereCond) {
        super(database, tableName, names, whereCond);
    }
    
    @Override
    public void executeCommand() {
    }
}