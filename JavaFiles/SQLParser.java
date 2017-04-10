/**
 * SQLParser - This class is to check and generate commands from SQL statements.
 * @author Sean Domingo, Michael Frederick, Megan Molumby, Mai Huong Nguyen, Richard Pratt
 */
import java.io.*;
import java.util.*;
import java.text.*;
import java.lang.*;

public class SQLParser {
   static ArrayList<Token> allTokens = new ArrayList<Token>();
   static ArrayList<Token> finalTokens = new ArrayList<Token>();
   static String[] keywords1 = {"CREATE", "DROP", "SAVE", "LOAD", "INSERT", "INPUT", "DELETE", "TSELECT", "SELECT", "COMMIT", "DATABASE", "TABLE", "INTO", "VALUES", "FROM"};
   static String[] operands = {"*", "[", "]", "(", ")", ";", "|", ","}; 
   
   //executes the parser
   public static void execute(){
      String commandLine = "";
      commandLine = getCommand();
      
      Lexical(commandLine);
      Syntax();
      //execute();
   }
   
   public static void Syntax(){
      for (Token token: finalTokens) {
         System.out.println(token.getToken());
      }
   } 
   
   //gets the input from the scanner
   public static String getCommand(){
      System.out.println("Input command: ");
      Scanner scan = new Scanner(System.in);
      String input = scan.nextLine();
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
            TransToken = convertUpperCase(currentToken);
            Token s = new Token(TransToken);
            finalTokens.add(s);
            //System.out.println(TransToken);
         }
         //System.out.println(token.getToken());
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
