/**
 * SQLParser - This class is to check and generate commands from SQL statements.
 * @author Sean Domingo, Michael Frederick, Megan Molumby, Mai Huong Nguyen, Richard Pratt
 */
 
/* 
Current functions of the SQL lexical
-Creates Tokens
-Checks for illegal chars such as *, &, !, etc
-Converts keywords into uppercase

PARSER TO DO LIST:
-create table
-create database
-drop table
-drop database
-save database
-load database
-commit
-input
-delete
-select
-tselect
-insert
*/

import java.util.*;

public class SQLParser {
   ArrayList<Token> allTokens;
   ArrayList<Token> finalTokens;
   
   String[] keywords = {"CREATE", "DROP", "SAVE", "LOAD", "INSERT", "INPUT", "DELETE", "TSELECT", "SELECT", "COMMIT", "DATABASE", "TABLE", "INTO", "VALUES", "FROM", "INTEGER", "CHARACTER", "NUMBER", "DATE", "WHERE"};
   String[] operands = {"*", "(", ")", ";", ",", "=", ">", "<", ">=", "<=" }; 
   int count;
   SQLCommand command;
   
    //executes the parser
    public SQLCommand executeSQLParser(String commandLine) throws Exception {
        this.resetParser();
        Lexical(commandLine);
        
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
        count = 0;
        return;
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