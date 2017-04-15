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
-create database
-drop table
-drop database
-save database
-load database
-commit
-delete
-select
-tselect
-insert
*/

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class SQLParser {
   ArrayList<Token> allTokens;
   ArrayList<Token> finalTokens;

   String[] keywords = {"CREATE", "DROP", "SAVE", "LOAD", "INSERT", "INPUT", "DELETE", "TSELECT", "SELECT", "COMMIT", "DATABASE", "TABLE", "INTO", "VALUES", "FROM", "INTEGER", "CHARACTER", "NUMBER", "DATE", "WHERE"};
   String[] operands = {"*", "(", ")", ";", ",", "=", ">", "<", ">=", "<=" };
   int tokenCount;
   SQLCommand command;

    //executes the parser
    public SQLCommand executeSQLParser(String commandLine) throws Exception {
        this.resetParser();
        this.generateTokens(commandLine);
        this.parseTokens();

        // Check generateTokens
        Token token;
        for (int i = 0; i < this.finalTokens.size(); i++) {
            token = this.finalTokens.get(i);
            System.out.printf("%s, ", token.getToken());
        }
        System.out.printf("%n");
        return command;
    }

    private void resetParser() {
        allTokens = new ArrayList<>();
        finalTokens = new ArrayList<>();
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
            throw new Exception("Invalid ID name or Command.");
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
        throw new Exception("Not implemented");
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
        throw new Exception("Not implemented");
    }

    private void generateDropDatabase() throws Exception {
        throw new Exception("Not implemented");
    }

    private void generateSaveDatabase() throws Exception {
        throw new Exception("Not implemented");
    }

    private void generateLoadDatabase() throws Exception {
        throw new Exception("Not implemented");
    }

    private void generateCommit() throws Exception {
        throw new Exception("Not implemented");
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