/**
 * UserInterface - This class is a buffer between the application and the User.
 *  All user interactions go through here
 * @author Sean Domingo, Michael Frederick, Megan Molumby, Mai Huong Nguyen, Richard Pratt
 */
import java.util.Scanner;

public class UserInterface {
    private Scanner scanner;
    
    public UserInterface() {
        this.scanner = new Scanner(System.in);
    }
}
