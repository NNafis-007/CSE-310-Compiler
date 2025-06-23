parser grammar C2105007Parser;

options {
	tokenVocab = C2105007Lexer;
}

@header {
import java.io.BufferedWriter;
import java.io.IOException;
import SymbolTable.SymbolInfo;
import java.util.Arrays;
}

@members {
    // helper to write into parserLogFile
    public boolean isValidStatement = true;
    public boolean isUnrecognizedChar = false;
    public ArrayList<String> varsInCurrFunc = new ArrayList<>();

    public List<String> splitByArithmeticOperators(String s) {
        List<String> expressions = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        int parenCount = 0;
        
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            
            if (c == '(') {
                parenCount++;
                current.append(c);
            } else if (c == ')') {
                parenCount--;
                current.append(c);
            } else if ((c == '+' || c == '-' || c == '*' || c == '/' || c == '%') && parenCount == 0) {
                // Found an operator at top level (not inside parentheses)
                if (current.length() > 0) {
                    expressions.add(current.toString().trim());
                    current = new StringBuilder();
                }
                // Skip the operator itself
            } else {
                current.append(c);
            }
        }
        
        // Add the last expression
        if (current.length() > 0) {
            expressions.add(current.toString().trim());
        }
        
        return expressions;
    }

    String getFuncType(String fnName){
        try{

            SymbolInfo fn_info = Main.st.lookUp(new SymbolInfo(fnName, "ID", null, null));
            if(fn_info == null){
                System.out.println("Function " + fnName + " not found in symbol table");
                return "";
            }
            return fn_info.getDataType();

        } catch (Exception e) {
            System.err.println("getFuncType error : " + e.getMessage());
            return "";
        }
    }

    // Helper method to check if an expression is a logical expression
    public boolean isLogicalExpression(String expr) {
        expr = expr.trim();
        
        // Check for logical operators: &&, ||, !, ==, !=, <, >, <=, >=, %
        if (expr.contains("&&") || expr.contains("||") || 
            expr.contains("==") || expr.contains("!=") || 
            expr.contains("<=") || expr.contains(">=") || 
            expr.contains("<") || expr.contains(">") ||
            expr.contains("%")) {
            return true;
        }
        
        // Check for negation operator at the beginning
        if (expr.startsWith("!")) {
            return true;
        }
        
        // Check for boolean literals
        if (expr.equals("true") || expr.equals("false")) {
            return true;
        }
        
        return false;
    }

    public String evaluateExpressionType(String expr) {
        if (expr.isEmpty()) {
            return "unknown";
        }
        
        // Remove outer parentheses if present
        while (expr.startsWith("(") && expr.endsWith(")")) {
            expr = expr.substring(1, expr.length() - 1).trim();
        }

        // Check if it's a logical expression (logical expressions always return int)
        if (isLogicalExpression(expr)) {
            return "int";
        }

        
        // Check if it's an integer literal
        try {
            Integer.parseInt(expr);
            return "int";
        } catch (NumberFormatException ignored) {}
        
        // Check if it's a float literal
        try {
            Float.parseFloat(expr);
            return "float";
        } catch (NumberFormatException ignored) {}
        
        // Check if it's a function call
        if (expr.contains("(") && expr.contains(")")) {
            String[] splitted = expr.split("[(]");
            String fn_type = getFuncType(splitted[0]);
            System.out.println("(in type detector) Type checking for fn " + splitted[0] + " | type: " + fn_type);
            return fn_type; // Return the function type
        }
        
        // Check if it's a variable (identifier)
        if (expr.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
            // This should be replaced with actual symbol table lookup
            // For now, return unknown for variables
            SymbolInfo varInfo = Main.st.currentScopeLookup(new SymbolInfo(expr, "ID", null, null));
            if(varInfo == null){
                varInfo = Main.st.lookUp(new SymbolInfo(expr, "ID", null, null));
            }
            if (varInfo != null) {
                String varType = varInfo.getDataType();
                System.out.println("Type checking for variable " + expr + " returning " + varType);
                return varType;
            }
            System.out.println("Type checking for variable " + expr + " returning unknown");
            return "unknown";
        }

        //check for array variable
        if (expr.matches("[a-zA-Z_][a-zA-Z0-9_]*\\[[0-9]+\\]")) {
            String varName = expr.split("\\[")[0];
            SymbolInfo varInfo = Main.st.currentScopeLookup(new SymbolInfo(varName, "ID", null, null));
            if(varInfo == null){
                varInfo = Main.st.lookUp(new SymbolInfo(varName, "ID", null, null));
            }
            if (varInfo != null) {
                String varType = varInfo.getDataType();
                return varType;
            }
            return "unknown";
        }

        // If it contains arithmetic operators, recursively evaluate
        if (expr.matches(".*[+\\-*/%].*")) {
            return typeDetector(expr);
        }
        
        return "unknown";
    }

    public String typeDetector(String s) {
        s = s.trim();
        
        // Check if it's a logical expression first (logical expressions always return int)
        if (isLogicalExpression(s)) {
            return "int";
        }


        // Base case: single literal int
        try {
            Integer.parseInt(s);
            return "int";
        } catch (Exception ignored) {}

        // Base case: single literal float
        try {
            Float.parseFloat(s);
            return "float";
        } catch (Exception ignored) {}

        // Remove outermost parentheses for cleaner parsing
        while (s.startsWith("(") && s.endsWith(")")) {
            s = s.substring(1, s.length() - 1).trim();
        }

        // Split expression by arithmetic operations (+, -, *, /, %)
        List<String> expressions = splitByArithmeticOperators(s);
        
        boolean hasFloat = false;
        boolean hasUnknown = false;
        boolean hasVoid = false;
        
        // Evaluate the type of each expression
        for (String expr : expressions) {
            System.out.println("Individual Expression: " + expr);
            String exprType = evaluateExpressionType(expr.trim());

            
            if (exprType.equals("unknown")) {
                hasUnknown = true;
            } else if (exprType.equals("float")) {
                hasFloat = true;
            } else if (exprType.equals("void")) {
                hasVoid = true; // Void type is not allowed in expressions
            }
        }
        
        // If there's no unknown expression type, check priority: float > int
        if (!hasUnknown) {
            if(hasVoid){
                return "void"; // If any expression is void, return void
            }
            if (hasFloat) {
                return "float"; // If any expression is float, return float
            }
            return "int"; // Otherwise, return int
        }
        
        return "unknown";
    }

    public boolean isArrayVar(String varName) {
        SymbolInfo varInfo = Main.st.currentScopeLookup(new SymbolInfo(varName, "ID", null, null));
        if(varInfo == null){
            varInfo = Main.st.lookUp(new SymbolInfo(varName, "ID", null, null));
        }
        if (varInfo != null) {
            String varType = varInfo.getDataType();
            System.out.println("Checking if var " + varName + " is Array? : " + varType);
            if (varType.trim().endsWith("[]")) {
                return true; // It's an array variable
            }
        }
        return false; // Not found or not an array variable
    }

 
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

    void logErr(String message){
        wErrorLog(message);
        wParserLog(message);
        Main.syntaxErrorCount++;
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

    boolean isFunction(String name){
        try{
            //currentScopeLookup
            SymbolInfo s = new SymbolInfo(name, "ID", null, null);
            SymbolInfo found = Main.st.currentScopeLookup(s);
            //
            if(found == null){
                return false;
            }
            return found.getIsFunction();
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

    class RuleReturnInfo{
        public int lineNo;
        public String text;
        public String expr_type;
        public RuleReturnInfo(){
            lineNo = 0;
            text = "";
        }

        public RuleReturnInfo(int ln, String txt){
            this.lineNo = ln;
            this.text = txt;
            this.expr_type = null; // default value
        }

        public RuleReturnInfo(int ln, String txt, String type){
            this.lineNo = ln;
            this.text = txt;
            this.expr_type = type;
        }


        // Copy constructor
        public RuleReturnInfo(RuleReturnInfo other) {
            this.lineNo = other.lineNo;
            this.text = other.text;
            this.expr_type = other.expr_type; // copy expr_type
        }
    }

}

start:
	p = program {

        int lineNo = $p.prog_rri.lineNo;
        wParserLog("Line " + lineNo + ": start : program\n");
        STprint();

        wParserLog("Total number of lines: " + lineNo);
        wParserLog("Total number of errors: " + Main.syntaxErrorCount);
      };

program
	returns[RuleReturnInfo prog_rri]:
	p = program u = unit {
            int lineNo = $u.unit_rri.lineNo;
            String text = $p.prog_rri.text + "\n" + $u.unit_rri.text.stripTrailing();
            wParserLog("Line " + lineNo + ": program : program unit\n");
            wParserLog(text + "\n");
            $prog_rri = new RuleReturnInfo(lineNo, text);
        }
	| u = unit {
            //print rri from unit
            int lineNo = $u.unit_rri.lineNo;
            String text = $u.unit_rri.text.stripTrailing();
            wParserLog("Line " + lineNo + ": program : unit\n");
            wParserLog(text + "\n");
            $prog_rri = new RuleReturnInfo(lineNo, text);
        };

unit
	returns[RuleReturnInfo unit_rri]:
	vd = var_declaration {
            int lineNo = $vd.vdec_rri.lineNo;
            String text = $vd.vdec_rri.text;
            wParserLog("Line " + lineNo + ": unit : var_declaration\n");
            wParserLog(text + "\n");
            $unit_rri = new RuleReturnInfo(lineNo, text);
        }
	| fdec = func_declaration {
            int lineNo = $fdec.fdec_rri.lineNo;
            String text = $fdec.fdec_rri.text;
            wParserLog("Line " + lineNo + ": unit : func_declaration\n");
            wParserLog(text + "\n");
            $unit_rri = new RuleReturnInfo($fdec.fdec_rri);
        }
	| f = func_definition {
            int lineNo = $f.fdef_rri.lineNo;
            String text = $f.fdef_rri.text;
            wParserLog("Line " + lineNo + ": unit : func_definition\n");
            wParserLog(text);
            $unit_rri = new RuleReturnInfo($f.fdef_rri);
        };

func_declaration
	returns[RuleReturnInfo fdec_rri]:
	t = type_specifier ID LPAREN p = parameter_list RPAREN SEMICOLON {
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
	| t = type_specifier ID LPAREN RPAREN SEMICOLON {
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
        };  

func_definition
	returns[RuleReturnInfo fdef_rri]:
	ts = type_specifier ID LPAREN p = parameter_list RPAREN {
                //FN DEF WITH ARGS
                int lineNo_now = $RPAREN.getLine();

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
                boolean isFunc = isFunction($ID.getText());

                

                // Previously declared function 
                if(!isInserted && isFunc){
                    System.out.println("Defining a previously declared fn " + $ID.getText());
                    String fnType = getFuncType($ID.getText());
                    System.out.println("    prev Type " + fnType);
                    System.out.println("    now Type " + ret_type);
                    if(!fnType.equalsIgnoreCase(ret_type)){
                        //Error at line 24: Return type mismatch of foo3
                        System.out.println("Error at line " + lineNo_now + ": Return type mismatch of " + $ID.getText());
                        logErr("Error at line " + lineNo_now + ": Return type mismatch of " + $ID.getText() + "\n");
                    }

                    SymbolInfo fn_info = Main.st.lookUp(new SymbolInfo($ID.getText(), "ID", null, null));
                    ArrayList<String> prev_params = fn_info.getParameters();
                    System.out.println("  prev params : " + prev_params + "| now params : " + paramList);

                    if(prev_params.size() != paramList.size()){
                        //Error at line 32: Total number of arguments mismatch with declaration in function var
                        System.out.println("Error at line " + lineNo_now + 
                                ": Total number of arguments mismatch with declaration in function " + $ID.getText());
                        logErr("Error at line " + lineNo_now + 
                                ": Total number of arguments mismatch with declaration in function " + $ID.getText() + "\n");
                    }
                }

                if(!isInserted && !isFunc){
                    //Error at line 28: Multiple declaration of z
                    System.out.println("Error at line " + lineNo_now + ": Multiple declaration of " + $ID.getText());
                    logErr("Error at line " + lineNo_now + ": Multiple declaration of " + $ID.getText() + "\n");
                }
                //----- INSERT INTO SYMBOL TABLE ---------
                enterScope();
                for (int i = 0; i < IDList.size(); i++) {
                    String id = IDList.get(i);
                    String idType = paramList.get(i);
                    if(STinsert(id, "ID", idType, null, false)){
                        System.out.println("Inserted " + id + " for curr func");
                    }
                    else{
                        System.out.println("Can not insert " + id + " for curr func");
                    }
                }
                Main.isFuncDefined = true;

            } cstat = compound_statement {
            //FN DEF WITH ARGS
            int lineNo = $cstat.Cstat_rri.lineNo;
            String fn_type = $ts.type_rri.text;
            String arg_list = "(" + $p.param_rri.text + ")";
            String fn_body = $cstat.Cstat_rri.text;
            String text = fn_type + " " + $ID.getText() + arg_list + fn_body;

            if(fn_body.contains("return") && fn_type.equalsIgnoreCase("void")){
                //Error at line 38: Cannot return value from function foo4 with void return type 
                System.out.println("Error : void Function " + $ID.getText() + " returns a value");
                logErr("Error at line " + lineNo + ": Cannot return value from function " + $ID.getText() + " with void return type\n");

            }

            wParserLog("Line " + lineNo + ": func_definition : type_specifier ID LPAREN parameter_list RPAREN compound_statement\n");
            wParserLog(text);
            Main.isFuncDefined = false;
            $fdef_rri = new RuleReturnInfo(lineNo, text);
        }
	| ts = type_specifier ID LPAREN RPAREN {
                System.out.println("Parsing Fn **definition** without args");
                System.out.println("  Fn name : " + $ID.getText());
                String ret_type = $ts.type_rri.text;
                boolean isInserted = STinsert($ID.getText(), "ID", ret_type, null, true); //DONE
                boolean isFunc = isFunction($ID.getText());

                if(!isInserted && !isFunc){
                    System.out.println("ERROR : " + $ID.getText() + " fn  aldy inserted before");
                }
            } cstat = compound_statement {
            //FN DEF WITHOUT ARGS
            int lineNo = $cstat.Cstat_rri.lineNo;
            String fn_type = $ts.type_rri.text;
            String fn_body = $cstat.Cstat_rri.text;
            String text = fn_type + " " + $ID.getText() + "()" + fn_body;

            wParserLog("Line " + lineNo + ": func_definition : type_specifier ID LPAREN RPAREN compound_statement\n");
            wParserLog(text);

            $fdef_rri = new RuleReturnInfo(lineNo, text);
        }
    | ts = type_specifier ID LPAREN p = parameter_list 
    non=non_params
        {
            int errLineNo = $non.non_param_rri.lineNo;
            String non_param = $non.non_param_rri.text;
            
            String message = "Error at line " + errLineNo + 
                ": syntax error, unexpected " + non_param + ", expecting RPAREN or COMMA";

            //Error at line 3: syntax error, unexpected ADDOP, expecting RPAREN or COMMA
            System.out.println(message);
            logErr(message + "\n");

        } 
    RPAREN {
                //FN DEF WITH ARGS
                int lineNo_now = $RPAREN.getLine();

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
                    if(splitted.length < 2) {
                        continue;
                    }
                    paramList.add(splitted[0]);
                    IDList.add(splitted[1]);
                    System.out.print("type : " + splitted[0] + ", ID : " + splitted[1] + "|");
                }
                System.out.println("");


                boolean isInserted = STinsert($ID.getText(), "ID", ret_type, paramList, true); //DONE
                boolean isFunc = isFunction($ID.getText());

                

                // Previously declared function 
                if(!isInserted && isFunc){
                    System.out.println("Defining a previously declared fn " + $ID.getText());
                    String fnType = getFuncType($ID.getText());
                    System.out.println("    prev Type " + fnType);
                    System.out.println("    now Type " + ret_type);
                    if(!fnType.equalsIgnoreCase(ret_type)){
                        //Error at line 24: Return type mismatch of foo3
                        System.out.println("Error at line " + lineNo_now + ": Return type mismatch of " + $ID.getText());
                        logErr("Error at line " + lineNo_now + ": Return type mismatch of " + $ID.getText() + "\n");
                    }

                    SymbolInfo fn_info = Main.st.lookUp(new SymbolInfo($ID.getText(), "ID", null, null));
                    ArrayList<String> prev_params = fn_info.getParameters();
                    System.out.println("  prev params : " + prev_params + "| now params : " + paramList);

                    if(prev_params.size() != paramList.size()){
                        //Error at line 32: Total number of arguments mismatch with declaration in function var
                        System.out.println("Error at line " + lineNo_now + 
                                ": Total number of arguments mismatch with declaration in function " + $ID.getText());
                        logErr("Error at line " + lineNo_now + 
                                ": Total number of arguments mismatch with declaration in function " + $ID.getText() + "\n");
                    }
                }

                if(!isInserted && !isFunc){
                    //Error at line 28: Multiple declaration of z
                    System.out.println("Error at line " + lineNo_now + ": Multiple declaration of " + $ID.getText());
                    logErr("Error at line " + lineNo_now + ": Multiple declaration of " + $ID.getText() + "\n");
                }
                //----- INSERT INTO SYMBOL TABLE ---------
                enterScope();
                for (int i = 0; i < IDList.size(); i++) {
                    String id = IDList.get(i);
                    String idType = paramList.get(i);
                    if(STinsert(id, "ID", idType, null, false)){
                        System.out.println("Inserted " + id + " for curr func");
                    }
                    else{
                        System.out.println("Can not insert " + id + " for curr func");
                    }
                }
                Main.isFuncDefined = true;

            } 
    cstat = compound_statement
        {
            //FN DEF WITH ARGS
            int lineNo = $cstat.Cstat_rri.lineNo;
            String fn_type = $ts.type_rri.text;
            String arg_list = "(" + $p.param_rri.text + ")";
            String fn_body = $cstat.Cstat_rri.text;
            String text = fn_type + " " + $ID.getText() + arg_list + fn_body + "\n";

            if(fn_body.contains("return") && fn_type.equalsIgnoreCase("void")){
                //Error at line 38: Cannot return value from function foo4 with void return type 
                System.out.println("Error : void Function " + $ID.getText() + " returns a value");
                logErr("Error at line " + lineNo + ": Cannot return value from function " + $ID.getText() + " with void return type\n");

            }

            wParserLog("Line " + lineNo + ": func_definition : type_specifier ID LPAREN RPAREN compound_statement\n");
            wParserLog(text);
            Main.isFuncDefined = false;
            $fdef_rri = new RuleReturnInfo(lineNo, text);
        }
    
    ;
parameter_list
	returns[RuleReturnInfo param_rri]:
	p = parameter_list COMMA t = type_specifier ID {
            //fn defn multiple args int foo(int a, float b)
            int lineNo = $COMMA.line;
            String type = $t.type_rri.text;
            String id = $ID.text;
            String prev_param_list = $p.param_rri.text;

            String[] params = prev_param_list.split(",");
            ArrayList<String> ids = new ArrayList<>();
            for(String param : params){
                String[] splitted = param.split(" ");
                // System.out.println("param : " + splitted[1]);
                ids.add(splitted[1]);
            }

            //update param_rri
            if(ids.contains(id)){
                //Error at line 20: Multiple declaration of a in parameter
                System.out.println("Error at line " + lineNo + ": Multiple declaration of " + id + " in parameter\n");

                wErrorLog("Error at line " + lineNo + ": Multiple declaration of " + id + " in parameter\n");
                wParserLog("Error at line " + lineNo + ": Multiple declaration of " + id + " in parameter\n");
                Main.syntaxErrorCount++;
            }
            String curr_param_list = prev_param_list + "," + type + " " + id;
            $param_rri = new RuleReturnInfo(lineNo, curr_param_list);

            wParserLog("Line " + lineNo + ": parameter_list : parameter_list COMMA type_specifier ID\n");
            wParserLog($param_rri.text + "\n");
        }
	| p = parameter_list COMMA t = type_specifier {
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
	| t = type_specifier ID { 
            //fn defn must int foo(int a)
            int lineNo = $t.type_rri.lineNo;
            String type = $t.type_rri.text;
            String id = $ID.text;

            String curr_param_list = type + " " + id;

            wParserLog("Line " + lineNo + ": parameter_list : type_specifier ID\n");
            wParserLog(curr_param_list + "\n");

            $param_rri = new RuleReturnInfo(lineNo, curr_param_list);

        }
	| t = type_specifier { 
            //used in func decl int foo(int)
            System.out.println("In parameter list: type_specifier");
            int lineNo = $t.type_rri.lineNo;
            String text = $t.type_rri.text;
            wParserLog("Line " + lineNo + ": parameter_list : type_specifier\n");
            wParserLog(text + "\n");    

            $param_rri = new RuleReturnInfo(lineNo, text);
        };

compound_statement
	returns[RuleReturnInfo Cstat_rri]:
	LCURL {
            //ENTER new scope if not inserted from function already
            if(!Main.isFuncDefined){
                System.out.println("Entering new scope");
                enterScope();

            }
        } s = statements RCURL {
            int lineNo = $RCURL.line;
            String text = "{\n" + $s.Stats_rri.text + "}\n";
            wParserLog("Line " + lineNo + ": compound_statement : LCURL statements RCURL\n");
            wParserLog(text);
            $Cstat_rri = new RuleReturnInfo(lineNo, text);

            //PRINT SYMBOL TABLE AND EXIT SCOPE
            STprint();
            exitScope();
        }
	| LCURL
        {
            if(!Main.isFuncDefined){
                System.out.println("Entering new scope");
                enterScope();
            }
        } 
    RCURL { 
            wParserLog("Line " + $RCURL.line + ": compound_statement : LCURL RCURL\n");
            wParserLog("{}\n");
            $Cstat_rri = new RuleReturnInfo($RCURL.line, "{}");
            STprint();
            exitScope();
        };

var_declaration
	returns[RuleReturnInfo vdec_rri]:
	t = type_specifier dl = declaration_list sm = SEMICOLON {
        wParserLog(
            "Line " + $sm.getLine()
            +": var_declaration : type_specifier declaration_list SEMICOLON\n"
        );

        int lineNo = $sm.getLine();
        String text = $t.type_rri.text + " " + $dl.dec_list + ";";
        $vdec_rri = new RuleReturnInfo(lineNo, text);

        String type = $t.type_rri.text;

        if(type.equalsIgnoreCase("void")){
            //Error at line 42: Variable type cannot be void
            System.out.println("Error at line " + lineNo + ": Variable type cannot be void");
            logErr("Error at line " + lineNo + ": Variable type cannot be void" + "\n");
        }

        wParserLog(text + "\n");

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
	| t = type_specifier de = declaration_list_err sm = SEMICOLON {
        wErrorLog(
            "Line# "
            + $sm.getLine()
            + " with error name: "
            + $de.error_name
            + " - Syntax error at declaration list of variable declaration"
        );
        Main.syntaxErrorCount++;
      };

declaration_list_err
	returns[String error_name]: { $error_name = "Error in declaration list"; };

type_specifier
	returns[RuleReturnInfo type_rri]:
	INT {
        $type_rri = new RuleReturnInfo();
        $type_rri.lineNo = $INT.line;
        $type_rri.text = "int";
        wParserLog("Line " + $INT.line + ": type_specifier : INT\n");
        wParserLog($INT.text + "\n");
      }
	| FLOAT {
        $type_rri = new RuleReturnInfo();
        $type_rri.lineNo = $FLOAT.line;
        $type_rri.text = "float";
        wParserLog("Line " + $FLOAT.line + ": type_specifier : FLOAT\n");
        wParserLog($FLOAT.text + "\n");
      }
	| VOID {
        $type_rri = new RuleReturnInfo();
        $type_rri.lineNo = $VOID.line;
        $type_rri.text = "void";
        wParserLog("Line " + $VOID.line + ": type_specifier : VOID\n");
        wParserLog($VOID.text + "\n");
      };

declaration_list
	returns[String dec_list]:
	dl = declaration_list COMMA ID {
            //multiple decl
            $dec_list = $dl.dec_list + "," + $ID.text;
            wParserLog("Line " + $ID.line + ": declaration_list : declaration_list COMMA ID\n");
            wParserLog($dec_list + "\n");
        }
    | dl=declaration_list np=non_params COMMA ID
        {
            // decl ERROR
            String msg = "Error at line " + $ID.getLine() + ": syntax error, unexpected " 
                    + $np.non_param_rri.text + ", expecting COMMA or SEMICOLON";

            System.out.println(msg);
            $dec_list = $dl.dec_list + "," + $ID.text;

            System.out.println("Line no " + $ID.getLine() + " declaration list: " + $dec_list);

            logErr(msg + "\n");
            wParserLog("Line " + $ID.line + ": declaration_list : declaration_list COMMA ID\n");
            wParserLog($dec_list + "\n");
        }


	| dl = declaration_list COMMA ID LTHIRD CONST_INT RTHIRD {
            //array er shathe aaro kichu
            $dec_list = $dl.dec_list + "," + $ID.text + "[" + $CONST_INT.text + "]";

            boolean aldyExists = STlookupCurrScope($ID.text);
            if(aldyExists){
                wErrorLog("Error at line " + $ID.line + ": Multiple declaration of " + $ID.text + "\n");
                wParserLog("Error at line " + $ID.line + ": Multiple declaration of " + $ID.text + "\n");
                Main.syntaxErrorCount++;
            }


            System.out.println("Line no " + $ID.getLine() + " : Prev declaration: " + $dl.dec_list);
            wParserLog("Line " + $ID.line + ": declaration_list : declaration_list COMMA ID LTHIRD CONST_INT RTHIRD\n");
            wParserLog($dec_list + "\n");
        }
	| ID {
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
	| ID LTHIRD CONST_INT RTHIRD {
            //array decl
            $dec_list = $ID.text + "[" + $CONST_INT.text + "]";
            wParserLog("Line " + $ID.line + ": declaration_list : ID LTHIRD CONST_INT RTHIRD\n");
            wParserLog($dec_list + "\n");
        };

statements
	returns[RuleReturnInfo Stats_rri]:
	s = statement {
            int lineNo = $s.stat_rri.lineNo;
            String text = $s.stat_rri.text;

            wParserLog("Line " + lineNo + ": statements : statement\n");
            wParserLog(text);
            $Stats_rri = new RuleReturnInfo(lineNo, text);
        }
	| ss = statements s = statement {

            if(isUnrecognizedChar){
                int lineNo = $s.stat_rri.lineNo;
                String msg = "statements : statements statement (jamuna)";
                // wParserLog("Line " + lineNo + ": " + msg + "\n");
                // wParserLog($s.stat_rri.text + "\n");
                $Stats_rri = new RuleReturnInfo(lineNo, $ss.Stats_rri.text);
                isUnrecognizedChar = false; // reset after logging
            }
            else{
                int lineNo = $s.stat_rri.lineNo;
                String text = $ss.Stats_rri.text + $s.stat_rri.text;

                wParserLog("Line " + lineNo + ": statements : statements statement\n");
                wParserLog(text);
                $Stats_rri = new RuleReturnInfo(lineNo, text);
            }
        };

statement
	returns[RuleReturnInfo stat_rri]:
	vd = var_declaration { 
            int lineNo = $vd.vdec_rri.lineNo;
            String text = $vd.vdec_rri.text + "\n";

            $stat_rri = new RuleReturnInfo(lineNo, text);
            wParserLog("Line " + lineNo + ": statement : var_declaration\n");
            wParserLog(text);
        }
	| es = expression_statement {

            if(isUnrecognizedChar){
                int lineNo = $es.Expr_stat_rri.lineNo;
                String msg = "statement : expression_statement (jamuna)";
                // wParserLog("Line " + lineNo + ": " + msg + "\n");
                // wParserLog($es.Expr_stat_rri.text + "\n");
                $stat_rri = new RuleReturnInfo(lineNo, $es.Expr_stat_rri.text + "\n");
            }
            else{
                int lineNo = $es.Expr_stat_rri.lineNo;
                String text = $es.Expr_stat_rri.text + "\n";

                $stat_rri = new RuleReturnInfo(lineNo, text);
                wParserLog("Line " + lineNo + ": statement : expression_statement\n");
                wParserLog(text);
            }
        }
	| c = compound_statement {
            int lineNo = $c.Cstat_rri.lineNo;
            String text = $c.Cstat_rri.text;
            $stat_rri = new RuleReturnInfo(lineNo, text);
            wParserLog("Line " + lineNo + ": statement : compound_statement\n");
            wParserLog(text);
            
        }
	| FOR LPAREN es1 = expression_statement es2 = expression_statement e = expression RPAREN s =
		statement {
            int lineNo = $s.stat_rri.lineNo;
            String condns = $es1.Expr_stat_rri.text
                            + $es2.Expr_stat_rri.text
                            + $e.Expr_rri.text;

            String text = "for(" + condns + ")" + $s.stat_rri.text;
            $stat_rri = new RuleReturnInfo(lineNo, text);
            wParserLog("Line " + lineNo + ": statement : FOR LPAREN expression_statement expression_statement expression RPAREN statement\n");
            wParserLog(text);

        }
	| IF LPAREN e = expression RPAREN s = statement {
            int lineNo = $s.stat_rri.lineNo;

            String expr = $e.Expr_rri.text;
            String st = $s.stat_rri.text;
            String text = "if(" + expr + ")" + st;
            $stat_rri = new RuleReturnInfo(lineNo, text);
            wParserLog("Line " + lineNo + ": statement : IF LPAREN expression RPAREN statement\n");
            wParserLog(text);
        }
	| IF LPAREN e = expression RPAREN s1 = statement ELSE s2 = statement {
            int lineNo = $s2.stat_rri.lineNo;

            String expr = $e.Expr_rri.text;
            String st1 = $s1.stat_rri.text;
            String st2 = $s2.stat_rri.text;

            String text = "if(" + expr + ")" + st1.stripTrailing() + "else " + st2;
            $stat_rri = new RuleReturnInfo(lineNo, text);
            wParserLog("Line " + lineNo + ": statement : IF LPAREN expression RPAREN statement ELSE statement\n");
            wParserLog(text);

        }
	| WHILE LPAREN e = expression RPAREN s = statement {
            // while(a[0]--){
            //     c = c - 2;
            // }

            int lineNo = $s.stat_rri.lineNo;
            String condns = $e.Expr_rri.text;

            String text = "while(" + condns + ")" + $s.stat_rri.text;
            $stat_rri = new RuleReturnInfo(lineNo, text);
            wParserLog("Line " + lineNo + ": statement : WHILE LPAREN expression RPAREN statement\n");
            wParserLog(text);


        }
	| PRINTLN LPAREN ID RPAREN SEMICOLON {
            System.out.println("Matched " + $PRINTLN.getText());
            int lineNo = $SEMICOLON.getLine();
            String text = "printf("  + $ID.getText() + ");\n";
            $stat_rri = new RuleReturnInfo(lineNo, text);
            wParserLog("Line " + lineNo + ": statement : PRINTLN LPAREN ID RPAREN SEMICOLON\n");

            //Check for Undeclared variable
            SymbolInfo var_info = Main.st.lookUp(new SymbolInfo($ID.getText(), "ID", null, null));
            boolean isNotDeclared = (var_info == null);
            if(isNotDeclared){
                //Error at line 67: Undeclared variable h
                logErr("Error at line " + lineNo + ": Undeclared variable " + $ID.getText() + "\n");
                System.out.println("Error at line " + lineNo + ": Undeclared variable " + $ID.getText() + "\n");
            }
            
            wParserLog(text);

        }
	| RETURN e = expression SEMICOLON {
            int lineNo = $RETURN.getLine();
            String text = "return" + " " + $e.Expr_rri.text + ";\n";

            wParserLog("Line " + lineNo + ": statement : RETURN expression SEMICOLON\n");
            wParserLog(text);
            $stat_rri = new RuleReturnInfo(lineNo, text);

        };

expression_statement
	returns[RuleReturnInfo Expr_stat_rri]:
	SEMICOLON {
            int lineNo = $SEMICOLON.getLine();
            String text = ";";
            wParserLog("Line " + lineNo + ": expression_statement : SEMICOLON\n");
            wParserLog(text + "\n");
            $Expr_stat_rri = new RuleReturnInfo(lineNo, text);
        }
	| e = expression SEMICOLON {
            if(isUnrecognizedChar){
                int lineNo = $e.Expr_rri.lineNo;
                String msg = "expression_statement : expression SEMICOLON (jamuna)";
                // wParserLog("Line " + lineNo + ": " + msg + "\n");
                // wParserLog($e.Expr_rri.text + "\n");
                $Expr_stat_rri = new RuleReturnInfo(lineNo, $e.Expr_rri.text);   
            }

            else{
                int lineNo = $SEMICOLON.getLine();
                String text = $e.Expr_rri.text + ";";

                wParserLog("Line " + lineNo + ": expression_statement : expression SEMICOLON\n");
                wParserLog(text + "\n");
                $Expr_stat_rri = new RuleReturnInfo(lineNo, text); 
                isValidStatement = true;           
            }
            
        };

variable
	returns[RuleReturnInfo var_rri]:
	ID {
            // USUAL VARIABLE
            int lineNo = $ID.getLine();
            $var_rri = new RuleReturnInfo(lineNo, $ID.getText());

            wParserLog("Line " + lineNo + ": variable : ID\n");

            String id = $ID.getText();
            SymbolInfo var_info = Main.st.lookUp(new SymbolInfo(id, "ID", null, null));
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
	| ID LTHIRD e = expression RTHIRD {
            // ARRAY VARIABLES
            int lineNo = $ID.getLine();
            String expr = $e.Expr_rri.text;

            String text = $ID.getText() + "[" + expr + "]";
            boolean isArray = isArrayVar($ID.getText());
            wParserLog("Line " + lineNo + ": variable : ID LTHIRD expression RTHIRD\n");


            if(!isArray){
                // Error at line 52: b not an array
                String message = "Error at line " + lineNo + ": " + $ID.getText() + " not an array\n";
                System.out.println(message);
                logErr(message);
                isValidStatement = false;
            }
            $var_rri = new RuleReturnInfo(lineNo, text);

            if(!isInteger(expr)) {
                wErrorLog("Error at line " + lineNo + ": Expression inside third brackets not an integer\n");
                wParserLog("Error at line " + lineNo + ": Expression inside third brackets not an integer\n");
                Main.syntaxErrorCount++;     
                isValidStatement = false;
            }

            wParserLog(text + "\n");

        };

expression
	returns[RuleReturnInfo Expr_rri]:
	l = logic_expression {
            int lineNo = $l.LogicExpr_rri.lineNo;
            String text = $l.LogicExpr_rri.text;
            wParserLog("Line " + lineNo + ": expression : logic_expression\n");
            wParserLog(text + "\n");
            $Expr_rri = new RuleReturnInfo(lineNo, text);
        }
	| var = variable ASSIGNOP l = logic_expression {
            if(isUnrecognizedChar){
                int lineNo = $l.LogicExpr_rri.lineNo;
                String msg = "expression : logic_expression";
                wParserLog("Line " + lineNo + ": " + msg + "\n");
                wParserLog($l.LogicExpr_rri.text + "\n");
                $Expr_rri = new RuleReturnInfo(lineNo, $l.LogicExpr_rri.text);   

            }
            else{
                int lineNo = $l.LogicExpr_rri.lineNo;
                String var_name = $var.var_rri.text;
                SymbolInfo var_info = Main.st.currentScopeLookup(new SymbolInfo(var_name, "ID", null, null));
                boolean isNotDeclared = (var_info == null);
                boolean isArrVar = (var_name.contains("[") && var_name.contains("]"));

                String logic_expr = $l.LogicExpr_rri.text;
                String expr_type = typeDetector(logic_expr);
                
                wParserLog("Line " + lineNo + ": expression : variable ASSIGNOP logic_expression\n");
                if(expr_type.equals("void") && isValidStatement){
                    //Error at line 57: Void function used in expression
                    System.out.println("Error at line " + lineNo + ": Void function used in expression");
                    logErr("Error at line " + lineNo + ": Void function used in expression\n");
                    isValidStatement = false;
                }

                String text = var_name + "=" + logic_expr;
                
                // CHECK FOR TYPE MISMATCH ERROR (for declared variables (Not array ones))
                if(!isNotDeclared){
                    ArrayList<String> validTypes = new ArrayList<>();
                    validTypes.add("int");
                    validTypes.add("float");

                    boolean isValidExprType = validTypes.contains(expr_type);
                    boolean isValidVarType = validTypes.contains(var_info.getDataType());
                    if (isValidExprType && isValidVarType && !var_info.getDataType().equalsIgnoreCase(expr_type) && isValidStatement) {
                        //Error at line 8: Type Mismatch
                        System.out.println("ERROR : Assigning " + var_name + "(" + var_info.getDataType() + ")" 
                                    + " with " + logic_expr + "(" + expr_type + ")");

                        wErrorLog("Error at line " + lineNo + ": Type Mismatch" + "\n");
                        wParserLog("Error at line " + lineNo + ": Type Mismatch" + "\n");
                        Main.syntaxErrorCount++;
                    }
                }

                // CHECK FOR TYPE MISMATCH ERROR (for array variables)
                if(isArrVar && isValidStatement){
                    String varType = typeDetector(var_name);
                    if(!varType.equalsIgnoreCase(expr_type)){
                        //Error at line 58: Type Mismatch
                        System.out.println("Error at line " + lineNo + ": Type Mismatch");
                        logErr("Error at line " + lineNo    + ": Type Mismatch\n");
                    }
                }

                wParserLog(text + "\n");
                $Expr_rri = new RuleReturnInfo(lineNo, text);   
            }            
        };

logic_expression
	returns[RuleReturnInfo LogicExpr_rri]:
	r = rel_expression {
            int lineNo = $r.relExpr_rri.lineNo;
            String text = $r.relExpr_rri.text;
            wParserLog("Line " + lineNo + ": logic_expression : rel_expression\n");
            wParserLog(text + "\n");
            $LogicExpr_rri = new RuleReturnInfo(lineNo, text);
        }
	| r1 = rel_expression LOGICOP r2 = rel_expression {
            int lineNo = $LOGICOP.getLine();
            String text = $r1.relExpr_rri.text + $LOGICOP.getText() + $r2.relExpr_rri.text;
            wParserLog("Line " + lineNo + ": logic_expression : rel_expression LOGICOP rel_expression\n");
            wParserLog(text + "\n");
            $LogicExpr_rri = new RuleReturnInfo(lineNo, text);
        };

rel_expression
	returns[RuleReturnInfo relExpr_rri]:
	s = simple_expression {
            int lineNo = $s.sm_expr_rri.lineNo;
            String text = $s.sm_expr_rri.text;
            wParserLog("Line " + lineNo + ": rel_expression : simple_expression\n");
            wParserLog(text + "\n");
            $relExpr_rri = new RuleReturnInfo(lineNo, text);
        }
	| s1 = simple_expression RELOP s2 = simple_expression {
            int lineNo = $RELOP.getLine();
            String text = $s1.sm_expr_rri.text + $RELOP.getText() + $s2.sm_expr_rri.text;
            wParserLog("Line " + lineNo + ": rel_expression : simple_expression RELOP simple_expression\n");
            wParserLog(text + "\n");
            $relExpr_rri = new RuleReturnInfo(lineNo, text);

        };

simple_expression
	returns[RuleReturnInfo sm_expr_rri]:
	t = term {
            int lineNo = $t.term_rri.lineNo;
            String text = $t.term_rri.text; 
            wParserLog("Line " + lineNo + ": simple_expression : term\n");
            wParserLog(text + "\n");
            $sm_expr_rri = new RuleReturnInfo(lineNo, text);

        }
	| sm_expr = simple_expression ADDOP t = term {
            int lineNo = $ADDOP.getLine();
            String op = $ADDOP.getText();
            String text = $sm_expr.sm_expr_rri.text + op + $t.term_rri.text;
            wParserLog("Line " + lineNo + ": simple_expression : simple_expression ADDOP term\n");
            wParserLog(text + "\n");
            $sm_expr_rri = new RuleReturnInfo(lineNo, text);
        }
    | sm=simple_expression ADDOP t=term ic=invalid_char
        {
            int lineNo = $ADDOP.getLine();
            int errLineNo = $ic.invalid_rri.lineNo;
        

            String errMsg = "Error at line " + errLineNo + ": Unrecognized character " + $ic.invalid_rri.text;
            System.out.println(errMsg);
            logErr(errMsg + "\n");
                        
            String op = $ADDOP.getText();
            String text = $t.term_rri.text;
            int newLineNo = errLineNo + 1;
            wParserLog("Line " + newLineNo + ": simple_expression : term\n");
            wParserLog(text + "\n");
            $sm_expr_rri = new RuleReturnInfo(newLineNo, text);
            isUnrecognizedChar = true;
        }
    ;


term
	returns[RuleReturnInfo term_rri]:
	u = unary_expression {
            int lineNo = $u.unary_rri.lineNo;
            String text = $u.unary_rri.text; 
            wParserLog("Line " + lineNo + ": term : unary_expression\n");
            wParserLog(text + "\n");
            $term_rri = new RuleReturnInfo(lineNo, text);

        }
	| t = term MULOP ue = unary_expression {
            int lineNo = $MULOP.getLine();
            String text = $t.term_rri.text + $MULOP.getText() + $ue.unary_rri.text; 
            wParserLog("Line " + lineNo + ": term : term MULOP unary_expression\n");

            String unary_expr_type = evaluateExpressionType($ue.unary_rri.text);
            if(unary_expr_type.equals("void")){
                //Error at line 54: Void function used in expression
                System.out.println("Error at line " + lineNo + ": Void function used in expression");
                logErr("Error at line " + lineNo + ": Void function used in expression\n");
                isValidStatement = false;
            }

            // Error at line 9: Non-Integer operand on modulus operator
            if($MULOP.getText().equals("%")){
                String expr_type = typeDetector($ue.unary_rri.text);
                if(!expr_type.equals("int")){
                    logErr("Error at line " + lineNo + ": Non-Integer operand on modulus operator\n");
                }
                else{
                    //check if expression is 0
                    try {
                        int value = Integer.parseInt($ue.unary_rri.text);
                        if(value == 0){
                            //Error at line 59: Modulus by Zero
                            System.out.println("Error at line " + lineNo + ": Modulus by Zero\n");
                            logErr("Error at line " + lineNo + ": Modulus by Zero\n");
                        }
                    } catch (NumberFormatException e) {}
                }
            }

            wParserLog(text + "\n");
            $term_rri = new RuleReturnInfo(lineNo, text);

        };

unary_expression
	returns[RuleReturnInfo unary_rri]:
	ADDOP u = unary_expression {
            int lineNo = $ADDOP.getLine();
            String text = $ADDOP.getText() + $u.unary_rri.text; 
            wParserLog("Line " + lineNo + ": unary_expression : ADDOP unary_expression\n");
            wParserLog(text + "\n");

            String type = $u.unary_rri.expr_type;
            $unary_rri = new RuleReturnInfo(lineNo, text, type);
        }
	| NOT u = unary_expression {
            int lineNo = $NOT.getLine();
            String text = $NOT.getText() + $u.unary_rri.text; 
            wParserLog("Line " + lineNo + ": unary_expression : NOT unary_expression\n");
            wParserLog(text + "\n");
            String type = $u.unary_rri.expr_type;

            $unary_rri = new RuleReturnInfo(lineNo, text, type);
        }
	| f = factor {
            int lineNo = $f.fact_rri.lineNo;
            String text = $f.fact_rri.text; 
            wParserLog("Line " + lineNo + ": unary_expression : factor\n");
            wParserLog(text + "\n");
            String expr_type = $f.fact_rri.expr_type;
            $unary_rri = new RuleReturnInfo(lineNo, text, expr_type);
        };

factor
	returns[RuleReturnInfo fact_rri]:
	var = variable {
            int lineNo = $var.var_rri.lineNo;
            String text = $var.var_rri.text;

            wParserLog("Line " + lineNo + ": factor : variable\n");
            wParserLog(text + "\n");

            $fact_rri = new RuleReturnInfo(lineNo, text);
        }
	| ID LPAREN al = argument_list RPAREN {
            //FUNC CALL
            int lineNo = $ID.getLine();
            String text = $ID.getText() + "(" + $al.argList_rri.text + ")";
            String fnName = $ID.getText().trim();
            String type = getFuncType(fnName);
            wParserLog("Line " + lineNo + ": factor : ID LPAREN argument_list RPAREN\n");


            SymbolInfo fn_info = Main.st.lookUp(new SymbolInfo(fnName, "ID", null, null));
            if(fn_info == null){
                //Error at line 62: Undefined function foo5
                System.out.println("Error at line " + lineNo + ": Undefined function " + fnName);
                logErr("Error at line " + lineNo + ": Undefined function " + fnName + "\n");
            }
            else{
                //Check if called with valid type of arguments
                ArrayList<String> fnParams = fn_info.getParameters();
                System.out.println("Function " + fnName + " params : " + fnParams);
                String[] args = $al.argList_rri.text.split(",");
                System.out.println("Function " + fnName + " called with args : " + Arrays.toString(args));
                if(args.length != fnParams.size()){
                    //Error at line 49: Total number of arguments mismatch with declaration in function correct_foo
                    System.out.println("Error at line " + lineNo + ": Total number of arguments mismatch with declaration in function " + fnName);
                    logErr("Error at line " + lineNo + ": Total number of arguments mismatch with declaration in function " + fnName + "\n");
                }

                else{
                    for(int i = 0; i < args.length; i++){
                        String arg = args[i].trim();
                        String argType = typeDetector(arg);
                        String paramType = fnParams.get(i).trim();
                        System.out.println("Arg " + (i+1) + " type : " + argType + "| Param " + (i+1) + " type : " + paramType);
                        boolean isArray = false;

                        if (arg.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
                            isArray = isArrayVar(arg);
                        }

                        if(isArray){
                            continue; // Skip array args for type checking
                        }

                        if(!argType.equals(paramType)){
                            int argNo = i + 1;
                            System.out.println("Error at line " + lineNo + ": " + argNo + "th argument mismatch in function " + fnName);
                            logErr("Error at line " + lineNo + ": " + argNo + "th argument mismatch in function " + fnName + "\n");
                            break;
                        }
                    }
                }
            }

            wParserLog(text + "\n");

            $fact_rri = new RuleReturnInfo(lineNo, text, type);            
        }
	| LPAREN e = expression RPAREN {
            int lineNo = $RPAREN.getLine();
            String text = "(" + $e.Expr_rri.text + ")";

            wParserLog("Line " + lineNo + ": factor : LPAREN expression RPAREN\n");
            wParserLog(text + "\n");

            $fact_rri = new RuleReturnInfo(lineNo, text);
        }
	| c = CONST_INT {
            int lineNo = $c.getLine();
            String text = $c.getText();

            wParserLog("Line " + lineNo + ": factor : CONST_INT\n");
            wParserLog(text + "\n");

            $fact_rri = new RuleReturnInfo(lineNo, text);
            $fact_rri.expr_type = "int"; // Set type for CONST_INT
        }
    | ASSIGNOP c=CONST_INT 
        {
            int lineNo = $c.getLine();
            String text = $c.getText();

            String msg = "Error at line " + lineNo +": syntax error, unexpected ASSIGNOP";
            logErr(msg + "\n");
            System.out.println(msg);
            wParserLog("Line " + lineNo + ": factor : CONST_INT\n");
            wParserLog(text + "\n");

            $fact_rri = new RuleReturnInfo(lineNo, text);
            $fact_rri.expr_type = "int"; 
        }

	| c = CONST_FLOAT {
            int lineNo = $c.getLine();
            String text = $c.getText();

            wParserLog("Line " + lineNo + ": factor : CONST_FLOAT\n");
            wParserLog(text + "\n");

            $fact_rri = new RuleReturnInfo(lineNo, text, "float"); // Set type for CONST_FLOAT
        }
	| v = variable INCOP {
            int lineNo = $INCOP.getLine();
            String text = $v.var_rri.text + $INCOP.getText(); 

            wParserLog("Line " + lineNo + ": factor : variable INCOP\n");
            wParserLog(text + "\n");

            $fact_rri = new RuleReturnInfo(lineNo, text);

        }
	| v = variable DECOP {
            int lineNo = $DECOP.getLine();
            String text = $v.var_rri.text + $DECOP.getText(); 

            wParserLog("Line " + lineNo + ": factor : variable DECOP\n");
            wParserLog(text + "\n");

            $fact_rri = new RuleReturnInfo(lineNo, text);

        };

argument_list
	returns[RuleReturnInfo argList_rri]:
	a = arguments {
            // arguments -> work with single/multiple arguments
            // argument_list -> work with all arguments together

            int lineNo = $a.arg_rri.lineNo;
            String text = $a.arg_rri.text;

            wParserLog("Line " + lineNo + ": argument_list : arguments\n");
            wParserLog(text + "\n");
            $argList_rri = new RuleReturnInfo(lineNo, text);
        }
	| /* empty */;

arguments
	returns[RuleReturnInfo arg_rri]:
	a = arguments COMMA le = logic_expression {
            int lineNo = $COMMA.getLine();
            String text = $a.arg_rri.text + "," + $le.LogicExpr_rri.text;

            wParserLog("Line " + lineNo + ": arguments : arguments COMMA logic_expression\n");
            wParserLog(text + "\n");
            $arg_rri = new RuleReturnInfo(lineNo, text);
        }
	| l = logic_expression {
            int lineNo = $l.LogicExpr_rri.lineNo;
            String text = $l.LogicExpr_rri.text;

            wParserLog("Line " + lineNo + ": arguments : logic_expression\n");
            wParserLog(text + "\n");
            $arg_rri = new RuleReturnInfo(lineNo, text);
        };

// Error handling grammers
non_params returns [RuleReturnInfo non_param_rri]:
    LPAREN
        {
            $non_param_rri = new RuleReturnInfo($LPAREN.line, "LPAREN");
        } 
    | LCURL {
            $non_param_rri = new RuleReturnInfo($LCURL.line, "LCURL");
        }
    | RCURL {
            $non_param_rri = new RuleReturnInfo($RCURL.line, "RCURL");
            }
    | SEMICOLON {
            $non_param_rri = new RuleReturnInfo($SEMICOLON.line, "SEMICOLON");
        }
    | LTHIRD {$non_param_rri = new RuleReturnInfo($LTHIRD.line, "LTHIRD");}
    | RTHIRD {
            $non_param_rri = new RuleReturnInfo($RTHIRD.line, "RTHIRD");
    }
    | ADDOP {$non_param_rri = new RuleReturnInfo($ADDOP.line, "ADDOP");} 
    | MULOP {$non_param_rri = new RuleReturnInfo($MULOP.line, "MULOP");} 
    | RELOP {$non_param_rri = new RuleReturnInfo($RELOP.line, "RELOP");}
    | LOGICOP {$non_param_rri = new RuleReturnInfo($LOGICOP.line, "LOGICOP");} 
    | INCOP {$non_param_rri = new RuleReturnInfo($INCOP.line, "INCOP");} 
    | DECOP {$non_param_rri = new RuleReturnInfo($DECOP.line, "DECOP");}
    | NOT 
        {
            $non_param_rri = new RuleReturnInfo($NOT.line, "NOT");
         };

// Error handling for invalid chars
invalid_char returns [RuleReturnInfo invalid_rri]
    :
    ic=INVALID_CHAR {
        //Error at line 10: Unrecognized character #
        // String errorMessage = "Error at line " + $ic.getLine() + ": Unrecognized character " + $ic.getText();
        // wParserLog(errorMessage + "\n");
        // logErr(errorMessage + "\n");
        $invalid_rri = new RuleReturnInfo($ic.getLine(), $ic.getText());
    };
