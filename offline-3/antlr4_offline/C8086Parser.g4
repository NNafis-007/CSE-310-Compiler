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

    boolean STinsert(String name, String token_type, String data_type, ArrayList<String> params, boolean isFunc) {
        try{
            SymbolInfo s1 = new SymbolInfo(name, token_type, data_type, params);
            if(isFunc) {
                s1.setIsFunction(true);
            }
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

    boolean STlookupCurrScope(String name){
        try{
            //currentScopeLookup
            SymbolInfo s = new SymbolInfo(name, "ID", null, null);
            SymbolInfo found = Main.st.currentScopeLookup(s);
            //
            if(found == null){
                return false;
            }
            return true;
        } catch(Exception e) {
            System.err.println("Symbol Table lookup error : " + e.getMessage());
            return false;
        }
    }

    boolean isInteger(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }

        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    String typeDetector(String s){
        try {
            Integer.parseInt(s);
            return "int";
        } catch (Exception e) {

        }

        try {
            Float.parseFloat(s);
            return "float";
        } catch (Exception e) {

        }
        return "unknown";
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
    : p=program
      {
        // wParserLog(
        //     "Parsing completed successfully with "
        //     + Main.syntaxErrorCount
        //     + " syntax errors."
        // );

        int lineNo = $p.prog_rri.lineNo;
        wParserLog("Line " + lineNo + ": start : program\n");
        STprint();

        wParserLog("\nTotal number of lines: " + lineNo);
        wParserLog("Total number of errors: " + Main.syntaxErrorCount);
      }
    ;

program returns [RuleReturnInfo prog_rri]
    : p=program u=unit
        {
            int lineNo = $u.unit_rri.lineNo;
            String text = $p.prog_rri.text + "\n" + $u.unit_rri.text;
            wParserLog("Line " + lineNo + ": program : program unit\n");
            wParserLog(text + "\n");
            $prog_rri = new RuleReturnInfo(lineNo, text);
        }
    | u=unit 
        {
            //print rri from unit
            int lineNo = $u.unit_rri.lineNo;
            String text = $u.unit_rri.text;
            wParserLog("Line " + lineNo + ": program : unit\n");
            wParserLog(text + "\n");
            $prog_rri = new RuleReturnInfo(lineNo, text);
        }
    ;

unit returns [RuleReturnInfo unit_rri]
    : vd=var_declaration
        {
            int lineNo = $vd.vdec_rri.lineNo;
            String text = $vd.vdec_rri.text;
            wParserLog("Line " + lineNo + ": unit : var_declaration\n");
            wParserLog(text + "\n");
            $unit_rri = new RuleReturnInfo(lineNo, text);
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
            String text = $f.fdef_rri.text;
            wParserLog("Line " + lineNo + ": unit : func_definition\n");
            wParserLog(text);
            $unit_rri = new RuleReturnInfo($f.fdef_rri);
        }
    ;

func_declaration returns [RuleReturnInfo fdec_rri]
    : t=type_specifier ID LPAREN p=parameter_list RPAREN SEMICOLON
        {
            //FUNC declaration WITH ARGS

            int lineNo = $ID.getLine();
            String type = $t.type_rri.text;
            String fn_name = $ID.getText();
            String params = $p.param_rri.text;

            System.out.println("PARSING func " + fn_name + " decl with ARGS");

            ArrayList<String> paramList = new ArrayList<>();
            for (String param : params.split(",")) {
                String[] splitted = param.split(" ");
                System.out.println("Param type : " + splitted[0]);
                paramList.add(splitted[0]);
            }

            boolean isInserted = STinsert(fn_name, "ID", type, paramList, true); //DONE
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
            //FUNC declaration WITHOUT ARGS

            int lineNo = $ID.getLine();
            String type = $t.type_rri.text;
            String fn_name = $ID.getText();

            System.out.println("PARSING func " + fn_name + " decl W/O ARGS");

            boolean isInserted = STinsert(fn_name, "ID", type, null, true); //DONE
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
                //FN DEF WITH ARGS

                System.out.println("Parsing Fn **definition** with args");
                System.out.println("  Fn name : " + $ID.getText());
                String ret_type = $ts.type_rri.text;

                // Store fn info in list
                ArrayList<String> paramList = new ArrayList<>();
                ArrayList<String> IDList = new ArrayList<>();
                String[] variables = $p.param_rri.text.split(",");
                System.out.print("  Params : ");
                for (String var : variables) {
                    String[] splitted = var.split(" ");
                    paramList.add(splitted[0]);
                    IDList.add(splitted[1]);
                    System.out.print("type : " + splitted[0] + ", ID : " + splitted[1] + "|");
                }
                System.out.println("");


                boolean isInserted = STinsert($ID.getText(), "ID", ret_type, paramList, true); //DONE
                if(!isInserted){
                    System.out.println("ERROR : " + $ID.getText() + " fn aldy inserted before");
                }
                else {
                    //----- INSERT INTO SYMBOL TABLE ---------
                    enterScope();
                    for (int i = 0; i < IDList.size(); i++) {
                        String id = IDList.get(i);
                        String idType = paramList.get(i);
                        STinsert(id, "ID", idType, null, false); //DONE
                    }
                    Main.isFuncDefined = true;
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
            wParserLog(text);
            Main.isFuncDefined = false;
            $fdef_rri = new RuleReturnInfo(lineNo, text);
        }
    | ts=type_specifier ID LPAREN 
        RPAREN 
            {
                System.out.println("Parsing Fn **definition** without args");
                System.out.println("  Fn name : " + $ID.getText());
                String ret_type = $ts.type_rri.text;
                boolean isInserted = STinsert($ID.getText(), "ID", ret_type, null, true); //DONE
                if(!isInserted){
                    System.out.println("ERROR : " + $ID.getText() + " fn  aldy inserted before");
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
            wParserLog(text);

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
            wParserLog(text);
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
            String varName = var.trim();
            String varType = $t.type_rri.text;
            
            //array declaration process differently
            if(var.contains("[")){    
                String[] parts = var.split("\\[");
                varName = new String(parts[0].trim());
                varType += "[]";
                System.out.println("Inserting array variable: " + varName + " of type " + varType);
            }
            boolean isInserted = STinsert(varName, "ID", varType, null, false); //DONE
            if(!isInserted){
                System.out.println("ERROR : " + varName + " already exists in current scope");
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
            boolean aldyExists = STlookupCurrScope($ID.text);
            if(aldyExists){
                wErrorLog("Error at line " + $ID.line + ": Multiple declaration of " + $ID.text + "\n");
                wParserLog("Error at line " + $ID.line + ": Multiple declaration of " + $ID.text + "\n");
                Main.syntaxErrorCount++;
            }
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

            wParserLog("Line " + lineNo + ": statements : statements statement\n");
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
            wParserLog("Line " + lineNo + ": statement : expression_statement\n");
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

            String id = $ID.getText();
            SymbolInfo var_info = Main.st.currentScopeLookup(new SymbolInfo(id, "ID", null, null));
            boolean isNotDeclared = (var_info == null);
            if(isNotDeclared){
                //Error at line 12: Undeclared variable b
                wErrorLog("Error at line " + lineNo + ": Undeclared variable " + id + "\n");
                wParserLog("Error at line " + lineNo + ": Undeclared variable " + id + "\n");
                System.out.println("ERROR: Variable " + id + " not declared before using at line " + lineNo);

                Main.syntaxErrorCount++;
            }

            boolean isArray = (var_info != null  && var_info.getDataType().endsWith("[]"));
            if(isArray){
                //Error at line 10: Type mismatch, a is an array
                wErrorLog("Error at line " + lineNo + ": Type mismatch, " + id + " is an array\n");
                wParserLog("Error at line " + lineNo + ": Type mismatch, " + id + " is an array\n");
                Main.syntaxErrorCount++;
            }

            wParserLog($ID.getText() + "\n");
        }
    | ID LTHIRD e=expression RTHIRD
        {
            // ARRAY VARIABLES
            int lineNo = $ID.getLine();
            String expr = $e.Expr_rri.text;

            String text = $ID.getText() + "[" + expr + "]";
            $var_rri = new RuleReturnInfo(lineNo, text);
            wParserLog("Line " + lineNo + ": variable : ID LTHIRD expression RTHIRD\n");

            if(!isInteger(expr)) {
                wErrorLog("Error at line " + lineNo + ": Expression inside third brackets not an integer\n");
                wParserLog("Error at line " + lineNo + ": Expression inside third brackets not an integer\n");
                Main.syntaxErrorCount++;     
            }

            wParserLog(text + "\n");

        }

    ;

expression returns [RuleReturnInfo Expr_rri]
    : l=logic_expression
        {
            int lineNo = $l.LogicExpr_rri.lineNo;
            String text = $l.LogicExpr_rri.text;
            wParserLog("Line " + lineNo + ": expression : logic_expression\n");
            wParserLog(text + "\n");
            $Expr_rri = new RuleReturnInfo(lineNo, text);
        }

    | var=variable ASSIGNOP l=logic_expression
        {
            int lineNo = $l.LogicExpr_rri.lineNo;
            String var_name = $var.var_rri.text;
            SymbolInfo var_info = Main.st.currentScopeLookup(new SymbolInfo(var_name, "ID", null, null));
            boolean isNotDeclared = (var_info == null);

            String logic_expr = $l.LogicExpr_rri.text;
            String expr_type = typeDetector(logic_expr);
            String text = var_name + "=" + logic_expr;
            wParserLog("Line " + lineNo + ": expression : variable ASSIGNOP logic_expression\n");
            
            // CHECK FOR TYPE MISMATCH ERROR (for declared variables)
            if(!isNotDeclared){
                ArrayList<String> validTypes = new ArrayList<>();
                validTypes.add("int");
                validTypes.add("float");

                boolean isValidExprType = validTypes.contains(expr_type);
                boolean isValidVarType = validTypes.contains(var_info.getDataType());
                if (isValidExprType && isValidVarType && !var_info.getDataType().equalsIgnoreCase(expr_type)) {
                    //Error at line 8: Type Mismatch
                    System.out.println("ERROR : Assigning " + var_name + "(" + var_info.getDataType() + ")" 
                                + " with " + logic_expr + "(" + expr_type + ")");

                    wErrorLog("Error at line " + lineNo + ": Type Mismatch" + "\n");
                    wParserLog("Error at line " + lineNo + ": Type Mismatch" + "\n");
                    Main.syntaxErrorCount++;
                }
            }

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

    | r1=rel_expression LOGICOP r2=rel_expression
        {
            int lineNo = $LOGICOP.getLine();
            String text = $r1.relExpr_rri.text + $LOGICOP.getText() + $r2.relExpr_rri.text;
            wParserLog("Line " + lineNo + ": logic_expression : rel_expression LOGICOP rel_expression\n");
            wParserLog(text + "\n");
            $LogicExpr_rri = new RuleReturnInfo(lineNo, text);
        }
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
    | s1=simple_expression RELOP s2=simple_expression
        {
            int lineNo = $RELOP.getLine();
            String text = $s1.sm_expr_rri.text + $RELOP.getText() + $s2.sm_expr_rri.text;
            wParserLog("Line " + lineNo + ": rel_expression : simple_expression RELOP simple_expression\n");
            wParserLog(text + "\n");
            $relExpr_rri = new RuleReturnInfo(lineNo, text);

        }
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
    | t=term MULOP ue=unary_expression
        {
            int lineNo = $MULOP.getLine();
            String text = $t.term_rri.text + $MULOP.getText() + $ue.unary_rri.text; 
            wParserLog("Line " + lineNo + ": term : term MULOP unary_expression\n");

            // Error at line 9: Non-Integer operand on modulus operator
            if($MULOP.getText().equals("%")){
                String expr_type = typeDetector($ue.unary_rri.text);
                if(!expr_type.equals("int")){
                    wErrorLog("Error at line " + lineNo + ": Non-Integer operand on modulus operator\n");
                    wParserLog("Error at line " + lineNo + ": Non-Integer operand on modulus operator\n");
                    Main.syntaxErrorCount++;
                }
            }

            wParserLog(text + "\n");
            $term_rri = new RuleReturnInfo(lineNo, text);

        }
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
    | ID LPAREN al=argument_list RPAREN
        {
            //FUNC CALL
            int lineNo = $ID.getLine();
            String text = $ID.getText() + "(" + $al.argList_rri.text + ")";

            wParserLog("Line " + lineNo + ": factor : ID LPAREN argument_list RPAREN\n");
            wParserLog(text + "\n");

            $fact_rri = new RuleReturnInfo(lineNo, text);            
        }
    | LPAREN e=expression RPAREN
        {
            int lineNo = $RPAREN.getLine();
            String text = "(" + $e.Expr_rri.text + ")";

            wParserLog("Line " + lineNo + ": factor : LPAREN expression RPAREN\n");
            wParserLog(text + "\n");

            $fact_rri = new RuleReturnInfo(lineNo, text);
        }
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

argument_list returns [RuleReturnInfo argList_rri]
    : a=arguments
        {
            // arguments -> work with single/multiple arguments
            // argument_list -> work with all arguments together

            int lineNo = $a.arg_rri.lineNo;
            String text = $a.arg_rri.text;

            wParserLog("Line " + lineNo + ": argument_list : arguments\n");
            wParserLog(text + "\n");
            $argList_rri = new RuleReturnInfo(lineNo, text);
        }
    | /* empty */
    ;

arguments returns [RuleReturnInfo arg_rri]
    : a=arguments COMMA le=logic_expression
        {
            int lineNo = $COMMA.getLine();
            String text = $a.arg_rri.text + "," + $le.LogicExpr_rri.text;

            wParserLog("Line " + lineNo + ": arguments : arguments COMMA logic_expression\n");
            wParserLog(text + "\n");
            $arg_rri = new RuleReturnInfo(lineNo, text);
        }
    | l=logic_expression
        {
            int lineNo = $l.LogicExpr_rri.lineNo;
            String text = $l.LogicExpr_rri.text;

            wParserLog("Line " + lineNo + ": arguments : logic_expression\n");
            wParserLog(text + "\n");
            $arg_rri = new RuleReturnInfo(lineNo, text);
        }
    ;
