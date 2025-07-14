parser grammar C2105007Parser;

options {
	tokenVocab = C2105007Lexer;
}

@header {
import java.io.BufferedWriter;
import java.io.IOException;
import SymbolTable.SymbolInfo;
}

@members {

    // helper to write into parserLogFile
    public boolean isGlobal = true;
    public int stackOffset = 0;
    public boolean isAdditionStarted = false;

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

    void printInAsm(String id){
      // MOV AX, i       ; Line 7
    	// CALL print_output
	    // CALL new_line
      String asmCode = "\tMOV AX, " + id + "\n" +
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
          String cmd = "[BP-" + varInfo.getOffset() + "]";
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
        String asmCode = $ID.getText() + " PROC\n" + "\tPUSH BP\n" + "\tMOV BP, SP\n";
        writeTempCode(asmCode);
      } LPAREN parameter_list RPAREN compound_statement {
        int lineNo = $ID.getLine();
        logParse("Line No : " + lineNo + " func_definition : type_specifier ID LPAREN parameter_list RPAREN compound_statement");
        isGlobal = true; // Reset isGlobal for next function
        String cleanFuncCode = "\tPOP BP\n" + "\tRET\n" + $fn_name + " ENDP\n";
        writeTempCode(cleanFuncCode);

      }
	| type_specifier ID {
        isGlobal=false;
        $fn_name = $ID.getText();
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

        if($fn_name.equals("main")){
          // Exit program for main
            String cleanMainCode = "\tPOP BP\n" + "\tMOV AX, 4Ch\n" 
                      + "\tINT 21h\n" + "main ENDP"; 
            writeTempCode(cleanMainCode);
        }
        else{
          String cleanFuncCode = "\tPOP BP\n" + "\tRET\n" + $fn_name + " ENDP\n";
          writeTempCode(cleanFuncCode);
        }

      };

parameter_list:
	parameter_list COMMA type_specifier ID {
        int lineNo = $ID.getLine();
        logParse("Line No : " + lineNo + " parameter_list : parameter_list COMMA type_specifier ID");
      }
	| parameter_list COMMA type_specifier {
        int lineNo = $COMMA.getLine();
        logParse("Line No : " + lineNo + " parameter_list : parameter_list COMMA type_specifier");
      }
	| type_specifier ID {
        int lineNo = $ID.getLine();
        logParse("Line No : " + lineNo + " parameter_list : type_specifier ID");
      }
	| type_specifier {
        logParse("parameter_list : type_specifier");
      };

compound_statement:
	LCURL statements RCURL {
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
	| FOR LPAREN expression_statement expression_statement expression RPAREN statement {
        int lineNo = $FOR.getLine();
        logParse("Line No : " + lineNo + " statement : FOR LPAREN expression_statement expression_statement expression RPAREN statement");
      }
	| IF LPAREN expression RPAREN statement {
        int lineNo = $IF.getLine();
        logParse("Line No : " + lineNo + " statement : IF LPAREN expression RPAREN statement");
      }
	| IF LPAREN expression RPAREN statement ELSE statement {
        int lineNo = $IF.getLine();
        logParse("Line No : " + lineNo + " statement : IF LPAREN expression RPAREN statement ELSE statement");
      }
	| WHILE LPAREN expression RPAREN statement {
        int lineNo = $WHILE.getLine();
        logParse("Line No : " + lineNo + " statement : WHILE LPAREN expression RPAREN statement");
      }
	| PRINTLN LPAREN ID RPAREN SEMICOLON {
        int lineNo = $PRINTLN.getLine();
        logParse("Line No : " + lineNo + " statement : PRINTLN LPAREN ID RPAREN SEMICOLON");
      }
	| RETURN expression SEMICOLON {
        int lineNo = $RETURN.getLine();
        logParse("Line No : " + lineNo + " statement : RETURN expression SEMICOLON");
      };

expression_statement:
	SEMICOLON {
        int lineNo = $SEMICOLON.getLine();
        logParse("Line No : " + lineNo + " expression_statement : SEMICOLON");
      }
	| expression SEMICOLON {
        int lineNo = $SEMICOLON.getLine();
        logParse("Line No : " + lineNo + " expression_statement : expression SEMICOLON");
      };

variable returns [String varName]:
	ID {
        int lineNo = $ID.getLine();
        logParse("Line No : " + lineNo + " variable : ID");
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

        // MOV var, expr_val
        String cmd = "MOV " + getAsmVar($v.varName) + ", <val>";
        System.out.println(cmd);

      };

logic_expression:
	r=rel_expression {
        logParse("logic_expression : rel_expression");
      }
	| rel_expression LOGICOP rel_expression {
        int lineNo = $LOGICOP.getLine();
        logParse("Line No : " + lineNo + " logic_expression : rel_expression LOGICOP rel_expression");
      };

rel_expression:
	s=simple_expression {
        logParse("rel_expression : simple_expression");

      }
	| simple_expression RELOP simple_expression {
        int lineNo = $RELOP.getLine();
        logParse("Line No : " + lineNo + " rel_expression : simple_expression RELOP simple_expression");
      };

simple_expression :
	term {
        logParse("simple_expression : term");
      }
	| simple_expression 
    ADDOP
      {

        logParse("Line No : " + $ADDOP.getLine() + " (IN ADDOP) simple_expression : simple_expression ADDOP term");
        if(!isAdditionStarted){
          writeTempCode("\tMOV DX, AX"); // gonna add so store AX in DX
        }
      }

   term {
        int lineNo = $ADDOP.getLine();
        logParse("Line No : " + lineNo + " simple_expression : simple_expression ADDOP term");
        if(isAdditionStarted){
          // Curr result is in stack, So pop to AX
          writeTempCode("\tMOV DX, AX");
          writeTempCode("\tPOP AX");
        }
        writeTempCode("\tADD AX, DX"); 
        writeTempCode("\tPUSH AX");
        isAdditionStarted = true; 
      };

term :
	u=unary_expression {
        logParse("term : unary_expression");
      }
	| term MULOP unary_expression {
        int lineNo = $MULOP.getLine();
        logParse("Line No : " + lineNo + " term : term MULOP unary_expression");
      };

unary_expression :
	ADDOP unary_expression {
        int lineNo = $ADDOP.getLine();
        logParse("Line No : " + lineNo + " unary_expression : ADDOP unary_expression");
      }
	| NOT unary_expression {
        int lineNo = $NOT.getLine();
        logParse("Line No : " + lineNo + " unary_expression : NOT unary_expression");
      }
	| f=factor {
        logParse("unary_expression : factor");
      };

factor:
	variable {
        logParse("factor : variable");
      }
	| ID LPAREN argument_list RPAREN {
        int lineNo = $ID.getLine();
        logParse("Line No : " + lineNo + " factor : ID LPAREN argument_list RPAREN");
      }
	| LPAREN expression RPAREN {
        int lineNo = $LPAREN.getLine();
        logParse("Line No : " + lineNo + " factor : LPAREN expression RPAREN");
      }
	| CONST_INT {
        int lineNo = $CONST_INT.getLine();
        int val = Integer.parseInt($CONST_INT.getText());
        logParse("Line No : " + lineNo + " factor : CONST_INT " + val);
        // MOV AX, INT_VAL
        String asmCode = "\tMOV AX, " + val;
        System.out.println(asmCode);
        writeTempCode(asmCode);
      }
	| CONST_FLOAT {
        int lineNo = $CONST_FLOAT.getLine();
        logParse("Line No : " + lineNo + " factor : CONST_FLOAT");
      }
	| variable INCOP {
        int lineNo = $INCOP.getLine();
        logParse("Line No : " + lineNo + " factor : variable INCOP");
      }
	| variable DECOP {
        int lineNo = $DECOP.getLine();
        logParse("Line No : " + lineNo + " factor : variable DECOP");
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
      }
	| logic_expression {
        logParse("arguments : logic_expression");
      };