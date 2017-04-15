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
    public SQLCommand executeSQLParser(String commandLine){
        Lexical(commandLine);
        Syntax();
        return command;
    }
   
   public void Syntax(){
      /*for (Token token: finalTokens) {
         System.out.println(token.getToken());
      }*/
      //if (count < finalTokens.size())
         Commands(finalTokens.get(count).getToken());
   } 
   public void Commands(String t){
      if(count+1 < finalTokens.size())
         count++;
      
      //System.out.println(t);
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
               System.out.println("Error: Invalid command following CREATE.");
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
               System.out.println("Error: Invalid command following DROP.");
            }

            break;
         case "SAVE":
            if (finalTokens.get(count).getToken().equals("DATABASE")) {
               //System.out.println(finalTokens.get(count).getToken());
               SAVEDROPLOAD("SAVE", "DATABASE");
            }
            else {
               System.out.println("Error: Invalid command following SAVE.");
            } 
            break;
         case "LOAD":
            if (finalTokens.get(count).getToken().equals("DATABASE")) {
               //System.out.println(finalTokens.get(count).getToken());
               SAVEDROPLOAD("LOAD", "DATABASE");
            }
            else {
               System.out.println("Error: Invalid command following LOAD.");
            } 
            break;
         case "COMMIT":
            //System.out.println(finalTokens.get(count).getToken());
            if (finalTokens.get(count).getToken().equals(";")) {
               //System.out.println(finalTokens.get(count).getToken());
               SQLCommand sql = new Commit(databaseName);
               allSQLCommands.add(sql);
            }
            else {
               System.out.println("Error: Invalid string following COMMIT.");
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
               else
                  System.out.println("Error: Expected ';'");
            }
            else {
               System.out.println("Error: Invalid file name following INPUT.");
            } 
            break;
         case "INSERT":
            break;
         case "DELETE":
            break;
         case "TSELECT":
            break;
         case "SELECT":
            break;
         default:
            System.out.println("Error: Not a valid start to a command");
            break;
      }      
   }
   //checks for illegal keywords in the middle of a command
   public void checkIllegalSOC(){
      for (int i = 0; i < keywords1.length; i++) {
         if (finalTokens.get(count).getToken().equals(keywords1[i])){  
            System.out.println("Error: Illegal start of command.");
            System.exit(0);
         }
      }      
   }
   
     public void CREATEDATABASE() {
      if(count+1 < finalTokens.size())
         count++;
      //System.out.println(finalTokens.get(count).getToken());
      if (finalTokens.get(count).getToken().matches("[a-zA-Z0-9]*")) {
         //System.out.println("Its a valid Identifier");
         checkIllegalSOC();
         EndCreateDatabaseCommand(finalTokens.get(count).getToken());
      }
      else {
         System.out.println("Error: Invalid token following DATABASE.");
      }
   }
   
   public void EndCreateDatabaseCommand(String databaseName){
      if(count+1 < finalTokens.size())
         count++;
         
      if (finalTokens.get(count).getToken().equals(";")) {
         SQLCommand sqlCommand = new CreateDatabase(databaseName);
         allSQLCommands.add(sqlCommand);
         //System.out.println(allSQLCommands.get(0).getDatabaseName());
      }
      else {
         System.out.println("Error: Invalid input after database name.");
      }          
   }

        
   public void CREATETABLE() {
      if(count+1 < finalTokens.size())
         count++;
         
      //System.out.println(finalTokens.get(count).getToken());
      if (finalTokens.get(count).getToken().matches("[a-zA-Z0-9]*")) {
         //System.out.println("Its a valid Identifier");
         checkIllegalSOC();
         tableName = finalTokens.get(count).getToken();
         assignFieldDef();
      }
      else {
         System.out.println("Error: Invalid token following TABLE.");
      }
   }
   
   public void assignFieldDef(){
      if(count+1 < finalTokens.size())
         count++; 
      
      int arraySize = countFieldDef();
      //System.out.println(arraySize);
      tCollumnNames = new String[arraySize];
      tCollumnTypes = new String[arraySize][5];
      tNullable = new boolean[arraySize];
      assignFieldName();
   }  
   
   public void assignFieldName(){
      if(count+1 < finalTokens.size())
         count++; 
      
      if (finalTokens.get(count).getToken().matches("[a-zA-Z0-9]*")) {
         //System.out.println("Its a valid Identifier");
         checkIllegalSOC();
         tCollumnNames[arrayCount] = finalTokens.get(count).getToken();
         assignFieldType();       
      }
      else {
         System.out.println("Error: Invalid token following TABLE.");
      }
   }
   
   public void assignFieldType(){
      if(count+1 < finalTokens.size())
         count++; 
      //System.out.println(finalTokens.get(count).getToken()); 
      String t = finalTokens.get(count).getToken();
      switch(t) {
         case "INTEGER":
            tCollumnTypes[arrayCount][0] = t;
            if(count+1 < finalTokens.size())
               count++;
               
            if(finalTokens.get(count).getToken().equals("(")){
               if(count+1 < finalTokens.size())
                  count++;
               else
                  System.out.println("Error: Can not find max length.");
               
               assignTypeInteger();
            }
            else if(finalTokens.get(count).getToken().equals(",")){
               endFieldDef();
            }
            else if(finalTokens.get(count).getToken().equals("NOT")){
               endFieldDef();
            }
            else if(finalTokens.get(count).getToken().equals(")")){
               if(count+1 < finalTokens.size())
                  count++;
         
               //System.out.println(finalTokens.get(count).getToken());
               if (finalTokens.get(count).getToken().matches(";")) {
                  SQLCommand sqlCommand = new CreateTable(databaseName, tableName, tCollumnNames, tCollumnTypes, tNullable);
                  allSQLCommands.add(sqlCommand);
               }
               else {
                  System.out.println("Error: Expected ';'");
               }
            }
            else{
               System.out.println("Error: Invalid entry post dataType declaration");
            }

            break;
         /*case "NUMBER":
            tCollumnTypes[arrayCount] = t;
            if(count+1 < finalTokens.size())
               count++;
               
            if(finalTokens.get(count).getToken().equals("(")){
               if(count+1 < finalTokens.size())
                  count++;
               else
                  System.out.println("Error: Can not find max length.");
               
               assignTypeNumber();
            }
            else if(finalTokens.get(count).getToken().equals(",")){
               endFieldDef();
            }
            else if(finalTokens.get(count).getToken().equals("NOT")){
               endFieldDef();
            }
            else if(finalTokens.get(count).getToken().equals(")")){
               if(count+1 < finalTokens.size())
                  count++;
               endFieldDef();
            }
            else{
               System.out.println("Error: Invalid entry post dataType declaration");
            }

            break;
         case "CHARACTER":
            tCollumnTypes[arrayCount] = t;
            if(count+1 < finalTokens.size())
               count++;
            if(finalTokens.get(count).getToken().equals("(")){
               if(count+1 < finalTokens.size())
                  count++;
               else
                  System.out.println("Error: Can not find max length.");
                  
               if(finalTokens.get(count).getToken().matches("[0-9]*")){
                  p5 = finalTokens.get(count).getToken();
                     if(count+1 < finalTokens.size())
                        count++;
                     else
                        System.out.println("Error: Invalid end to a statement.");
                     
                     if(finalTokens.get(count).getToken().equals(")")) {
                        if(count+1 < finalTokens.size())
                           count++;
                         //System.out.println(finalTokens.get(count).getToken()); 
                        endFieldDef();
                     }
                     else
                        System.out.println("Error: Expected a ')'");
               }
               else{
                  System.out.println("Error: Not a valid numeric value.");
               }
            }
            else{
               System.out.println("Error: Missing '('");
            }
            break;
         case "DATE":
            tCollumnTypes[arrayCount] = t;
            if(count+1 < finalTokens.size())
               count++;
        
            if(finalTokens.get(count).getToken().equals("(")){
               if(count+1 < finalTokens.size())
                  count++;
               else
                  System.out.println("Error: Can not find max length.");
                  
               assignDate();
            }
            else{
               System.out.println("Error: Missing '('");
            }
            break;*/
         default:
            System.out.println("Error: Invalid data type entry.");
            break;
      }
   }

    public void assignTypeInteger(){
      //System.out.println(finalTokens.get(count).getToken());
      if(finalTokens.get(count).getToken().matches("[0-9]*")){
         tCollumnTypes[arrayCount][1] = finalTokens.get(count).getToken();
         if(count+1 < finalTokens.size())
            count++;
         else
            System.out.println("Error: Invalid end to a statement.");
                   
         if(finalTokens.get(count).getToken().equals(")")) {
            if(count+1 < finalTokens.size())
               count++;
            endFieldDef();
         }
         else
            System.out.println("Error: Expected a ')'");
         }
      else{
         System.out.println("Error: Not a valid numeric value.");
      }
   }
   
   public void endFieldDef(){
      //System.out.println(finalTokens.get(count).getToken());   
      if(finalTokens.get(count).getToken().equals("NOT")) {
         if(count+1 < finalTokens.size())
            count++; 
         else
            System.out.println("Error: Invalid end to a statement");
         
         if (finalTokens.get(count).getToken().equals("NULL")){
            notNull = true;
            if(count+1 < finalTokens.size())
               count++; 
            else
               System.out.println("Error: Invalid end to a statement");
            endFieldDef();
         }
         else
            System.out.println("Error: Expected 'NULL'");
      }
      else if(finalTokens.get(count).getToken().equals(")")){
         if(count+1 < finalTokens.size())
            count++;
            
         //System.out.println(finalTokens.get(count).getToken());
         if (finalTokens.get(count).getToken().matches(";")) {
            SQLCommand sqlCommand = new CreateTable(databaseName, tableName, tCollumnNames, tCollumnTypes, tNullable);
            allSQLCommands.add(sqlCommand);
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
         //System.out.println(token.getToken());
         if(token.getToken().equals(",")){
            count++;
         }
      }
      return count;  
   }
   
   public void SAVEDROPLOAD(String Command, String Structure) {
      
      if(count+1 < finalTokens.size())
         count++;
      //System.out.println(finalTokens.get(count).getToken());
      if (finalTokens.get(count).getToken().matches("[a-zA-Z0-9]*")) {
         //System.out.println("Its a valid Identifier");
         checkIllegalSOC();
         endDatabaseCommand(Command, Structure);
      }
      else {
         System.out.println("Error: Invalid token following TABLE or DATABASE.");
      }
   }

    public void endDatabaseCommand(String Command, String Structure){
      if(Structure.equals("DATABASE"))
         databaseName = finalTokens.get(count).getToken();
      else if(Structure.equals("TABLE"))
         tableName = finalTokens.get(count).getToken();
      else
         System.out.println("Error: Expected 'DATABASE' or 'TABLE'");
      if(count+1 < finalTokens.size())
         count++;
      //System.out.println(finalTokens.get(count).getToken());
      if (finalTokens.get(count).getToken().matches(";")) {
         switch(Command){
            case "DROP":
               if(Structure.equals("TABLE")){
                  SQLCommand sqlCommand0 = new DropTable(databaseName, tableName);
                  allSQLCommands.add(sqlCommand0);
               }
               else if (Structure.equals("DATABASE")){
                  SQLCommand sqlCommand1 = new DropDatabase(databaseName);
                  allSQLCommands.add(sqlCommand1);
               }
               else
                  System.out.println("Error: Invalid entry following the command.");
                  
               break;
            case "SAVE":
               SQLCommand sqlCommand2 = new SaveDatabase(databaseName);
               allSQLCommands.add(sqlCommand2);
               break;
            case "LOAD":
               SQLCommand sqlCommand3 = new LoadDatabase(databaseName);
               allSQLCommands.add(sqlCommand3);
               break;
            default:
               System.out.println("Error: No commands.");
         }
      }
      else {
         System.out.println("Error: Expected ';'");
      }
   }

 
//Lexical Analyzer Classes------------------------------------------------------------------------------     
   //Uses tokenizer to break the string according to whitespaces and makes sure the token 
   //is further broken down to simple form and the keywords are all in a uniform case format
   public void Lexical(String input) {
      String currentToken = "";
      StringTokenizer tokenizer = new StringTokenizer(input);
      while(tokenizer.hasMoreElements()) {
         currentToken = tokenizer.nextToken();
         //System.out.println(currentToken);
         seperateOperands(currentToken);
         /*Token t = new Token(currentToken);
         allTokens.add(t);*/
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
   
   public void checkFirstChar(String t) {
      char c = t.charAt(0);
      String s = Character.toString(c);   
      if(!s.matches("[a-zA-Z]*")) { 
         System.out.println("Error: Invalid ID name or Command.");
         System.exit(0);
      }
   }   
   public String convertUpperCase(String t) {
      String returnToken = "";
      for(int i = 0; i < keywords1.length; i++) {            
         if (t.equalsIgnoreCase(keywords1[i])){
            returnToken = t.toUpperCase();
            //System.out.println(returnToken);
            return returnToken;
         }
      }
      //System.out.println(t);
      return t;
   }      
   //seperate IDs from operands and scan for illegal characters   
   public void seperateOperands(String t){
      String buildToken = "";
      char c = ' ';
      boolean a = false;
      
      for (int i = 0; i < t.length(); i++){
         c = t.charAt(i);
         a = isOperand(c);
         //System.out.println(a);
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
   public void isLegal(char c) {
      String t = Character.toString(c);
      if(t.matches("[a-zA-Z0-9]*")) {   
         //System.out.println("Matches");
      }
      else{
         System.out.println("Illegal char");
         System.exit(0);
      }
   }
   
   public boolean isOperand(char c) {
      //System.out.println(c);
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