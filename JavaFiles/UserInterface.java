/**
 * UserInterface - This class is a buffer between the application and the User.
 *  All user interactions go through here
 * @author Sean Domingo, Michael Frederick, Megan Molumby, Mai Huong Nguyen, Richard Pratt
 */
import java.util.NoSuchElementException;
import java.util.Scanner;

public class UserInterface {
    private Scanner scanner;
    private int stackCount;
    
    public UserInterface() {
        this.scanner = new Scanner(System.in);
        this.stackCount = 0;
    }
    
    public String getInput() {
        this.stackCount++;
        String input;
        try {
            // Change this if we want to pull by ';'
            this.showUser("sql> ", false);
            input = scanner.nextLine();
        }
        catch (NoSuchElementException | IllegalStateException e) {
            if (this.stackCount < 10) {
                input = this.getInput();
            }
            else {
                // User will get help prompt, because they stupid
                input = "help";
            }
        }
        
        // Reset stackCount
        this.stackCount = 0;
        return input;
    }
    
    public void showUser(String message) {
        System.out.println(message);
    }
    
    public void showUser(String message, boolean newLine) {
        if (newLine) {
            this.showUser(message);
        }
        else {
            System.out.printf(message);
        }
    }
    
    public void showDatabaseHelp() {
        this.showUser("Available Commands");
        this.showUser("CREATE DATABASE database;");
        this.showUser("LOAD DATABASE database;");
        this.showUser("DROP DATABASE database;");
        //this.showUser("SET TIME;");
        this.showUser("HELP");
        this.showUser("EXIT");
    }
    
    public void showTableHelp(String currentDatabase) {
        this.showUser("Available Commands");
        this.showUser("Current Database: " + currentDatabase);
        this.showUser("CREATE TABLE tableName (fieldName fieldType(n)[ NOT NULL], ...);");
        this.showUser("DROP TABLE tableName;");
        this.showUser("INSERT INTO tableName[ (fieldName, ...)] VALUES (fieldValue, ...);");
        this.showUser("CONVERT XML xmlFilename, XSD filename AS sqlFile;");
        this.showUser("INPUT sqlFile;");
        this.showUser("[t]SELECT [*|(field, ...)] FROM tableName [WHERE (field = value)]");
        this.showUser("SAVE DATABASE");
        this.showUser("HELP");
    }
    
    public void showUserError() {
        this.showUser("Invalid or Unrecognized Command. Type 'HELP' if needed");
    }
}
