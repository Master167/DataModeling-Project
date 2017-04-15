/**
 * SQLParser - This class is to check and generate commands from SQL statements.
 * @author Sean Domingo, Michael Frederick, Megan Molumby, Mai Huong Nguyen, Richard Pratt
 */
 
/* 
Current functions of the SQL lexical
-Creates Tokens
-Checks for illegal chars such as *, &, !, etc
-Converts keywords into uppercase
-Checks for the first char of a non-operand String to be a english letter
-checks for illegal starts of commands

Current functions of the Syntax analyzer
-Creates create database object

works apparently
-CREATES
-drop
-save
-load
-COmmit
-input

need to convert to Michael's object classes
-delete
-seelect
-tselect

need to create
-insert
*/
 
import java.io.*;
import java.util.*;
import java.text.*;
import java.lang.*;

public class SQLParser {
   ArrayList<Token> allTokens = new ArrayList<Token>();
   ArrayList<Token> finalTokens = new ArrayList<Token>();
   
   String[] keywords1 = {"CREATE", "DROP", "SAVE", "LOAD", "INSERT", "INPUT", "DELETE", "TSELECT", "SELECT", "COMMIT", "DATABASE", "TABLE", "INTO", "VALUES", "FROM", "INTEGER", "CHARACTER", "NUMBER", "DATE", "WHERE"};
   String[] operands = {"*", "(", ")", ";", ",", "/"}; 
   int count = 0;
   
   String tableName = "NULL";
   String[] tCollumnNames;
   String[][] tCollumnTypes;
   boolean[] tNullable;
   String databaseName = "NULL";
   int arrayCount = 0;
   boolean notNull = false;
   
   SQLCommand command;
   
    //executes the parser
    public SQLCommand executeSQLParser(String commandLine) throws Exception {
        Lexical(commandLine);
        Syntax();
        return command;
    }
   
    public void Syntax() throws Exception {
        Commands(finalTokens.get(count).getToken());
    } 
   public void Commands(String t) throws Exception {
        if(count+1 < finalTokens.size()) {
            count++;
        }
      
        switch(t){
            case "CREATE":
                //System.out.println(finalTokens.get(count).getToken());
                if (finalTokens.get(count).getToken().equals("DATABASE")) {
                    //System.out.println(finalTokens.get(count).getToken());
                    CREATEDATABASE();
                }
                else if (finalTokens.get(count).getToken().equals("TABLE")) {
                    //System.out.println("CREATE TABLE");
                    CREATETABLE();
                }
                else {
                    throw new Exception("Invalid command following CREATE.");
                }
                break;   
            case "DROP":
                //System.out.println(finalTokens.get(count).getToken());
                if (finalTokens.get(count).getToken().equals("DATABASE")) {
                    //System.out.println(finalTokens.get(count).getToken());
                    SAVEDROPLOAD("DROP", "DATABASE");
                }
                else if (finalTokens.get(count).getToken().equals("TABLE")) {
                    //System.out.println("CREATE TABLE");
                    SAVEDROPLOAD("DROP", "TABLE");
                }
                else {
                    throw new Exception("Invalid command following DROP.");
                }
                break;
            case "SAVE":
                if (finalTokens.get(count).getToken().equals("DATABASE")) {
                    //System.out.println(finalTokens.get(count).getToken());
                    SAVEDROPLOAD("SAVE", "DATABASE");
                }
                else {
                    throw new Exception("Invalid command following SAVE.");
                } 
                break;
            case "LOAD":
                if (finalTokens.get(count).getToken().equals("DATABASE")) {
                    //System.out.println(finalTokens.get(count).getToken());
                    SAVEDROPLOAD("LOAD", "DATABASE");
                }
                else {
                     throw new Exception("Invalid command following LOAD.");
                } 
                break;
            case "COMMIT":
                if (finalTokens.get(count).getToken().equals(";")) {
                    //System.out.println(finalTokens.get(count).getToken());
                    command = new Commit(databaseName);
                }
                else {
                    throw new Exception("Invalid string following COMMIT.");
                }
                break;
            case "INPUT":
                //System.out.println(finalTokens.get(count).getToken());
                if (finalTokens.get(count).getToken().matches("[a-zA-Z0-9]*")) {
                    //System.out.println(finalTokens.get(count).getToken());
                    String fn = finalTokens.get(count).getToken();
                    if(count+1 < finalTokens.size())
                        count++;
                    if (finalTokens.get(count).getToken().matches(";")) {
                        SQLCommand sql2 = new Input(databaseName, fn);
                    }
                    else {
                        throw new Exception("Expected ';'");
                    }
                }
                else {
                    throw new Exception("Invalid file name following INPUT.");
                } 
                break;
            case "INSERT":
                throw new Exception("INSERT NOT IMPLEMENTED");
                break;
            case "DELETE":
                throw new Exception("DELETE NOT IMPLEMENTED");
                break;
            case "TSELECT":
                throw new Exception("TSELECT NOT IMPLEMENTED");
                break;
            case "SELECT":
                throw new Exception("SELECT NOT IMPLEMENTED");
                break;
            default:
                throw new Exception("Not a valid start to a command");
        }      
    }
    //checks for illegal keywords in the middle of a command
    public void checkIllegalSOC() throws Exception {
        for (int i = 0; i < keywords1.length; i++) {
            if (finalTokens.get(count).getToken().equals(keywords1[i])){  
                throw new Exception("Illegal start of command.");
            }
        }      
    }

      public void CREATEDATABASE() throws Exception {
        if(count+1 < finalTokens.size()) {
            count++;
        }
       //System.out.println(finalTokens.get(count).getToken());
        if (finalTokens.get(count).getToken().matches("[a-zA-Z0-9]*")) {
             //System.out.println("Its a valid Identifier");
             checkIllegalSOC();
             EndCreateDatabaseCommand(finalTokens.get(count).getToken());
        }
        else {
            throw new Exception("Invalid token following DATABASE.");
        }
    }

    public void EndCreateDatabaseCommand(String databaseName){
        if(count+1 < finalTokens.size()) {
            count++;
        }

        if (finalTokens.get(count).getToken().equals(";")) {
            command = new CreateDatabase(databaseName);
        }
        else {
            System.out.println("Error: Invalid input after database name.");
        }          
    }


    public void CREATETABLE() throws Exception {
        if(count+1 < finalTokens.size()) {
            count++;
        }

        if (finalTokens.get(count).getToken().matches("[a-zA-Z0-9]*")) {
            checkIllegalSOC();
            tableName = finalTokens.get(count).getToken();
            assignFieldDef();
        }
        else {
            throw new Exception("Invalid token following TABLE.");
        }
    }

    public void assignFieldDef() throws Exception {
        if(count+1 < finalTokens.size()) {
            count++; 
        }

        int arraySize = countFieldDef();
        tCollumnNames = new String[arraySize];
        tCollumnTypes = new String[arraySize][5];
        tNullable = new boolean[arraySize];
        assignFieldName();
    }  

    public void assignFieldName() throws Exception {
        if(count+1 < finalTokens.size()) {
            count++; 
        }

        if (finalTokens.get(count).getToken().matches("[a-zA-Z0-9]*")) {
            //System.out.println("Its a valid Identifier");
            checkIllegalSOC();
            tCollumnNames[arrayCount] = finalTokens.get(count).getToken();
            assignFieldType();       
        }
        else {
            throw new Exception("Invalid token following TABLE.");
        }
    }

    public void assignFieldType() throws Exception {
        if(count+1 < finalTokens.size()) {
           count++;
        }
        String t = finalTokens.get(count).getToken();
        switch(t) {
            case "INTEGER":
                tCollumnTypes[arrayCount][0] = t;
                if(count+1 < finalTokens.size()) {
                    count++;
                }

                if(finalTokens.get(count).getToken().equals("(")){
                    if(count+1 < finalTokens.size()) {
                        count++;
                    }
                    else {
                        throw new Exception("Can not find max length for INTEGER.");
                    }

                    assignTypeInteger();
                }
                else if(finalTokens.get(count).getToken().equals(",")) {
                    endFieldDef();
                }
                else if(finalTokens.get(count).getToken().equals("NOT")){
                    endFieldDef();
                }
                else if(finalTokens.get(count).getToken().equals(")")){
                    if(count+1 < finalTokens.size()) {
                       count++;
                    }

                    if (finalTokens.get(count).getToken().matches(";")) {
                        command = new CreateTable(databaseName, tableName, tCollumnNames, tCollumnTypes, tNullable);
                    }
                    else {
                        throw new Exception("Expected ';'");
                    }
                }
                else{
                    throw new Exception("Invalid entry post dataType declaration");
                }

                break;
            case "NUMBER":
                throw new Exception("Number Datatype not implemented");
                break;
            case "CHARACTER":
                throw new Exception("Character datatype not implemented");
                break;
            case "DATE":
                throw new Exception("Date stattype not implemented");
            default:
                throw new Exception("Invalid data type entry.");
                break;
        }
    }

     public void assignTypeInteger() throws Exception {
        if(finalTokens.get(count).getToken().matches("[0-9]*")){
            tCollumnTypes[arrayCount][1] = finalTokens.get(count).getToken();
            if(count+1 < finalTokens.size()) {
                count++;
            }
            else {
                throw new Exception("Invalid end to a statement.");
            }

            if(finalTokens.get(count).getToken().equals(")")) {
                if(count+1 < finalTokens.size()) {
                   count++;
                }
                endFieldDef();
            }
            else {
                 throw new Exception("Expected a ')'");
            }
        }
        else{
            throw new Exception("Not a valid numeric value.");
        }
    }

    public void endFieldDef() throws Exception {  
        if(finalTokens.get(count).getToken().equals("NOT")) {
            if(count+1 < finalTokens.size()) {
                count++;
            }
            else {
               throw new Exception("Invalid end to a statement");
            }

            if (finalTokens.get(count).getToken().equals("NULL")){
                notNull = true;
                if(count+1 < finalTokens.size()) {
                    count++; 
                }
                else {
                    throw new Exception("Invalid end to a statement");
                }
                endFieldDef();
            }
            else {
                System.out.println("Error: Expected 'NULL'");
            }
        }
        else if(finalTokens.get(count).getToken().equals(")")){
            if(count+1 < finalTokens.size()) {
                count++;
            }

            if (finalTokens.get(count).getToken().matches(";")) {
                command = new CreateTable(databaseName, tableName, tCollumnNames, tCollumnTypes, tNullable);
            }
            else {
                System.out.println("Error: Expected ';'");
            }  
        }
        else if(finalTokens.get(count).getToken().equals(",")){
            arrayCount++;
            assignFieldName();    
        }   
        else {
            System.out.println("Error in endFieldDef function.");
        }                
    }


    public int countFieldDef(){ 
        int count = 1;
        for(Token token: finalTokens){
            if(token.getToken().equals(",")){
                count++;
            }
        }
        return count;  
    }

    public void SAVEDROPLOAD(String Command, String Structure) throws Exception {
        if(count+1 < finalTokens.size()) {
             count++;
        }

        if (finalTokens.get(count).getToken().matches("[a-zA-Z0-9]*")) {
            checkIllegalSOC();
            endDatabaseCommand(Command, Structure);
        }
        else {
            throw new Exception("Invalid token following TABLE or DATABASE.");
        }
    }

     public void endDatabaseCommand(String Command, String Structure) throws Exception {
        if(Structure.equals("DATABASE")) {
            databaseName = finalTokens.get(count).getToken();
        }
        else if(Structure.equals("TABLE"))
           tableName = finalTokens.get(count).getToken();
        else {
           throw new Exception("Expected 'DATABASE' or 'TABLE'");
        }
        if(count+1 < finalTokens.size()) {
           count++;
        }

        if (finalTokens.get(count).getToken().matches(";")) {
            switch(Command){
                case "DROP":
                    if(Structure.equals("TABLE")){
                       command = new DropTable(databaseName, tableName);
                    }
                    else if (Structure.equals("DATABASE")){
                       command = new DropDatabase(databaseName);
                    }
                    else {
                       throw new Exception("Invalid entry following the command.");
                    }
                    break;
                case "SAVE":
                    command = new SaveDatabase(databaseName);
                    break;
                case "LOAD":
                    command = new LoadDatabase(databaseName);
                    break;
                default:
                   throw new Exception("No commands.");
                   break;
            }
        }
        else {
            throw new Exception("Expected ';'");
        }
    }


 //Lexical Analyzer Classes------------------------------------------------------------------------------     
    //Uses tokenizer to break the string according to whitespaces and makes sure the token 
    //is further broken down to simple form and the keywords are all in a uniform case format
    public void Lexical(String input) throws Exception {
        String currentToken = "";
        StringTokenizer tokenizer = new StringTokenizer(input);
        while(tokenizer.hasMoreElements()) {
            currentToken = tokenizer.nextToken();
            seperateOperands(currentToken);
        }

        String TransToken = "";
        for (Token token: allTokens) {
            currentToken = token.getToken();
            if(currentToken != ""){
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
        for(int i = 0; i < keywords1.length; i++) {            
            if (t.equalsIgnoreCase(keywords1[i])){
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