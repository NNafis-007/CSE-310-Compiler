parser grammar C8086Parser;

options {
    tokenVocab = C8086Lexer;
}

@header {
import java.io.BufferedWriter;
import java.io.IOException;
import SymbolTable.SymbolInfo;
}

@members {
    // helper to write into parserLogFile
    void wParserLog(String message) {
        try {
            Main.parserLogFile.write(message);
            Main.parserLogFile.newLine();
            Main.parserLogFile.flush();
        } catch (IOException e) {
            System.err.println("Parser log error: " + e.getMessage());
        }
    }

    // helper to write into Main.errorFile
    void wErrorLog(String message) {
        try {
            Main.errorFile.write(message);
            Main.errorFile.newLine();
            Main.errorFile.flush();
        } catch (IOException e) {
            System.err.println("Error file write error: " + e.getMessage());
        }
    }

    void testSymbolTable(){
        try {
            SymbolInfo s1 = new SymbolInfo("x", "INT");
            Main.st.insertSymbol(s1);
            Main.st.printAllScopeTable();
        } catch(Exception e) {
            System.err.println("ST test error : " + e.getMessage());
        }
    }

    boolean STinsert(String name, String type){
        try{
            SymbolInfo s1 = new SymbolInfo(name, type);
            boolean isInserted = Main.st.insertSymbol(s1);
            return isInserted;
            
        } catch(Exception e) {
            System.err.println("Symbol Table insert error : " + e.getMessage());
            return false;
        }
    }

    void STprint(){
        try{
            Main.st.printAllScopeTable();
        } catch(Exception e) {
            System.err.println("Symbol Table PRINT error : " + e.getMessage());
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

    class RuleReturnInfo{
        public int lineNo;
        public String text;
        public RuleReturnInfo(){
            lineNo = 0;
            text = "";
        }

        public RuleReturnInfo(int ln, String txt){
            this.lineNo = ln;
            this.text = txt;
        }

        // Copy constructor
        public RuleReturnInfo(RuleReturnInfo other) {
            this.lineNo = other.lineNo;
            this.text = other.text;
        }
    }

}

start
    : program
      {
        wParserLog(
            "Parsing completed successfully with "
            + Main.syntaxErrorCount
            + " syntax errors."
        );

      }
    ;

program returns [String prog]
    : p=program u=unit
        {
            int lineNo = $u.unit_rri.lineNo;
            String text = $u.unit_rri.text;
            wParserLog("Line " + lineNo + ": program : program unit\n");
            $prog = $p.prog + text + "\n";
            wParserLog($prog + "\n");
        }
    | u=unit 
        {
            //print rri from unit
            int lineNo = $u.unit_rri.lineNo;
            String text = $u.unit_rri.text;
            wParserLog("Line " + lineNo + ": program : unit\n");
            wParserLog(text + "\n");
            $prog = text + "\n";

        }
    ;

unit returns [RuleReturnInfo unit_rri]
    : vd=var_declaration
        {
            int lineNo = $vd.vdec_rri.lineNo;
            String text = $vd.vdec_rri.text;
            wParserLog("Line " + lineNo + ": unit : var_declaration\n");
            wParserLog(text + "\n");
            $unit_rri = new RuleReturnInfo($vd.vdec_rri);
        }
    | fdec=func_declaration
        {
            int lineNo = $fdec.fdec_rri.lineNo;
            String text = $fdec.fdec_rri.text;
            wParserLog("Line " + lineNo + ": unit : func_declaration\n");
            wParserLog(text + "\n");
            $unit_rri = new RuleReturnInfo($fdec.fdec_rri);
        }
    | f=func_definition
        {
            int lineNo = $f.fdef_rri.lineNo;
            String text = $f.fdef_rri.text + "\n";
            wParserLog("Line " + lineNo + ": unit : func_definition\n");
            wParserLog(text + "\n");
            $unit_rri = new RuleReturnInfo($f.fdef_rri);
        }
    ;

func_declaration returns [RuleReturnInfo fdec_rri]
    : t=type_specifier ID LPAREN p=parameter_list RPAREN SEMICOLON
        {
            //fn with args
            System.out.println("PARSING FN with ARGS");
            int lineNo = $ID.getLine();
            String type = $t.type_rri.text;
            String fn_name = $ID.getText();
            boolean isInserted = STinsert(fn_name, "ID");
            if(!isInserted){
                System.out.println("ERROR : " + fn_name + " already exists in current scope");
            }

            String fd = type + " " + fn_name + "(" + $p.param_rri.text + ");";
            wParserLog("Line " + lineNo + ": func_declaration : type_specifier ID LPAREN parameter_list RPAREN SEMICOLON\n");
            wParserLog(fd + "\n");

            $fdec_rri = new RuleReturnInfo(lineNo, fd);
        }
    | t=type_specifier ID LPAREN RPAREN SEMICOLON
        {
            //fn without args
            System.out.println("PARSING FN W/O ARGS");
            int lineNo = $ID.getLine();
            String type = $t.type_rri.text;
            String fn_name = $ID.getText();

            boolean isInserted = STinsert(fn_name, "ID");
            if(!isInserted){
                System.out.println("ERROR : " + fn_name + " already exists in current scope");
            }
            
            wParserLog("Line " + lineNo + ": func_declaration : type_specifier ID LPAREN RPAREN SEMICOLON\n");
            wParserLog(type + " " + fn_name + "();\n");

            $fdec_rri = new RuleReturnInfo();
            $fdec_rri.text = type + " " + fn_name + "();";
            $fdec_rri.lineNo = lineNo;
        }
    ;

func_definition returns [RuleReturnInfo fdef_rri]
    : ts=type_specifier ID LPAREN p=parameter_list 
        RPAREN 
            {
                System.out.println("Storing fn info in symbol table");
                System.out.println("Fn name : " + $ID.getText());
                boolean isInserted = STinsert($ID.getText(), "ID");
                if(!isInserted){
                    System.out.println("ERROR : " + $ID.getText() + " fn aldy inserted before");
                }
                else {
                    System.out.print("Params : ");
                    //----- INSERT INTO SYMBOL TABLE ---------
                    String[] variables = $p.param_rri.text.split(",");
                    enterScope();
                    for (String var : variables) {
                        String[] splitted = var.split(" ");
                        STinsert(splitted[1], "ID");
                    }
                    Main.isFuncDefined = true;
                    System.out.println("");
                }

            } 
        cstat=compound_statement
        {
            //FN DEF WITH ARGS
            int lineNo = $cstat.Cstat_rri.lineNo;
            String fn_type = $ts.type_rri.text;
            String arg_list = "(" + $p.param_rri.text + ")";
            String fn_body = $cstat.Cstat_rri.text;
            String text = fn_type + " " + $ID.getText() + arg_list + fn_body;

            wParserLog("Line " + lineNo + ": func_definition : type_specifier ID LPAREN parameter_list RPAREN compound_statement\n");
            wParserLog(text + "\n");
            Main.isFuncDefined = false;
            $fdef_rri = new RuleReturnInfo(lineNo, text);
        }
    | ts=type_specifier ID LPAREN 
        RPAREN 
            {
                System.out.println("Storing fn info in symbol table");
                System.out.println("Fn name : " + $ID.getText());
                boolean isInserted = STinsert($ID.getText(), "ID");
                if(!isInserted){
                    System.out.println($ID.getText() + " fn  aldy inserted before");
                }
            } 
        cstat=compound_statement
        {
            //FN DEF WITHOUT ARGS
            int lineNo = $cstat.Cstat_rri.lineNo;
            String fn_type = $ts.type_rri.text;
            String fn_body = $cstat.Cstat_rri.text;
            String text = fn_type + " " + $ID.getText() + "()" + fn_body;

            wParserLog("Line " + lineNo + ": func_definition : type_specifier ID LPAREN RPAREN compound_statement\n");
            wParserLog(text + "\n");

            $fdef_rri = new RuleReturnInfo(lineNo, text);
        }
    ;

parameter_list returns [RuleReturnInfo param_rri]
    : p=parameter_list COMMA t=type_specifier ID
        {
            //fn defn multiple args int foo(int a, float b)
            int lineNo = $COMMA.line;
            String type = $t.type_rri.text;
            String id = $ID.text;
            String prev_param_list = $p.param_rri.text;

            //update param_rri
            String curr_param_list = prev_param_list + "," + type + " " + id;
            $param_rri = new RuleReturnInfo(lineNo, curr_param_list);

            wParserLog("Line " + lineNo + ": parameter_list : parameter_list COMMA type_specifier ID\n");
            wParserLog($param_rri.text + "\n");
        }
    | p=parameter_list COMMA t=type_specifier
        {
            //func decl multiple args int foo(int, float)
            int lineNo = $COMMA.line;
            String type = $t.type_rri.text;
            String prev_param_list = $p.param_rri.text;

            //update param_rri
            String curr_param_list = prev_param_list + "," + type;
            $param_rri = new RuleReturnInfo(lineNo, curr_param_list);

            wParserLog("Line " + lineNo + ": parameter_list : parameter_list COMMA type_specifier\n");
            wParserLog($param_rri.text + "\n");
        }
    | t=type_specifier ID
        { 
            //fn defn must int foo(int a)
            int lineNo = $t.type_rri.lineNo;
            String type = $t.type_rri.text;
            String id = $ID.text;

            String curr_param_list = type + " " + id;

            wParserLog("Line " + lineNo + ": parameter_list : type_specifier ID\n");
            wParserLog(curr_param_list + "\n");

            $param_rri = new RuleReturnInfo(lineNo, curr_param_list);

        }
    | t=type_specifier
        { 
            //used in func decl int foo(int)
            int lineNo = $t.type_rri.lineNo;
            String text = $t.type_rri.text;
            wParserLog("Line " + lineNo + ": parameter_list : type_specifier\n");
            wParserLog(text + "\n");    

            $param_rri = new RuleReturnInfo(lineNo, text);
        }
    ;

compound_statement returns [RuleReturnInfo Cstat_rri]
    : LCURL 
        {
            //ENTER new scope if not inserted from function already
            if(!Main.isFuncDefined){
                System.out.println("Entering new scope");
                enterScope();

            }
        } 
    s=statements RCURL
        {
            int lineNo = $RCURL.line;
            String text = "{\n" + $s.Stats_rri.text + "}\n";
            wParserLog("Line " + lineNo + ": compound_statement : LCURL statements RCURL\n");
            wParserLog(text + "\n");
            $Cstat_rri = new RuleReturnInfo(lineNo, text);

            //PRINT SYMBOL TABLE AND EXIT SCOPE
            STprint();
            exitScope();
        }

    | LCURL RCURL
        { 
            enterScope();
            wParserLog("Line " + $LCURL.line + ": compound_statement : LCURL RCURL\n");
            wParserLog("{}\n");
            $Cstat_rri = new RuleReturnInfo($LCURL.line, "{}");
            exitScope();
        }
    ;

var_declaration returns [RuleReturnInfo vdec_rri]
    : t=type_specifier dl=declaration_list sm=SEMICOLON
      {
        wParserLog(
            "Line " + $sm.getLine()
            +": var_declaration : type_specifier declaration_list SEMICOLON\n"
        );
        $vdec_rri = new RuleReturnInfo();
        $vdec_rri.text = $t.type_rri.text + " " + $dl.dec_list + ";";
        $vdec_rri.lineNo = $sm.getLine(); 

        wParserLog($t.type_rri.text + " " + $dl.dec_list + ";\n");

        //INSERT INTO SYMBOL TABLE
        String[] variables = $dl.dec_list.split(",");
        for (String var : variables) {
            boolean isInserted = STinsert(var.trim(), "ID");
            if(!isInserted){
                System.out.println("ERROR : " + var.trim() + " already exists in current scope");
            }
        }
      }
    | t=type_specifier de=declaration_list_err sm=SEMICOLON
      {
        wErrorLog(
            "Line# "
            + $sm.getLine()
            + " with error name: "
            + $de.error_name
            + " - Syntax error at declaration list of variable declaration"
        );
        Main.syntaxErrorCount++;
      }
    ;

declaration_list_err
    returns [String error_name]
    : { $error_name = "Error in declaration list"; }
    ;

type_specifier
    returns [RuleReturnInfo type_rri]
    : INT
      {
        $type_rri = new RuleReturnInfo();
        $type_rri.lineNo = $INT.line;
        $type_rri.text = "int";
        wParserLog("Line " + $INT.line + ": type_specifier : INT\n");
        wParserLog($INT.text + "\n");
      }
    | FLOAT
      {
        $type_rri = new RuleReturnInfo();
        $type_rri.lineNo = $FLOAT.line;
        $type_rri.text = "float";
        wParserLog("Line " + $FLOAT.line + ": type_specifier : FLOAT\n");
        wParserLog($FLOAT.text + "\n");
      }
    | VOID
      {
        $type_rri = new RuleReturnInfo();
        $type_rri.lineNo = $VOID.line;
        $type_rri.text = "void";
        wParserLog("Line " + $VOID.line + ": type_specifier : VOID\n");
        wParserLog($VOID.text + "\n");
      }
    ;

declaration_list returns [String dec_list]
    : dl=declaration_list COMMA ID 
        {
            //multiple decl
            $dec_list = $dl.dec_list + "," + $ID.text;
            System.out.println($dec_list);

            wParserLog("Line " + $ID.line + ": declaration_list : declaration_list COMMA ID\n");
            wParserLog($dec_list + "\n");
        }
    | dl=declaration_list COMMA ID LTHIRD CONST_INT RTHIRD 
        {
            //array er shathe aaro kichu
            $dec_list = $dl.dec_list + "," + $ID.text + "[" + $CONST_INT.text + "]";
            wParserLog("Line " + $ID.line + ": declaration_list : declaration_list COMMA ID LTHIRD CONST_INT RTHIRD\n");
            wParserLog($dec_list + "\n");
        }
    | ID 
        {
            //single decl
            $dec_list = $ID.text;
            System.out.println($dec_list);
            wParserLog("Line " + $ID.line + ": declaration_list : ID\n");
            wParserLog($dec_list + "\n");
        }
    | ID LTHIRD CONST_INT RTHIRD 
        {
            //array decl
            $dec_list = $ID.text + "[" + $CONST_INT.text + "]";
            wParserLog("Line " + $ID.line + ": declaration_list : ID LTHIRD CONST_INT RTHIRD\n");
            wParserLog($dec_list + "\n");
        }
    ;

statements returns [RuleReturnInfo Stats_rri]
    : s=statement
        {
            int lineNo = $s.stat_rri.lineNo;
            String text = $s.stat_rri.text;

            wParserLog("Line " + lineNo + ": statements : statement\n");
            wParserLog(text);
            $Stats_rri = new RuleReturnInfo(lineNo, text);
        }
    | ss=statements s=statement
        {
            int lineNo = $s.stat_rri.lineNo;
            String text = $ss.Stats_rri.text + $s.stat_rri.text;

            wParserLog("Line " + lineNo + ": statements : statement\n");
            wParserLog(text);
            $Stats_rri = new RuleReturnInfo(lineNo, text);
        }
    ;

statement returns [RuleReturnInfo stat_rri]
    : vd=var_declaration
        { 
            int lineNo = $vd.vdec_rri.lineNo;
            String text = $vd.vdec_rri.text + "\n";

            $stat_rri = new RuleReturnInfo(lineNo, text);
            wParserLog("Line " + lineNo + ": statement : var_declaration\n");
            wParserLog(text);
        }
    | es=expression_statement
        {
            int lineNo = $es.Expr_stat_rri.lineNo;
            String text = $es.Expr_stat_rri.text + "\n";

            $stat_rri = new RuleReturnInfo(lineNo, text);
            wParserLog("Line " + lineNo + ": statement : expression_statment\n");
            wParserLog(text);
        }
    | compound_statement
    | FOR LPAREN expression_statement expression_statement expression RPAREN statement
    | IF LPAREN expression RPAREN statement
    | IF LPAREN expression RPAREN statement ELSE statement
    | WHILE LPAREN expression RPAREN statement
    | PRINTLN LPAREN ID RPAREN SEMICOLON
    | RETURN e=expression SEMICOLON
        {
            int lineNo = $RETURN.getLine();
            String text = "return" + " " + $e.Expr_rri.text + ";\n";

            wParserLog("Line " + lineNo + ": statement : RETURN expression SEMICOLON\n");
            wParserLog(text);
            $stat_rri = new RuleReturnInfo(lineNo, text);

        }
    ;

expression_statement returns [RuleReturnInfo Expr_stat_rri]
    : SEMICOLON
        {
            int lineNo = $SEMICOLON.getLine();
            String text = ";";
            wParserLog("Line " + lineNo + ": expression_statement : SEMICOLON\n");
            wParserLog(text + "\n");
            $Expr_stat_rri = new RuleReturnInfo(lineNo, text);
        }
    | e=expression SEMICOLON
        {
            int lineNo = $SEMICOLON.getLine();
            String text = $e.Expr_rri.text + ";";

            wParserLog("Line " + lineNo + ": expression_statement : expression SEMICOLON\n");
            wParserLog(text + "\n");
            $Expr_stat_rri = new RuleReturnInfo(lineNo, text);            
        }
    ;

variable returns [RuleReturnInfo var_rri]
    : ID
        {
            // USUAL VARIABLE
            int lineNo = $ID.getLine();
            $var_rri = new RuleReturnInfo(lineNo, $ID.getText());

            wParserLog("Line " + lineNo + ": variable : ID\n");
            wParserLog($ID.getText() + "\n");
        }
    | ID LTHIRD expression RTHIRD
        {
            // ARRAY VARIABLES
            int lineNo = $ID.getLine();
            String text = $ID.getText() + "[" + "expr (placeholder)" + "]";
            $var_rri = new RuleReturnInfo(lineNo, text);

            wParserLog("Line " + lineNo + ": variable : ID LTHIRD expression RTHIRD\n");
            wParserLog(text + "\n");

        }

    ;

expression returns [RuleReturnInfo Expr_rri]
    : l=logic_expression
        {
            int lineNo = $l.LogicExpr_rri.lineNo;
            String text = $l.LogicExpr_rri.text;
            wParserLog("Line " + lineNo + ": expression : logic expression\n");
            wParserLog(text + "\n");
            $Expr_rri = new RuleReturnInfo(lineNo, text);
        }

    | var=variable ASSIGNOP l=logic_expression
        {
            int lineNo = $l.LogicExpr_rri.lineNo;
            String var_name = $var.var_rri.text;

            String text = var_name + "=" + $l.LogicExpr_rri.text;
            wParserLog("Line " + lineNo + ": expression : variable ASSIGNOP logic expression\n");
            wParserLog(text + "\n");
            $Expr_rri = new RuleReturnInfo(lineNo, text);
            
        }
    ;

logic_expression returns [RuleReturnInfo LogicExpr_rri]
    : r=rel_expression
        {
            int lineNo = $r.relExpr_rri.lineNo;
            String text = $r.relExpr_rri.text;
            wParserLog("Line " + lineNo + ": logic_expression : rel_expression\n");
            wParserLog(text + "\n");
            $LogicExpr_rri = new RuleReturnInfo(lineNo, text);
        }

    | rel_expression LOGICOP rel_expression
    ;

rel_expression returns [RuleReturnInfo relExpr_rri]
    : s=simple_expression
        {
            int lineNo = $s.sm_expr_rri.lineNo;
            String text = $s.sm_expr_rri.text;
            wParserLog("Line " + lineNo + ": rel_expression : simple_expression\n");
            wParserLog(text + "\n");
            $relExpr_rri = new RuleReturnInfo(lineNo, text);
        }
    | simple_expression RELOP simple_expression
    ;

simple_expression returns [RuleReturnInfo sm_expr_rri]
    : t=term
        {
            int lineNo = $t.term_rri.lineNo;
            String text = $t.term_rri.text; 
            wParserLog("Line " + lineNo + ": simple_expression : term\n");
            wParserLog(text + "\n");
            $sm_expr_rri = new RuleReturnInfo(lineNo, text);

        }
    | sm_expr=simple_expression ADDOP t=term
        {
            int lineNo = $ADDOP.getLine();
            String op = $ADDOP.getText();
            String text = $sm_expr.sm_expr_rri.text + op + $t.term_rri.text;
            wParserLog("Line " + lineNo + ": simple_expression : simple_expression ADDOP term\n");
            wParserLog(text + "\n");
            $sm_expr_rri = new RuleReturnInfo(lineNo, text);
        }
    ;

term returns [RuleReturnInfo term_rri]
    : u=unary_expression
        {
            int lineNo = $u.unary_rri.lineNo;
            String text = $u.unary_rri.text; 
            wParserLog("Line " + lineNo + ": term : unary_expression\n");
            wParserLog(text + "\n");
            $term_rri = new RuleReturnInfo(lineNo, text);

        }
    | term MULOP unary_expression
    ;

unary_expression returns [RuleReturnInfo unary_rri]
    : ADDOP unary_expression
    | NOT unary_expression
    | f=factor
        {
            int lineNo = $f.fact_rri.lineNo;
            String text = $f.fact_rri.text; 
            wParserLog("Line " + lineNo + ": unary_expression : factor\n");
            wParserLog(text + "\n");

            $unary_rri = new RuleReturnInfo(lineNo, text);
        }
    ;

factor returns [RuleReturnInfo fact_rri]
    : var=variable
        {
            int lineNo = $var.var_rri.lineNo;
            String text = $var.var_rri.text;

            wParserLog("Line " + lineNo + ": factor : variable\n");
            wParserLog(text + "\n");

            $fact_rri = new RuleReturnInfo(lineNo, text);
        }
    | ID LPAREN argument_list RPAREN
    | LPAREN expression RPAREN
    | c=CONST_INT
        {
            int lineNo = $c.getLine();
            String text = $c.getText();

            wParserLog("Line " + lineNo + ": factor : CONST_INT\n");
            wParserLog(text + "\n");

            $fact_rri = new RuleReturnInfo(lineNo, text);
        }
    | c=CONST_FLOAT
        {
            int lineNo = $c.getLine();
            String text = $c.getText();

            wParserLog("Line " + lineNo + ": factor : CONST_FLOAT\n");
            wParserLog(text + "\n");

            $fact_rri = new RuleReturnInfo(lineNo, text);
        }

    | variable INCOP
    | variable DECOP
    ;

argument_list
    : arguments
    | /* empty */
    ;

arguments
    : arguments COMMA logic_expression
    | logic_expression
    ;
