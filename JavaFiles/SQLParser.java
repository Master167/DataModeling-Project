/**
 * SQLParser - This class is to check and generate commands from SQL statements.
 * @author Sean Domingo, Michael Frederick, Megan Molumby, Mai Huong Nguyen, Richard Pratt
 */

/*
PARSER TO DO LIST:
Implement makeDatatypeCheck
Implement makeColumnCheck
Implement makeTableCheck
Implement isColumnNullable
*/

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SQLParser {
   ArrayList<Token> allTokens;
   ArrayList<Token> finalTokens;
   
   ArrayList<String> columnNames;
   ArrayList<String> columnTypes;
   ArrayList<String> columnLength;
   ArrayList<Boolean> columnNullable;
   String[] whereConditional;

   String[] keywords = {"CREATE", "DROP", "SAVE", "LOAD", "INSERT", "INPUT", "DELETE", "TSELECT", "SELECT", "COMMIT", "DATABASE", "TABLE", "INTO", "VALUES", "FROM", "INTEGER", "CHARACTER", "NUMBER", "DATE", "WHERE"};
   String[] operands = {"*", "(", ")", ";", ",", "/", "=", ">", "<", ">=", "<=" };
   int tokenCount;
   String currentDatabase;
   SQLCommand command;
   
   Document databaseCatalog;

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
            System.out.printf("%s%n", token.getToken());
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
        whereConditional = new String[3];
        databaseCatalog = null;
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
        String returnToken;
       for (String keyword : keywords) {
           if (t.equalsIgnoreCase(keyword)) {
               returnToken = t.toUpperCase();
               
               return returnToken;
           }
       }

        return t;
    }

    //seperate IDs from operands and scan for illegal characters
    public void seperateOperands(String t) throws Exception {
        String buildToken = "";
        Token s;
        Token r;
        char c;
        boolean a;

        for (int i = 0; i < t.length(); i++){
            c = t.charAt(i);
            a = isOperand(c);

            if (a == true) {
                if ((t.length() > i + 1) && t.charAt(i + 1) == '=' && (c == '>' || c == '<' || c == '!')) {
                    s = new Token(Character.toString(c) + Character.toString(t.charAt(i + 1)));
                    i++;
                    allTokens.add(s);
                }
                else {
                    s = new Token(buildToken);
                    r = new Token(String.valueOf(c));

                    allTokens.add(s);
                    allTokens.add(r);
                    buildToken = "";
                }
             }
            else if ((t.length() > i + 1) && t.charAt(i + 1) == '=' && c == '!') {
                s = new Token(Character.toString(c) + Character.toString(t.charAt(i + 1)));
                i++;
                allTokens.add(s);
            }
            else{
                isLegal(c);
                if (c !='\'') {
                    buildToken += c;
                }
             }
             a = false;
        }
        s = new Token(buildToken);
        allTokens.add(s);
    }

    public void isLegal(char c) throws Exception {
        String t = Character.toString(c);
        if(!t.matches("[a-zA-Z0-9]*|'|.|!")) {
            throw new Exception("Illegal char: " + t);
        }
    }

    public boolean isOperand(char c) {
       for (String operand : operands) {
           if (c == operand.charAt(0)) {
               return true;
           }
       }
        return false;
    }
    
    // End Lexical Analzyer Methods---------------------------------------------------------------------------

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
        String databaseName;
        if (this.finalTokens.get(tokenCount++).getToken().equals("DATABASE")) {
            databaseName = this.finalTokens.get(tokenCount++).getToken();
            if (this.currentDatabase.equals(databaseName)) {
                if (this.checkEndOfCommand()) {
                    this.command = new SaveDatabase(databaseName);
                    this.command.database = "";
                }
                else {
                    this.badEndOfCommand();
                }
            }
            else {
                // They are not in the database they are trying to save
                throw new Exception("Unable to save " + databaseName);
            }
        }
        else {
            this.badEndOfCommand();
        }
    }

    private void generateLoadDatabase() throws Exception {
        String databaseName;
        if (this.finalTokens.get(tokenCount++).getToken().equals("DATABASE")) {
            databaseName = this.finalTokens.get(tokenCount++).getToken();
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
        else {
            this.badEndOfCommand();
        }
    }

    private void generateCommit() throws Exception {
        this.command = new Commit(this.currentDatabase);
    }

    private void generateSelect() throws Exception {
        String tableName;
        ArrayList<String> selectedColumns = new ArrayList<>();
        String temp = this.finalTokens.get(tokenCount++).getToken();
        boolean checkIfWhere;
        if (!temp.equals("*")) {
            // Get all the columns and iterate through them
            selectedColumns.add(temp);
            temp = this.finalTokens.get(tokenCount++).getToken();
            while (!temp.equals("FROM") && !temp.equals(";")) {
                if (temp.equals(",")) {
                    temp = this.finalTokens.get(tokenCount++).getToken();
                }
                selectedColumns.add(temp);
                temp = this.finalTokens.get(tokenCount++).getToken();
            }
            if (!temp.equals("FROM")) {
                throw new Exception("No Table selected using FROM");
            }
        }
        else {
            if (!this.finalTokens.get(tokenCount++).getToken().equals("FROM")) {
                throw new Exception("No Table selected using FROM");
            }
        }
        
        temp = this.finalTokens.get(tokenCount++).getToken();
        if (this.makeTableCheck(this.currentDatabase, temp)) {
            tableName = temp;
        }
        else {
            throw new Exception(temp + " does not exists in " + this.currentDatabase + " database");
        }
        
        if (selectedColumns.size() == 0) {
            selectedColumns = this.getTableColumns(this.currentDatabase, tableName);
        }
        
        // Check for where
        if (this.finalTokens.get(tokenCount).getToken().equals("WHERE")) {
            tokenCount++;
            // If so, make whereConditional and check if whereColumn is one of the columns selected
            this.makeWhereConditional(tableName);
            checkIfWhere = false;
            for (int i = 0; i < selectedColumns.size(); i++) {
                if (selectedColumns.get(i).equals(this.whereConditional[0])) {
                    checkIfWhere = true;
                }
            }
            if (checkIfWhere) {
                if (this.checkEndOfCommand()) {
                    this.command = new Select(this.currentDatabase, tableName, this.convertObjectArrayString(selectedColumns.toArray()), this.whereConditional);
                }
                else {
                    this.badEndOfCommand();
                }
            }
            else {
                throw new Exception("Unknown Column in WHERE");
            }
        }
        else {
            if (this.checkEndOfCommand()) {
                this.command = new Select(this.currentDatabase, tableName, this.convertObjectArrayString(selectedColumns.toArray()), null);
            }
            else {
                this.badEndOfCommand();
            }
        }
        
    }

    private void generateTSelect() throws Exception {
        String tableName;
        ArrayList<String> selectedColumns = new ArrayList<>();
        String temp = this.finalTokens.get(tokenCount++).getToken();
        boolean checkIfWhere;
        if (!temp.equals("*")) {
            // Get all the columns and iterate through them
            selectedColumns.add(temp);
            temp = this.finalTokens.get(tokenCount++).getToken();
            while (!temp.equals("FROM") && !temp.equals(";")) {
                if (temp.equals(",")) {
                    temp = this.finalTokens.get(tokenCount++).getToken();
                }
                selectedColumns.add(temp);
                temp = this.finalTokens.get(tokenCount++).getToken();
            }
            if (!temp.equals("FROM")) {
                throw new Exception("No columns Selected");
            }
        }
        else {      
            if (!this.finalTokens.get(tokenCount++).getToken().equals("FROM")) {        
                throw new Exception("No Table selected using FROM");        
            }       
        }
        
        temp = this.finalTokens.get(tokenCount++).getToken();
        if (this.makeTableCheck(this.currentDatabase, temp)) {
            tableName = temp;
        }
        else {
            throw new Exception("No Table selected using FROM");
        }
        
        if (selectedColumns.size() == 0) {
            selectedColumns = this.getTableColumns(this.currentDatabase, tableName);
        }
        
        // Check for where
        if (this.finalTokens.get(tokenCount).getToken().equals("WHERE")) {
            tokenCount++;
            // If so, make whereConditional and check if whereColumn is one of the columns selected
            this.makeWhereConditional(tableName);
            checkIfWhere = false;
            for (int i = 0; i < selectedColumns.size(); i++) {
                if (selectedColumns.get(i).equals(this.whereConditional[0])) {
                    checkIfWhere = true;
                }
            }
            if (checkIfWhere) {
                if (this.checkEndOfCommand()) {
                    this.command = new TSelect(this.currentDatabase, tableName, this.convertObjectArrayString(selectedColumns.toArray()), this.whereConditional);
                }
                else {
                    this.badEndOfCommand();
                }
            }
            else {
                throw new Exception("Unknown Column in WHERE");
            }
        }
        else {
            if (this.checkEndOfCommand()) {
                this.command = new Select(this.currentDatabase, tableName, this.convertObjectArrayString(selectedColumns.toArray()), null);
            }
            else {
                this.badEndOfCommand();
            }
        }
        
    }

    private void generateInsert() throws Exception {
        boolean foundColumn;
        String tableName;
        String temp;
        ArrayList<String> selectedColumns = new ArrayList<>();
        ArrayList<String> values = new ArrayList<>();
        ArrayList<String> tableColumns = new ArrayList<>();
        
        if (!this.finalTokens.get(tokenCount++).getToken().equals("INTO")) {
            throw new Exception("Missing INTO");
        }
        tableName = this.finalTokens.get(tokenCount++).getToken();
        // Check if the table exists in the database
        if (this.makeTableCheck(this.currentDatabase, tableName)) {
            // get Columns
            temp = this.finalTokens.get(tokenCount).getToken();
            if (temp.equals("(")) {
                tokenCount++;
                temp = this.finalTokens.get(tokenCount++).getToken();
                while (!temp.equals("VALUES") && !temp.equals(";") && !temp.equals(")")) {
                    if (temp.equals(",")) {
                        temp = this.finalTokens.get(tokenCount++).getToken();
                    }
                    if (this.makeColumnCheck(this.currentDatabase, tableName, temp)) {
                        selectedColumns.add(temp);
                        temp = this.finalTokens.get(tokenCount++).getToken();
                    }
                    else {
                        throw new Exception(temp + " does not exist in the " + tableName + " table");
                    }
                }
            }
            else {
                selectedColumns = this.getTableColumns(this.currentDatabase, tableName);
            }
            // check if VALUES
            if (this.finalTokens.get(tokenCount++).getToken().equals("VALUES")) {
                temp = this.finalTokens.get(tokenCount).getToken();
                if (temp.equals("(")) {
                    tokenCount++;
                    for (int i = 0; i < selectedColumns.size(); i++) {
                        temp = this.finalTokens.get(tokenCount++).getToken();
                        if (temp.equals(",")) {
                            temp = this.finalTokens.get(tokenCount++).getToken();
                        }
                        if (this.makeDatatypeCheck(this.currentDatabase, tableName, selectedColumns.get(i), temp)) {
                            values.add(temp);
                        }
                        else {
                            throw new Exception("Invalid Input for Column " + selectedColumns.get(i));
                        }
                    }
                    if (!this.finalTokens.get(tokenCount++).getToken().equals(")")) {
                        throw new Exception("Missing )");
                    }
                    if (selectedColumns.size() != values.size()) {
                        throw new Exception("Invalid number of values");
                    }
                }
                else {
                    throw new Exception("Unable to determine start of values");
                }
            }
            else {
                throw new Exception("Missing VALUES");
            }
            
            // Make nullable check
            tableColumns = this.getTableColumns(this.currentDatabase, tableName);
            if (tableColumns.size() > selectedColumns.size()) {
                for (int i = 0; i < tableColumns.size(); i++) {
                    foundColumn = false;
                    for (String selectedColumn : selectedColumns) {
                        if (tableColumns.get(i).equals(selectedColumn)) {
                            foundColumn = true;
                            break;
                        }
                    }
                    if (!foundColumn && !this.isColumnNullable(this.currentDatabase, tableName, tableColumns.get(i))) {
                        throw new Exception("Column " + tableColumns.get(i) + " does not have a value");
                    }
                }
            }
            else if (tableColumns.size() < selectedColumns.size()) {
                throw new Exception("Invalid number of columns");
            }
            
            // Command should be good
            if (this.checkEndOfCommand()) {
                this.command = new Insert(this.currentDatabase, tableName, this.convertObjectArrayString(selectedColumns.toArray()), this.convertObjectArrayString(values.toArray()));
            }
            else {
                this.badEndOfCommand();
            }
        }
        else {
            throw new Exception(tableName + " does not exist in " + this.currentDatabase);
        }
    }

    private void generateDelete() throws Exception {
        String tableName;
        if (this.finalTokens.get(tokenCount++).getToken().equals("FROM")) {
            tableName = this.finalTokens.get(tokenCount++).getToken();
            this.checkFirstChar(tableName);
            if (this.makeTableCheck(this.currentDatabase, tableName)) {
                if (this.finalTokens.get(tokenCount).getToken().equals("WHERE")) {
                    tokenCount++;
                    this.makeWhereConditional(tableName);
                    if (this.checkEndOfCommand()) {
                        this.command = new Delete(this.currentDatabase, tableName, this.whereConditional);
                    }
                    else {
                        this.badEndOfCommand();
                    }
                }
                else {
                    if (this.checkEndOfCommand()) {
                        this.command = new Delete(this.currentDatabase, tableName, null);
                    }
                    else {
                        this.badEndOfCommand();
                    }
                }
            }
            else {
                throw new Exception(tableName + " does not exist in " + this.currentDatabase + " database");
            }
        }
        else {
            throw new Exception("No FROM in Delete");
        }
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
        if (temp.equals("INTEGER")) {
            this.columnTypes.add(temp);
            if (this.finalTokens.get(tokenCount).getToken().equals("(")) {
                tokenCount++;
                temp = this.finalTokens.get(tokenCount++).getToken();
                if (temp.matches("[0-9]+")) {
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
                this.columnLength.add("");
            }
        }
        else if (temp.equals("CHARACTER")) {
            this.columnTypes.add(temp);
            if (this.finalTokens.get(tokenCount++).getToken().equals("(")) {
                temp = this.finalTokens.get(tokenCount++).getToken();
                if (temp.matches("[0-9]+")) {
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
            this.columnTypes.add(temp);
            if (this.finalTokens.get(tokenCount).getToken().equals("(")) {
                tokenCount++;
                temp = this.finalTokens.get(tokenCount).getToken();
                if (temp.matches("[0-9]+")) {
                    tokenCount++;
                    lengthTemp = temp;
                    if (this.finalTokens.get(tokenCount).getToken().equals(",")) {
                        tokenCount++;
                        lengthTemp += ",";
                        temp = this.finalTokens.get(tokenCount++).getToken();
                        if (temp.matches("[0-9]+")) {
                            this.columnLength.add(lengthTemp + temp);
                        }
                        else {
                            throw new Exception("Missing Number for column length");
                        }
                    }
                    else {
                        this.columnLength.add(lengthTemp);
                    }
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
                this.columnLength.add("");
            }
        }
        else if (temp.equals("DATE")) {
            this.columnTypes.add(temp);
            if (this.finalTokens.get(tokenCount++).getToken().equals("(")) {
                // Check Each token of the Date Datatype
                temp = this.finalTokens.get(tokenCount++).getToken();
                if (temp.equals("mm")) {
                    temp = this.finalTokens.get(tokenCount++).getToken();
                    if (temp.equals("/")) {
                        temp = this.finalTokens.get(tokenCount++).getToken();
                        if (temp.equals("dd")) {
                            temp = this.finalTokens.get(tokenCount++).getToken();
                            if (temp.equals("/")) {
                                temp = this.finalTokens.get(tokenCount++).getToken();
                                if (temp.equals("yy")) {
                                    this.columnLength.add("mm/dd/yy");
                                }
                                else if (temp.equals("yyyy")) {
                                    this.columnLength.add("mm/dd/yyyy");
                                }
                                else {
                                    throw new Exception("Invalid DATE definition");
                                }
                            }
                            else {
                                throw new Exception("Invalid DATE definition");
                            }
                        }
                        else {
                            throw new Exception("Invalid DATE definition");
                        }
                    }
                    else {
                        throw new Exception("Invalid DATE definition");
                    }
                }
                else {
                    throw new Exception("Invalid DATE definition");
                }
            }
            else {
                throw new Exception("Missing (");
            }
            // Check for closing )
            if (!this.finalTokens.get(tokenCount++).getToken().equals(")")) {
                throw new Exception("Missing )");
            }
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
    
    // Just a strach board for now.
    private boolean makeDatatypeCheck(String database, String table, String column, String value) throws Exception {
        boolean dataTypeCorrect = false;
        boolean columnDataTypeFound = false;
        String columnDataType = "";
        String columnLength = "";
        int columnDecimal = 0;
        int valueLength;
        int valueDecimalLength;
        Document catalog;
        Node tempNode;
        Element columnElement;
        Element tempElement;
        
        // Get Database Catalog and column Type.
        if (this.makeColumnCheck(database, table, column)) {
            catalog = this.getDatabase(database);
            tempNode = catalog.getFirstChild();
            // Find the right table
            for (Node tableNode = tempNode.getFirstChild(); (tableNode != null) && !columnDataTypeFound; tableNode = tableNode.getNextSibling()) {
                if (tableNode.getNodeType() == Node.ELEMENT_NODE && tableNode.getNodeName().equals(table)) {
                    // Find the right Column
                    for (Node columnNode = tableNode.getFirstChild(); (columnNode != null) && !columnDataTypeFound; columnNode = columnNode.getNextSibling()) {
                        // Get the info about the column
                        if (columnNode.getNodeType() == Node.ELEMENT_NODE && columnNode.getNodeName().equals(column)) {
                            columnElement = (Element) columnNode;
                            tempNode = columnElement.getElementsByTagName("type").item(0);
                            columnDataType = tempNode.getTextContent();
                            if (columnDataType.equalsIgnoreCase("CHARACTER") || columnDataType.equalsIgnoreCase("INTEGER") || columnDataType.equalsIgnoreCase("DATE") || columnDataType.equalsIgnoreCase("TIME")) {
                                tempNode = columnElement.getElementsByTagName("length").item(0);
                                columnLength = tempNode.getTextContent();
                                columnDataTypeFound = true;
                            }
                            else if (columnDataType.equalsIgnoreCase("NUMBER")) {
                                tempNode = columnElement.getElementsByTagName("length").item(0);
                                tempElement = (Element) tempNode;
                                columnLength = tempElement.getElementsByTagName("digits").item(0).getTextContent();
                                // Check for empty decimal
                                if (tempElement.getElementsByTagName("decimals").item(0).getTextContent().length() >= 1) {
                                    columnDecimal = Integer.parseInt(tempElement.getElementsByTagName("decimals").item(0).getTextContent());
                                }
                                else {
                                    columnDecimal = 0;
                                }
                                columnDataTypeFound = true;
                            }
                        }
                    }
                }
            }
            
            // Check if we found the column datatype
            if (columnDataTypeFound) {
                // Check Type of value is compatible with columnDatatype
                if (columnDataType.equalsIgnoreCase("INTEGER")) {
                    if (value.matches("[0-9]+")) {
                        if (value.length() <= Integer.parseInt(columnLength)) {
                            dataTypeCorrect = true;
                        }
                    }
                }
                else if (columnDataType.equalsIgnoreCase("NUMBER")) {
                    // Check if value has decimal
                    if (value.matches("[0-9]+\\.[0-9]+")) {
                        valueDecimalLength = value.replaceAll("[0-9]+\\.", "").length();
                        valueLength = value.replaceAll("\\.[0-9]+", "").length() + valueDecimalLength;
                        if (valueLength <= Integer.parseInt(columnLength) && valueDecimalLength <= columnDecimal) {
                            dataTypeCorrect = true;
                        }
                    }
                    // Ok no decimal, does the column need a decimal?
                    else if (columnDecimal == 0 && value.matches("[0-9]+")) {
                        if (value.length() <= Integer.parseInt(columnLength)) {
                            dataTypeCorrect = true;
                        }
                    }
                }
                else if (columnDataType.equalsIgnoreCase("CHARACTER")) {
                    if (value.length() <= Integer.parseInt(columnLength)) {
                        dataTypeCorrect = true;
                    }
                }
                else if (columnDataType.equalsIgnoreCase("DATE")) {
                    // Get the rest of the date
                    for (int i = 0; i < 4; i++) {
                        value += this.finalTokens.get(tokenCount++).getToken();
                    }
                    // Determine Java dateformat
                    String dateFormat;
                    if (columnLength.length() > "mm/dd/yy".length()) {
                        dateFormat = "MM/dd/yyyy";
                    }
                    else {
                        dateFormat = "MM/dd/yy";
                    }
                    // Simliar to: http://stackoverflow.com/questions/20231539/java-check-the-date-format-of-current-string-is-according-to-required-format-or
                    Date date;
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
                        date = sdf.parse(value);
                        if (!value.equals(sdf.format(date))) {
                            date = null;
                        }
                    } catch (ParseException e) {
                        date = null;
                    }
                    dataTypeCorrect = (date != null);
                }
                else if (columnDataType.equalsIgnoreCase("TIME")) {
                    // Get the rest of the date
                    for (int i = 0; i < 4; i++) {
                        value += this.finalTokens.get(tokenCount++).getToken();
                    }
                    value += " " + this.finalTokens.get(tokenCount++).getToken();
                    // Determine Java dateformat
                    String dateFormat = "MM/dd/yyyy HH:mm:ss";
                    // Simliar to: http://stackoverflow.com/questions/20231539/java-check-the-date-format-of-current-string-is-according-to-required-format-or
                    Date date;
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
                        date = sdf.parse(value);
                        if (!value.equals(sdf.format(date))) {
                            date = null;
                        }
                    } catch (ParseException e) {
                        date = null;
                    }
                    dataTypeCorrect = (date != null);
                }
            }
        }
        return dataTypeCorrect;
    }
    
    // Just a strach board for now.
    private boolean makeColumnCheck(String database, String table, String column) throws Exception {
        boolean columnExists = false;
        Document catalog;
        Element tableElement;
        Element columnElement;
        Node tempNode;
        if (makeTableCheck(database, table)) {
            // Get database catalog
            catalog = this.getDatabase(database);
            // Working on the tables level
            tempNode = catalog.getFirstChild();
            for (Node tableNode = tempNode.getFirstChild(); (tableNode != null) && !columnExists; tableNode = tableNode.getNextSibling()) {
                if (tableNode.getNodeType() == Node.ELEMENT_NODE) {
                    //Check table name
                    tableElement = (Element) tableNode;
                    if (tableElement.getNodeName().equals(table)) {
                        // Now the Column Level
                        for (Node columnNode = tableNode.getFirstChild(); (columnNode != null) && !columnExists; columnNode = columnNode.getNextSibling()) {
                            // Check if column exists in this table
                            if (columnNode.getNodeType() == Node.ELEMENT_NODE) {
                                columnElement = (Element) columnNode;
                                if (columnElement.getNodeName().equals(column)) {
                                    columnExists = true;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
             
        }
        return columnExists;
    }

    private boolean makeTableCheck(String database, String table) throws Exception {
        boolean tableExists = false;
        // Get database catalog
        Document catalog = this.getDatabase(database);
        // Check if table exists in catalog
        Node tempNode = catalog.getFirstChild();
        NodeList childList;
        Element tempElement;
        // Check first child
        if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
            tempElement = (Element) tempNode;
            if (tempElement.hasChildNodes()) {
                childList = tempElement.getElementsByTagName(table);
                if (childList.getLength() == 1) {
                    tableExists = true;
                }
            }
        }
        while (tempNode.getNextSibling() != null && !tableExists) {
            tempNode = tempNode.getNextSibling();
            if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
                tempElement = (Element) tempNode;
                if (tempElement.hasChildNodes()) {
                    childList = tempElement.getElementsByTagName(table);
                    if (childList.getLength() == 1) {
                        tableExists = true;
                        break;
                    }
                }
            }
        }
        return tableExists;
    }
    
    private void makeWhereConditional(String tableName) throws Exception {
        String column;
        String conditional;
        String value;

        column = this.finalTokens.get(tokenCount++).getToken();
        if (this.makeColumnCheck(this.currentDatabase, tableName, column)) {
            conditional = this.finalTokens.get(tokenCount++).getToken();
            if (conditional.equals("=") || conditional.equals(">=") || conditional.equals("<=") || conditional.equals("!=") || conditional.equals(">") || conditional.equals("<")) {
                value = this.finalTokens.get(tokenCount++).getToken();
                if (this.makeDatatypeCheck(this.currentDatabase, tableName, column, value)) {
                    this.whereConditional[0] = column;
                    this.whereConditional[1] = conditional;
                    this.whereConditional[2] = value;
                }
                else {
                    throw new Exception("Invalid Datatype in WHERE");
                }
            }
            else {
                throw new Exception("Invalid conditional");
            }
        }
        else {
            throw new Exception(column + " does not exist in " + tableName + " table");
        }
    }
    
    // DEFINE ME MORE
    private ArrayList<String> getTableColumns(String database, String table) throws Exception {
        ArrayList<String> array = new ArrayList<>();
        array.add("name");
        array.add("wage");
        array.add("something");
        array.add("else");
        return array;
    }
    
    private boolean isColumnNullable(String databaseName, String tableName, String column) throws Exception {
        // Return true if the column is nullable
        return true;
    }
    
    // Checks if catalog has already been loaded, if not builds it.
    private Document getDatabase(String database) throws Exception {
        if (this.databaseCatalog == null) {
            String databaseLocation = "databases\\" + database + ".xml";
            DOMUtility util = new DOMUtility();
            Document doc = util.XMLtoDOM(new File(databaseLocation));
            this.databaseCatalog = doc;
        }
        return this.databaseCatalog;
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