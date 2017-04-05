/**
 * UserInterface - This class is a buffer between the application and the User.
 *  All user interactions go through here
 * @author Sean Domingo, Michael Frederick, Megan Molumby, Mai Huong Nguyen, Richard Pratt
 */
import java.util.NoSuchElementException;
import java.lang.IllegalStateException;
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
            this.showUser("sql> ");
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
    
    public void showDatabaseHelp() {
        
    }
    
    public void showTableHelp(String currentDatabase) {
        
    }
}
