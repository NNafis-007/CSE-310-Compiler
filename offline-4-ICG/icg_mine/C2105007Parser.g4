parser grammar C2105007Parser;

options {
	tokenVocab = C2105007Lexer;
}

@header {
import java.io.BufferedWriter;
import java.io.IOException;
import SymbolTable.SymbolInfo;
import java.util.ArrayList;
}

@members {

    // helper to write into parserLogFile
    public boolean isGlobal = true;
    public int stackOffset = 0;
    public int labelCount = 0;
    
    

    public String getLabel(){
        labelCount++;
        String label = "L" + labelCount;
        return label;
    }

    public String getAsmRelop(String relop) {
      //'<=' | '==' | '>=' | '>' | '<' | '!='
        switch (relop) {
            case "<":
                return "JL";
            case "<=":
                return "JLE";
            case ">":
                return "JG";
            case ">=":
                return "JGE";
            case "==":
                return "JE";
            case "!=":
                return "JNE";
            default:
                return "";
        }
    }

    // Write grammar matching Log   
    void logParse(String message) {
        try {
            Main.parserLogFile.write(message);
            Main.parserLogFile.newLine();
            Main.parserLogFile.newLine();
            Main.parserLogFile.flush();
        } catch (IOException e) {
            System.err.println("Parser log error: " + e.getMessage());
        }
    }

    // helper to write into Main.errorFile
    void logErr(String message) {
        try {
            Main.errorFile.write(message);
            Main.errorFile.newLine();
            Main.errorFile.flush();
        } catch (IOException e) {
            System.err.println("Error file write error: " + e.getMessage());
        }
    }

    // Write assembly
    void writeCode(String message){
        try {
            Main.ICGFile.write(message);
            Main.ICGFile.newLine();
            Main.ICGFile.flush();
        } catch (IOException e) {
            System.err.println("Error assembly code write error: " + e.getMessage());
        }
    }

    // Write temporary code (only code segment)
    void writeTempCode(String message){
        try {
            Main.tempFile.write(message);
            Main.tempFile.newLine();
            Main.tempFile.flush();
        } catch (IOException e) {
            System.err.println("Temp file write error: " + e.getMessage());
        }
    }

    void printInAsm(String id, int lineNo) {
      // MOV AX, i       ; Line 7
    	// CALL print_output
	    // CALL new_line
      String asmCode = "\tMOV AX, " + id + "\t\t; Line " + lineNo + "\n" +
                       "\tCALL print_output\n" +
                       "\tCALL new_line\n";
      writeTempCode(asmCode);
    }

    String getAsmVar(String varName){
      SymbolInfo varInfo = STlookup(varName);
        if(varInfo.getScope() == "global"){
          return varInfo.getName();
        }
        else{
          int offset = varInfo.getOffset();
          String op = (offset < 0) ? "+" : "-";

          String cmd = "[BP" + op + Math.abs(offset) + "]";
          return cmd;
        }
    }

    public int getOffset(){
        stackOffset += 2;
        return stackOffset;
    }

    public int getOffset(int noBytes){
        int startOffset = stackOffset + 2;
        stackOffset += 2 * (noBytes);
        return startOffset;
    }

    boolean STinsert(String name, String token_type, 
        String data_type, ArrayList<String> params, 
        boolean isFunc, int offset) {
        try{

            SymbolInfo s1 = new SymbolInfo(name, token_type, data_type, params);
            if(isFunc) {
                s1.setIsFunction(true);
            }
            s1.setOffset(offset);
            boolean isInserted = Main.st.insertSymbol(s1);

            return isInserted;
            
        } catch(Exception e) {
            System.err.println("Symbol Table insert error : " + e.getMessage());
            return false;
        }
    }

    SymbolInfo STlookup(String name) {
        try {
            SymbolInfo s = new SymbolInfo(name, "local", "int", null);
            SymbolInfo foundSymbol = Main.st.lookUp(s);
            if (foundSymbol != null) {
                return foundSymbol;
            } else {
                System.err.println("Symbol not found in Symbol Table: " + s.getName());
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error looking up symbol: " + e.getMessage());
            return null;
        }
    }

    void enterScope(){
        try{
            Main.st.enterScope();
        } catch(Exception e) {
            System.err.println("Symbol Table ENTER scope error : " + e.getMessage());
        }
    }

    void exitScope(){
        try{
            Main.st.exitScope();
        } catch(Exception e) {
         System.err.println("Symbol Table EXIT scope error : " + e.getMessage());
        }
    }   

    // boolean STlookupCurrScope(String name){
    //     try{
    //         //currentScopeLookup
    //         SymbolInfo2 s = new SymbolInfo2(name, "ID", null, null);
    //         SymbolInfo2 found = Main.st.currentScopeLookup(s);
    //         //
    //         if(found == null){
    //             return false;
    //         }
    //         return true;
    //     } catch(Exception e) {
    //         System.err.println("Symbol Table lookup error : " + e.getMessage());
    //         return false;
    //     }
    // }


    
}

start:
	program {
        logParse(
            "Parsing completed successfully with "
            + Main.syntaxErrorCount
            + " syntax errors."
        );
        Main.st.printAllScopeTable();
      };

program:
	program unit {
        logParse("program : program unit");
      }
	| unit {
        logParse("program : unit");
      };

unit:
	var_declaration {
        logParse("unit : var_declaration");
      }
	| func_declaration {
        logParse("unit : func_declaration");
      }
	| func_definition {
        logParse("unit : func_definition");
      };

func_declaration:
	type_specifier ID LPAREN parameter_list RPAREN SEMICOLON {
        int lineNo = $SEMICOLON.getLine();
        logParse("Line No : " + lineNo + " func_declaration : type_specifier ID LPAREN parameter_list RPAREN SEMICOLON");
      }
	| type_specifier ID LPAREN RPAREN SEMICOLON {
        int lineNo = $SEMICOLON.getLine();
        logParse("Line No : " + lineNo + " func_declaration : type_specifier ID LPAREN RPAREN SEMICOLON");
      };

func_definition
	returns[String fn_name]:
	type_specifier ID {
        isGlobal=false;
        $fn_name = $ID.getText();
        System.out.println("Disabling global scope, ENTERING function " + $fn_name);
        enterScope(); // Enter function scope

        String asmCode = $ID.getText() + " PROC\n" + "\tPUSH BP\n" + "\tMOV BP, SP\n";
        writeTempCode(asmCode);
        int prevStackOffset = stackOffset; // Save current stack offset
        stackOffset = 2;
      } LPAREN parameter_list 
        RPAREN
          {
            String funcName = $ID.getText();            
          } 
        compound_statement {
        int lineNo = $ID.getLine();
        logParse("Line No : " + lineNo + " (" + $fn_name + ") func_definition  : type_specifier ID LPAREN parameter_list RPAREN compound_statement");

        isGlobal = true; // Reset isGlobal for next function
        System.out.println("Enabling global scope, EXITING function " + $fn_name);
        Main.st.printAllScopeTable(); // Print scope table for function
        exitScope(); // Exit function scope
        
        int retBytes = stackOffset / 2; // Calculate return bytes
        String retBytesStr = (retBytes > 0)? String.valueOf(retBytes) : "";
        stackOffset = prevStackOffset; // Restore previous stack offset
        String cleanFuncCode = "\tPOP BP\n" + "\tRET " + retBytesStr + "\n"    
              + $fn_name + " ENDP\n";
        writeTempCode(cleanFuncCode);
      }
	| type_specifier ID {
        isGlobal=false;
        $fn_name = $ID.getText();
        System.out.println("Disabling global scope, ENTERING function " + $fn_name);
        enterScope(); // Enter function scope

        String asmCode = $ID.getText() + " PROC\n";
        if($fn_name.equals("main")){
            asmCode += "\tMOV AX, @DATA\n" + "\tMOV DS, AX\n"; // Initialize data segment for main
        }
        asmCode += "\tPUSH BP\n" + "\tMOV BP, SP\n";
        writeTempCode(asmCode);
      } LPAREN RPAREN compound_statement {
        int lineNo = $ID.getLine();
        logParse("Line No : " + lineNo + " (" + $fn_name + ") func_definition  : type_specifier ID LPAREN RPAREN compound_statement");
        isGlobal = true; // Reset isGlobal for next function
        System.out.println("Enabling global scope, EXITING function " + $fn_name);
        Main.st.printAllScopeTable(); // Print scope table for function
        exitScope(); // Exit function scope

        if($fn_name.equals("main")){
          // Exit program for main
            String cleanMainCode = "\n\tPOP BP\n" + "\tMOV AX, 4Ch\n" 
                      + "\tINT 21h\n" + "main ENDP"; 
            writeTempCode(cleanMainCode);
        }
        else{
          String cleanFuncCode = "\n\tPOP BP\n" + "\tRET\n" + $fn_name + " ENDP\n";
          writeTempCode(cleanFuncCode);
        }

      };

parameter_list returns [int num_params]:
	pl=parameter_list COMMA type_specifier ID {
        int lineNo = $ID.getLine();
        logParse("Line No : " + lineNo + " parameter_list : parameter_list COMMA type_specifier ID");
        $num_params = $pl.num_params + 1; // Count this parameter
        int offset = -1 * getOffset();
        boolean isInserted = STinsert($ID.getText(), "local", "int", null, false, offset);
      }
	| parameter_list COMMA type_specifier {
        int lineNo = $COMMA.getLine();
        logParse("Line No : " + lineNo + " parameter_list : parameter_list COMMA type_specifier");
      }

	| type_specifier ID 
      {
        int lineNo = $ID.getLine();
        logParse("Line No : " + lineNo + " parameter_list : type_specifier ID");
        $num_params = 1; // Count this parameter
        // insert into ST
        int offset = -1 * getOffset();
        boolean isInserted = STinsert($ID.getText(), "local", "int", null, false, offset);
      }

	| type_specifier {
        logParse("parameter_list : type_specifier");
      };

compound_statement:
	LCURL statements RCURL {
        // func only defined globally

        int lineNo = $LCURL.getLine();
        logParse("Line No : " + lineNo + " compound_statement : LCURL statements RCURL");
      }
	| LCURL RCURL {
        int lineNo = $LCURL.getLine();
        logParse("Line No : " + lineNo + " compound_statement : LCURL RCURL");
      };

var_declaration:
	t = type_specifier dl = declaration_list sm = SEMICOLON {
        int lineNo = $sm.getLine();
        logParse("Line No : " + lineNo + " var_declaration : type_specifier dec_list SEMICOLON");
      }
	| t = type_specifier de = declaration_list_err sm = SEMICOLON {
        // logErr(
        //     "Line# "
        //     + $sm.getLine()
        //     + " with error name: "
        //     + $de.error_name
        //     + " - Syntax error at declaration list of variable declaration"
        // );
        Main.syntaxErrorCount++;
      };

declaration_list_err
	returns[String error_name]: { $error_name = "Error in declaration list"; };

type_specifier
	returns[String name_line]:
	INT {
        $name_line = "Line No : " + $INT.getLine() + " type_specifier : INT";
        logParse($name_line);
      }
	| FLOAT {
        $name_line = "Line No : " + $FLOAT.getLine() + " type_specifier : FLOAT";
        logParse($name_line);
      }
	| VOID {
        $name_line = "Line No : " + $VOID.getLine() + " type_specifier : VOID";
        logParse($name_line);
      };

declaration_list:
	declaration_list COMMA ID {
        int lineNo = $ID.getLine();
        logParse("Line No : " + lineNo + " declaration_list : declaration_list COMMA ID");
        if(!isGlobal){
            int offset = getOffset();
            logParse("----- " + $ID.getText() + ", offest : " + offset + " ----");
            boolean isInserted = STinsert($ID.getText(), "local", "int", null, false, offset);
            writeTempCode("\tSUB SP , " + 2);
        }
        else{
            String asmCode = "\t" + $ID.getText() + " DW 1 DUP (0000h)";
            writeCode(asmCode);
            boolean isInserted = STinsert($ID.getText(), "global", "int", null, false, 0);
            if(!isInserted){
                logErr("Line# " + lineNo + " - Variable " + $ID.getText() + " CANT INSERT IN ST");
            }
            logParse("----- " + $ID.getText() + " NO OFFSET, Dec in DS" + "----");
        }
      }
	| declaration_list COMMA ID LTHIRD CONST_INT RTHIRD {
        int lineNo = $ID.getLine();
        logParse("Line No : " + lineNo + " declaration_list : declaration_list COMMA ID LTHIRD CONST_INT RTHIRD");

        int noElems = Integer.parseInt($CONST_INT.getText());
        if(!isGlobal)
            logParse("----- " + $ID.getText() + ", offest : " + getOffset(noElems) + "----");
        else{
            String asmCode = "\t" + $ID.getText() + " DW " + noElems + " DUP (0000h)";
            writeCode(asmCode);
            logParse("----- " + $ID.getText() + " NO OFFSET, (BUT DUP " + noElems + ") Dec in DS" + "----");
        }
      }
	| ID {
        int lineNo = $ID.getLine();
        logParse("Line No : " + lineNo + " declaration_list : ID");

        if(!isGlobal){
            int offset = getOffset();
            logParse("----- " + $ID.getText() + ", offest : " + offset + " ----");
            boolean isInserted = STinsert($ID.getText(), "local", "int", null, false, offset);
            writeTempCode("\tSUB SP , " + 2);
        }
        else{
            String asmCode = "\t" + $ID.getText() + " DW 1 DUP (0000h)";
            writeCode(asmCode);
            boolean isInserted = STinsert($ID.getText(), "global", "int", null, false, 0);
            if(!isInserted){
                logErr("Line# " + lineNo + " - Variable " + $ID.getText() + " CANT INSERT IN ST");
            }
            logParse("----- " + $ID.getText() + " NO OFFSET, Dec in DS" + "----");
        }

      }
	| ID LTHIRD CONST_INT RTHIRD {
        int lineNo = $ID.getLine();
        logParse("Line No : " + lineNo + " declaration_list : ID LTHIRD CONST_INT RTHIRD");

        int noElems = Integer.parseInt($CONST_INT.getText());
        if(!isGlobal)
            logParse("----- " + $ID.getText() + ", offest : " + getOffset(noElems) + "----");
        else{
            String asmCode = "\t" + $ID.getText() + " DW " + noElems + " DUP (0000h)";
            writeCode(asmCode);
            logParse("----- " + $ID.getText() + " NO OFFSET, (BUT DUP " + noElems + ") Dec in DS" + "----");
        }
      };

statements:
	statement {
        logParse("statements : statement");
      }
	| statements statement {
        logParse("statements : statements statement");
      };

statement:
	var_declaration {
        logParse("statement : var_declaration");
      }
	| expression_statement {
        logParse("statement : expression_statement");
      }
	| compound_statement {
        logParse("statement : compound_statement");
      }
	| FOR {int lineNo = $FOR.getLine();} LPAREN 
    expression_statement
      {
        String condnLabel = getLabel();
        String endLabel = getLabel();
        String stmtLabel = getLabel();
        String updateLabel = getLabel();
        writeTempCode(condnLabel + ":"); // Start of for loop condn check
      } 
    es=expression_statement
      {
        // condn evaluated, check and continue loop
        // the result of condn expr aldy popped by expr_stmt
        writeTempCode("\tCMP AX, 0");
        writeTempCode("\tJE " + endLabel); // Jump to end if false
        writeTempCode("\tJMP " + stmtLabel); // Jump to statement if true
        writeTempCode(updateLabel + ":"); // Start of update portion

      } 
    expression {
      // update portion written already, go to condn check
        writeTempCode("\tJMP " + condnLabel); // Jump to condition check
      } 
    RPAREN
      {
        // start stmt/body of for loop
        writeTempCode(stmtLabel + ":");
      }
     statement {
        logParse("Line No : " + lineNo + " statement : FOR LPAREN expression_statement expression_statement expression RPAREN statement");
        // Statement written, go to updateLabel
        writeTempCode("\tJMP " + updateLabel); // Jump to update code
        writeTempCode(endLabel + ":"); // End of for loop
      }
	| IF LPAREN expression 
    RPAREN
    {
        int lineNo = $IF.getLine();
        logParse("Line No : " + lineNo + " statement : IF LPAREN expression RPAREN statement");
        writeTempCode("\tPOP AX"); // Pop the result of expression to AX
        String endLabel = getLabel();
        writeTempCode("\tCMP AX, 0"); // Compare AX with 0
        writeTempCode("\tJE " + endLabel); // Jump to end if false
        // if true, Statement will generate the asm code  
    }
   statement {
        logParse("Line No : " + lineNo + " statement : IF LPAREN expression RPAREN statement");

        // Done generateing True portion
        // END / False portion
        writeTempCode(endLabel + ":"); // Jump to end after true
      }
	| IF LPAREN expression 
    RPAREN
      {
        writeTempCode("\tPOP AX");
        String falseLabel = getLabel();
        String endLabel = getLabel();
        
        writeTempCode("\tCMP AX, 0"); // Compare AX with 0
        writeTempCode("\tJE " + falseLabel); // Jump to false if condition is
      } 
    statement 
      {
        // Done writing True portion, Jump to end
        writeTempCode("\tJMP " + endLabel); // Jump to end after true
      }
    ELSE
      {
        int lineNo = $ELSE.getLine();
        writeTempCode(falseLabel + ":"); // else code starts here

      } 
    statement {
        // Else portion code done, Start end/usual code portion
        logParse("Line No : " + lineNo + " statement : IF LPAREN expression RPAREN statement ELSE statement");
        writeTempCode(endLabel + ":"); // Jump to end after true
      }
	| WHILE 
    LPAREN
      {
        String condnLabel = getLabel();
        String endLabel = getLabel();
        writeTempCode(condnLabel + ":"); // Start of loop condn check
      } 
    expression
      {
        // expr evaluted, check if 0
        writeTempCode("\tPOP AX"); // Pop the result of expression to AX
        writeTempCode("\tCMP AX, 0"); // Compare AX with 0
        writeTempCode("\tJE " + endLabel); // Jump to end if false
        // do while loop body now
      } 
    RPAREN 
  statement {
        // body written, check condn again
        int lineNo = $WHILE.getLine();
        writeTempCode("\tJMP " + condnLabel); // Jump to condition check
        writeTempCode(endLabel + ":"); // End of while loop        
      }
	| PRINTLN LPAREN ID RPAREN SEMICOLON {
        int lineNo = $PRINTLN.getLine();
        logParse("Line No : " + lineNo + " statement : PRINTLN LPAREN ID RPAREN SEMICOLON");
        String asmId = getAsmVar($ID.getText());
        printInAsm(asmId, $ID.getLine());
      }
	| RETURN expression SEMICOLON {
        int lineNo = $RETURN.getLine();
        logParse("Line No : " + lineNo + " statement : RETURN expression SEMICOLON");
        // Result currently at top of the stack, Pop to AX
        writeTempCode("\tPOP AX \t\t; Line " + lineNo);

      };

expression_statement:
	SEMICOLON {
        int lineNo = $SEMICOLON.getLine();
        logParse("Line No : " + lineNo + " expression_statement : SEMICOLON");
      }
	| expression SEMICOLON {
        int lineNo = $SEMICOLON.getLine();
        logParse("Line No : " + lineNo + " expression_statement : expression SEMICOLON");
        writeTempCode("\tPOP AX \t\t; Line " + lineNo + "\n"); // Pop the result of expression to AX
      };

variable returns [String varName]:
	ID {
        int lineNo = $ID.getLine();
        logParse("Line No : " + lineNo + " variable : ID " + $ID.getText());
        $varName = $ID.getText();
      }
	| ID LTHIRD expression RTHIRD {
        int lineNo = $ID.getLine();
        logParse("Line No : " + lineNo + " variable : ID LTHIRD expression RTHIRD");
        $varName = $ID.getText();
      };

expression:
	logic_expression {
        logParse("expression : logic_expression");
      }
	| v=variable ASSIGNOP l=logic_expression {
        int lineNo = $ASSIGNOP.getLine();
        logParse("Line No : " + lineNo + " expression : variable ASSIGNOP logic_expression");

        writeTempCode("\tPOP AX"); // Pop the result of logic_expression to AX
        String cmd = "\tMOV " + getAsmVar($v.varName) + ", AX";

        System.out.println(cmd);
        writeTempCode(cmd);
        writeTempCode("\tPUSH AX"); // Push the value back to stack
      };

logic_expression:
	r=rel_expression {
        logParse("logic_expression : rel_expression");
      }
	| rel_expression LOGICOP rel_expression {
        int lineNo = $LOGICOP.getLine();
        logParse("Line No : " + lineNo + " logic_expression : rel_expression LOGICOP rel_expression");
        writeTempCode("\tPOP AX"); // Pop the second simple_expression to AX
        writeTempCode("\tPOP DX"); // Pop the first simple_expression to DX

        if($LOGICOP.getText().equals("||")) {
          writeTempCode("\tCMP DX, 0"); // Check if first expression is false
          String set1Label = getLabel();
          String endLabel = getLabel();

          writeTempCode("\tJNE " + set1Label); // set 1 if 1st op is true
          writeTempCode("\tCMP AX, 0"); 
          writeTempCode("\tJNE " + set1Label); // set 1 if 2nd expression is true
          writeTempCode("\tMOV AX, 0"); // If either is false, set result to 0
          writeTempCode("\tPUSH AX"); // Push result back to stack
          writeTempCode("\tJMP " + endLabel); // Jump to end

          // Set 1 block
          String set1Code = set1Label + ":\n" + "\tMOV AX, 1\n"
                        + "\tPUSH AX\n" + "\tJMP " + endLabel;
          writeTempCode(set1Code); // True block

          // End block
          writeTempCode(endLabel + ":");
        } else {
          writeTempCode("\tCMP DX, 0"); // Check if first expression is zero
          String set0Label = getLabel();
          String endLabel = getLabel();

          writeTempCode("\tJE " + set0Label); // set 1 if 1st op is zero
          writeTempCode("\tCMP AX, 0"); 
          writeTempCode("\tJE " + set0Label); // set 1 if 2nd expression is zero
          writeTempCode("\tMOV AX, 1"); // If neither is zero, set result to 1
          writeTempCode("\tPUSH AX"); // Push result back to stack
          writeTempCode("\tJMP " + endLabel); // Jump to end

          // Set 0 block
          String set0Code = set0Label + ":\n" + "\tMOV AX, 0\n"
                        + "\tPUSH AX\n" + "\tJMP " + endLabel;
          writeTempCode(set0Code); // True block

          // End block
          writeTempCode(endLabel + ":");
        } 

      };

rel_expression:
	s=simple_expression {
        logParse("rel_expression : simple_expression");

      }
	| simple_expression RELOP simple_expression {
        int lineNo = $RELOP.getLine();
        logParse("Line No : " + lineNo + " rel_expression : simple_expression RELOP simple_expression");
        writeTempCode("\tPOP AX"); // Pop the second simple_expression to AX
        writeTempCode("\tPOP DX"); // Pop the first simple_expression to DX
        writeTempCode("\tCMP DX, AX"); // Compare DX with AX
        
        String asmRelop = getAsmRelop($RELOP.getText());
        String trueLabel = getLabel();
        String falseLabel = getLabel();
        String endLabel = getLabel();
        
        writeTempCode("\t" + asmRelop + " " + trueLabel); // Jump if condition is true
        writeTempCode("\tJMP " + falseLabel); // Jump to false label

        String trueCode = trueLabel + ":\n" + "\tMOV AX, 1\n"
                        + "\tPUSH AX\n" + "\tJMP " + endLabel;
        writeTempCode(trueCode); // True block

        String falseCode = falseLabel + ":\n" + "\tMOV AX, 0\n"
                        + "\tPUSH AX\n" + endLabel + ":";
        writeTempCode(falseCode); // False block             
      };

simple_expression :
	term {
        logParse("simple_expression : term");
      }
	| simple_expression 
    {
      // ADD operation so store AX
    }
    ADDOP
      {
        logParse("Line No : " + $ADDOP.getLine() 
        + " (IN ADDOP) simple_expression : simple_expression ADDOP term");
      }

   term {
        int lineNo = $ADDOP.getLine();
        logParse("Line No : " + lineNo + " simple_expression : simple_expression ADDOP term");
        // POP into DX and AX then ADD
        writeTempCode("\tPOP AX"); // Pop the previous result to DX
        writeTempCode("\tPOP DX"); // Pop the current term to AX
        writeTempCode("\tADD AX, DX"); // Add DX to AX
        writeTempCode("\tPUSH AX"); // Push the result back to stack
      };

term :
	u=unary_expression {
        logParse("term : unary_expression");
      }
	| term 
    MULOP 
    unary_expression {
        int lineNo = $MULOP.getLine();
        logParse("Line No : " + lineNo + " term : term MULOP unary_expression");
        writeTempCode("\tPOP AX"); // Pop the current unary_expression to AX
        writeTempCode("\tPOP CX"); // Pop the previous result to CX

        String asmCode;
        if($MULOP.getText().equals("*")){
          asmCode = "\tCWD\n" + "\tMUL CX\n" + "\tPUSH AX";
        }
        else{
          // XCHG CX and AX for division
          asmCode = "\tXCHG CX, AX\n" + "\tCWD\n" + "\tDIV CX\n" + "\tPUSH DX";
        }
        writeTempCode(asmCode);
      };

unary_expression :
	ADDOP unary_expression {
        int lineNo = $ADDOP.getLine();
        logParse("Line No : " + lineNo + " unary_expression : ADDOP unary_expression");
        if($ADDOP.getText().equals("-")){
            writeTempCode("\tPOP AX"); // Pop the unary_expression to AX
            writeTempCode("\tNEG AX"); // Negate the value in AX
            writeTempCode("\tPUSH AX"); // Push the negated value back to stack
        }
        // No need to do anything for (+value)      
      }
	| NOT unary_expression {
        int lineNo = $NOT.getLine();
        logParse("Line No : " + lineNo + " unary_expression : NOT unary_expression");
        writeTempCode("\tPOP AX"); // Pop the unary_expression to AX
        writeTempCode("\tNOT AX"); // Negate the value in AX
        writeTempCode("\tPUSH AX"); // Push the negated value back to stack
      }
	| f=factor {
        logParse("unary_expression : factor");
      };

factor:
	variable {
        logParse("factor : variable");
        String asmVar = getAsmVar($variable.varName);
        writeTempCode("\tMOV AX, " + asmVar);
        writeTempCode("\tPUSH AX"); 
        
      }
	| ID LPAREN argument_list RPAREN {
        // Function CALL
        int lineNo = $ID.getLine();
        logParse("Line No : " + lineNo + " factor : ID LPAREN argument_list RPAREN");
        String funcName = $ID.getText();
        writeTempCode("\tCALL " + funcName + "\t\t; Line " + lineNo);
        writeTempCode("\tPUSH AX"); // Push the return value of the function
      }
	| LPAREN expression RPAREN {
        int lineNo = $LPAREN.getLine();
        logParse("Line No : " + lineNo + " factor : LPAREN expression RPAREN");
      }
	| CONST_INT {
        int lineNo = $CONST_INT.getLine();
        int val = Integer.parseInt($CONST_INT.getText());
        logParse("Line No : " + lineNo + " factor : CONST_INT " + val);
        String asmCode = "\tMOV AX, " + val + "\t\t; Line " + lineNo;
        writeTempCode(asmCode);
        writeTempCode("\tPUSH AX"); 
      }
	| CONST_FLOAT {
        int lineNo = $CONST_FLOAT.getLine();
        logParse("Line No : " + lineNo + " factor : CONST_FLOAT");
      }
	| variable INCOP {
        int lineNo = $INCOP.getLine();
        logParse("Line No : " + lineNo + " factor : variable INCOP");
        String asmVar = getAsmVar($variable.varName);
        writeTempCode("\tMOV AX, " + asmVar);
        writeTempCode("\tPUSH AX"); // store val before inc
        writeTempCode("\tINC AX"); // Increment the value
        writeTempCode("\tMOV " + asmVar + ", AX"); // Store incremented value back into mem
      }
	| variable DECOP {
        int lineNo = $DECOP.getLine();
        logParse("Line No : " + lineNo + " factor : variable DECOP");
        String asmVar = getAsmVar($variable.varName);
        writeTempCode("\tMOV AX, " + asmVar);
        writeTempCode("\tPUSH AX"); 
        writeTempCode("\tDEC AX"); // Increment the value
        writeTempCode("\tMOV " + asmVar + ", AX"); // Store incremented value back into mem
      };

argument_list:
	arguments {
        logParse("argument_list : arguments");
      }
	| /* empty */ {
        logParse("argument_list : empty");
      };

arguments:
	arguments COMMA logic_expression {
        int lineNo = $COMMA.getLine();
        logParse("Line No : " + lineNo + " arguments : arguments COMMA logic_expression");
        // ----- Stack top has the argument value, NO PUSH needed ----

      }
	| logic_expression {
        logParse("arguments : logic_expression");
        // ----- Stack top has the argument value, NO PUSH needed ----
      };