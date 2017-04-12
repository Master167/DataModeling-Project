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

Current functions of the Syntax analyzer
-Creates create database object

Issues 
-command line entries with no letter/numbers/symbols
*/
 
import java.io.*;
import java.util.*;
import java.text.*;
import java.lang.*;

public class SQLParser {
   static ArrayList<Token> allTokens = new ArrayList<Token>();
   static ArrayList<Token> finalTokens = new ArrayList<Token>();
   static ArrayList<AttributeDefinitions> allCommandObjects = new ArrayList<AttributeDefinitions>();
   static String[] keywords1 = {"CREATE", "DROP", "SAVE", "LOAD", "INSERT", "INPUT", "DELETE", "TSELECT", "SELECT", "COMMIT", "DATABASE", "TABLE", "INTO", "VALUES", "FROM"};
   static String[] operands = {"*", "[", "]", "(", ")", ";", "|", ","}; 
   static int count = 0;
   
   static String p1 = "NULL";
   static String p2 = "NULL";
   static String p3 = "NULL";
   static String p4 = "NULL";
   static String p5 = "NULL";
   static String p6 = "NULL";
   static String p7 = "NULL";
   static String p8 = "NULL";
   static Boolean p9 = false;
   
   public static void main(String[] args) {
      executeSQLParser();
   }
   
   //executes the parser
   public static void executeSQLParser(){
      String commandLine = "";
      commandLine = getCommand();
      
      Lexical(commandLine);
      Syntax();
   }
   
   public static void Syntax(){
      /*for (Token token: finalTokens) {
         System.out.println(token.getToken());
      }*/
      //if (count < finalTokens.size())
         Commands(finalTokens.get(count).getToken());
   } 
   public static void Commands(String t){
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
         case "DROP":
            //System.out.println(finalTokens.get(count).getToken());
            if (finalTokens.get(count).getToken().equals("DATABASE")) {
               //System.out.println(finalTokens.get(count).getToken());
               p1 = "DROP DATABASE";
               SAVEDROPLOAD();
            }
            else if (finalTokens.get(count).getToken().equals("TABLE")) {
               //System.out.println("CREATE TABLE");
               p1 = "DROP TABLE";
               SAVEDROPLOAD();
            }
            else {
               System.out.println("Error: Invalid command following DROP.");
            }   
         case "SAVE":
            //System.out.println(finalTokens.get(count).getToken());
            if (finalTokens.get(count).getToken().equals("DATABASE")) {
               //System.out.println(finalTokens.get(count).getToken());
               p1 = "SAVE DATABASE";
               SAVEDROPLOAD();
            }
            else {
               System.out.println("Error: Invalid command following SAVE.");
            } 
         case "LOAD":
            //System.out.println(finalTokens.get(count).getToken());
            if (finalTokens.get(count).getToken().equals("DATABASE")) {
               //System.out.println(finalTokens.get(count).getToken());
               p1 = "LOAD DATABASE";
               SAVEDROPLOAD();
            }
            else {
               System.out.println("Error: Invalid command following LOAD.");
            } 
         case "COMMIT":
            //System.out.println(finalTokens.get(count).getToken());
            if (finalTokens.get(count).getToken().equals(";")) {
               //System.out.println(finalTokens.get(count).getToken());
               p1 = "COMMIT";
               AttributeDefinitions s = new AttributeDefinitions(p1, p2, p3, p4, p5, p6, p7, p8, p9);
               allCommandObjects.add(s);  
               System.out.println(allCommandObjects.get(0).getCommandType());
               //System.exit(0);
            }
            else {
               System.out.println("Error: Invalid string following COMMIT.");
            } 
         case "INPUT":
            //System.out.println(finalTokens.get(count).getToken());
            if (finalTokens.get(count).getToken().matches("[a-zA-Z0-9]*")) {
               //System.out.println(finalTokens.get(count).getToken());
               p1 = "INPUT";
               semiColon();
            }
            else {
               System.out.println("Error: Invalid file name following INPUT.");
            } 
         case "INSERT":
         case "DELETE":
         case "TSELECT":
         case "SELECT":
         default:
            //System.out.println("Error: Not a valid start to a command");
      }      
   }
      
   public static void checkIllegalSOC(){
      for (int i = 0; i < keywords1.length; i++) {
         if (finalTokens.get(count).getToken().equals(keywords1[i])){  
            System.out.println("Error: Illegal start of command.");
               System.exit(0);
         }
      }      
   }
   
   public static void SAVEDROPLOAD() {
      if(count+1 < finalTokens.size())
         count++;
      //System.out.println(finalTokens.get(count).getToken());
      if (finalTokens.get(count).getToken().matches("[a-zA-Z0-9]*")) {
         //System.out.println("Its a valid Identifier");
         checkIllegalSOC();
         semiColon();
      }
      else {
         System.out.println("Error: Invalid token following TABLE or DATABASE.");
      }
   }
      
   public static void CREATEDATABASE() {
      if(count+1 < finalTokens.size())
         count++;
      //System.out.println(finalTokens.get(count).getToken());
      if (finalTokens.get(count).getToken().matches("[a-zA-Z0-9]*")) {
         //System.out.println("Its a valid Identifier");
         checkIllegalSOC();
         p2 = finalTokens.get(count).getToken();
         EndCreateDatabaseCommand();
      }
      else {
         System.out.println("Error: Invalid token following DATABASE.");
      }
   }
   public static void semiColon(){
      p2 = finalTokens.get(count).getToken();
      if(count+1 < finalTokens.size())
         count++;
      //System.out.println(finalTokens.get(count).getToken());
      if (finalTokens.get(count).getToken().matches(";")) {
         AttributeDefinitions s = new AttributeDefinitions(p1, p2, p3, p4, p5, p6, p7, p8, p9);
         allCommandObjects.add(s);  
         System.out.println(allCommandObjects.get(0).getCommandType() + " named: " + allCommandObjects.get(0).getStructureName());
         System.exit(0);
      }
      else {
         System.out.println("Error: Expected ';'");
      }
   }

      
      
   public static void CREATETABLE() {
      if(count+1 < finalTokens.size())
         p1 = "CREATE TABLE";
         count++;
         
      //System.out.println(finalTokens.get(count).getToken());
      if (finalTokens.get(count).getToken().matches("[a-zA-Z0-9]*")) {
         //System.out.println("Its a valid Identifier");
         checkIllegalSOC();
         p2 = finalTokens.get(count).getToken();
         firstFieldDef();
      }
      else {
         System.out.println("Error: Invalid token following DATABASE.");
      }
   }

   public static void firstFieldDef() {
      if(count+1 < finalTokens.size())
         count++;
      //System.out.println(finalTokens.get(count).getToken());      
      //assignFieldType();
   }
   
   /*public static void assignFieldType(){
      if(count+1 < finalTokens.size())
         count++; 
         
      //System.out.println(finalTokens.get(count).getToken()); 
      if (finalTokens.get(count).getToken().matches("[a-zA-Z0-9]*")) {
         //System.out.println("Its a valid Identifier");
         checkIllegalSOC();
         p2 = finalTokens.get(count).getToken();
         if(count+1 < finalTokens.size())
            count++; 
         if (finalTokens.get(count).getToken().matches("[a-zA-Z0-9]*"))
         firstFieldDef();
      }
      else {
         System.out.println("Error: Invalid token following DATABASE.");
      }         
   }   */
      
   public static void EndCreateDatabaseCommand(){
      p2 = finalTokens.get(count).getToken();
      if(count+1 < finalTokens.size())
         count++;
         
      //System.out.println(finalTokens.get(count).getToken());
      if (finalTokens.get(count).getToken().equals(";")) {
         p1 = "CREATE DATABASE";
         AttributeDefinitions s = new AttributeDefinitions(p1, p2, p3, p4, p5, p6, p7, p8, p9);
         allCommandObjects.add(s);  
         System.out.println(allCommandObjects.get(0).getCommandType() + " named: " + allCommandObjects.get(0).getStructureName());
         System.exit(0);
      }
      else {
         System.out.println("Error: Invalid input after database name.");
      }          
   }
   
   //gets the input from the scanner
   public static String getCommand(){
      System.out.println("Input command: ");
      Scanner scan = new Scanner(System.in);
      String input = scan.nextLine();
      //need to fix command line entries with no letters/numbers/symbols
      if (input.equals("")){
         System.out.println("Error: Nothing entered.");
         System.exit(0);
      }
      return input;
   }
   //Uses tokenizer to break the string according to whitespaces and makes sure the token 
   //is further broken down to simple form and the keywords are all in a uniform case format
   public static void Lexical(String input) {
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
            for(int i = 0; i < operands.length; i++){
               char c = currentToken.charAt(0);
               boolean a = isOperand(c); 
               if(a != true) {
                  checkFirstChar(currentToken);
               }
            }
            TransToken = convertUpperCase(currentToken);
            Token s = new Token(TransToken);
            finalTokens.add(s);
            //System.out.println(TransToken);
         }
         //System.out.println(token.getToken());
      }
        
   }
   
   public static void checkFirstChar(String t) {
      char c = t.charAt(0);
      String s = Character.toString(c);   
      if(!s.matches("[a-zA-Z]*")) { 
         System.out.println("Error: Invalid ID name or Command.");
         System.exit(0);
      }
   }   
   public static String convertUpperCase(String t) {
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
   public static void seperateOperands(String t){
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
   public static void isLegal(char c) {
      String t = Character.toString(c);
      if(t.matches("[a-zA-Z0-9]*")) {   
         //System.out.println("Matches");
      }
      else{
         System.out.println("Illegal char");
         System.exit(0);
      }
   }
   
   public static boolean isOperand(char c) {
      //System.out.println(c);
      for (int j = 0; j < operands.length; j++){
         if (c == operands[j].charAt(0))
            return true;            
      }
      return false;
   }
}

class Token{
   private String token;
   
   public Token(String token) {
      this.token = token;
   }
   
   public String getToken(){
      return token;
   }
}

class AttributeDefinitions {
    private String commandType;   
    private String structureName;
    private String dataType;
    private String length;
    private String max;
    private String decimal;
    private String dataName;
    private String date;
    private boolean Nullable;
 
    public AttributeDefinitions(String commandType, String structureName, String dataType, String length, String max, String decimal, String dataName, String date, Boolean Nullable){
       this.commandType = commandType;
       this.structureName = structureName;
       this.dataName = dataName;
       this.dataType = dataType;
       this.length = length;
       this.max = max;
       this.decimal = decimal;
       this.date = date;
       this.Nullable = Nullable;
    }
    
    public String getCommandType(){
       return commandType;
    }

    public String getStructureName(){
       return structureName;
    }
    
    public String getDataName(){
       return dataName;
    }
    
    public String getDataType(){
       return dataType;
    }
    
    public String getLength(){
        return length;
    }
    
    public String getMax(){
       return max;
    }    
    
    public String getDecimal(){
        return decimal;
    }
    
    public String getDate(){
       return date;
    }
    
    public Boolean Nullable(){
       return Nullable;
    }
}
