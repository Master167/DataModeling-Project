public class Input extends SQLCommand {
    public String fileName;
    
    public Input(String database, String fileName) {
        super(database);
        this.fileName = fileName;
    }
    
    @Override
    public void executeCommand() {
    }
}