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
   static String[] keywords1 = {"CREATE", "DROP", "SAVE", "LOAD", "INSERT", "INPUT", "DELETE", "TSELECT", "SELECT", "COMMIT", "DATABASE", "TABLE", "INTO", "VALUES", "FROM", "INTEGER", "CHARACTER", "NUMBER", "DATE"};
   static String[] operands = {"*", "(", ")", ";", ",", "/"}; 
   static int count = 0;
   
   static String p1 = "NULL";
   static String p2 = "NULL";
   static String p3 = "NULL";
   static String p4 = "NULL";
   static String p5 = "NULL";
   static String p6 = "NULL";
   static String p7 = "NULL";
   static String p8 = "NULL";
   static Boolean p9 = true;
   static String p10 = "NULL";
   static String p11 = "NULL";
   
   public static void main(String[] args) {
      executeSQLParser();
   }
   
   //executes the parser
   public static void executeSQLParser(){
      String commandLine = "";
      commandLine = getCommand();
      
      Lexical(commandLine);
      Syntax();
      for (int i = 0; i < allCommandObjects.size(); i++) {
         System.out.println("commandType: " + allCommandObjects.get(i).getCommandType() + 
            " structureName: " + allCommandObjects.get(i).getStructureName() +
            " dataType: " + allCommandObjects.get(i).getDataType() + " length: " + allCommandObjects.get(i).getLength() +
            " max: " + allCommandObjects.get(i).getMax() + " decimal: " + allCommandObjects.get(i).getDecimal() +
            " dataName: " + allCommandObjects.get(i).getDataName() + " date: " + 
            allCommandObjects.get(i).getDate() + " Nullable: " + allCommandObjects.get(i).isNullable()
            + " value: " + allCommandObjects.get(i).getValue());
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
            break;   
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
            break;   
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
            break;
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
            break;
         case "COMMIT":
            //System.out.println(finalTokens.get(count).getToken());
            if (finalTokens.get(count).getToken().equals(";")) {
               //System.out.println(finalTokens.get(count).getToken());
               p1 = "COMMIT";
               AttributeDefinitions s = new AttributeDefinitions(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11);
               allCommandObjects.add(s);  
               resetValues();
            }
            else {
               System.out.println("Error: Invalid string following COMMIT.");
            } 
            break;
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
            break;
         case "INSERT":
            break;
         case "DELETE":
            break;
         case "TSELECT":
            if (finalTokens.get(count).getToken().equals("*")) {
               //System.out.println(finalTokens.get(count).getToken());
               p1 = t;
               p10 = finalTokens.get(count).getToken();
               
               if(count+1 < finalTokens.size())
                  count++;
               else
                  System.out.println("Error: Unexpected end of command.");
               
               if (finalTokens.get(count).getToken().equals("FROM")){
                  if(count+1 < finalTokens.size())
                     count++;
                  else
                     System.out.println("Error: Unexpected end of command.");
                  
                  
                  if (finalTokens.get(count).getToken().matches("[a-zA-Z0-9]*")){
                     p2 = finalTokens.get(count).getToken();
                     if(count+1 < finalTokens.size())
                        count++;
                     else
                        System.out.println("Error: Unexpected end of command.");
                        
                     if(finalTokens.get(count).getToken().equals("WHERE")){
                     }
                     else if(finalTokens.get(count).getToken().equals(";")){
                     }
                     else
                        System.out.println("Error: Issue at TSELECT command.");                     
                  }
                  else
                     System.out.println("Error: Invalid table name.");
               }
               else
                  System.out.println("Error: Expected 'FROM' following '*'");
                  
            }
            else if (finalTokens.get(count).getToken().matches("[a-zA-Z0-9]*")) {
               //System.out.println("CREATE TABLE");
               CREATETABLE();
            }
            else {
               System.out.println("Error: Invalid command following TSELECT.");
            }

            break;
         case "SELECT":
            break;
         default:
            System.out.println("Error: Not a valid start to a command");
            break;
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
         AttributeDefinitions s = new AttributeDefinitions(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11);
         allCommandObjects.add(s);  
         resetValues();
      }
      else {
         System.out.println("Error: Expected ';'");
      }
   }

      
      
   public static void CREATETABLE() {
      p1 = "CREATE TABLE";
      if(count+1 < finalTokens.size())
         count++;
         
      //System.out.println(finalTokens.get(count).getToken());
      if (finalTokens.get(count).getToken().matches("[a-zA-Z0-9]*")) {
         //System.out.println("Its a valid Identifier");
         checkIllegalSOC();
         p2 = finalTokens.get(count).getToken();
         assignFieldDef();
      }
      else {
         System.out.println("Error: Invalid token following TABLE.");
      }
   }

   /*public static void firstFieldDef() {
      //add check for parenthesis
      if(count+1 < finalTokens.size())
         count++;
      //System.out.println(finalTokens.get(count).getToken());      
      //assignFieldType();
   }*/
   
   public static void assignFieldDef(){
      if(count+1 < finalTokens.size())
         count++; 
      
      assignFieldName();   
      //System.out.println(finalTokens.get(count).getToken()); 
      /*if (finalTokens.get(count).getToken().matches("[a-zA-Z0-9]*")) {
         //System.out.println("Its a valid Identifier");
         checkIllegalSOC();
         p2 = finalTokens.get(count).getToken();
         
      }
      else {
         System.out.println("Error: Invalid token following DATABASE.");
      }*/         
   }  
   public static void assignFieldName(){
      if(count+1 < finalTokens.size())
         count++; 
      //System.out.println(finalTokens.get(count).getToken()); 
      if (finalTokens.get(count).getToken().matches("[a-zA-Z0-9]*")) {
         //System.out.println("Its a valid Identifier");
         checkIllegalSOC();
         p7 = finalTokens.get(count).getToken();
         assignFieldType();        
      }
      else {
         System.out.println("Error: Invalid token following DATABASE.");
      }
   }   
   
   public static void assignFieldType(){
      if(count+1 < finalTokens.size())
         count++; 
      //System.out.println(finalTokens.get(count).getToken()); 
      String t = finalTokens.get(count).getToken();
      switch(t) {
         case "INTEGER":
            p3 = t;
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
                  AttributeDefinitions s = new AttributeDefinitions(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11);
                  allCommandObjects.add(s);  
                  resetValues();
               }
               else {
                  System.out.println("Error: Expected ';'");
               }
            }
            else{
               System.out.println("Error: Invalid entry post dataType declaration");
            }

            break;
         case "NUMBER":
            p3 = t;
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
            p3 = t;
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
            p3 = t;
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
            break;
         default:
            System.out.println("Error: Invalid data type entry.");
            break;
      }
   }
   public static void assignDate(){
      String temporaryDate = "";
      //make sure the month is number
      if (finalTokens.get(count).getToken().matches("[0-9]*")){
         //make sure the month value is between 0 and 12
         int month = Integer.valueOf(finalTokens.get(count).getToken());
         if(month > 0 && month < 13){
            temporaryDate += finalTokens.get(count).getToken();
            if(count+1 < finalTokens.size())
               count++;
               
            if (finalTokens.get(count).getToken().equals("/")){
               temporaryDate += finalTokens.get(count).getToken();
               if(count+1 < finalTokens.size())
               count++;
               
               if(finalTokens.get(count).getToken().matches("[0-9]*")){
                  int day = Integer.valueOf(finalTokens.get(count).getToken());
                  if(day > 0 && day < 32){
                     temporaryDate+= finalTokens.get(count).getToken();
                     if(count+1 < finalTokens.size())
                        count++;
                     
                     if (finalTokens.get(count).getToken().equals("/")){
                        temporaryDate += finalTokens.get(count).getToken();
                        if (count+1 < finalTokens.size())
                           count++;
                           
                        if(finalTokens.get(count).getToken().matches("[0-9]*")){
                           if (finalTokens.get(count).getToken().length() == 2 || finalTokens.get(count).getToken().length() == 4){
                              temporaryDate += finalTokens.get(count).getToken();
                              if(count+1 < finalTokens.size())
                                 count++;
                              
                              if(finalTokens.get(count).getToken().equals(")")){
                                 if(count+1 < finalTokens.size())
                                    count++;
                                 p8 = temporaryDate;
                                 endFieldDef();
                              }
                              else
                                 System.out.println("Error: Expected ')'");
                           }
                           else
                              System.out.println("Error: Invalid entry for year.");
                        }
                        else
                           System.out.println("Error: Expected a numeric value for year.");
                     }   
                     else
                        System.out.println("Error: Expected a '/'");
                  }
                  else
                     System.out.println("Error: Invalid numeric range for the day."); 
               }
               else
                  System.out.println("Error: Expected a numeric value for day.");    
            }
            else
               System.out.println("Error: Expected a '/'");
         }
         else
            System.out.println("Error: Invalid entry for month value.");
      }
      else
         System.out.println("Error: Expected a numeric value.");
   }
   
   public static void assignTypeNumber(){
      if(finalTokens.get(count).getToken().matches("[0-9]*")){
         p4 = finalTokens.get(count).getToken();
         if(count+1 < finalTokens.size())
            count++;
         else
            System.out.println("Error: Invalid end to a statement.");
                     
         if(finalTokens.get(count).getToken().equals(")")) {
            if(count+1 < finalTokens.size())
               count++;
            endFieldDef();
         }
         else if(finalTokens.get(count).getToken().equals(",")){
            if(count+1 < finalTokens.size())
               count++;
            else
               System.out.println("Error: Invalid end to a statement.");
            
            //System.out.println(finalTokens.get(count).getToken());
            if(finalTokens.get(count).getToken().matches("[0-9]*")){
               p6 = finalTokens.get(count).getToken();
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
                  System.out.println("Error: Invalid end to a statement.");
            }
            else
               System.out.println("Error: Expected a value for decimal.");
         }
         else 
            System.out.println("Error: Expected a ')'");
         }
      else{
         System.out.println("Error: Not a valid numeric value.");
      }
   }
   public static void assignTypeInteger(){
      if(finalTokens.get(count).getToken().matches("[0-9]*")){
         p5 = finalTokens.get(count).getToken();
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
   public static void endFieldDef(){
      //System.out.println(finalTokens.get(count).getToken());   
      if(finalTokens.get(count).getToken().equals("NOT")) {
         if(count+1 < finalTokens.size())
            count++; 
         else
            System.out.println("Error: Invalid end to a statement");
         
         if (finalTokens.get(count).getToken().equals("NULL")){
            p9 = false;
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
            AttributeDefinitions s = new AttributeDefinitions(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11);
            allCommandObjects.add(s);  
            resetValues();
         }
         else {
            System.out.println("Error: Expected ';'");
         }  
   
      }
      else if(finalTokens.get(count).getToken().equals(",")){
         AttributeDefinitions s = new AttributeDefinitions(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11);
         allCommandObjects.add(s);  
         resetValues();
         assignFieldName();    
      }   
      else {
         System.out.println("Error in endFieldDef function.");
      }                
   }

   public static void EndCreateDatabaseCommand(){
      p2 = finalTokens.get(count).getToken();
      if(count+1 < finalTokens.size())
         count++;
         
      //System.out.println(finalTokens.get(count).getToken());
      if (finalTokens.get(count).getToken().equals(";")) {
         p1 = "CREATE DATABASE";
         AttributeDefinitions s = new AttributeDefinitions(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11);
         allCommandObjects.add(s);  
         resetValues();
      }
      else {
         System.out.println("Error: Invalid input after database name.");
      }          
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
   public static void resetValues(){
      p1 = "NULL";
      p2 = "NULL";
      p3 = "NULL";
      p4 = "NULL";
      p5 = "NULL";
      p6 = "NULL";
      p7 = "NULL";
      p8 = "NULL";
      p9 = true;  
      p10 = "NULL";
      p11 = "NULL";
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
    private String value;
    private String condition;
    
    public AttributeDefinitions(String commandType, String structureName, String dataType, String length, String max, String decimal, String dataName, String date, Boolean Nullable, String value, String condition){
       this.commandType = commandType;
       this.structureName = structureName;
       this.dataName = dataName;
       this.dataType = dataType;
       this.length = length;
       this.max = max;
       this.decimal = decimal;
       this.date = date;
       this.Nullable = Nullable;
       this.value = value;
       this.condition = condition;
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
    
    public Boolean isNullable(){
       return Nullable;
    }
    
    public String getValue(){
       return value;
    }
    
    public String getCondition(){
       return condition;
    }
}
