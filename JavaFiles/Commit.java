public class Commit extends SQLCommand {
    public Commit(String databaseName) {
        super(databaseName);
    }
    
    @Override
    public void executeCommand() {
        System.out.printf("Changes Commited to database %s%n", this.database);
    }
}