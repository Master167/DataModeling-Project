import java.io.File;

/**
 * FileReader - This class is handles any and all File system Input/Output operations
 * @author Sean Domingo, Michael Frederick, Megan Molumby, Mai Huong Nguyen, Richard Pratt
 */
public class FileReader {
    File inputFile;

    public void setInputFile(){
        //Change later to get input from command line
        inputFile = new File("xmlTestFile.txt");

    }
    public File getInputFile(){
        return inputFile;
    }

}
