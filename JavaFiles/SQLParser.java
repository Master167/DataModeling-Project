/**
 * SQLParser - This class is to check and generate commands from SQL statements.
 * @author Sean Domingo, Michael Frederick, Megan Molumby, Mai Huong Nguyen, Richard Pratt
 */

/*
Current functions of the SQL lexical
-Creates Tokens
-Checks for illegal chars such as *, &, !, etc
-Converts keywords into uppercase

Problem: Multiple character operands !=, >=, <=

PARSER TO DO LIST:
-create table
-delete

WHERE FORMAT: { temp, comparator, value }

-insert
-select
-tselect
*/

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class SQLParser {
   ArrayList<Token> allTokens;
   ArrayList<Token> finalTokens;
   
   ArrayList<String> columnNames;
   ArrayList<String> columnTypes;
   ArrayList<String> columnLength;
   ArrayList<Boolean> columnNullable;

   String[] keywords = {"CREATE", "DROP", "SAVE", "LOAD", "INSERT", "INPUT", "DELETE", "TSELECT", "SELECT", "COMMIT", "DATABASE", "TABLE", "INTO", "VALUES", "FROM", "INTEGER", "CHARACTER", "NUMBER", "DATE", "WHERE"};
   String[] operands = {"*", "(", ")", ";", ",", "/", "=", ">", "<", ">=", "<=" };
   int tokenCount;
   String currentDatabase;
   SQLCommand command;

    //executes the parser
    public SQLCommand executeSQLParser(String commandLine, String currentDatabase) throws Exception {
        this.resetParser();
        this.currentDatabase = currentDatabase;
        this.generateTokens(commandLine);
        /*
        // Check generateTokens
        Token token;
        for (int i = 0; i < this.finalTokens.size(); i++) {
            token = this.finalTokens.get(i);
            System.out.printf("%s, ", token.getToken());
        }
        System.out.printf("%n");
        */
        this.parseTokens();

        return command;
    }

    private void resetParser() {
        allTokens = new ArrayList<>();
        finalTokens = new ArrayList<>();
        columnNames = new ArrayList<>();
        columnTypes = new ArrayList<>();
        columnLength = new ArrayList<>();
        columnNullable = new ArrayList<>();
        tokenCount = 0;
        return;
    }

    //Lexical Analyzer Classes------------------------------------------------------------------------------
    //Uses tokenizer to break the string according to whitespaces and makes sure the token
    //is further broken down to simple form and the keywords are all in a uniform case format
    public void generateTokens(String input) throws Exception {
        String currentToken = "";
        StringTokenizer tokenizer = new StringTokenizer(input);
        while(tokenizer.hasMoreElements()) {
            currentToken = tokenizer.nextToken();
            seperateOperands(currentToken);
        }

        String TransToken = "";
        for (Token token: allTokens) {
            currentToken = token.getToken();
            if(!"".equals(currentToken)){
                //System.out.println(currentToken);
                /*for(int i = 0; i < operands.length; i++){
                   char c = currentToken.charAt(0);
                   boolean a = isOperand(c);
                   if(a != true) {
                      checkFirstChar(currentToken);
                   }
                }*/
                TransToken = convertUpperCase(currentToken);
                Token s = new Token(TransToken);
                finalTokens.add(s);
                //System.out.println(TransToken);
            }
            //System.out.println(token.getToken());
        }

    }

    public void checkFirstChar(String t) throws Exception {
        char c = t.charAt(0);
        String s = Character.toString(c);
        if(!s.matches("[a-zA-Z]*")) {
            throw new Exception(t + " is an Invalid ID name or Command.");
        }
    }

    public String convertUpperCase(String t) {
        String returnToken = "";
        for(int i = 0; i < keywords.length; i++) {
            if (t.equalsIgnoreCase(keywords[i])){
                returnToken = t.toUpperCase();

                return returnToken;
            }
        }

        return t;
    }

    //seperate IDs from operands and scan for illegal characters
    public void seperateOperands(String t) throws Exception {
        String buildToken = "";
        char c = ' ';
        boolean a = false;

        for (int i = 0; i < t.length(); i++){
            c = t.charAt(i);
            a = isOperand(c);

            if (a == true) {
                Token s = new Token(buildToken);
                Token r = new Token(String.valueOf(c));

                allTokens.add(s);
                allTokens.add(r);
                buildToken = "";
             }
             else{
                isLegal(c);
                buildToken += c;
             }
             a = false;
        }
        Token s = new Token(buildToken);
        allTokens.add(s);
    }

    public void isLegal(char c) throws Exception {
        String t = Character.toString(c);
        if(!t.matches("[a-zA-Z0-9]*")) {
            throw new Exception("Illegal char: " + t);
        }
    }

    public boolean isOperand(char c) {
        for (int j = 0; j < operands.length; j++){
            if (c == operands[j].charAt(0))
                return true;
        }
        return false;
    }

    private void parseTokens() throws Exception {
        // Determine which parse path to follow
        // See methods for implementations
        switch (this.finalTokens.get(tokenCount++).getToken()) {
            case "CREATE":
                switch (this.finalTokens.get(tokenCount++).getToken()) {
                    case "TABLE":
                        this.generateCreateTable();
                        break;
                    case "DATABASE":
                        this.generateCreateDatabase();
                        break;
                    default:
                        throw new Exception("Invalid Create command");
                }
                break;
            case "DROP":
                switch (this.finalTokens.get(tokenCount++).getToken()) {
                    case "TABLE":
                        this.generateDropTable();
                        break;
                    case "DATABASE":
                        this.generateDropDatabase();
                        break;
                    default:
                        throw new Exception("Invalid Drop command");
                }
                break;
            case "SAVE":
                this.generateSaveDatabase();
                break;
            case "LOAD":
                this.generateLoadDatabase();
                break;
            case "COMMIT":
                this.generateCommit();
                break;
            case "DELETE":
                this.generateDelete();
                break;
            case "SELECT":
                this.generateSelect();
                break;
            case "TSELECT":
                this.generateTSelect();
                break;
            case "INSERT":
                this.generateInsert();
                break;
            default:
                throw new Exception("Unrecognized Command Entered");
        }
    }

    private void generateCreateTable() throws Exception {
        String tableName = this.finalTokens.get(tokenCount++).getToken();
        this.checkFirstChar(tableName);
        if (this.finalTokens.get(tokenCount++).getToken().equals("(")) {
            this.getColumnDefinition();
            if (this.finalTokens.get(tokenCount++).getToken().equals(")")) {
                if (this.checkEndOfCommand()) {
                    this.command = new CreateTable(this.currentDatabase,
                                                tableName,
                                                this.convertObjectArrayString(this.columnNames.toArray()),
                                                this.convertObjectArrayString(this.columnTypes.toArray()),
                                                this.convertObjectArrayString(this.columnLength.toArray()),
                                                this.convertObjectArrayBoolean(this.columnNullable.toArray())
                                            );
                    /*
                    System.out.printf("Database: %s Table: %s%n", this.currentDatabase, tableName);
                    for (int i = 0; i < this.columnNames.size(); i++) {
                        System.out.printf("%s %s %s %s%n", this.columnNames.get(i), this.columnTypes.get(i), this.columnLength.get(i), this.columnNullable.get(i).toString());
                    }
                    */
                }
                else {
                    this.badEndOfCommand();
                }
            }
            else {
                throw new Exception("Unable to find end of table columns");
            }
        }
        else {
            throw new Exception("Unable to find start of table columns");
        }
    }

    private void generateCreateDatabase() throws Exception {
        // Check if database exists
        String databaseName = this.finalTokens.get(tokenCount++).getToken();
        if (!this.checkIfFileExists("databases\\" + databaseName + ".xml")) {
            if (this.checkEndOfCommand()) {
                this.command = new CreateDatabase(databaseName);
            }
            else {
                this.badEndOfCommand();
            }
        }
        else {
            throw new Exception(databaseName + " database already exists");
        }
    }

    private void generateDropTable() throws Exception {
        String tableName = this.finalTokens.get(tokenCount++).getToken();
        if (this.checkIfFileExists("tables\\" + this.currentDatabase + tableName + ".xml")) {
            if (this.checkEndOfCommand()) {
                this.command = new DropTable(this.currentDatabase, tableName);
            }
            else {
                this.badEndOfCommand();
            }
        }
        else {
            throw new Exception(tableName + " does not exist");
        }
    }

    private void generateDropDatabase() throws Exception {
        String databaseName = this.finalTokens.get(tokenCount++).getToken();
        if (this.checkIfFileExists("databases\\" + databaseName + ".xml")) {
            if (this.checkEndOfCommand()) {
                this.command = new DropDatabase(databaseName);
                this.command.database = "";
            }
            else {
                this.badEndOfCommand();
            }
        }
        else {
            throw new Exception(databaseName + " database does not exist");
        }
    }

    private void generateSaveDatabase() throws Exception {
        String databaseName = this.finalTokens.get(tokenCount++).getToken();
        if (this.currentDatabase.equals(databaseName)) {
            this.command = new SaveDatabase(databaseName);
            this.command.database = "";
        }
        else {
            // They are not in the database they are trying to save
            throw new Exception("Unable to save " + databaseName);
        }
    }

    private void generateLoadDatabase() throws Exception {
        String databaseName = this.finalTokens.get(tokenCount++).getToken();
        if (this.checkIfFileExists("databases\\" + databaseName + ".xml")) {
            if (this.checkEndOfCommand()) {
                this.command = new LoadDatabase(databaseName);
            }
            else {
                this.badEndOfCommand();
            }
        }
        else {
            throw new Exception(databaseName + " does not exist");
        }
    }

    private void generateCommit() throws Exception {
        this.command = new Commit(this.currentDatabase);
    }

    private void generateSelect() throws Exception {
        throw new Exception("Not implemented");
    }

    private void generateTSelect() throws Exception {
        throw new Exception("Not implemented");
    }

    private void generateInsert() throws Exception {
        throw new Exception("Not implemented");
    }

    private void generateDelete() throws Exception {
        throw new Exception("Not implemented");
    }
    
    private boolean checkIfFileExists(String filepath) {
        Path path = Paths.get(filepath);
        boolean a = Files.exists(path);
        return a;
    }
    
    private boolean checkEndOfCommand() {
        return (this.finalTokens.get(tokenCount).getToken().equals(";"));
    }
    
    private void badEndOfCommand() throws Exception {
        throw new Exception("Unable to determine end of command");
    }
    
    private void getColumnDefinition() throws Exception {
        String lengthTemp;
        String temp = this.finalTokens.get(tokenCount++).getToken();
        //Check column name
        this.checkFirstChar(temp);
        this.columnNames.add(temp);
        temp = this.finalTokens.get(tokenCount++).getToken();
        // Check column type
        if (temp.equals("INTEGER") || temp.equals("CHARACTER")) {
            this.columnTypes.add(temp);
            if (this.finalTokens.get(tokenCount++).getToken().equals("(")) {
                temp = this.finalTokens.get(tokenCount++).getToken();
                if (temp.matches("[0-9]*")) {
                    this.columnLength.add(temp);
                }
                else {
                    throw new Exception("Missing Number for column length");
                }
                // Check for closing )
                if (!this.finalTokens.get(tokenCount++).getToken().equals(")")) {
                    throw new Exception("Missing )");
                }
            }
            else {
                throw new Exception("Missing (");
            }
        }
        else if (temp.equals("NUMBER")) {
            
        }
        else if (temp.equals("DATE")) {
            
        }
        else {
            throw new Exception("Unknown Datatype");
        }
        
        // Check for nullable
        temp = this.finalTokens.get(tokenCount).getToken();
        if (temp.equals("NOT")) {
            tokenCount++;
            if (this.finalTokens.get(tokenCount++).getToken().equals("NULL")) {
                this.columnNullable.add(Boolean.FALSE);
            }
            else {
                throw new Exception("Unknown NOT");
            }
        }
        else {
            this.columnNullable.add(Boolean.TRUE);
        }
        
        // Check if , then call this function otherwise return
        if (this.finalTokens.get(tokenCount).getToken().equals(",")) {
            // Only Increment tokenCount if nextToken is a ','
            tokenCount++;
            this.getColumnDefinition();
        }
        return;
    }
    
    private String[] convertObjectArrayString(Object[] objs) {
        String[] strs = new String[objs.length];
        for (int i = 0; i < strs.length; i++) {
            strs[i] = (String)objs[i];
        }
        return strs;
    }
    
    private boolean[] convertObjectArrayBoolean(Object[] objs) {
        boolean[] bools = new boolean[objs.length];
        Object tempObj;
        for (int i = 0; i < bools.length; i++) {
            tempObj = objs[i];
            bools[i] = (Boolean)tempObj;
            
        }
        return bools;
    }

}

//Object Classes ---------------------------------------------------------------------------------------
class Token {
    private String token;

    public Token(String token) {
        this.token = token;
    }

    public String getToken(){
        return token;
    }
}