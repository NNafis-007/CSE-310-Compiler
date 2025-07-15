// Generated from C2105007Parser.g4 by ANTLR 4.13.2

import java.io.BufferedWriter;
import java.io.IOException;
import SymbolTable2.SymbolInfo2;
import java.util.Arrays;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class C2105007Parser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		LINE_COMMENT=1, BLOCK_COMMENT=2, STRING=3, WS=4, IF=5, ELSE=6, FOR=7, 
		WHILE=8, PRINTLN=9, RETURN=10, INT=11, FLOAT=12, VOID=13, LPAREN=14, RPAREN=15, 
		LCURL=16, RCURL=17, LTHIRD=18, RTHIRD=19, SEMICOLON=20, COMMA=21, ADDOP=22, 
		SUBOP=23, MULOP=24, INCOP=25, DECOP=26, NOT=27, RELOP=28, LOGICOP=29, 
		ASSIGNOP=30, ID=31, CONST_INT=32, CONST_FLOAT=33, INVALID_CHAR=34;
	public static final int
		RULE_start = 0, RULE_program = 1, RULE_unit = 2, RULE_func_declaration = 3, 
		RULE_func_definition = 4, RULE_parameter_list = 5, RULE_compound_statement = 6, 
		RULE_var_declaration = 7, RULE_declaration_list_err = 8, RULE_type_specifier = 9, 
		RULE_declaration_list = 10, RULE_statements = 11, RULE_statement = 12, 
		RULE_expression_statement = 13, RULE_variable = 14, RULE_expression = 15, 
		RULE_logic_expression = 16, RULE_rel_expression = 17, RULE_simple_expression = 18, 
		RULE_term = 19, RULE_unary_expression = 20, RULE_factor = 21, RULE_argument_list = 22, 
		RULE_arguments = 23, RULE_non_params = 24, RULE_invalid_char = 25;
	private static String[] makeRuleNames() {
		return new String[] {
			"start", "program", "unit", "func_declaration", "func_definition", "parameter_list", 
			"compound_statement", "var_declaration", "declaration_list_err", "type_specifier", 
			"declaration_list", "statements", "statement", "expression_statement", 
			"variable", "expression", "logic_expression", "rel_expression", "simple_expression", 
			"term", "unary_expression", "factor", "argument_list", "arguments", "non_params", 
			"invalid_char"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, null, null, "'if'", "'else'", "'for'", "'while'", "'printf'", 
			"'return'", "'int'", "'float'", "'void'", "'('", "')'", "'{'", "'}'", 
			"'['", "']'", "';'", "','", null, null, null, "'++'", "'--'", "'!'", 
			null, null, "'='"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "LINE_COMMENT", "BLOCK_COMMENT", "STRING", "WS", "IF", "ELSE", 
			"FOR", "WHILE", "PRINTLN", "RETURN", "INT", "FLOAT", "VOID", "LPAREN", 
			"RPAREN", "LCURL", "RCURL", "LTHIRD", "RTHIRD", "SEMICOLON", "COMMA", 
			"ADDOP", "SUBOP", "MULOP", "INCOP", "DECOP", "NOT", "RELOP", "LOGICOP", 
			"ASSIGNOP", "ID", "CONST_INT", "CONST_FLOAT", "INVALID_CHAR"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "C2105007Parser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }


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

	            SymbolInfo2 fn_info = Main.st.lookUp(new SymbolInfo2(fnName, "ID", null, null));
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
	            SymbolInfo2 varInfo = Main.st.currentScopeLookup(new SymbolInfo2(expr, "ID", null, null));
	            if(varInfo == null){
	                varInfo = Main.st.lookUp(new SymbolInfo2(expr, "ID", null, null));
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
	            SymbolInfo2 varInfo = Main.st.currentScopeLookup(new SymbolInfo2(varName, "ID", null, null));
	            if(varInfo == null){
	                varInfo = Main.st.lookUp(new SymbolInfo2(varName, "ID", null, null));
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
	        SymbolInfo2 varInfo = Main.st.currentScopeLookup(new SymbolInfo2(varName, "ID", null, null));
	        if(varInfo == null){
	            varInfo = Main.st.lookUp(new SymbolInfo2(varName, "ID", null, null));
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

	            SymbolInfo2 s1 = new SymbolInfo2(name, token_type, data_type, params);
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
	            SymbolInfo2 s = new SymbolInfo2(name, "ID", null, null);
	            SymbolInfo2 found = Main.st.currentScopeLookup(s);
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
	            SymbolInfo2 s = new SymbolInfo2(name, "ID", null, null);
	            SymbolInfo2 found = Main.st.currentScopeLookup(s);
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


	public C2105007Parser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StartContext extends ParserRuleContext {
		public ProgramContext p;
		public ProgramContext program() {
			return getRuleContext(ProgramContext.class,0);
		}
		public StartContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_start; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof C2105007ParserListener ) ((C2105007ParserListener)listener).enterStart(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof C2105007ParserListener ) ((C2105007ParserListener)listener).exitStart(this);
		}
	}

	public final StartContext start() throws RecognitionException {
		StartContext _localctx = new StartContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_start);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(52);
			((StartContext)_localctx).p = program(0);


			        int lineNo = ((StartContext)_localctx).p.prog_rri.lineNo;
			        wParserLog("Line " + lineNo + ": start : program\n");
			        STprint();

			        wParserLog("Total number of lines: " + lineNo);
			        wParserLog("Total number of errors: " + Main.syntaxErrorCount);
			      
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ProgramContext extends ParserRuleContext {
		public RuleReturnInfo prog_rri;
		public ProgramContext p;
		public UnitContext u;
		public UnitContext unit() {
			return getRuleContext(UnitContext.class,0);
		}
		public ProgramContext program() {
			return getRuleContext(ProgramContext.class,0);
		}
		public ProgramContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_program; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof C2105007ParserListener ) ((C2105007ParserListener)listener).enterProgram(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof C2105007ParserListener ) ((C2105007ParserListener)listener).exitProgram(this);
		}
	}

	public final ProgramContext program() throws RecognitionException {
		return program(0);
	}

	private ProgramContext program(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ProgramContext _localctx = new ProgramContext(_ctx, _parentState);
		ProgramContext _prevctx = _localctx;
		int _startState = 2;
		enterRecursionRule(_localctx, 2, RULE_program, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(56);
			((ProgramContext)_localctx).u = unit();

			            //print rri from unit
			            int lineNo = ((ProgramContext)_localctx).u.unit_rri.lineNo;
			            String text = ((ProgramContext)_localctx).u.unit_rri.text.stripTrailing();
			            wParserLog("Line " + lineNo + ": program : unit\n");
			            wParserLog(text + "\n");
			            ((ProgramContext)_localctx).prog_rri =  new RuleReturnInfo(lineNo, text);
			        
			}
			_ctx.stop = _input.LT(-1);
			setState(65);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new ProgramContext(_parentctx, _parentState);
					_localctx.p = _prevctx;
					pushNewRecursionContext(_localctx, _startState, RULE_program);
					setState(59);
					if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
					setState(60);
					((ProgramContext)_localctx).u = unit();

					                      int lineNo = ((ProgramContext)_localctx).u.unit_rri.lineNo;
					                      String text = ((ProgramContext)_localctx).p.prog_rri.text + "\n" + ((ProgramContext)_localctx).u.unit_rri.text.stripTrailing();
					                      wParserLog("Line " + lineNo + ": program : program unit\n");
					                      wParserLog(text + "\n");
					                      ((ProgramContext)_localctx).prog_rri =  new RuleReturnInfo(lineNo, text);
					                  
					}
					} 
				}
				setState(67);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class UnitContext extends ParserRuleContext {
		public RuleReturnInfo unit_rri;
		public Var_declarationContext vd;
		public Func_declarationContext fdec;
		public Func_definitionContext f;
		public Var_declarationContext var_declaration() {
			return getRuleContext(Var_declarationContext.class,0);
		}
		public Func_declarationContext func_declaration() {
			return getRuleContext(Func_declarationContext.class,0);
		}
		public Func_definitionContext func_definition() {
			return getRuleContext(Func_definitionContext.class,0);
		}
		public UnitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unit; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof C2105007ParserListener ) ((C2105007ParserListener)listener).enterUnit(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof C2105007ParserListener ) ((C2105007ParserListener)listener).exitUnit(this);
		}
	}

	public final UnitContext unit() throws RecognitionException {
		UnitContext _localctx = new UnitContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_unit);
		try {
			setState(77);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(68);
				((UnitContext)_localctx).vd = var_declaration();

				            int lineNo = ((UnitContext)_localctx).vd.vdec_rri.lineNo;
				            String text = ((UnitContext)_localctx).vd.vdec_rri.text;
				            wParserLog("Line " + lineNo + ": unit : var_declaration\n");
				            wParserLog(text + "\n");
				            ((UnitContext)_localctx).unit_rri =  new RuleReturnInfo(lineNo, text);
				        
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(71);
				((UnitContext)_localctx).fdec = func_declaration();

				            int lineNo = ((UnitContext)_localctx).fdec.fdec_rri.lineNo;
				            String text = ((UnitContext)_localctx).fdec.fdec_rri.text;
				            wParserLog("Line " + lineNo + ": unit : func_declaration\n");
				            wParserLog(text + "\n");
				            ((UnitContext)_localctx).unit_rri =  new RuleReturnInfo(((UnitContext)_localctx).fdec.fdec_rri);
				        
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(74);
				((UnitContext)_localctx).f = func_definition();

				            int lineNo = ((UnitContext)_localctx).f.fdef_rri.lineNo;
				            String text = ((UnitContext)_localctx).f.fdef_rri.text;
				            wParserLog("Line " + lineNo + ": unit : func_definition\n");
				            wParserLog(text);
				            ((UnitContext)_localctx).unit_rri =  new RuleReturnInfo(((UnitContext)_localctx).f.fdef_rri);
				        
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Func_declarationContext extends ParserRuleContext {
		public RuleReturnInfo fdec_rri;
		public Type_specifierContext t;
		public Token ID;
		public Parameter_listContext p;
		public TerminalNode ID() { return getToken(C2105007Parser.ID, 0); }
		public TerminalNode LPAREN() { return getToken(C2105007Parser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(C2105007Parser.RPAREN, 0); }
		public TerminalNode SEMICOLON() { return getToken(C2105007Parser.SEMICOLON, 0); }
		public Type_specifierContext type_specifier() {
			return getRuleContext(Type_specifierContext.class,0);
		}
		public Parameter_listContext parameter_list() {
			return getRuleContext(Parameter_listContext.class,0);
		}
		public Func_declarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_func_declaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof C2105007ParserListener ) ((C2105007ParserListener)listener).enterFunc_declaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof C2105007ParserListener ) ((C2105007ParserListener)listener).exitFunc_declaration(this);
		}
	}

	public final Func_declarationContext func_declaration() throws RecognitionException {
		Func_declarationContext _localctx = new Func_declarationContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_func_declaration);
		try {
			setState(94);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(79);
				((Func_declarationContext)_localctx).t = type_specifier();
				setState(80);
				((Func_declarationContext)_localctx).ID = match(ID);
				setState(81);
				match(LPAREN);
				setState(82);
				((Func_declarationContext)_localctx).p = parameter_list(0);
				setState(83);
				match(RPAREN);
				setState(84);
				match(SEMICOLON);

				            //FUNC declaration WITH ARGS

				            int lineNo = ((Func_declarationContext)_localctx).ID.getLine();
				            String type = ((Func_declarationContext)_localctx).t.type_rri.text;
				            String fn_name = ((Func_declarationContext)_localctx).ID.getText();
				            String params = ((Func_declarationContext)_localctx).p.param_rri.text;

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

				            String fd = type + " " + fn_name + "(" + ((Func_declarationContext)_localctx).p.param_rri.text + ");";
				            wParserLog("Line " + lineNo + ": func_declaration : type_specifier ID LPAREN parameter_list RPAREN SEMICOLON\n");
				            wParserLog(fd + "\n");

				            ((Func_declarationContext)_localctx).fdec_rri =  new RuleReturnInfo(lineNo, fd);
				        
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(87);
				((Func_declarationContext)_localctx).t = type_specifier();
				setState(88);
				((Func_declarationContext)_localctx).ID = match(ID);
				setState(89);
				match(LPAREN);
				setState(90);
				match(RPAREN);
				setState(91);
				match(SEMICOLON);

				            //FUNC declaration WITHOUT ARGS

				            int lineNo = ((Func_declarationContext)_localctx).ID.getLine();
				            String type = ((Func_declarationContext)_localctx).t.type_rri.text;
				            String fn_name = ((Func_declarationContext)_localctx).ID.getText();

				            System.out.println("PARSING func " + fn_name + " decl W/O ARGS");

				            boolean isInserted = STinsert(fn_name, "ID", type, null, true); //DONE
				            if(!isInserted){
				                System.out.println("ERROR : " + fn_name + " already exists in current scope");
				            }
				            
				            wParserLog("Line " + lineNo + ": func_declaration : type_specifier ID LPAREN RPAREN SEMICOLON\n");
				            wParserLog(type + " " + fn_name + "();\n");

				            ((Func_declarationContext)_localctx).fdec_rri =  new RuleReturnInfo();
				            _localctx.fdec_rri.text = type + " " + fn_name + "();";
				            _localctx.fdec_rri.lineNo = lineNo;
				        
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Func_definitionContext extends ParserRuleContext {
		public RuleReturnInfo fdef_rri;
		public Type_specifierContext ts;
		public Token ID;
		public Parameter_listContext p;
		public Token RPAREN;
		public Compound_statementContext cstat;
		public Non_paramsContext non;
		public TerminalNode ID() { return getToken(C2105007Parser.ID, 0); }
		public TerminalNode LPAREN() { return getToken(C2105007Parser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(C2105007Parser.RPAREN, 0); }
		public Type_specifierContext type_specifier() {
			return getRuleContext(Type_specifierContext.class,0);
		}
		public Parameter_listContext parameter_list() {
			return getRuleContext(Parameter_listContext.class,0);
		}
		public Compound_statementContext compound_statement() {
			return getRuleContext(Compound_statementContext.class,0);
		}
		public Non_paramsContext non_params() {
			return getRuleContext(Non_paramsContext.class,0);
		}
		public Func_definitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_func_definition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof C2105007ParserListener ) ((C2105007ParserListener)listener).enterFunc_definition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof C2105007ParserListener ) ((C2105007ParserListener)listener).exitFunc_definition(this);
		}
	}

	public final Func_definitionContext func_definition() throws RecognitionException {
		Func_definitionContext _localctx = new Func_definitionContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_func_definition);
		try {
			setState(124);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(96);
				((Func_definitionContext)_localctx).ts = type_specifier();
				setState(97);
				((Func_definitionContext)_localctx).ID = match(ID);
				setState(98);
				match(LPAREN);
				setState(99);
				((Func_definitionContext)_localctx).p = parameter_list(0);
				setState(100);
				((Func_definitionContext)_localctx).RPAREN = match(RPAREN);

				                //FN DEF WITH ARGS
				                int lineNo_now = ((Func_definitionContext)_localctx).RPAREN.getLine();

				                System.out.println("Parsing Fn **definition** with args");
				                System.out.println("  Fn name : " + ((Func_definitionContext)_localctx).ID.getText());
				                String ret_type = ((Func_definitionContext)_localctx).ts.type_rri.text;

				                // Store fn info in list
				                ArrayList<String> paramList = new ArrayList<>();
				                ArrayList<String> IDList = new ArrayList<>();
				                String[] variables = ((Func_definitionContext)_localctx).p.param_rri.text.split(",");
				                System.out.print("  Params : ");
				                for (String var : variables) {
				                    String[] splitted = var.split(" ");
				                    paramList.add(splitted[0]);
				                    IDList.add(splitted[1]);
				                    System.out.print("type : " + splitted[0] + ", ID : " + splitted[1] + "|");
				                }
				                System.out.println("");


				                boolean isInserted = STinsert(((Func_definitionContext)_localctx).ID.getText(), "ID", ret_type, paramList, true); //DONE
				                boolean isFunc = isFunction(((Func_definitionContext)_localctx).ID.getText());

				                

				                // Previously declared function 
				                if(!isInserted && isFunc){
				                    System.out.println("Defining a previously declared fn " + ((Func_definitionContext)_localctx).ID.getText());
				                    String fnType = getFuncType(((Func_definitionContext)_localctx).ID.getText());
				                    System.out.println("    prev Type " + fnType);
				                    System.out.println("    now Type " + ret_type);
				                    if(!fnType.equalsIgnoreCase(ret_type)){
				                        //Error at line 24: Return type mismatch of foo3
				                        System.out.println("Error at line " + lineNo_now + ": Return type mismatch of " + ((Func_definitionContext)_localctx).ID.getText());
				                        logErr("Error at line " + lineNo_now + ": Return type mismatch of " + ((Func_definitionContext)_localctx).ID.getText() + "\n");
				                    }

				                    SymbolInfo2 fn_info = Main.st.lookUp(new SymbolInfo2(((Func_definitionContext)_localctx).ID.getText(), "ID", null, null));
				                    ArrayList<String> prev_params = fn_info.getParameters();
				                    System.out.println("  prev params : " + prev_params + "| now params : " + paramList);

				                    if(prev_params.size() != paramList.size()){
				                        //Error at line 32: Total number of arguments mismatch with declaration in function var
				                        System.out.println("Error at line " + lineNo_now + 
				                                ": Total number of arguments mismatch with declaration in function " + ((Func_definitionContext)_localctx).ID.getText());
				                        logErr("Error at line " + lineNo_now + 
				                                ": Total number of arguments mismatch with declaration in function " + ((Func_definitionContext)_localctx).ID.getText() + "\n");
				                    }
				                }

				                if(!isInserted && !isFunc){
				                    //Error at line 28: Multiple declaration of z
				                    System.out.println("Error at line " + lineNo_now + ": Multiple declaration of " + ((Func_definitionContext)_localctx).ID.getText());
				                    logErr("Error at line " + lineNo_now + ": Multiple declaration of " + ((Func_definitionContext)_localctx).ID.getText() + "\n");
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

				            
				setState(102);
				((Func_definitionContext)_localctx).cstat = compound_statement();

				            //FN DEF WITH ARGS
				            int lineNo = ((Func_definitionContext)_localctx).cstat.Cstat_rri.lineNo;
				            String fn_type = ((Func_definitionContext)_localctx).ts.type_rri.text;
				            String arg_list = "(" + ((Func_definitionContext)_localctx).p.param_rri.text + ")";
				            String fn_body = ((Func_definitionContext)_localctx).cstat.Cstat_rri.text;
				            String text = fn_type + " " + ((Func_definitionContext)_localctx).ID.getText() + arg_list + fn_body;

				            if(fn_body.contains("return") && fn_type.equalsIgnoreCase("void")){
				                //Error at line 38: Cannot return value from function foo4 with void return type 
				                System.out.println("Error : void Function " + ((Func_definitionContext)_localctx).ID.getText() + " returns a value");
				                logErr("Error at line " + lineNo + ": Cannot return value from function " + ((Func_definitionContext)_localctx).ID.getText() + " with void return type\n");

				            }

				            wParserLog("Line " + lineNo + ": func_definition : type_specifier ID LPAREN parameter_list RPAREN compound_statement\n");
				            wParserLog(text);
				            Main.isFuncDefined = false;
				            ((Func_definitionContext)_localctx).fdef_rri =  new RuleReturnInfo(lineNo, text);
				        
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(105);
				((Func_definitionContext)_localctx).ts = type_specifier();
				setState(106);
				((Func_definitionContext)_localctx).ID = match(ID);
				setState(107);
				match(LPAREN);
				setState(108);
				match(RPAREN);

				                System.out.println("Parsing Fn **definition** without args");
				                System.out.println("  Fn name : " + ((Func_definitionContext)_localctx).ID.getText());
				                String ret_type = ((Func_definitionContext)_localctx).ts.type_rri.text;
				                boolean isInserted = STinsert(((Func_definitionContext)_localctx).ID.getText(), "ID", ret_type, null, true); //DONE
				                boolean isFunc = isFunction(((Func_definitionContext)_localctx).ID.getText());

				                if(!isInserted && !isFunc){
				                    System.out.println("ERROR : " + ((Func_definitionContext)_localctx).ID.getText() + " fn  aldy inserted before");
				                }
				            
				setState(110);
				((Func_definitionContext)_localctx).cstat = compound_statement();

				            //FN DEF WITHOUT ARGS
				            int lineNo = ((Func_definitionContext)_localctx).cstat.Cstat_rri.lineNo;
				            String fn_type = ((Func_definitionContext)_localctx).ts.type_rri.text;
				            String fn_body = ((Func_definitionContext)_localctx).cstat.Cstat_rri.text;
				            String text = fn_type + " " + ((Func_definitionContext)_localctx).ID.getText() + "()" + fn_body;

				            wParserLog("Line " + lineNo + ": func_definition : type_specifier ID LPAREN RPAREN compound_statement\n");
				            wParserLog(text);

				            ((Func_definitionContext)_localctx).fdef_rri =  new RuleReturnInfo(lineNo, text);
				        
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(113);
				((Func_definitionContext)_localctx).ts = type_specifier();
				setState(114);
				((Func_definitionContext)_localctx).ID = match(ID);
				setState(115);
				match(LPAREN);
				setState(116);
				((Func_definitionContext)_localctx).p = parameter_list(0);
				setState(117);
				((Func_definitionContext)_localctx).non = non_params();

				            int errLineNo = ((Func_definitionContext)_localctx).non.non_param_rri.lineNo;
				            String non_param = ((Func_definitionContext)_localctx).non.non_param_rri.text;
				            
				            String message = "Error at line " + errLineNo + 
				                ": syntax error, unexpected " + non_param + ", expecting RPAREN or COMMA";

				            //Error at line 3: syntax error, unexpected ADDOP, expecting RPAREN or COMMA
				            System.out.println(message);
				            logErr(message + "\n");

				        
				setState(119);
				((Func_definitionContext)_localctx).RPAREN = match(RPAREN);

				                //FN DEF WITH ARGS
				                int lineNo_now = ((Func_definitionContext)_localctx).RPAREN.getLine();

				                System.out.println("Parsing Fn **definition** with args");
				                System.out.println("  Fn name : " + ((Func_definitionContext)_localctx).ID.getText());
				                String ret_type = ((Func_definitionContext)_localctx).ts.type_rri.text;

				                // Store fn info in list
				                ArrayList<String> paramList = new ArrayList<>();
				                ArrayList<String> IDList = new ArrayList<>();
				                String[] variables = ((Func_definitionContext)_localctx).p.param_rri.text.split(",");
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


				                boolean isInserted = STinsert(((Func_definitionContext)_localctx).ID.getText(), "ID", ret_type, paramList, true); //DONE
				                boolean isFunc = isFunction(((Func_definitionContext)_localctx).ID.getText());

				                

				                // Previously declared function 
				                if(!isInserted && isFunc){
				                    System.out.println("Defining a previously declared fn " + ((Func_definitionContext)_localctx).ID.getText());
				                    String fnType = getFuncType(((Func_definitionContext)_localctx).ID.getText());
				                    System.out.println("    prev Type " + fnType);
				                    System.out.println("    now Type " + ret_type);
				                    if(!fnType.equalsIgnoreCase(ret_type)){
				                        //Error at line 24: Return type mismatch of foo3
				                        System.out.println("Error at line " + lineNo_now + ": Return type mismatch of " + ((Func_definitionContext)_localctx).ID.getText());
				                        logErr("Error at line " + lineNo_now + ": Return type mismatch of " + ((Func_definitionContext)_localctx).ID.getText() + "\n");
				                    }

				                    SymbolInfo2 fn_info = Main.st.lookUp(new SymbolInfo2(((Func_definitionContext)_localctx).ID.getText(), "ID", null, null));
				                    ArrayList<String> prev_params = fn_info.getParameters();
				                    System.out.println("  prev params : " + prev_params + "| now params : " + paramList);

				                    if(prev_params.size() != paramList.size()){
				                        //Error at line 32: Total number of arguments mismatch with declaration in function var
				                        System.out.println("Error at line " + lineNo_now + 
				                                ": Total number of arguments mismatch with declaration in function " + ((Func_definitionContext)_localctx).ID.getText());
				                        logErr("Error at line " + lineNo_now + 
				                                ": Total number of arguments mismatch with declaration in function " + ((Func_definitionContext)_localctx).ID.getText() + "\n");
				                    }
				                }

				                if(!isInserted && !isFunc){
				                    //Error at line 28: Multiple declaration of z
				                    System.out.println("Error at line " + lineNo_now + ": Multiple declaration of " + ((Func_definitionContext)_localctx).ID.getText());
				                    logErr("Error at line " + lineNo_now + ": Multiple declaration of " + ((Func_definitionContext)_localctx).ID.getText() + "\n");
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

				            
				setState(121);
				((Func_definitionContext)_localctx).cstat = compound_statement();

				            //FN DEF WITH ARGS
				            int lineNo = ((Func_definitionContext)_localctx).cstat.Cstat_rri.lineNo;
				            String fn_type = ((Func_definitionContext)_localctx).ts.type_rri.text;
				            String arg_list = "(" + ((Func_definitionContext)_localctx).p.param_rri.text + ")";
				            String fn_body = ((Func_definitionContext)_localctx).cstat.Cstat_rri.text;
				            String text = fn_type + " " + ((Func_definitionContext)_localctx).ID.getText() + arg_list + fn_body + "\n";

				            if(fn_body.contains("return") && fn_type.equalsIgnoreCase("void")){
				                //Error at line 38: Cannot return value from function foo4 with void return type 
				                System.out.println("Error : void Function " + ((Func_definitionContext)_localctx).ID.getText() + " returns a value");
				                logErr("Error at line " + lineNo + ": Cannot return value from function " + ((Func_definitionContext)_localctx).ID.getText() + " with void return type\n");

				            }

				            wParserLog("Line " + lineNo + ": func_definition : type_specifier ID LPAREN RPAREN compound_statement\n");
				            wParserLog(text);
				            Main.isFuncDefined = false;
				            ((Func_definitionContext)_localctx).fdef_rri =  new RuleReturnInfo(lineNo, text);
				        
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Parameter_listContext extends ParserRuleContext {
		public RuleReturnInfo param_rri;
		public Parameter_listContext p;
		public Type_specifierContext t;
		public Token ID;
		public Token COMMA;
		public TerminalNode ID() { return getToken(C2105007Parser.ID, 0); }
		public Type_specifierContext type_specifier() {
			return getRuleContext(Type_specifierContext.class,0);
		}
		public TerminalNode COMMA() { return getToken(C2105007Parser.COMMA, 0); }
		public Parameter_listContext parameter_list() {
			return getRuleContext(Parameter_listContext.class,0);
		}
		public Parameter_listContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameter_list; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof C2105007ParserListener ) ((C2105007ParserListener)listener).enterParameter_list(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof C2105007ParserListener ) ((C2105007ParserListener)listener).exitParameter_list(this);
		}
	}

	public final Parameter_listContext parameter_list() throws RecognitionException {
		return parameter_list(0);
	}

	private Parameter_listContext parameter_list(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		Parameter_listContext _localctx = new Parameter_listContext(_ctx, _parentState);
		Parameter_listContext _prevctx = _localctx;
		int _startState = 10;
		enterRecursionRule(_localctx, 10, RULE_parameter_list, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(134);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				{
				setState(127);
				((Parameter_listContext)_localctx).t = type_specifier();
				setState(128);
				((Parameter_listContext)_localctx).ID = match(ID);
				 
				            //fn defn must int foo(int a)
				            int lineNo = ((Parameter_listContext)_localctx).t.type_rri.lineNo;
				            String type = ((Parameter_listContext)_localctx).t.type_rri.text;
				            String id = (((Parameter_listContext)_localctx).ID!=null?((Parameter_listContext)_localctx).ID.getText():null);

				            String curr_param_list = type + " " + id;

				            wParserLog("Line " + lineNo + ": parameter_list : type_specifier ID\n");
				            wParserLog(curr_param_list + "\n");

				            ((Parameter_listContext)_localctx).param_rri =  new RuleReturnInfo(lineNo, curr_param_list);

				        
				}
				break;
			case 2:
				{
				setState(131);
				((Parameter_listContext)_localctx).t = type_specifier();
				 
				            //used in func decl int foo(int)
				            System.out.println("In parameter list: type_specifier");
				            int lineNo = ((Parameter_listContext)_localctx).t.type_rri.lineNo;
				            String text = ((Parameter_listContext)_localctx).t.type_rri.text;
				            wParserLog("Line " + lineNo + ": parameter_list : type_specifier\n");
				            wParserLog(text + "\n");    

				            ((Parameter_listContext)_localctx).param_rri =  new RuleReturnInfo(lineNo, text);
				        
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(149);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(147);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
					case 1:
						{
						_localctx = new Parameter_listContext(_parentctx, _parentState);
						_localctx.p = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_parameter_list);
						setState(136);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(137);
						((Parameter_listContext)_localctx).COMMA = match(COMMA);
						setState(138);
						((Parameter_listContext)_localctx).t = type_specifier();
						setState(139);
						((Parameter_listContext)_localctx).ID = match(ID);

						                      //fn defn multiple args int foo(int a, float b)
						                      int lineNo = (((Parameter_listContext)_localctx).COMMA!=null?((Parameter_listContext)_localctx).COMMA.getLine():0);
						                      String type = ((Parameter_listContext)_localctx).t.type_rri.text;
						                      String id = (((Parameter_listContext)_localctx).ID!=null?((Parameter_listContext)_localctx).ID.getText():null);
						                      String prev_param_list = ((Parameter_listContext)_localctx).p.param_rri.text;

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
						                      ((Parameter_listContext)_localctx).param_rri =  new RuleReturnInfo(lineNo, curr_param_list);

						                      wParserLog("Line " + lineNo + ": parameter_list : parameter_list COMMA type_specifier ID\n");
						                      wParserLog(_localctx.param_rri.text + "\n");
						                  
						}
						break;
					case 2:
						{
						_localctx = new Parameter_listContext(_parentctx, _parentState);
						_localctx.p = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_parameter_list);
						setState(142);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(143);
						((Parameter_listContext)_localctx).COMMA = match(COMMA);
						setState(144);
						((Parameter_listContext)_localctx).t = type_specifier();

						                      //func decl multiple args int foo(int, float)
						                      int lineNo = (((Parameter_listContext)_localctx).COMMA!=null?((Parameter_listContext)_localctx).COMMA.getLine():0);
						                      String type = ((Parameter_listContext)_localctx).t.type_rri.text;
						                      String prev_param_list = ((Parameter_listContext)_localctx).p.param_rri.text;            

						                      //update param_rri
						                      String curr_param_list = prev_param_list + "," + type;
						                      ((Parameter_listContext)_localctx).param_rri =  new RuleReturnInfo(lineNo, curr_param_list);

						                      wParserLog("Line " + lineNo + ": parameter_list : parameter_list COMMA type_specifier\n");
						                      wParserLog(_localctx.param_rri.text + "\n");
						                  
						}
						break;
					}
					} 
				}
				setState(151);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Compound_statementContext extends ParserRuleContext {
		public RuleReturnInfo Cstat_rri;
		public StatementsContext s;
		public Token RCURL;
		public TerminalNode LCURL() { return getToken(C2105007Parser.LCURL, 0); }
		public TerminalNode RCURL() { return getToken(C2105007Parser.RCURL, 0); }
		public StatementsContext statements() {
			return getRuleContext(StatementsContext.class,0);
		}
		public Compound_statementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compound_statement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof C2105007ParserListener ) ((C2105007ParserListener)listener).enterCompound_statement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof C2105007ParserListener ) ((C2105007ParserListener)listener).exitCompound_statement(this);
		}
	}

	public final Compound_statementContext compound_statement() throws RecognitionException {
		Compound_statementContext _localctx = new Compound_statementContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_compound_statement);
		try {
			setState(162);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(152);
				match(LCURL);

				            //ENTER new scope if not inserted from function already
				            if(!Main.isFuncDefined){
				                System.out.println("Entering new scope");
				                enterScope();

				            }
				        
				setState(154);
				((Compound_statementContext)_localctx).s = statements(0);
				setState(155);
				((Compound_statementContext)_localctx).RCURL = match(RCURL);

				            int lineNo = (((Compound_statementContext)_localctx).RCURL!=null?((Compound_statementContext)_localctx).RCURL.getLine():0);
				            String text = "{\n" + ((Compound_statementContext)_localctx).s.Stats_rri.text + "}\n";
				            wParserLog("Line " + lineNo + ": compound_statement : LCURL statements RCURL\n");
				            wParserLog(text);
				            ((Compound_statementContext)_localctx).Cstat_rri =  new RuleReturnInfo(lineNo, text);

				            //PRINT SYMBOL TABLE AND EXIT SCOPE
				            STprint();
				            exitScope();
				        
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(158);
				match(LCURL);

				            if(!Main.isFuncDefined){
				                System.out.println("Entering new scope");
				                enterScope();
				            }
				        
				setState(160);
				((Compound_statementContext)_localctx).RCURL = match(RCURL);
				 
				            wParserLog("Line " + (((Compound_statementContext)_localctx).RCURL!=null?((Compound_statementContext)_localctx).RCURL.getLine():0) + ": compound_statement : LCURL RCURL\n");
				            wParserLog("{}\n");
				            ((Compound_statementContext)_localctx).Cstat_rri =  new RuleReturnInfo((((Compound_statementContext)_localctx).RCURL!=null?((Compound_statementContext)_localctx).RCURL.getLine():0), "{}");
				            STprint();
				            exitScope();
				        
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Var_declarationContext extends ParserRuleContext {
		public RuleReturnInfo vdec_rri;
		public Type_specifierContext t;
		public Declaration_listContext dl;
		public Token sm;
		public Declaration_list_errContext de;
		public Type_specifierContext type_specifier() {
			return getRuleContext(Type_specifierContext.class,0);
		}
		public Declaration_listContext declaration_list() {
			return getRuleContext(Declaration_listContext.class,0);
		}
		public TerminalNode SEMICOLON() { return getToken(C2105007Parser.SEMICOLON, 0); }
		public Declaration_list_errContext declaration_list_err() {
			return getRuleContext(Declaration_list_errContext.class,0);
		}
		public Var_declarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_var_declaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof C2105007ParserListener ) ((C2105007ParserListener)listener).enterVar_declaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof C2105007ParserListener ) ((C2105007ParserListener)listener).exitVar_declaration(this);
		}
	}

	public final Var_declarationContext var_declaration() throws RecognitionException {
		Var_declarationContext _localctx = new Var_declarationContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_var_declaration);
		try {
			setState(174);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(164);
				((Var_declarationContext)_localctx).t = type_specifier();
				setState(165);
				((Var_declarationContext)_localctx).dl = declaration_list(0);
				setState(166);
				((Var_declarationContext)_localctx).sm = match(SEMICOLON);

				        wParserLog(
				            "Line " + ((Var_declarationContext)_localctx).sm.getLine()
				            +": var_declaration : type_specifier declaration_list SEMICOLON\n"
				        );

				        int lineNo = ((Var_declarationContext)_localctx).sm.getLine();
				        String text = ((Var_declarationContext)_localctx).t.type_rri.text + " " + ((Var_declarationContext)_localctx).dl.dec_list + ";";
				        ((Var_declarationContext)_localctx).vdec_rri =  new RuleReturnInfo(lineNo, text);

				        String type = ((Var_declarationContext)_localctx).t.type_rri.text;

				        if(type.equalsIgnoreCase("void")){
				            //Error at line 42: Variable type cannot be void
				            System.out.println("Error at line " + lineNo + ": Variable type cannot be void");
				            logErr("Error at line " + lineNo + ": Variable type cannot be void" + "\n");
				        }

				        wParserLog(text + "\n");

				        //INSERT INTO SYMBOL TABLE
				        String[] variables = ((Var_declarationContext)_localctx).dl.dec_list.split(",");
				        for (String var : variables) {
				            String varName = var.trim();
				            String varType = ((Var_declarationContext)_localctx).t.type_rri.text;
				            
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
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(169);
				((Var_declarationContext)_localctx).t = type_specifier();
				setState(170);
				((Var_declarationContext)_localctx).de = declaration_list_err();
				setState(171);
				((Var_declarationContext)_localctx).sm = match(SEMICOLON);

				        wErrorLog(
				            "Line# "
				            + ((Var_declarationContext)_localctx).sm.getLine()
				            + " with error name: "
				            + ((Var_declarationContext)_localctx).de.error_name
				            + " - Syntax error at declaration list of variable declaration"
				        );
				        Main.syntaxErrorCount++;
				      
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Declaration_list_errContext extends ParserRuleContext {
		public String error_name;
		public Declaration_list_errContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declaration_list_err; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof C2105007ParserListener ) ((C2105007ParserListener)listener).enterDeclaration_list_err(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof C2105007ParserListener ) ((C2105007ParserListener)listener).exitDeclaration_list_err(this);
		}
	}

	public final Declaration_list_errContext declaration_list_err() throws RecognitionException {
		Declaration_list_errContext _localctx = new Declaration_list_errContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_declaration_list_err);
		try {
			enterOuterAlt(_localctx, 1);
			{
			 ((Declaration_list_errContext)_localctx).error_name =  "Error in declaration list"; 
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Type_specifierContext extends ParserRuleContext {
		public RuleReturnInfo type_rri;
		public Token INT;
		public Token FLOAT;
		public Token VOID;
		public TerminalNode INT() { return getToken(C2105007Parser.INT, 0); }
		public TerminalNode FLOAT() { return getToken(C2105007Parser.FLOAT, 0); }
		public TerminalNode VOID() { return getToken(C2105007Parser.VOID, 0); }
		public Type_specifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type_specifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof C2105007ParserListener ) ((C2105007ParserListener)listener).enterType_specifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof C2105007ParserListener ) ((C2105007ParserListener)listener).exitType_specifier(this);
		}
	}

	public final Type_specifierContext type_specifier() throws RecognitionException {
		Type_specifierContext _localctx = new Type_specifierContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_type_specifier);
		try {
			setState(184);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case INT:
				enterOuterAlt(_localctx, 1);
				{
				setState(178);
				((Type_specifierContext)_localctx).INT = match(INT);

				        ((Type_specifierContext)_localctx).type_rri =  new RuleReturnInfo();
				        _localctx.type_rri.lineNo = (((Type_specifierContext)_localctx).INT!=null?((Type_specifierContext)_localctx).INT.getLine():0);
				        _localctx.type_rri.text = "int";
				        wParserLog("Line " + (((Type_specifierContext)_localctx).INT!=null?((Type_specifierContext)_localctx).INT.getLine():0) + ": type_specifier : INT\n");
				        wParserLog((((Type_specifierContext)_localctx).INT!=null?((Type_specifierContext)_localctx).INT.getText():null) + "\n");
				      
				}
				break;
			case FLOAT:
				enterOuterAlt(_localctx, 2);
				{
				setState(180);
				((Type_specifierContext)_localctx).FLOAT = match(FLOAT);

				        ((Type_specifierContext)_localctx).type_rri =  new RuleReturnInfo();
				        _localctx.type_rri.lineNo = (((Type_specifierContext)_localctx).FLOAT!=null?((Type_specifierContext)_localctx).FLOAT.getLine():0);
				        _localctx.type_rri.text = "float";
				        wParserLog("Line " + (((Type_specifierContext)_localctx).FLOAT!=null?((Type_specifierContext)_localctx).FLOAT.getLine():0) + ": type_specifier : FLOAT\n");
				        wParserLog((((Type_specifierContext)_localctx).FLOAT!=null?((Type_specifierContext)_localctx).FLOAT.getText():null) + "\n");
				      
				}
				break;
			case VOID:
				enterOuterAlt(_localctx, 3);
				{
				setState(182);
				((Type_specifierContext)_localctx).VOID = match(VOID);

				        ((Type_specifierContext)_localctx).type_rri =  new RuleReturnInfo();
				        _localctx.type_rri.lineNo = (((Type_specifierContext)_localctx).VOID!=null?((Type_specifierContext)_localctx).VOID.getLine():0);
				        _localctx.type_rri.text = "void";
				        wParserLog("Line " + (((Type_specifierContext)_localctx).VOID!=null?((Type_specifierContext)_localctx).VOID.getLine():0) + ": type_specifier : VOID\n");
				        wParserLog((((Type_specifierContext)_localctx).VOID!=null?((Type_specifierContext)_localctx).VOID.getText():null) + "\n");
				      
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Declaration_listContext extends ParserRuleContext {
		public String dec_list;
		public Declaration_listContext dl;
		public Token ID;
		public Token CONST_INT;
		public Non_paramsContext np;
		public TerminalNode ID() { return getToken(C2105007Parser.ID, 0); }
		public TerminalNode LTHIRD() { return getToken(C2105007Parser.LTHIRD, 0); }
		public TerminalNode CONST_INT() { return getToken(C2105007Parser.CONST_INT, 0); }
		public TerminalNode RTHIRD() { return getToken(C2105007Parser.RTHIRD, 0); }
		public TerminalNode COMMA() { return getToken(C2105007Parser.COMMA, 0); }
		public Declaration_listContext declaration_list() {
			return getRuleContext(Declaration_listContext.class,0);
		}
		public Non_paramsContext non_params() {
			return getRuleContext(Non_paramsContext.class,0);
		}
		public Declaration_listContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declaration_list; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof C2105007ParserListener ) ((C2105007ParserListener)listener).enterDeclaration_list(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof C2105007ParserListener ) ((C2105007ParserListener)listener).exitDeclaration_list(this);
		}
	}

	public final Declaration_listContext declaration_list() throws RecognitionException {
		return declaration_list(0);
	}

	private Declaration_listContext declaration_list(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		Declaration_listContext _localctx = new Declaration_listContext(_ctx, _parentState);
		Declaration_listContext _prevctx = _localctx;
		int _startState = 20;
		enterRecursionRule(_localctx, 20, RULE_declaration_list, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(194);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				{
				setState(187);
				((Declaration_listContext)_localctx).ID = match(ID);

				            //single decl
				            ((Declaration_listContext)_localctx).dec_list =  (((Declaration_listContext)_localctx).ID!=null?((Declaration_listContext)_localctx).ID.getText():null);
				            boolean aldyExists = STlookupCurrScope((((Declaration_listContext)_localctx).ID!=null?((Declaration_listContext)_localctx).ID.getText():null));
				            if(aldyExists){
				                wErrorLog("Error at line " + (((Declaration_listContext)_localctx).ID!=null?((Declaration_listContext)_localctx).ID.getLine():0) + ": Multiple declaration of " + (((Declaration_listContext)_localctx).ID!=null?((Declaration_listContext)_localctx).ID.getText():null) + "\n");
				                wParserLog("Error at line " + (((Declaration_listContext)_localctx).ID!=null?((Declaration_listContext)_localctx).ID.getLine():0) + ": Multiple declaration of " + (((Declaration_listContext)_localctx).ID!=null?((Declaration_listContext)_localctx).ID.getText():null) + "\n");
				                Main.syntaxErrorCount++;
				            }
				            wParserLog("Line " + (((Declaration_listContext)_localctx).ID!=null?((Declaration_listContext)_localctx).ID.getLine():0) + ": declaration_list : ID\n");
				            wParserLog(_localctx.dec_list + "\n");
				        
				}
				break;
			case 2:
				{
				setState(189);
				((Declaration_listContext)_localctx).ID = match(ID);
				setState(190);
				match(LTHIRD);
				setState(191);
				((Declaration_listContext)_localctx).CONST_INT = match(CONST_INT);
				setState(192);
				match(RTHIRD);

				            //array decl
				            ((Declaration_listContext)_localctx).dec_list =  (((Declaration_listContext)_localctx).ID!=null?((Declaration_listContext)_localctx).ID.getText():null) + "[" + (((Declaration_listContext)_localctx).CONST_INT!=null?((Declaration_listContext)_localctx).CONST_INT.getText():null) + "]";
				            wParserLog("Line " + (((Declaration_listContext)_localctx).ID!=null?((Declaration_listContext)_localctx).ID.getLine():0) + ": declaration_list : ID LTHIRD CONST_INT RTHIRD\n");
				            wParserLog(_localctx.dec_list + "\n");
				        
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(215);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(213);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
					case 1:
						{
						_localctx = new Declaration_listContext(_parentctx, _parentState);
						_localctx.dl = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_declaration_list);
						setState(196);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(197);
						match(COMMA);
						setState(198);
						((Declaration_listContext)_localctx).ID = match(ID);

						                      //multiple decl
						                      ((Declaration_listContext)_localctx).dec_list =  ((Declaration_listContext)_localctx).dl.dec_list + "," + (((Declaration_listContext)_localctx).ID!=null?((Declaration_listContext)_localctx).ID.getText():null);
						                      wParserLog("Line " + (((Declaration_listContext)_localctx).ID!=null?((Declaration_listContext)_localctx).ID.getLine():0) + ": declaration_list : declaration_list COMMA ID\n");
						                      wParserLog(_localctx.dec_list + "\n");
						                  
						}
						break;
					case 2:
						{
						_localctx = new Declaration_listContext(_parentctx, _parentState);
						_localctx.dl = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_declaration_list);
						setState(200);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(201);
						((Declaration_listContext)_localctx).np = non_params();
						setState(202);
						match(COMMA);
						setState(203);
						((Declaration_listContext)_localctx).ID = match(ID);

						                      // decl ERROR
						                      String msg = "Error at line " + ((Declaration_listContext)_localctx).ID.getLine() + ": syntax error, unexpected " 
						                              + ((Declaration_listContext)_localctx).np.non_param_rri.text + ", expecting COMMA or SEMICOLON";

						                      System.out.println(msg);
						                      ((Declaration_listContext)_localctx).dec_list =  ((Declaration_listContext)_localctx).dl.dec_list + "," + (((Declaration_listContext)_localctx).ID!=null?((Declaration_listContext)_localctx).ID.getText():null);

						                      System.out.println("Line no " + ((Declaration_listContext)_localctx).ID.getLine() + " declaration list: " + _localctx.dec_list);

						                      logErr(msg + "\n");
						                      wParserLog("Line " + (((Declaration_listContext)_localctx).ID!=null?((Declaration_listContext)_localctx).ID.getLine():0) + ": declaration_list : declaration_list COMMA ID\n");
						                      wParserLog(_localctx.dec_list + "\n");
						                  
						}
						break;
					case 3:
						{
						_localctx = new Declaration_listContext(_parentctx, _parentState);
						_localctx.dl = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_declaration_list);
						setState(206);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(207);
						match(COMMA);
						setState(208);
						((Declaration_listContext)_localctx).ID = match(ID);
						setState(209);
						match(LTHIRD);
						setState(210);
						((Declaration_listContext)_localctx).CONST_INT = match(CONST_INT);
						setState(211);
						match(RTHIRD);

						                      //array er shathe aaro kichu
						                      ((Declaration_listContext)_localctx).dec_list =  ((Declaration_listContext)_localctx).dl.dec_list + "," + (((Declaration_listContext)_localctx).ID!=null?((Declaration_listContext)_localctx).ID.getText():null) + "[" + (((Declaration_listContext)_localctx).CONST_INT!=null?((Declaration_listContext)_localctx).CONST_INT.getText():null) + "]";

						                      boolean aldyExists = STlookupCurrScope((((Declaration_listContext)_localctx).ID!=null?((Declaration_listContext)_localctx).ID.getText():null));
						                      if(aldyExists){
						                          wErrorLog("Error at line " + (((Declaration_listContext)_localctx).ID!=null?((Declaration_listContext)_localctx).ID.getLine():0) + ": Multiple declaration of " + (((Declaration_listContext)_localctx).ID!=null?((Declaration_listContext)_localctx).ID.getText():null) + "\n");
						                          wParserLog("Error at line " + (((Declaration_listContext)_localctx).ID!=null?((Declaration_listContext)_localctx).ID.getLine():0) + ": Multiple declaration of " + (((Declaration_listContext)_localctx).ID!=null?((Declaration_listContext)_localctx).ID.getText():null) + "\n");
						                          Main.syntaxErrorCount++;
						                      }


						                      System.out.println("Line no " + ((Declaration_listContext)_localctx).ID.getLine() + " : Prev declaration: " + ((Declaration_listContext)_localctx).dl.dec_list);
						                      wParserLog("Line " + (((Declaration_listContext)_localctx).ID!=null?((Declaration_listContext)_localctx).ID.getLine():0) + ": declaration_list : declaration_list COMMA ID LTHIRD CONST_INT RTHIRD\n");
						                      wParserLog(_localctx.dec_list + "\n");
						                  
						}
						break;
					}
					} 
				}
				setState(217);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StatementsContext extends ParserRuleContext {
		public RuleReturnInfo Stats_rri;
		public StatementsContext ss;
		public StatementContext s;
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public StatementsContext statements() {
			return getRuleContext(StatementsContext.class,0);
		}
		public StatementsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statements; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof C2105007ParserListener ) ((C2105007ParserListener)listener).enterStatements(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof C2105007ParserListener ) ((C2105007ParserListener)listener).exitStatements(this);
		}
	}

	public final StatementsContext statements() throws RecognitionException {
		return statements(0);
	}

	private StatementsContext statements(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		StatementsContext _localctx = new StatementsContext(_ctx, _parentState);
		StatementsContext _prevctx = _localctx;
		int _startState = 22;
		enterRecursionRule(_localctx, 22, RULE_statements, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(219);
			((StatementsContext)_localctx).s = statement();

			            int lineNo = ((StatementsContext)_localctx).s.stat_rri.lineNo;
			            String text = ((StatementsContext)_localctx).s.stat_rri.text;

			            wParserLog("Line " + lineNo + ": statements : statement\n");
			            wParserLog(text);
			            ((StatementsContext)_localctx).Stats_rri =  new RuleReturnInfo(lineNo, text);
			        
			}
			_ctx.stop = _input.LT(-1);
			setState(228);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new StatementsContext(_parentctx, _parentState);
					_localctx.ss = _prevctx;
					pushNewRecursionContext(_localctx, _startState, RULE_statements);
					setState(222);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(223);
					((StatementsContext)_localctx).s = statement();


					                      if(isUnrecognizedChar){
					                          int lineNo = ((StatementsContext)_localctx).s.stat_rri.lineNo;
					                          String msg = "statements : statements statement (jamuna)";
					                          // wParserLog("Line " + lineNo + ": " + msg + "\n");
					                          // wParserLog(((StatementsContext)_localctx).s.stat_rri.text + "\n");
					                          ((StatementsContext)_localctx).Stats_rri =  new RuleReturnInfo(lineNo, ((StatementsContext)_localctx).ss.Stats_rri.text);
					                          isUnrecognizedChar = false; // reset after logging
					                      }
					                      else{
					                          int lineNo = ((StatementsContext)_localctx).s.stat_rri.lineNo;
					                          String text = ((StatementsContext)_localctx).ss.Stats_rri.text + ((StatementsContext)_localctx).s.stat_rri.text;

					                          wParserLog("Line " + lineNo + ": statements : statements statement\n");
					                          wParserLog(text);
					                          ((StatementsContext)_localctx).Stats_rri =  new RuleReturnInfo(lineNo, text);
					                      }
					                  
					}
					} 
				}
				setState(230);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StatementContext extends ParserRuleContext {
		public RuleReturnInfo stat_rri;
		public Var_declarationContext vd;
		public Expression_statementContext es;
		public Compound_statementContext c;
		public Expression_statementContext es1;
		public Expression_statementContext es2;
		public ExpressionContext e;
		public StatementContext s;
		public StatementContext s1;
		public StatementContext s2;
		public Token PRINTLN;
		public Token ID;
		public Token SEMICOLON;
		public Token RETURN;
		public Var_declarationContext var_declaration() {
			return getRuleContext(Var_declarationContext.class,0);
		}
		public List<Expression_statementContext> expression_statement() {
			return getRuleContexts(Expression_statementContext.class);
		}
		public Expression_statementContext expression_statement(int i) {
			return getRuleContext(Expression_statementContext.class,i);
		}
		public Compound_statementContext compound_statement() {
			return getRuleContext(Compound_statementContext.class,0);
		}
		public TerminalNode FOR() { return getToken(C2105007Parser.FOR, 0); }
		public TerminalNode LPAREN() { return getToken(C2105007Parser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(C2105007Parser.RPAREN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public TerminalNode IF() { return getToken(C2105007Parser.IF, 0); }
		public TerminalNode ELSE() { return getToken(C2105007Parser.ELSE, 0); }
		public TerminalNode WHILE() { return getToken(C2105007Parser.WHILE, 0); }
		public TerminalNode PRINTLN() { return getToken(C2105007Parser.PRINTLN, 0); }
		public TerminalNode ID() { return getToken(C2105007Parser.ID, 0); }
		public TerminalNode SEMICOLON() { return getToken(C2105007Parser.SEMICOLON, 0); }
		public TerminalNode RETURN() { return getToken(C2105007Parser.RETURN, 0); }
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof C2105007ParserListener ) ((C2105007ParserListener)listener).enterStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof C2105007ParserListener ) ((C2105007ParserListener)listener).exitStatement(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_statement);
		try {
			setState(283);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(231);
				((StatementContext)_localctx).vd = var_declaration();
				 
				            int lineNo = ((StatementContext)_localctx).vd.vdec_rri.lineNo;
				            String text = ((StatementContext)_localctx).vd.vdec_rri.text + "\n";

				            ((StatementContext)_localctx).stat_rri =  new RuleReturnInfo(lineNo, text);
				            wParserLog("Line " + lineNo + ": statement : var_declaration\n");
				            wParserLog(text);
				        
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(234);
				((StatementContext)_localctx).es = expression_statement();


				            if(isUnrecognizedChar){
				                int lineNo = ((StatementContext)_localctx).es.Expr_stat_rri.lineNo;
				                String msg = "statement : expression_statement (jamuna)";
				                // wParserLog("Line " + lineNo + ": " + msg + "\n");
				                // wParserLog(((StatementContext)_localctx).es.Expr_stat_rri.text + "\n");
				                ((StatementContext)_localctx).stat_rri =  new RuleReturnInfo(lineNo, ((StatementContext)_localctx).es.Expr_stat_rri.text + "\n");
				            }
				            else{
				                int lineNo = ((StatementContext)_localctx).es.Expr_stat_rri.lineNo;
				                String text = ((StatementContext)_localctx).es.Expr_stat_rri.text + "\n";

				                ((StatementContext)_localctx).stat_rri =  new RuleReturnInfo(lineNo, text);
				                wParserLog("Line " + lineNo + ": statement : expression_statement\n");
				                wParserLog(text);
				            }
				        
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(237);
				((StatementContext)_localctx).c = compound_statement();

				            int lineNo = ((StatementContext)_localctx).c.Cstat_rri.lineNo;
				            String text = ((StatementContext)_localctx).c.Cstat_rri.text;
				            ((StatementContext)_localctx).stat_rri =  new RuleReturnInfo(lineNo, text);
				            wParserLog("Line " + lineNo + ": statement : compound_statement\n");
				            wParserLog(text);
				            
				        
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(240);
				match(FOR);
				setState(241);
				match(LPAREN);
				setState(242);
				((StatementContext)_localctx).es1 = expression_statement();
				setState(243);
				((StatementContext)_localctx).es2 = expression_statement();
				setState(244);
				((StatementContext)_localctx).e = expression();
				setState(245);
				match(RPAREN);
				setState(246);
				((StatementContext)_localctx).s = statement();

				            int lineNo = ((StatementContext)_localctx).s.stat_rri.lineNo;
				            String condns = ((StatementContext)_localctx).es1.Expr_stat_rri.text
				                            + ((StatementContext)_localctx).es2.Expr_stat_rri.text
				                            + ((StatementContext)_localctx).e.Expr_rri.text;

				            String text = "for(" + condns + ")" + ((StatementContext)_localctx).s.stat_rri.text;
				            ((StatementContext)_localctx).stat_rri =  new RuleReturnInfo(lineNo, text);
				            wParserLog("Line " + lineNo + ": statement : FOR LPAREN expression_statement expression_statement expression RPAREN statement\n");
				            wParserLog(text);

				        
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(249);
				match(IF);
				setState(250);
				match(LPAREN);
				setState(251);
				((StatementContext)_localctx).e = expression();
				setState(252);
				match(RPAREN);
				setState(253);
				((StatementContext)_localctx).s = statement();

				            int lineNo = ((StatementContext)_localctx).s.stat_rri.lineNo;

				            String expr = ((StatementContext)_localctx).e.Expr_rri.text;
				            String st = ((StatementContext)_localctx).s.stat_rri.text;
				            String text = "if(" + expr + ")" + st;
				            ((StatementContext)_localctx).stat_rri =  new RuleReturnInfo(lineNo, text);
				            wParserLog("Line " + lineNo + ": statement : IF LPAREN expression RPAREN statement\n");
				            wParserLog(text);
				        
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(256);
				match(IF);
				setState(257);
				match(LPAREN);
				setState(258);
				((StatementContext)_localctx).e = expression();
				setState(259);
				match(RPAREN);
				setState(260);
				((StatementContext)_localctx).s1 = statement();
				setState(261);
				match(ELSE);
				setState(262);
				((StatementContext)_localctx).s2 = statement();

				            int lineNo = ((StatementContext)_localctx).s2.stat_rri.lineNo;

				            String expr = ((StatementContext)_localctx).e.Expr_rri.text;
				            String st1 = ((StatementContext)_localctx).s1.stat_rri.text;
				            String st2 = ((StatementContext)_localctx).s2.stat_rri.text;

				            String text = "if(" + expr + ")" + st1.stripTrailing() + "else " + st2;
				            ((StatementContext)_localctx).stat_rri =  new RuleReturnInfo(lineNo, text);
				            wParserLog("Line " + lineNo + ": statement : IF LPAREN expression RPAREN statement ELSE statement\n");
				            wParserLog(text);

				        
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(265);
				match(WHILE);
				setState(266);
				match(LPAREN);
				setState(267);
				((StatementContext)_localctx).e = expression();
				setState(268);
				match(RPAREN);
				setState(269);
				((StatementContext)_localctx).s = statement();

				            // while(a[0]--){
				            //     c = c - 2;
				            // }

				            int lineNo = ((StatementContext)_localctx).s.stat_rri.lineNo;
				            String condns = ((StatementContext)_localctx).e.Expr_rri.text;

				            String text = "while(" + condns + ")" + ((StatementContext)_localctx).s.stat_rri.text;
				            ((StatementContext)_localctx).stat_rri =  new RuleReturnInfo(lineNo, text);
				            wParserLog("Line " + lineNo + ": statement : WHILE LPAREN expression RPAREN statement\n");
				            wParserLog(text);


				        
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(272);
				((StatementContext)_localctx).PRINTLN = match(PRINTLN);
				setState(273);
				match(LPAREN);
				setState(274);
				((StatementContext)_localctx).ID = match(ID);
				setState(275);
				match(RPAREN);
				setState(276);
				((StatementContext)_localctx).SEMICOLON = match(SEMICOLON);

				            System.out.println("Matched " + ((StatementContext)_localctx).PRINTLN.getText());
				            int lineNo = ((StatementContext)_localctx).SEMICOLON.getLine();
				            String text = "printf("  + ((StatementContext)_localctx).ID.getText() + ");\n";
				            ((StatementContext)_localctx).stat_rri =  new RuleReturnInfo(lineNo, text);
				            wParserLog("Line " + lineNo + ": statement : PRINTLN LPAREN ID RPAREN SEMICOLON\n");

				            //Check for Undeclared variable
				            SymbolInfo2 var_info = Main.st.lookUp(new SymbolInfo2(((StatementContext)_localctx).ID.getText(), "ID", null, null));
				            boolean isNotDeclared = (var_info == null);
				            if(isNotDeclared){
				                //Error at line 67: Undeclared variable h
				                logErr("Error at line " + lineNo + ": Undeclared variable " + ((StatementContext)_localctx).ID.getText() + "\n");
				                System.out.println("Error at line " + lineNo + ": Undeclared variable " + ((StatementContext)_localctx).ID.getText() + "\n");
				            }
				            
				            wParserLog(text);

				        
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(278);
				((StatementContext)_localctx).RETURN = match(RETURN);
				setState(279);
				((StatementContext)_localctx).e = expression();
				setState(280);
				match(SEMICOLON);

				            int lineNo = ((StatementContext)_localctx).RETURN.getLine();
				            String text = "return" + " " + ((StatementContext)_localctx).e.Expr_rri.text + ";\n";

				            wParserLog("Line " + lineNo + ": statement : RETURN expression SEMICOLON\n");
				            wParserLog(text);
				            ((StatementContext)_localctx).stat_rri =  new RuleReturnInfo(lineNo, text);

				        
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Expression_statementContext extends ParserRuleContext {
		public RuleReturnInfo Expr_stat_rri;
		public Token SEMICOLON;
		public ExpressionContext e;
		public TerminalNode SEMICOLON() { return getToken(C2105007Parser.SEMICOLON, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public Expression_statementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression_statement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof C2105007ParserListener ) ((C2105007ParserListener)listener).enterExpression_statement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof C2105007ParserListener ) ((C2105007ParserListener)listener).exitExpression_statement(this);
		}
	}

	public final Expression_statementContext expression_statement() throws RecognitionException {
		Expression_statementContext _localctx = new Expression_statementContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_expression_statement);
		try {
			setState(291);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SEMICOLON:
				enterOuterAlt(_localctx, 1);
				{
				setState(285);
				((Expression_statementContext)_localctx).SEMICOLON = match(SEMICOLON);

				            int lineNo = ((Expression_statementContext)_localctx).SEMICOLON.getLine();
				            String text = ";";
				            wParserLog("Line " + lineNo + ": expression_statement : SEMICOLON\n");
				            wParserLog(text + "\n");
				            ((Expression_statementContext)_localctx).Expr_stat_rri =  new RuleReturnInfo(lineNo, text);
				        
				}
				break;
			case LPAREN:
			case ADDOP:
			case NOT:
			case ASSIGNOP:
			case ID:
			case CONST_INT:
			case CONST_FLOAT:
				enterOuterAlt(_localctx, 2);
				{
				setState(287);
				((Expression_statementContext)_localctx).e = expression();
				setState(288);
				((Expression_statementContext)_localctx).SEMICOLON = match(SEMICOLON);

				            if(isUnrecognizedChar){
				                int lineNo = ((Expression_statementContext)_localctx).e.Expr_rri.lineNo;
				                String msg = "expression_statement : expression SEMICOLON (jamuna)";
				                // wParserLog("Line " + lineNo + ": " + msg + "\n");
				                // wParserLog(((Expression_statementContext)_localctx).e.Expr_rri.text + "\n");
				                ((Expression_statementContext)_localctx).Expr_stat_rri =  new RuleReturnInfo(lineNo, ((Expression_statementContext)_localctx).e.Expr_rri.text);   
				            }

				            else{
				                int lineNo = ((Expression_statementContext)_localctx).SEMICOLON.getLine();
				                String text = ((Expression_statementContext)_localctx).e.Expr_rri.text + ";";

				                wParserLog("Line " + lineNo + ": expression_statement : expression SEMICOLON\n");
				                wParserLog(text + "\n");
				                ((Expression_statementContext)_localctx).Expr_stat_rri =  new RuleReturnInfo(lineNo, text); 
				                isValidStatement = true;           
				            }
				            
				        
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VariableContext extends ParserRuleContext {
		public RuleReturnInfo var_rri;
		public Token ID;
		public ExpressionContext e;
		public TerminalNode ID() { return getToken(C2105007Parser.ID, 0); }
		public TerminalNode LTHIRD() { return getToken(C2105007Parser.LTHIRD, 0); }
		public TerminalNode RTHIRD() { return getToken(C2105007Parser.RTHIRD, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public VariableContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variable; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof C2105007ParserListener ) ((C2105007ParserListener)listener).enterVariable(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof C2105007ParserListener ) ((C2105007ParserListener)listener).exitVariable(this);
		}
	}

	public final VariableContext variable() throws RecognitionException {
		VariableContext _localctx = new VariableContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_variable);
		try {
			setState(301);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,16,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(293);
				((VariableContext)_localctx).ID = match(ID);

				            // USUAL VARIABLE
				            int lineNo = ((VariableContext)_localctx).ID.getLine();
				            ((VariableContext)_localctx).var_rri =  new RuleReturnInfo(lineNo, ((VariableContext)_localctx).ID.getText());

				            wParserLog("Line " + lineNo + ": variable : ID\n");

				            String id = ((VariableContext)_localctx).ID.getText();
				            SymbolInfo2 var_info = Main.st.lookUp(new SymbolInfo2(id, "ID", null, null));
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

				            wParserLog(((VariableContext)_localctx).ID.getText() + "\n");
				        
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(295);
				((VariableContext)_localctx).ID = match(ID);
				setState(296);
				match(LTHIRD);
				setState(297);
				((VariableContext)_localctx).e = expression();
				setState(298);
				match(RTHIRD);

				            // ARRAY VARIABLES
				            int lineNo = ((VariableContext)_localctx).ID.getLine();
				            String expr = ((VariableContext)_localctx).e.Expr_rri.text;

				            String text = ((VariableContext)_localctx).ID.getText() + "[" + expr + "]";
				            boolean isArray = isArrayVar(((VariableContext)_localctx).ID.getText());
				            wParserLog("Line " + lineNo + ": variable : ID LTHIRD expression RTHIRD\n");


				            if(!isArray){
				                // Error at line 52: b not an array
				                String message = "Error at line " + lineNo + ": " + ((VariableContext)_localctx).ID.getText() + " not an array\n";
				                System.out.println(message);
				                logErr(message);
				                isValidStatement = false;
				            }
				            ((VariableContext)_localctx).var_rri =  new RuleReturnInfo(lineNo, text);

				            if(!isInteger(expr)) {
				                wErrorLog("Error at line " + lineNo + ": Expression inside third brackets not an integer\n");
				                wParserLog("Error at line " + lineNo + ": Expression inside third brackets not an integer\n");
				                Main.syntaxErrorCount++;     
				                isValidStatement = false;
				            }

				            wParserLog(text + "\n");

				        
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExpressionContext extends ParserRuleContext {
		public RuleReturnInfo Expr_rri;
		public Logic_expressionContext l;
		public VariableContext var;
		public Logic_expressionContext logic_expression() {
			return getRuleContext(Logic_expressionContext.class,0);
		}
		public TerminalNode ASSIGNOP() { return getToken(C2105007Parser.ASSIGNOP, 0); }
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof C2105007ParserListener ) ((C2105007ParserListener)listener).enterExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof C2105007ParserListener ) ((C2105007ParserListener)listener).exitExpression(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		ExpressionContext _localctx = new ExpressionContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_expression);
		try {
			setState(311);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,17,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(303);
				((ExpressionContext)_localctx).l = logic_expression();

				            int lineNo = ((ExpressionContext)_localctx).l.LogicExpr_rri.lineNo;
				            String text = ((ExpressionContext)_localctx).l.LogicExpr_rri.text;
				            wParserLog("Line " + lineNo + ": expression : logic_expression\n");
				            wParserLog(text + "\n");
				            ((ExpressionContext)_localctx).Expr_rri =  new RuleReturnInfo(lineNo, text);
				        
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(306);
				((ExpressionContext)_localctx).var = variable();
				setState(307);
				match(ASSIGNOP);
				setState(308);
				((ExpressionContext)_localctx).l = logic_expression();

				            if(isUnrecognizedChar){
				                int lineNo = ((ExpressionContext)_localctx).l.LogicExpr_rri.lineNo;
				                String msg = "expression : logic_expression";
				                wParserLog("Line " + lineNo + ": " + msg + "\n");
				                wParserLog(((ExpressionContext)_localctx).l.LogicExpr_rri.text + "\n");
				                ((ExpressionContext)_localctx).Expr_rri =  new RuleReturnInfo(lineNo, ((ExpressionContext)_localctx).l.LogicExpr_rri.text);   

				            }
				            else{
				                int lineNo = ((ExpressionContext)_localctx).l.LogicExpr_rri.lineNo;
				                String var_name = ((ExpressionContext)_localctx).var.var_rri.text;
				                SymbolInfo2 var_info = Main.st.currentScopeLookup(new SymbolInfo2(var_name, "ID", null, null));
				                boolean isNotDeclared = (var_info == null);
				                boolean isArrVar = (var_name.contains("[") && var_name.contains("]"));

				                String logic_expr = ((ExpressionContext)_localctx).l.LogicExpr_rri.text;
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
				                ((ExpressionContext)_localctx).Expr_rri =  new RuleReturnInfo(lineNo, text);   
				            }            
				        
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Logic_expressionContext extends ParserRuleContext {
		public RuleReturnInfo LogicExpr_rri;
		public Rel_expressionContext r;
		public Rel_expressionContext r1;
		public Token LOGICOP;
		public Rel_expressionContext r2;
		public List<Rel_expressionContext> rel_expression() {
			return getRuleContexts(Rel_expressionContext.class);
		}
		public Rel_expressionContext rel_expression(int i) {
			return getRuleContext(Rel_expressionContext.class,i);
		}
		public TerminalNode LOGICOP() { return getToken(C2105007Parser.LOGICOP, 0); }
		public Logic_expressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_logic_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof C2105007ParserListener ) ((C2105007ParserListener)listener).enterLogic_expression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof C2105007ParserListener ) ((C2105007ParserListener)listener).exitLogic_expression(this);
		}
	}

	public final Logic_expressionContext logic_expression() throws RecognitionException {
		Logic_expressionContext _localctx = new Logic_expressionContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_logic_expression);
		try {
			setState(321);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,18,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(313);
				((Logic_expressionContext)_localctx).r = rel_expression();

				            int lineNo = ((Logic_expressionContext)_localctx).r.relExpr_rri.lineNo;
				            String text = ((Logic_expressionContext)_localctx).r.relExpr_rri.text;
				            wParserLog("Line " + lineNo + ": logic_expression : rel_expression\n");
				            wParserLog(text + "\n");
				            ((Logic_expressionContext)_localctx).LogicExpr_rri =  new RuleReturnInfo(lineNo, text);
				        
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(316);
				((Logic_expressionContext)_localctx).r1 = rel_expression();
				setState(317);
				((Logic_expressionContext)_localctx).LOGICOP = match(LOGICOP);
				setState(318);
				((Logic_expressionContext)_localctx).r2 = rel_expression();

				            int lineNo = ((Logic_expressionContext)_localctx).LOGICOP.getLine();
				            String text = ((Logic_expressionContext)_localctx).r1.relExpr_rri.text + ((Logic_expressionContext)_localctx).LOGICOP.getText() + ((Logic_expressionContext)_localctx).r2.relExpr_rri.text;
				            wParserLog("Line " + lineNo + ": logic_expression : rel_expression LOGICOP rel_expression\n");
				            wParserLog(text + "\n");
				            ((Logic_expressionContext)_localctx).LogicExpr_rri =  new RuleReturnInfo(lineNo, text);
				        
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Rel_expressionContext extends ParserRuleContext {
		public RuleReturnInfo relExpr_rri;
		public Simple_expressionContext s;
		public Simple_expressionContext s1;
		public Token RELOP;
		public Simple_expressionContext s2;
		public List<Simple_expressionContext> simple_expression() {
			return getRuleContexts(Simple_expressionContext.class);
		}
		public Simple_expressionContext simple_expression(int i) {
			return getRuleContext(Simple_expressionContext.class,i);
		}
		public TerminalNode RELOP() { return getToken(C2105007Parser.RELOP, 0); }
		public Rel_expressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_rel_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof C2105007ParserListener ) ((C2105007ParserListener)listener).enterRel_expression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof C2105007ParserListener ) ((C2105007ParserListener)listener).exitRel_expression(this);
		}
	}

	public final Rel_expressionContext rel_expression() throws RecognitionException {
		Rel_expressionContext _localctx = new Rel_expressionContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_rel_expression);
		try {
			setState(331);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,19,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(323);
				((Rel_expressionContext)_localctx).s = simple_expression(0);

				            int lineNo = ((Rel_expressionContext)_localctx).s.sm_expr_rri.lineNo;
				            String text = ((Rel_expressionContext)_localctx).s.sm_expr_rri.text;
				            wParserLog("Line " + lineNo + ": rel_expression : simple_expression\n");
				            wParserLog(text + "\n");
				            ((Rel_expressionContext)_localctx).relExpr_rri =  new RuleReturnInfo(lineNo, text);
				        
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(326);
				((Rel_expressionContext)_localctx).s1 = simple_expression(0);
				setState(327);
				((Rel_expressionContext)_localctx).RELOP = match(RELOP);
				setState(328);
				((Rel_expressionContext)_localctx).s2 = simple_expression(0);

				            int lineNo = ((Rel_expressionContext)_localctx).RELOP.getLine();
				            String text = ((Rel_expressionContext)_localctx).s1.sm_expr_rri.text + ((Rel_expressionContext)_localctx).RELOP.getText() + ((Rel_expressionContext)_localctx).s2.sm_expr_rri.text;
				            wParserLog("Line " + lineNo + ": rel_expression : simple_expression RELOP simple_expression\n");
				            wParserLog(text + "\n");
				            ((Rel_expressionContext)_localctx).relExpr_rri =  new RuleReturnInfo(lineNo, text);

				        
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Simple_expressionContext extends ParserRuleContext {
		public RuleReturnInfo sm_expr_rri;
		public Simple_expressionContext sm_expr;
		public Simple_expressionContext sm;
		public TermContext t;
		public Token ADDOP;
		public Invalid_charContext ic;
		public TermContext term() {
			return getRuleContext(TermContext.class,0);
		}
		public TerminalNode ADDOP() { return getToken(C2105007Parser.ADDOP, 0); }
		public Simple_expressionContext simple_expression() {
			return getRuleContext(Simple_expressionContext.class,0);
		}
		public Invalid_charContext invalid_char() {
			return getRuleContext(Invalid_charContext.class,0);
		}
		public Simple_expressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simple_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof C2105007ParserListener ) ((C2105007ParserListener)listener).enterSimple_expression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof C2105007ParserListener ) ((C2105007ParserListener)listener).exitSimple_expression(this);
		}
	}

	public final Simple_expressionContext simple_expression() throws RecognitionException {
		return simple_expression(0);
	}

	private Simple_expressionContext simple_expression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		Simple_expressionContext _localctx = new Simple_expressionContext(_ctx, _parentState);
		Simple_expressionContext _prevctx = _localctx;
		int _startState = 36;
		enterRecursionRule(_localctx, 36, RULE_simple_expression, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(334);
			((Simple_expressionContext)_localctx).t = term(0);

			            int lineNo = ((Simple_expressionContext)_localctx).t.term_rri.lineNo;
			            String text = ((Simple_expressionContext)_localctx).t.term_rri.text; 
			            wParserLog("Line " + lineNo + ": simple_expression : term\n");
			            wParserLog(text + "\n");
			            ((Simple_expressionContext)_localctx).sm_expr_rri =  new RuleReturnInfo(lineNo, text);

			        
			}
			_ctx.stop = _input.LT(-1);
			setState(350);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,21,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(348);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,20,_ctx) ) {
					case 1:
						{
						_localctx = new Simple_expressionContext(_parentctx, _parentState);
						_localctx.sm_expr = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_simple_expression);
						setState(337);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(338);
						((Simple_expressionContext)_localctx).ADDOP = match(ADDOP);
						setState(339);
						((Simple_expressionContext)_localctx).t = term(0);

						                      int lineNo = ((Simple_expressionContext)_localctx).ADDOP.getLine();
						                      String op = ((Simple_expressionContext)_localctx).ADDOP.getText();
						                      String text = ((Simple_expressionContext)_localctx).sm_expr.sm_expr_rri.text + op + ((Simple_expressionContext)_localctx).t.term_rri.text;
						                      wParserLog("Line " + lineNo + ": simple_expression : simple_expression ADDOP term\n");
						                      wParserLog(text + "\n");
						                      ((Simple_expressionContext)_localctx).sm_expr_rri =  new RuleReturnInfo(lineNo, text);
						                  
						}
						break;
					case 2:
						{
						_localctx = new Simple_expressionContext(_parentctx, _parentState);
						_localctx.sm = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_simple_expression);
						setState(342);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(343);
						((Simple_expressionContext)_localctx).ADDOP = match(ADDOP);
						setState(344);
						((Simple_expressionContext)_localctx).t = term(0);
						setState(345);
						((Simple_expressionContext)_localctx).ic = invalid_char();

						                      int lineNo = ((Simple_expressionContext)_localctx).ADDOP.getLine();
						                      int errLineNo = ((Simple_expressionContext)_localctx).ic.invalid_rri.lineNo;
						                  

						                      String errMsg = "Error at line " + errLineNo + ": Unrecognized character " + ((Simple_expressionContext)_localctx).ic.invalid_rri.text;
						                      System.out.println(errMsg);
						                      logErr(errMsg + "\n");
						                                  
						                      String op = ((Simple_expressionContext)_localctx).ADDOP.getText();
						                      String text = ((Simple_expressionContext)_localctx).t.term_rri.text;
						                      int newLineNo = errLineNo + 1;
						                      wParserLog("Line " + newLineNo + ": simple_expression : term\n");
						                      wParserLog(text + "\n");
						                      ((Simple_expressionContext)_localctx).sm_expr_rri =  new RuleReturnInfo(newLineNo, text);
						                      isUnrecognizedChar = true;
						                  
						}
						break;
					}
					} 
				}
				setState(352);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,21,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TermContext extends ParserRuleContext {
		public RuleReturnInfo term_rri;
		public TermContext t;
		public Unary_expressionContext u;
		public Token MULOP;
		public Unary_expressionContext ue;
		public Unary_expressionContext unary_expression() {
			return getRuleContext(Unary_expressionContext.class,0);
		}
		public TerminalNode MULOP() { return getToken(C2105007Parser.MULOP, 0); }
		public TermContext term() {
			return getRuleContext(TermContext.class,0);
		}
		public TermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_term; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof C2105007ParserListener ) ((C2105007ParserListener)listener).enterTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof C2105007ParserListener ) ((C2105007ParserListener)listener).exitTerm(this);
		}
	}

	public final TermContext term() throws RecognitionException {
		return term(0);
	}

	private TermContext term(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		TermContext _localctx = new TermContext(_ctx, _parentState);
		TermContext _prevctx = _localctx;
		int _startState = 38;
		enterRecursionRule(_localctx, 38, RULE_term, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(354);
			((TermContext)_localctx).u = unary_expression();

			            int lineNo = ((TermContext)_localctx).u.unary_rri.lineNo;
			            String text = ((TermContext)_localctx).u.unary_rri.text; 
			            wParserLog("Line " + lineNo + ": term : unary_expression\n");
			            wParserLog(text + "\n");
			            ((TermContext)_localctx).term_rri =  new RuleReturnInfo(lineNo, text);

			        
			}
			_ctx.stop = _input.LT(-1);
			setState(364);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,22,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new TermContext(_parentctx, _parentState);
					_localctx.t = _prevctx;
					pushNewRecursionContext(_localctx, _startState, RULE_term);
					setState(357);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(358);
					((TermContext)_localctx).MULOP = match(MULOP);
					setState(359);
					((TermContext)_localctx).ue = unary_expression();

					                      int lineNo = ((TermContext)_localctx).MULOP.getLine();
					                      String text = ((TermContext)_localctx).t.term_rri.text + ((TermContext)_localctx).MULOP.getText() + ((TermContext)_localctx).ue.unary_rri.text; 
					                      wParserLog("Line " + lineNo + ": term : term MULOP unary_expression\n");

					                      String unary_expr_type = evaluateExpressionType(((TermContext)_localctx).ue.unary_rri.text);
					                      if(unary_expr_type.equals("void")){
					                          //Error at line 54: Void function used in expression
					                          System.out.println("Error at line " + lineNo + ": Void function used in expression");
					                          logErr("Error at line " + lineNo + ": Void function used in expression\n");
					                          isValidStatement = false;
					                      }

					                      // Error at line 9: Non-Integer operand on modulus operator
					                      if(((TermContext)_localctx).MULOP.getText().equals("%")){
					                          String expr_type = typeDetector(((TermContext)_localctx).ue.unary_rri.text);
					                          if(!expr_type.equals("int")){
					                              logErr("Error at line " + lineNo + ": Non-Integer operand on modulus operator\n");
					                          }
					                          else{
					                              //check if expression is 0
					                              try {
					                                  int value = Integer.parseInt(((TermContext)_localctx).ue.unary_rri.text);
					                                  if(value == 0){
					                                      //Error at line 59: Modulus by Zero
					                                      System.out.println("Error at line " + lineNo + ": Modulus by Zero\n");
					                                      logErr("Error at line " + lineNo + ": Modulus by Zero\n");
					                                  }
					                              } catch (NumberFormatException e) {}
					                          }
					                      }

					                      wParserLog(text + "\n");
					                      ((TermContext)_localctx).term_rri =  new RuleReturnInfo(lineNo, text);

					                  
					}
					} 
				}
				setState(366);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,22,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Unary_expressionContext extends ParserRuleContext {
		public RuleReturnInfo unary_rri;
		public Token ADDOP;
		public Unary_expressionContext u;
		public Token NOT;
		public FactorContext f;
		public TerminalNode ADDOP() { return getToken(C2105007Parser.ADDOP, 0); }
		public Unary_expressionContext unary_expression() {
			return getRuleContext(Unary_expressionContext.class,0);
		}
		public TerminalNode NOT() { return getToken(C2105007Parser.NOT, 0); }
		public FactorContext factor() {
			return getRuleContext(FactorContext.class,0);
		}
		public Unary_expressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unary_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof C2105007ParserListener ) ((C2105007ParserListener)listener).enterUnary_expression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof C2105007ParserListener ) ((C2105007ParserListener)listener).exitUnary_expression(this);
		}
	}

	public final Unary_expressionContext unary_expression() throws RecognitionException {
		Unary_expressionContext _localctx = new Unary_expressionContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_unary_expression);
		try {
			setState(378);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ADDOP:
				enterOuterAlt(_localctx, 1);
				{
				setState(367);
				((Unary_expressionContext)_localctx).ADDOP = match(ADDOP);
				setState(368);
				((Unary_expressionContext)_localctx).u = unary_expression();

				            int lineNo = ((Unary_expressionContext)_localctx).ADDOP.getLine();
				            String text = ((Unary_expressionContext)_localctx).ADDOP.getText() + ((Unary_expressionContext)_localctx).u.unary_rri.text; 
				            wParserLog("Line " + lineNo + ": unary_expression : ADDOP unary_expression\n");
				            wParserLog(text + "\n");

				            String type = ((Unary_expressionContext)_localctx).u.unary_rri.expr_type;
				            ((Unary_expressionContext)_localctx).unary_rri =  new RuleReturnInfo(lineNo, text, type);
				        
				}
				break;
			case NOT:
				enterOuterAlt(_localctx, 2);
				{
				setState(371);
				((Unary_expressionContext)_localctx).NOT = match(NOT);
				setState(372);
				((Unary_expressionContext)_localctx).u = unary_expression();

				            int lineNo = ((Unary_expressionContext)_localctx).NOT.getLine();
				            String text = ((Unary_expressionContext)_localctx).NOT.getText() + ((Unary_expressionContext)_localctx).u.unary_rri.text; 
				            wParserLog("Line " + lineNo + ": unary_expression : NOT unary_expression\n");
				            wParserLog(text + "\n");
				            String type = ((Unary_expressionContext)_localctx).u.unary_rri.expr_type;

				            ((Unary_expressionContext)_localctx).unary_rri =  new RuleReturnInfo(lineNo, text, type);
				        
				}
				break;
			case LPAREN:
			case ASSIGNOP:
			case ID:
			case CONST_INT:
			case CONST_FLOAT:
				enterOuterAlt(_localctx, 3);
				{
				setState(375);
				((Unary_expressionContext)_localctx).f = factor();

				            int lineNo = ((Unary_expressionContext)_localctx).f.fact_rri.lineNo;
				            String text = ((Unary_expressionContext)_localctx).f.fact_rri.text; 
				            wParserLog("Line " + lineNo + ": unary_expression : factor\n");
				            wParserLog(text + "\n");
				            String expr_type = ((Unary_expressionContext)_localctx).f.fact_rri.expr_type;
				            ((Unary_expressionContext)_localctx).unary_rri =  new RuleReturnInfo(lineNo, text, expr_type);
				        
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FactorContext extends ParserRuleContext {
		public RuleReturnInfo fact_rri;
		public VariableContext var;
		public Token ID;
		public Argument_listContext al;
		public ExpressionContext e;
		public Token RPAREN;
		public Token c;
		public VariableContext v;
		public Token INCOP;
		public Token DECOP;
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public TerminalNode ID() { return getToken(C2105007Parser.ID, 0); }
		public TerminalNode LPAREN() { return getToken(C2105007Parser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(C2105007Parser.RPAREN, 0); }
		public Argument_listContext argument_list() {
			return getRuleContext(Argument_listContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode CONST_INT() { return getToken(C2105007Parser.CONST_INT, 0); }
		public TerminalNode ASSIGNOP() { return getToken(C2105007Parser.ASSIGNOP, 0); }
		public TerminalNode CONST_FLOAT() { return getToken(C2105007Parser.CONST_FLOAT, 0); }
		public TerminalNode INCOP() { return getToken(C2105007Parser.INCOP, 0); }
		public TerminalNode DECOP() { return getToken(C2105007Parser.DECOP, 0); }
		public FactorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_factor; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof C2105007ParserListener ) ((C2105007ParserListener)listener).enterFactor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof C2105007ParserListener ) ((C2105007ParserListener)listener).exitFactor(this);
		}
	}

	public final FactorContext factor() throws RecognitionException {
		FactorContext _localctx = new FactorContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_factor);
		try {
			setState(409);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,24,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(380);
				((FactorContext)_localctx).var = variable();

				            int lineNo = ((FactorContext)_localctx).var.var_rri.lineNo;
				            String text = ((FactorContext)_localctx).var.var_rri.text;

				            wParserLog("Line " + lineNo + ": factor : variable\n");
				            wParserLog(text + "\n");

				            ((FactorContext)_localctx).fact_rri =  new RuleReturnInfo(lineNo, text);
				        
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(383);
				((FactorContext)_localctx).ID = match(ID);
				setState(384);
				match(LPAREN);
				setState(385);
				((FactorContext)_localctx).al = argument_list();
				setState(386);
				match(RPAREN);

				            //FUNC CALL
				            int lineNo = ((FactorContext)_localctx).ID.getLine();
				            String text = ((FactorContext)_localctx).ID.getText() + "(" + ((FactorContext)_localctx).al.argList_rri.text + ")";
				            String fnName = ((FactorContext)_localctx).ID.getText().trim();
				            String type = getFuncType(fnName);
				            wParserLog("Line " + lineNo + ": factor : ID LPAREN argument_list RPAREN\n");


				            SymbolInfo2 fn_info = Main.st.lookUp(new SymbolInfo2(fnName, "ID", null, null));
				            if(fn_info == null){
				                //Error at line 62: Undefined function foo5
				                System.out.println("Error at line " + lineNo + ": Undefined function " + fnName);
				                logErr("Error at line " + lineNo + ": Undefined function " + fnName + "\n");
				            }
				            else{
				                //Check if called with valid type of arguments
				                ArrayList<String> fnParams = fn_info.getParameters();
				                System.out.println("Function " + fnName + " params : " + fnParams);
				                String[] args = ((FactorContext)_localctx).al.argList_rri.text.split(",");
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

				            ((FactorContext)_localctx).fact_rri =  new RuleReturnInfo(lineNo, text, type);            
				        
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(389);
				match(LPAREN);
				setState(390);
				((FactorContext)_localctx).e = expression();
				setState(391);
				((FactorContext)_localctx).RPAREN = match(RPAREN);

				            int lineNo = ((FactorContext)_localctx).RPAREN.getLine();
				            String text = "(" + ((FactorContext)_localctx).e.Expr_rri.text + ")";

				            wParserLog("Line " + lineNo + ": factor : LPAREN expression RPAREN\n");
				            wParserLog(text + "\n");

				            ((FactorContext)_localctx).fact_rri =  new RuleReturnInfo(lineNo, text);
				        
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(394);
				((FactorContext)_localctx).c = match(CONST_INT);

				            int lineNo = ((FactorContext)_localctx).c.getLine();
				            String text = ((FactorContext)_localctx).c.getText();

				            wParserLog("Line " + lineNo + ": factor : CONST_INT\n");
				            wParserLog(text + "\n");

				            ((FactorContext)_localctx).fact_rri =  new RuleReturnInfo(lineNo, text);
				            _localctx.fact_rri.expr_type = "int"; // Set type for CONST_INT
				        
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(396);
				match(ASSIGNOP);
				setState(397);
				((FactorContext)_localctx).c = match(CONST_INT);

				            int lineNo = ((FactorContext)_localctx).c.getLine();
				            String text = ((FactorContext)_localctx).c.getText();

				            String msg = "Error at line " + lineNo +": syntax error, unexpected ASSIGNOP";
				            logErr(msg + "\n");
				            System.out.println(msg);
				            wParserLog("Line " + lineNo + ": factor : CONST_INT\n");
				            wParserLog(text + "\n");

				            ((FactorContext)_localctx).fact_rri =  new RuleReturnInfo(lineNo, text);
				            _localctx.fact_rri.expr_type = "int"; 
				        
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(399);
				((FactorContext)_localctx).c = match(CONST_FLOAT);

				            int lineNo = ((FactorContext)_localctx).c.getLine();
				            String text = ((FactorContext)_localctx).c.getText();

				            wParserLog("Line " + lineNo + ": factor : CONST_FLOAT\n");
				            wParserLog(text + "\n");

				            ((FactorContext)_localctx).fact_rri =  new RuleReturnInfo(lineNo, text, "float"); // Set type for CONST_FLOAT
				        
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(401);
				((FactorContext)_localctx).v = variable();
				setState(402);
				((FactorContext)_localctx).INCOP = match(INCOP);

				            int lineNo = ((FactorContext)_localctx).INCOP.getLine();
				            String text = ((FactorContext)_localctx).v.var_rri.text + ((FactorContext)_localctx).INCOP.getText(); 

				            wParserLog("Line " + lineNo + ": factor : variable INCOP\n");
				            wParserLog(text + "\n");

				            ((FactorContext)_localctx).fact_rri =  new RuleReturnInfo(lineNo, text);

				        
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(405);
				((FactorContext)_localctx).v = variable();
				setState(406);
				((FactorContext)_localctx).DECOP = match(DECOP);

				            int lineNo = ((FactorContext)_localctx).DECOP.getLine();
				            String text = ((FactorContext)_localctx).v.var_rri.text + ((FactorContext)_localctx).DECOP.getText(); 

				            wParserLog("Line " + lineNo + ": factor : variable DECOP\n");
				            wParserLog(text + "\n");

				            ((FactorContext)_localctx).fact_rri =  new RuleReturnInfo(lineNo, text);

				        
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Argument_listContext extends ParserRuleContext {
		public RuleReturnInfo argList_rri;
		public ArgumentsContext a;
		public ArgumentsContext arguments() {
			return getRuleContext(ArgumentsContext.class,0);
		}
		public Argument_listContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argument_list; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof C2105007ParserListener ) ((C2105007ParserListener)listener).enterArgument_list(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof C2105007ParserListener ) ((C2105007ParserListener)listener).exitArgument_list(this);
		}
	}

	public final Argument_listContext argument_list() throws RecognitionException {
		Argument_listContext _localctx = new Argument_listContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_argument_list);
		try {
			setState(415);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LPAREN:
			case ADDOP:
			case NOT:
			case ASSIGNOP:
			case ID:
			case CONST_INT:
			case CONST_FLOAT:
				enterOuterAlt(_localctx, 1);
				{
				setState(411);
				((Argument_listContext)_localctx).a = arguments(0);

				            // arguments -> work with single/multiple arguments
				            // argument_list -> work with all arguments together

				            int lineNo = ((Argument_listContext)_localctx).a.arg_rri.lineNo;
				            String text = ((Argument_listContext)_localctx).a.arg_rri.text;

				            wParserLog("Line " + lineNo + ": argument_list : arguments\n");
				            wParserLog(text + "\n");
				            ((Argument_listContext)_localctx).argList_rri =  new RuleReturnInfo(lineNo, text);
				        
				}
				break;
			case RPAREN:
				enterOuterAlt(_localctx, 2);
				{
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ArgumentsContext extends ParserRuleContext {
		public RuleReturnInfo arg_rri;
		public ArgumentsContext a;
		public Logic_expressionContext l;
		public Token COMMA;
		public Logic_expressionContext le;
		public Logic_expressionContext logic_expression() {
			return getRuleContext(Logic_expressionContext.class,0);
		}
		public TerminalNode COMMA() { return getToken(C2105007Parser.COMMA, 0); }
		public ArgumentsContext arguments() {
			return getRuleContext(ArgumentsContext.class,0);
		}
		public ArgumentsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arguments; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof C2105007ParserListener ) ((C2105007ParserListener)listener).enterArguments(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof C2105007ParserListener ) ((C2105007ParserListener)listener).exitArguments(this);
		}
	}

	public final ArgumentsContext arguments() throws RecognitionException {
		return arguments(0);
	}

	private ArgumentsContext arguments(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ArgumentsContext _localctx = new ArgumentsContext(_ctx, _parentState);
		ArgumentsContext _prevctx = _localctx;
		int _startState = 46;
		enterRecursionRule(_localctx, 46, RULE_arguments, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(418);
			((ArgumentsContext)_localctx).l = logic_expression();

			            int lineNo = ((ArgumentsContext)_localctx).l.LogicExpr_rri.lineNo;
			            String text = ((ArgumentsContext)_localctx).l.LogicExpr_rri.text;

			            wParserLog("Line " + lineNo + ": arguments : logic_expression\n");
			            wParserLog(text + "\n");
			            ((ArgumentsContext)_localctx).arg_rri =  new RuleReturnInfo(lineNo, text);
			        
			}
			_ctx.stop = _input.LT(-1);
			setState(428);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,26,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new ArgumentsContext(_parentctx, _parentState);
					_localctx.a = _prevctx;
					pushNewRecursionContext(_localctx, _startState, RULE_arguments);
					setState(421);
					if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
					setState(422);
					((ArgumentsContext)_localctx).COMMA = match(COMMA);
					setState(423);
					((ArgumentsContext)_localctx).le = logic_expression();

					                      int lineNo = ((ArgumentsContext)_localctx).COMMA.getLine();
					                      String text = ((ArgumentsContext)_localctx).a.arg_rri.text + "," + ((ArgumentsContext)_localctx).le.LogicExpr_rri.text;

					                      wParserLog("Line " + lineNo + ": arguments : arguments COMMA logic_expression\n");
					                      wParserLog(text + "\n");
					                      ((ArgumentsContext)_localctx).arg_rri =  new RuleReturnInfo(lineNo, text);
					                  
					}
					} 
				}
				setState(430);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,26,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Non_paramsContext extends ParserRuleContext {
		public RuleReturnInfo non_param_rri;
		public Token LPAREN;
		public Token LCURL;
		public Token RCURL;
		public Token SEMICOLON;
		public Token LTHIRD;
		public Token RTHIRD;
		public Token ADDOP;
		public Token MULOP;
		public Token RELOP;
		public Token LOGICOP;
		public Token INCOP;
		public Token DECOP;
		public Token NOT;
		public TerminalNode LPAREN() { return getToken(C2105007Parser.LPAREN, 0); }
		public TerminalNode LCURL() { return getToken(C2105007Parser.LCURL, 0); }
		public TerminalNode RCURL() { return getToken(C2105007Parser.RCURL, 0); }
		public TerminalNode SEMICOLON() { return getToken(C2105007Parser.SEMICOLON, 0); }
		public TerminalNode LTHIRD() { return getToken(C2105007Parser.LTHIRD, 0); }
		public TerminalNode RTHIRD() { return getToken(C2105007Parser.RTHIRD, 0); }
		public TerminalNode ADDOP() { return getToken(C2105007Parser.ADDOP, 0); }
		public TerminalNode MULOP() { return getToken(C2105007Parser.MULOP, 0); }
		public TerminalNode RELOP() { return getToken(C2105007Parser.RELOP, 0); }
		public TerminalNode LOGICOP() { return getToken(C2105007Parser.LOGICOP, 0); }
		public TerminalNode INCOP() { return getToken(C2105007Parser.INCOP, 0); }
		public TerminalNode DECOP() { return getToken(C2105007Parser.DECOP, 0); }
		public TerminalNode NOT() { return getToken(C2105007Parser.NOT, 0); }
		public Non_paramsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_non_params; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof C2105007ParserListener ) ((C2105007ParserListener)listener).enterNon_params(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof C2105007ParserListener ) ((C2105007ParserListener)listener).exitNon_params(this);
		}
	}

	public final Non_paramsContext non_params() throws RecognitionException {
		Non_paramsContext _localctx = new Non_paramsContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_non_params);
		try {
			setState(457);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LPAREN:
				enterOuterAlt(_localctx, 1);
				{
				setState(431);
				((Non_paramsContext)_localctx).LPAREN = match(LPAREN);

				            ((Non_paramsContext)_localctx).non_param_rri =  new RuleReturnInfo((((Non_paramsContext)_localctx).LPAREN!=null?((Non_paramsContext)_localctx).LPAREN.getLine():0), "LPAREN");
				        
				}
				break;
			case LCURL:
				enterOuterAlt(_localctx, 2);
				{
				setState(433);
				((Non_paramsContext)_localctx).LCURL = match(LCURL);

				            ((Non_paramsContext)_localctx).non_param_rri =  new RuleReturnInfo((((Non_paramsContext)_localctx).LCURL!=null?((Non_paramsContext)_localctx).LCURL.getLine():0), "LCURL");
				        
				}
				break;
			case RCURL:
				enterOuterAlt(_localctx, 3);
				{
				setState(435);
				((Non_paramsContext)_localctx).RCURL = match(RCURL);

				            ((Non_paramsContext)_localctx).non_param_rri =  new RuleReturnInfo((((Non_paramsContext)_localctx).RCURL!=null?((Non_paramsContext)_localctx).RCURL.getLine():0), "RCURL");
				            
				}
				break;
			case SEMICOLON:
				enterOuterAlt(_localctx, 4);
				{
				setState(437);
				((Non_paramsContext)_localctx).SEMICOLON = match(SEMICOLON);

				            ((Non_paramsContext)_localctx).non_param_rri =  new RuleReturnInfo((((Non_paramsContext)_localctx).SEMICOLON!=null?((Non_paramsContext)_localctx).SEMICOLON.getLine():0), "SEMICOLON");
				        
				}
				break;
			case LTHIRD:
				enterOuterAlt(_localctx, 5);
				{
				setState(439);
				((Non_paramsContext)_localctx).LTHIRD = match(LTHIRD);
				((Non_paramsContext)_localctx).non_param_rri =  new RuleReturnInfo((((Non_paramsContext)_localctx).LTHIRD!=null?((Non_paramsContext)_localctx).LTHIRD.getLine():0), "LTHIRD");
				}
				break;
			case RTHIRD:
				enterOuterAlt(_localctx, 6);
				{
				setState(441);
				((Non_paramsContext)_localctx).RTHIRD = match(RTHIRD);

				            ((Non_paramsContext)_localctx).non_param_rri =  new RuleReturnInfo((((Non_paramsContext)_localctx).RTHIRD!=null?((Non_paramsContext)_localctx).RTHIRD.getLine():0), "RTHIRD");
				    
				}
				break;
			case ADDOP:
				enterOuterAlt(_localctx, 7);
				{
				setState(443);
				((Non_paramsContext)_localctx).ADDOP = match(ADDOP);
				((Non_paramsContext)_localctx).non_param_rri =  new RuleReturnInfo((((Non_paramsContext)_localctx).ADDOP!=null?((Non_paramsContext)_localctx).ADDOP.getLine():0), "ADDOP");
				}
				break;
			case MULOP:
				enterOuterAlt(_localctx, 8);
				{
				setState(445);
				((Non_paramsContext)_localctx).MULOP = match(MULOP);
				((Non_paramsContext)_localctx).non_param_rri =  new RuleReturnInfo((((Non_paramsContext)_localctx).MULOP!=null?((Non_paramsContext)_localctx).MULOP.getLine():0), "MULOP");
				}
				break;
			case RELOP:
				enterOuterAlt(_localctx, 9);
				{
				setState(447);
				((Non_paramsContext)_localctx).RELOP = match(RELOP);
				((Non_paramsContext)_localctx).non_param_rri =  new RuleReturnInfo((((Non_paramsContext)_localctx).RELOP!=null?((Non_paramsContext)_localctx).RELOP.getLine():0), "RELOP");
				}
				break;
			case LOGICOP:
				enterOuterAlt(_localctx, 10);
				{
				setState(449);
				((Non_paramsContext)_localctx).LOGICOP = match(LOGICOP);
				((Non_paramsContext)_localctx).non_param_rri =  new RuleReturnInfo((((Non_paramsContext)_localctx).LOGICOP!=null?((Non_paramsContext)_localctx).LOGICOP.getLine():0), "LOGICOP");
				}
				break;
			case INCOP:
				enterOuterAlt(_localctx, 11);
				{
				setState(451);
				((Non_paramsContext)_localctx).INCOP = match(INCOP);
				((Non_paramsContext)_localctx).non_param_rri =  new RuleReturnInfo((((Non_paramsContext)_localctx).INCOP!=null?((Non_paramsContext)_localctx).INCOP.getLine():0), "INCOP");
				}
				break;
			case DECOP:
				enterOuterAlt(_localctx, 12);
				{
				setState(453);
				((Non_paramsContext)_localctx).DECOP = match(DECOP);
				((Non_paramsContext)_localctx).non_param_rri =  new RuleReturnInfo((((Non_paramsContext)_localctx).DECOP!=null?((Non_paramsContext)_localctx).DECOP.getLine():0), "DECOP");
				}
				break;
			case NOT:
				enterOuterAlt(_localctx, 13);
				{
				setState(455);
				((Non_paramsContext)_localctx).NOT = match(NOT);

				            ((Non_paramsContext)_localctx).non_param_rri =  new RuleReturnInfo((((Non_paramsContext)_localctx).NOT!=null?((Non_paramsContext)_localctx).NOT.getLine():0), "NOT");
				         
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Invalid_charContext extends ParserRuleContext {
		public RuleReturnInfo invalid_rri;
		public Token ic;
		public TerminalNode INVALID_CHAR() { return getToken(C2105007Parser.INVALID_CHAR, 0); }
		public Invalid_charContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_invalid_char; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof C2105007ParserListener ) ((C2105007ParserListener)listener).enterInvalid_char(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof C2105007ParserListener ) ((C2105007ParserListener)listener).exitInvalid_char(this);
		}
	}

	public final Invalid_charContext invalid_char() throws RecognitionException {
		Invalid_charContext _localctx = new Invalid_charContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_invalid_char);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(459);
			((Invalid_charContext)_localctx).ic = match(INVALID_CHAR);

			        //Error at line 10: Unrecognized character #
			        // String errorMessage = "Error at line " + ((Invalid_charContext)_localctx).ic.getLine() + ": Unrecognized character " + ((Invalid_charContext)_localctx).ic.getText();
			        // wParserLog(errorMessage + "\n");
			        // logErr(errorMessage + "\n");
			        ((Invalid_charContext)_localctx).invalid_rri =  new RuleReturnInfo(((Invalid_charContext)_localctx).ic.getLine(), ((Invalid_charContext)_localctx).ic.getText());
			    
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 1:
			return program_sempred((ProgramContext)_localctx, predIndex);
		case 5:
			return parameter_list_sempred((Parameter_listContext)_localctx, predIndex);
		case 10:
			return declaration_list_sempred((Declaration_listContext)_localctx, predIndex);
		case 11:
			return statements_sempred((StatementsContext)_localctx, predIndex);
		case 18:
			return simple_expression_sempred((Simple_expressionContext)_localctx, predIndex);
		case 19:
			return term_sempred((TermContext)_localctx, predIndex);
		case 23:
			return arguments_sempred((ArgumentsContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean program_sempred(ProgramContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 2);
		}
		return true;
	}
	private boolean parameter_list_sempred(Parameter_listContext _localctx, int predIndex) {
		switch (predIndex) {
		case 1:
			return precpred(_ctx, 4);
		case 2:
			return precpred(_ctx, 3);
		}
		return true;
	}
	private boolean declaration_list_sempred(Declaration_listContext _localctx, int predIndex) {
		switch (predIndex) {
		case 3:
			return precpred(_ctx, 5);
		case 4:
			return precpred(_ctx, 4);
		case 5:
			return precpred(_ctx, 3);
		}
		return true;
	}
	private boolean statements_sempred(StatementsContext _localctx, int predIndex) {
		switch (predIndex) {
		case 6:
			return precpred(_ctx, 1);
		}
		return true;
	}
	private boolean simple_expression_sempred(Simple_expressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 7:
			return precpred(_ctx, 2);
		case 8:
			return precpred(_ctx, 1);
		}
		return true;
	}
	private boolean term_sempred(TermContext _localctx, int predIndex) {
		switch (predIndex) {
		case 9:
			return precpred(_ctx, 1);
		}
		return true;
	}
	private boolean arguments_sempred(ArgumentsContext _localctx, int predIndex) {
		switch (predIndex) {
		case 10:
			return precpred(_ctx, 2);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u0001\"\u01cf\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"+
		"\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012"+
		"\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007\u0015"+
		"\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002\u0018\u0007\u0018"+
		"\u0002\u0019\u0007\u0019\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0005\u0001@\b\u0001\n\u0001\f\u0001C\t\u0001\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0003\u0002N\b\u0002\u0001\u0003\u0001\u0003"+
		"\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003"+
		"\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003"+
		"\u0001\u0003\u0003\u0003_\b\u0003\u0001\u0004\u0001\u0004\u0001\u0004"+
		"\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004"+
		"\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004"+
		"\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004"+
		"\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004"+
		"\u0001\u0004\u0003\u0004}\b\u0004\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0003\u0005"+
		"\u0087\b\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0005\u0005\u0094\b\u0005\n\u0005\f\u0005\u0097\t\u0005\u0001\u0006\u0001"+
		"\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001"+
		"\u0006\u0001\u0006\u0001\u0006\u0003\u0006\u00a3\b\u0006\u0001\u0007\u0001"+
		"\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001"+
		"\u0007\u0001\u0007\u0001\u0007\u0003\u0007\u00af\b\u0007\u0001\b\u0001"+
		"\b\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0003\t\u00b9\b\t\u0001"+
		"\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0003\n\u00c3"+
		"\b\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001"+
		"\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0005"+
		"\n\u00d6\b\n\n\n\f\n\u00d9\t\n\u0001\u000b\u0001\u000b\u0001\u000b\u0001"+
		"\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0005\u000b\u00e3"+
		"\b\u000b\n\u000b\f\u000b\u00e6\t\u000b\u0001\f\u0001\f\u0001\f\u0001\f"+
		"\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001"+
		"\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001"+
		"\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001"+
		"\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001"+
		"\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001"+
		"\f\u0001\f\u0001\f\u0001\f\u0003\f\u011c\b\f\u0001\r\u0001\r\u0001\r\u0001"+
		"\r\u0001\r\u0001\r\u0003\r\u0124\b\r\u0001\u000e\u0001\u000e\u0001\u000e"+
		"\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0003\u000e"+
		"\u012e\b\u000e\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f"+
		"\u0001\u000f\u0001\u000f\u0001\u000f\u0003\u000f\u0138\b\u000f\u0001\u0010"+
		"\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010"+
		"\u0001\u0010\u0003\u0010\u0142\b\u0010\u0001\u0011\u0001\u0011\u0001\u0011"+
		"\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0003\u0011"+
		"\u014c\b\u0011\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012"+
		"\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012"+
		"\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0005\u0012\u015d\b\u0012"+
		"\n\u0012\f\u0012\u0160\t\u0012\u0001\u0013\u0001\u0013\u0001\u0013\u0001"+
		"\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0005"+
		"\u0013\u016b\b\u0013\n\u0013\f\u0013\u016e\t\u0013\u0001\u0014\u0001\u0014"+
		"\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014"+
		"\u0001\u0014\u0001\u0014\u0001\u0014\u0003\u0014\u017b\b\u0014\u0001\u0015"+
		"\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015"+
		"\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015"+
		"\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015"+
		"\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015"+
		"\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0003\u0015\u019a\b\u0015"+
		"\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0003\u0016\u01a0\b\u0016"+
		"\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017"+
		"\u0001\u0017\u0001\u0017\u0001\u0017\u0005\u0017\u01ab\b\u0017\n\u0017"+
		"\f\u0017\u01ae\t\u0017\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018"+
		"\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018"+
		"\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018"+
		"\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018"+
		"\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0003\u0018\u01ca\b\u0018"+
		"\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0000\u0007\u0002\n\u0014"+
		"\u0016$&.\u001a\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014"+
		"\u0016\u0018\u001a\u001c\u001e \"$&(*,.02\u0000\u0000\u01ed\u00004\u0001"+
		"\u0000\u0000\u0000\u00027\u0001\u0000\u0000\u0000\u0004M\u0001\u0000\u0000"+
		"\u0000\u0006^\u0001\u0000\u0000\u0000\b|\u0001\u0000\u0000\u0000\n\u0086"+
		"\u0001\u0000\u0000\u0000\f\u00a2\u0001\u0000\u0000\u0000\u000e\u00ae\u0001"+
		"\u0000\u0000\u0000\u0010\u00b0\u0001\u0000\u0000\u0000\u0012\u00b8\u0001"+
		"\u0000\u0000\u0000\u0014\u00c2\u0001\u0000\u0000\u0000\u0016\u00da\u0001"+
		"\u0000\u0000\u0000\u0018\u011b\u0001\u0000\u0000\u0000\u001a\u0123\u0001"+
		"\u0000\u0000\u0000\u001c\u012d\u0001\u0000\u0000\u0000\u001e\u0137\u0001"+
		"\u0000\u0000\u0000 \u0141\u0001\u0000\u0000\u0000\"\u014b\u0001\u0000"+
		"\u0000\u0000$\u014d\u0001\u0000\u0000\u0000&\u0161\u0001\u0000\u0000\u0000"+
		"(\u017a\u0001\u0000\u0000\u0000*\u0199\u0001\u0000\u0000\u0000,\u019f"+
		"\u0001\u0000\u0000\u0000.\u01a1\u0001\u0000\u0000\u00000\u01c9\u0001\u0000"+
		"\u0000\u00002\u01cb\u0001\u0000\u0000\u000045\u0003\u0002\u0001\u0000"+
		"56\u0006\u0000\uffff\uffff\u00006\u0001\u0001\u0000\u0000\u000078\u0006"+
		"\u0001\uffff\uffff\u000089\u0003\u0004\u0002\u00009:\u0006\u0001\uffff"+
		"\uffff\u0000:A\u0001\u0000\u0000\u0000;<\n\u0002\u0000\u0000<=\u0003\u0004"+
		"\u0002\u0000=>\u0006\u0001\uffff\uffff\u0000>@\u0001\u0000\u0000\u0000"+
		"?;\u0001\u0000\u0000\u0000@C\u0001\u0000\u0000\u0000A?\u0001\u0000\u0000"+
		"\u0000AB\u0001\u0000\u0000\u0000B\u0003\u0001\u0000\u0000\u0000CA\u0001"+
		"\u0000\u0000\u0000DE\u0003\u000e\u0007\u0000EF\u0006\u0002\uffff\uffff"+
		"\u0000FN\u0001\u0000\u0000\u0000GH\u0003\u0006\u0003\u0000HI\u0006\u0002"+
		"\uffff\uffff\u0000IN\u0001\u0000\u0000\u0000JK\u0003\b\u0004\u0000KL\u0006"+
		"\u0002\uffff\uffff\u0000LN\u0001\u0000\u0000\u0000MD\u0001\u0000\u0000"+
		"\u0000MG\u0001\u0000\u0000\u0000MJ\u0001\u0000\u0000\u0000N\u0005\u0001"+
		"\u0000\u0000\u0000OP\u0003\u0012\t\u0000PQ\u0005\u001f\u0000\u0000QR\u0005"+
		"\u000e\u0000\u0000RS\u0003\n\u0005\u0000ST\u0005\u000f\u0000\u0000TU\u0005"+
		"\u0014\u0000\u0000UV\u0006\u0003\uffff\uffff\u0000V_\u0001\u0000\u0000"+
		"\u0000WX\u0003\u0012\t\u0000XY\u0005\u001f\u0000\u0000YZ\u0005\u000e\u0000"+
		"\u0000Z[\u0005\u000f\u0000\u0000[\\\u0005\u0014\u0000\u0000\\]\u0006\u0003"+
		"\uffff\uffff\u0000]_\u0001\u0000\u0000\u0000^O\u0001\u0000\u0000\u0000"+
		"^W\u0001\u0000\u0000\u0000_\u0007\u0001\u0000\u0000\u0000`a\u0003\u0012"+
		"\t\u0000ab\u0005\u001f\u0000\u0000bc\u0005\u000e\u0000\u0000cd\u0003\n"+
		"\u0005\u0000de\u0005\u000f\u0000\u0000ef\u0006\u0004\uffff\uffff\u0000"+
		"fg\u0003\f\u0006\u0000gh\u0006\u0004\uffff\uffff\u0000h}\u0001\u0000\u0000"+
		"\u0000ij\u0003\u0012\t\u0000jk\u0005\u001f\u0000\u0000kl\u0005\u000e\u0000"+
		"\u0000lm\u0005\u000f\u0000\u0000mn\u0006\u0004\uffff\uffff\u0000no\u0003"+
		"\f\u0006\u0000op\u0006\u0004\uffff\uffff\u0000p}\u0001\u0000\u0000\u0000"+
		"qr\u0003\u0012\t\u0000rs\u0005\u001f\u0000\u0000st\u0005\u000e\u0000\u0000"+
		"tu\u0003\n\u0005\u0000uv\u00030\u0018\u0000vw\u0006\u0004\uffff\uffff"+
		"\u0000wx\u0005\u000f\u0000\u0000xy\u0006\u0004\uffff\uffff\u0000yz\u0003"+
		"\f\u0006\u0000z{\u0006\u0004\uffff\uffff\u0000{}\u0001\u0000\u0000\u0000"+
		"|`\u0001\u0000\u0000\u0000|i\u0001\u0000\u0000\u0000|q\u0001\u0000\u0000"+
		"\u0000}\t\u0001\u0000\u0000\u0000~\u007f\u0006\u0005\uffff\uffff\u0000"+
		"\u007f\u0080\u0003\u0012\t\u0000\u0080\u0081\u0005\u001f\u0000\u0000\u0081"+
		"\u0082\u0006\u0005\uffff\uffff\u0000\u0082\u0087\u0001\u0000\u0000\u0000"+
		"\u0083\u0084\u0003\u0012\t\u0000\u0084\u0085\u0006\u0005\uffff\uffff\u0000"+
		"\u0085\u0087\u0001\u0000\u0000\u0000\u0086~\u0001\u0000\u0000\u0000\u0086"+
		"\u0083\u0001\u0000\u0000\u0000\u0087\u0095\u0001\u0000\u0000\u0000\u0088"+
		"\u0089\n\u0004\u0000\u0000\u0089\u008a\u0005\u0015\u0000\u0000\u008a\u008b"+
		"\u0003\u0012\t\u0000\u008b\u008c\u0005\u001f\u0000\u0000\u008c\u008d\u0006"+
		"\u0005\uffff\uffff\u0000\u008d\u0094\u0001\u0000\u0000\u0000\u008e\u008f"+
		"\n\u0003\u0000\u0000\u008f\u0090\u0005\u0015\u0000\u0000\u0090\u0091\u0003"+
		"\u0012\t\u0000\u0091\u0092\u0006\u0005\uffff\uffff\u0000\u0092\u0094\u0001"+
		"\u0000\u0000\u0000\u0093\u0088\u0001\u0000\u0000\u0000\u0093\u008e\u0001"+
		"\u0000\u0000\u0000\u0094\u0097\u0001\u0000\u0000\u0000\u0095\u0093\u0001"+
		"\u0000\u0000\u0000\u0095\u0096\u0001\u0000\u0000\u0000\u0096\u000b\u0001"+
		"\u0000\u0000\u0000\u0097\u0095\u0001\u0000\u0000\u0000\u0098\u0099\u0005"+
		"\u0010\u0000\u0000\u0099\u009a\u0006\u0006\uffff\uffff\u0000\u009a\u009b"+
		"\u0003\u0016\u000b\u0000\u009b\u009c\u0005\u0011\u0000\u0000\u009c\u009d"+
		"\u0006\u0006\uffff\uffff\u0000\u009d\u00a3\u0001\u0000\u0000\u0000\u009e"+
		"\u009f\u0005\u0010\u0000\u0000\u009f\u00a0\u0006\u0006\uffff\uffff\u0000"+
		"\u00a0\u00a1\u0005\u0011\u0000\u0000\u00a1\u00a3\u0006\u0006\uffff\uffff"+
		"\u0000\u00a2\u0098\u0001\u0000\u0000\u0000\u00a2\u009e\u0001\u0000\u0000"+
		"\u0000\u00a3\r\u0001\u0000\u0000\u0000\u00a4\u00a5\u0003\u0012\t\u0000"+
		"\u00a5\u00a6\u0003\u0014\n\u0000\u00a6\u00a7\u0005\u0014\u0000\u0000\u00a7"+
		"\u00a8\u0006\u0007\uffff\uffff\u0000\u00a8\u00af\u0001\u0000\u0000\u0000"+
		"\u00a9\u00aa\u0003\u0012\t\u0000\u00aa\u00ab\u0003\u0010\b\u0000\u00ab"+
		"\u00ac\u0005\u0014\u0000\u0000\u00ac\u00ad\u0006\u0007\uffff\uffff\u0000"+
		"\u00ad\u00af\u0001\u0000\u0000\u0000\u00ae\u00a4\u0001\u0000\u0000\u0000"+
		"\u00ae\u00a9\u0001\u0000\u0000\u0000\u00af\u000f\u0001\u0000\u0000\u0000"+
		"\u00b0\u00b1\u0006\b\uffff\uffff\u0000\u00b1\u0011\u0001\u0000\u0000\u0000"+
		"\u00b2\u00b3\u0005\u000b\u0000\u0000\u00b3\u00b9\u0006\t\uffff\uffff\u0000"+
		"\u00b4\u00b5\u0005\f\u0000\u0000\u00b5\u00b9\u0006\t\uffff\uffff\u0000"+
		"\u00b6\u00b7\u0005\r\u0000\u0000\u00b7\u00b9\u0006\t\uffff\uffff\u0000"+
		"\u00b8\u00b2\u0001\u0000\u0000\u0000\u00b8\u00b4\u0001\u0000\u0000\u0000"+
		"\u00b8\u00b6\u0001\u0000\u0000\u0000\u00b9\u0013\u0001\u0000\u0000\u0000"+
		"\u00ba\u00bb\u0006\n\uffff\uffff\u0000\u00bb\u00bc\u0005\u001f\u0000\u0000"+
		"\u00bc\u00c3\u0006\n\uffff\uffff\u0000\u00bd\u00be\u0005\u001f\u0000\u0000"+
		"\u00be\u00bf\u0005\u0012\u0000\u0000\u00bf\u00c0\u0005 \u0000\u0000\u00c0"+
		"\u00c1\u0005\u0013\u0000\u0000\u00c1\u00c3\u0006\n\uffff\uffff\u0000\u00c2"+
		"\u00ba\u0001\u0000\u0000\u0000\u00c2\u00bd\u0001\u0000\u0000\u0000\u00c3"+
		"\u00d7\u0001\u0000\u0000\u0000\u00c4\u00c5\n\u0005\u0000\u0000\u00c5\u00c6"+
		"\u0005\u0015\u0000\u0000\u00c6\u00c7\u0005\u001f\u0000\u0000\u00c7\u00d6"+
		"\u0006\n\uffff\uffff\u0000\u00c8\u00c9\n\u0004\u0000\u0000\u00c9\u00ca"+
		"\u00030\u0018\u0000\u00ca\u00cb\u0005\u0015\u0000\u0000\u00cb\u00cc\u0005"+
		"\u001f\u0000\u0000\u00cc\u00cd\u0006\n\uffff\uffff\u0000\u00cd\u00d6\u0001"+
		"\u0000\u0000\u0000\u00ce\u00cf\n\u0003\u0000\u0000\u00cf\u00d0\u0005\u0015"+
		"\u0000\u0000\u00d0\u00d1\u0005\u001f\u0000\u0000\u00d1\u00d2\u0005\u0012"+
		"\u0000\u0000\u00d2\u00d3\u0005 \u0000\u0000\u00d3\u00d4\u0005\u0013\u0000"+
		"\u0000\u00d4\u00d6\u0006\n\uffff\uffff\u0000\u00d5\u00c4\u0001\u0000\u0000"+
		"\u0000\u00d5\u00c8\u0001\u0000\u0000\u0000\u00d5\u00ce\u0001\u0000\u0000"+
		"\u0000\u00d6\u00d9\u0001\u0000\u0000\u0000\u00d7\u00d5\u0001\u0000\u0000"+
		"\u0000\u00d7\u00d8\u0001\u0000\u0000\u0000\u00d8\u0015\u0001\u0000\u0000"+
		"\u0000\u00d9\u00d7\u0001\u0000\u0000\u0000\u00da\u00db\u0006\u000b\uffff"+
		"\uffff\u0000\u00db\u00dc\u0003\u0018\f\u0000\u00dc\u00dd\u0006\u000b\uffff"+
		"\uffff\u0000\u00dd\u00e4\u0001\u0000\u0000\u0000\u00de\u00df\n\u0001\u0000"+
		"\u0000\u00df\u00e0\u0003\u0018\f\u0000\u00e0\u00e1\u0006\u000b\uffff\uffff"+
		"\u0000\u00e1\u00e3\u0001\u0000\u0000\u0000\u00e2\u00de\u0001\u0000\u0000"+
		"\u0000\u00e3\u00e6\u0001\u0000\u0000\u0000\u00e4\u00e2\u0001\u0000\u0000"+
		"\u0000\u00e4\u00e5\u0001\u0000\u0000\u0000\u00e5\u0017\u0001\u0000\u0000"+
		"\u0000\u00e6\u00e4\u0001\u0000\u0000\u0000\u00e7\u00e8\u0003\u000e\u0007"+
		"\u0000\u00e8\u00e9\u0006\f\uffff\uffff\u0000\u00e9\u011c\u0001\u0000\u0000"+
		"\u0000\u00ea\u00eb\u0003\u001a\r\u0000\u00eb\u00ec\u0006\f\uffff\uffff"+
		"\u0000\u00ec\u011c\u0001\u0000\u0000\u0000\u00ed\u00ee\u0003\f\u0006\u0000"+
		"\u00ee\u00ef\u0006\f\uffff\uffff\u0000\u00ef\u011c\u0001\u0000\u0000\u0000"+
		"\u00f0\u00f1\u0005\u0007\u0000\u0000\u00f1\u00f2\u0005\u000e\u0000\u0000"+
		"\u00f2\u00f3\u0003\u001a\r\u0000\u00f3\u00f4\u0003\u001a\r\u0000\u00f4"+
		"\u00f5\u0003\u001e\u000f\u0000\u00f5\u00f6\u0005\u000f\u0000\u0000\u00f6"+
		"\u00f7\u0003\u0018\f\u0000\u00f7\u00f8\u0006\f\uffff\uffff\u0000\u00f8"+
		"\u011c\u0001\u0000\u0000\u0000\u00f9\u00fa\u0005\u0005\u0000\u0000\u00fa"+
		"\u00fb\u0005\u000e\u0000\u0000\u00fb\u00fc\u0003\u001e\u000f\u0000\u00fc"+
		"\u00fd\u0005\u000f\u0000\u0000\u00fd\u00fe\u0003\u0018\f\u0000\u00fe\u00ff"+
		"\u0006\f\uffff\uffff\u0000\u00ff\u011c\u0001\u0000\u0000\u0000\u0100\u0101"+
		"\u0005\u0005\u0000\u0000\u0101\u0102\u0005\u000e\u0000\u0000\u0102\u0103"+
		"\u0003\u001e\u000f\u0000\u0103\u0104\u0005\u000f\u0000\u0000\u0104\u0105"+
		"\u0003\u0018\f\u0000\u0105\u0106\u0005\u0006\u0000\u0000\u0106\u0107\u0003"+
		"\u0018\f\u0000\u0107\u0108\u0006\f\uffff\uffff\u0000\u0108\u011c\u0001"+
		"\u0000\u0000\u0000\u0109\u010a\u0005\b\u0000\u0000\u010a\u010b\u0005\u000e"+
		"\u0000\u0000\u010b\u010c\u0003\u001e\u000f\u0000\u010c\u010d\u0005\u000f"+
		"\u0000\u0000\u010d\u010e\u0003\u0018\f\u0000\u010e\u010f\u0006\f\uffff"+
		"\uffff\u0000\u010f\u011c\u0001\u0000\u0000\u0000\u0110\u0111\u0005\t\u0000"+
		"\u0000\u0111\u0112\u0005\u000e\u0000\u0000\u0112\u0113\u0005\u001f\u0000"+
		"\u0000\u0113\u0114\u0005\u000f\u0000\u0000\u0114\u0115\u0005\u0014\u0000"+
		"\u0000\u0115\u011c\u0006\f\uffff\uffff\u0000\u0116\u0117\u0005\n\u0000"+
		"\u0000\u0117\u0118\u0003\u001e\u000f\u0000\u0118\u0119\u0005\u0014\u0000"+
		"\u0000\u0119\u011a\u0006\f\uffff\uffff\u0000\u011a\u011c\u0001\u0000\u0000"+
		"\u0000\u011b\u00e7\u0001\u0000\u0000\u0000\u011b\u00ea\u0001\u0000\u0000"+
		"\u0000\u011b\u00ed\u0001\u0000\u0000\u0000\u011b\u00f0\u0001\u0000\u0000"+
		"\u0000\u011b\u00f9\u0001\u0000\u0000\u0000\u011b\u0100\u0001\u0000\u0000"+
		"\u0000\u011b\u0109\u0001\u0000\u0000\u0000\u011b\u0110\u0001\u0000\u0000"+
		"\u0000\u011b\u0116\u0001\u0000\u0000\u0000\u011c\u0019\u0001\u0000\u0000"+
		"\u0000\u011d\u011e\u0005\u0014\u0000\u0000\u011e\u0124\u0006\r\uffff\uffff"+
		"\u0000\u011f\u0120\u0003\u001e\u000f\u0000\u0120\u0121\u0005\u0014\u0000"+
		"\u0000\u0121\u0122\u0006\r\uffff\uffff\u0000\u0122\u0124\u0001\u0000\u0000"+
		"\u0000\u0123\u011d\u0001\u0000\u0000\u0000\u0123\u011f\u0001\u0000\u0000"+
		"\u0000\u0124\u001b\u0001\u0000\u0000\u0000\u0125\u0126\u0005\u001f\u0000"+
		"\u0000\u0126\u012e\u0006\u000e\uffff\uffff\u0000\u0127\u0128\u0005\u001f"+
		"\u0000\u0000\u0128\u0129\u0005\u0012\u0000\u0000\u0129\u012a\u0003\u001e"+
		"\u000f\u0000\u012a\u012b\u0005\u0013\u0000\u0000\u012b\u012c\u0006\u000e"+
		"\uffff\uffff\u0000\u012c\u012e\u0001\u0000\u0000\u0000\u012d\u0125\u0001"+
		"\u0000\u0000\u0000\u012d\u0127\u0001\u0000\u0000\u0000\u012e\u001d\u0001"+
		"\u0000\u0000\u0000\u012f\u0130\u0003 \u0010\u0000\u0130\u0131\u0006\u000f"+
		"\uffff\uffff\u0000\u0131\u0138\u0001\u0000\u0000\u0000\u0132\u0133\u0003"+
		"\u001c\u000e\u0000\u0133\u0134\u0005\u001e\u0000\u0000\u0134\u0135\u0003"+
		" \u0010\u0000\u0135\u0136\u0006\u000f\uffff\uffff\u0000\u0136\u0138\u0001"+
		"\u0000\u0000\u0000\u0137\u012f\u0001\u0000\u0000\u0000\u0137\u0132\u0001"+
		"\u0000\u0000\u0000\u0138\u001f\u0001\u0000\u0000\u0000\u0139\u013a\u0003"+
		"\"\u0011\u0000\u013a\u013b\u0006\u0010\uffff\uffff\u0000\u013b\u0142\u0001"+
		"\u0000\u0000\u0000\u013c\u013d\u0003\"\u0011\u0000\u013d\u013e\u0005\u001d"+
		"\u0000\u0000\u013e\u013f\u0003\"\u0011\u0000\u013f\u0140\u0006\u0010\uffff"+
		"\uffff\u0000\u0140\u0142\u0001\u0000\u0000\u0000\u0141\u0139\u0001\u0000"+
		"\u0000\u0000\u0141\u013c\u0001\u0000\u0000\u0000\u0142!\u0001\u0000\u0000"+
		"\u0000\u0143\u0144\u0003$\u0012\u0000\u0144\u0145\u0006\u0011\uffff\uffff"+
		"\u0000\u0145\u014c\u0001\u0000\u0000\u0000\u0146\u0147\u0003$\u0012\u0000"+
		"\u0147\u0148\u0005\u001c\u0000\u0000\u0148\u0149\u0003$\u0012\u0000\u0149"+
		"\u014a\u0006\u0011\uffff\uffff\u0000\u014a\u014c\u0001\u0000\u0000\u0000"+
		"\u014b\u0143\u0001\u0000\u0000\u0000\u014b\u0146\u0001\u0000\u0000\u0000"+
		"\u014c#\u0001\u0000\u0000\u0000\u014d\u014e\u0006\u0012\uffff\uffff\u0000"+
		"\u014e\u014f\u0003&\u0013\u0000\u014f\u0150\u0006\u0012\uffff\uffff\u0000"+
		"\u0150\u015e\u0001\u0000\u0000\u0000\u0151\u0152\n\u0002\u0000\u0000\u0152"+
		"\u0153\u0005\u0016\u0000\u0000\u0153\u0154\u0003&\u0013\u0000\u0154\u0155"+
		"\u0006\u0012\uffff\uffff\u0000\u0155\u015d\u0001\u0000\u0000\u0000\u0156"+
		"\u0157\n\u0001\u0000\u0000\u0157\u0158\u0005\u0016\u0000\u0000\u0158\u0159"+
		"\u0003&\u0013\u0000\u0159\u015a\u00032\u0019\u0000\u015a\u015b\u0006\u0012"+
		"\uffff\uffff\u0000\u015b\u015d\u0001\u0000\u0000\u0000\u015c\u0151\u0001"+
		"\u0000\u0000\u0000\u015c\u0156\u0001\u0000\u0000\u0000\u015d\u0160\u0001"+
		"\u0000\u0000\u0000\u015e\u015c\u0001\u0000\u0000\u0000\u015e\u015f\u0001"+
		"\u0000\u0000\u0000\u015f%\u0001\u0000\u0000\u0000\u0160\u015e\u0001\u0000"+
		"\u0000\u0000\u0161\u0162\u0006\u0013\uffff\uffff\u0000\u0162\u0163\u0003"+
		"(\u0014\u0000\u0163\u0164\u0006\u0013\uffff\uffff\u0000\u0164\u016c\u0001"+
		"\u0000\u0000\u0000\u0165\u0166\n\u0001\u0000\u0000\u0166\u0167\u0005\u0018"+
		"\u0000\u0000\u0167\u0168\u0003(\u0014\u0000\u0168\u0169\u0006\u0013\uffff"+
		"\uffff\u0000\u0169\u016b\u0001\u0000\u0000\u0000\u016a\u0165\u0001\u0000"+
		"\u0000\u0000\u016b\u016e\u0001\u0000\u0000\u0000\u016c\u016a\u0001\u0000"+
		"\u0000\u0000\u016c\u016d\u0001\u0000\u0000\u0000\u016d\'\u0001\u0000\u0000"+
		"\u0000\u016e\u016c\u0001\u0000\u0000\u0000\u016f\u0170\u0005\u0016\u0000"+
		"\u0000\u0170\u0171\u0003(\u0014\u0000\u0171\u0172\u0006\u0014\uffff\uffff"+
		"\u0000\u0172\u017b\u0001\u0000\u0000\u0000\u0173\u0174\u0005\u001b\u0000"+
		"\u0000\u0174\u0175\u0003(\u0014\u0000\u0175\u0176\u0006\u0014\uffff\uffff"+
		"\u0000\u0176\u017b\u0001\u0000\u0000\u0000\u0177\u0178\u0003*\u0015\u0000"+
		"\u0178\u0179\u0006\u0014\uffff\uffff\u0000\u0179\u017b\u0001\u0000\u0000"+
		"\u0000\u017a\u016f\u0001\u0000\u0000\u0000\u017a\u0173\u0001\u0000\u0000"+
		"\u0000\u017a\u0177\u0001\u0000\u0000\u0000\u017b)\u0001\u0000\u0000\u0000"+
		"\u017c\u017d\u0003\u001c\u000e\u0000\u017d\u017e\u0006\u0015\uffff\uffff"+
		"\u0000\u017e\u019a\u0001\u0000\u0000\u0000\u017f\u0180\u0005\u001f\u0000"+
		"\u0000\u0180\u0181\u0005\u000e\u0000\u0000\u0181\u0182\u0003,\u0016\u0000"+
		"\u0182\u0183\u0005\u000f\u0000\u0000\u0183\u0184\u0006\u0015\uffff\uffff"+
		"\u0000\u0184\u019a\u0001\u0000\u0000\u0000\u0185\u0186\u0005\u000e\u0000"+
		"\u0000\u0186\u0187\u0003\u001e\u000f\u0000\u0187\u0188\u0005\u000f\u0000"+
		"\u0000\u0188\u0189\u0006\u0015\uffff\uffff\u0000\u0189\u019a\u0001\u0000"+
		"\u0000\u0000\u018a\u018b\u0005 \u0000\u0000\u018b\u019a\u0006\u0015\uffff"+
		"\uffff\u0000\u018c\u018d\u0005\u001e\u0000\u0000\u018d\u018e\u0005 \u0000"+
		"\u0000\u018e\u019a\u0006\u0015\uffff\uffff\u0000\u018f\u0190\u0005!\u0000"+
		"\u0000\u0190\u019a\u0006\u0015\uffff\uffff\u0000\u0191\u0192\u0003\u001c"+
		"\u000e\u0000\u0192\u0193\u0005\u0019\u0000\u0000\u0193\u0194\u0006\u0015"+
		"\uffff\uffff\u0000\u0194\u019a\u0001\u0000\u0000\u0000\u0195\u0196\u0003"+
		"\u001c\u000e\u0000\u0196\u0197\u0005\u001a\u0000\u0000\u0197\u0198\u0006"+
		"\u0015\uffff\uffff\u0000\u0198\u019a\u0001\u0000\u0000\u0000\u0199\u017c"+
		"\u0001\u0000\u0000\u0000\u0199\u017f\u0001\u0000\u0000\u0000\u0199\u0185"+
		"\u0001\u0000\u0000\u0000\u0199\u018a\u0001\u0000\u0000\u0000\u0199\u018c"+
		"\u0001\u0000\u0000\u0000\u0199\u018f\u0001\u0000\u0000\u0000\u0199\u0191"+
		"\u0001\u0000\u0000\u0000\u0199\u0195\u0001\u0000\u0000\u0000\u019a+\u0001"+
		"\u0000\u0000\u0000\u019b\u019c\u0003.\u0017\u0000\u019c\u019d\u0006\u0016"+
		"\uffff\uffff\u0000\u019d\u01a0\u0001\u0000\u0000\u0000\u019e\u01a0\u0001"+
		"\u0000\u0000\u0000\u019f\u019b\u0001\u0000\u0000\u0000\u019f\u019e\u0001"+
		"\u0000\u0000\u0000\u01a0-\u0001\u0000\u0000\u0000\u01a1\u01a2\u0006\u0017"+
		"\uffff\uffff\u0000\u01a2\u01a3\u0003 \u0010\u0000\u01a3\u01a4\u0006\u0017"+
		"\uffff\uffff\u0000\u01a4\u01ac\u0001\u0000\u0000\u0000\u01a5\u01a6\n\u0002"+
		"\u0000\u0000\u01a6\u01a7\u0005\u0015\u0000\u0000\u01a7\u01a8\u0003 \u0010"+
		"\u0000\u01a8\u01a9\u0006\u0017\uffff\uffff\u0000\u01a9\u01ab\u0001\u0000"+
		"\u0000\u0000\u01aa\u01a5\u0001\u0000\u0000\u0000\u01ab\u01ae\u0001\u0000"+
		"\u0000\u0000\u01ac\u01aa\u0001\u0000\u0000\u0000\u01ac\u01ad\u0001\u0000"+
		"\u0000\u0000\u01ad/\u0001\u0000\u0000\u0000\u01ae\u01ac\u0001\u0000\u0000"+
		"\u0000\u01af\u01b0\u0005\u000e\u0000\u0000\u01b0\u01ca\u0006\u0018\uffff"+
		"\uffff\u0000\u01b1\u01b2\u0005\u0010\u0000\u0000\u01b2\u01ca\u0006\u0018"+
		"\uffff\uffff\u0000\u01b3\u01b4\u0005\u0011\u0000\u0000\u01b4\u01ca\u0006"+
		"\u0018\uffff\uffff\u0000\u01b5\u01b6\u0005\u0014\u0000\u0000\u01b6\u01ca"+
		"\u0006\u0018\uffff\uffff\u0000\u01b7\u01b8\u0005\u0012\u0000\u0000\u01b8"+
		"\u01ca\u0006\u0018\uffff\uffff\u0000\u01b9\u01ba\u0005\u0013\u0000\u0000"+
		"\u01ba\u01ca\u0006\u0018\uffff\uffff\u0000\u01bb\u01bc\u0005\u0016\u0000"+
		"\u0000\u01bc\u01ca\u0006\u0018\uffff\uffff\u0000\u01bd\u01be\u0005\u0018"+
		"\u0000\u0000\u01be\u01ca\u0006\u0018\uffff\uffff\u0000\u01bf\u01c0\u0005"+
		"\u001c\u0000\u0000\u01c0\u01ca\u0006\u0018\uffff\uffff\u0000\u01c1\u01c2"+
		"\u0005\u001d\u0000\u0000\u01c2\u01ca\u0006\u0018\uffff\uffff\u0000\u01c3"+
		"\u01c4\u0005\u0019\u0000\u0000\u01c4\u01ca\u0006\u0018\uffff\uffff\u0000"+
		"\u01c5\u01c6\u0005\u001a\u0000\u0000\u01c6\u01ca\u0006\u0018\uffff\uffff"+
		"\u0000\u01c7\u01c8\u0005\u001b\u0000\u0000\u01c8\u01ca\u0006\u0018\uffff"+
		"\uffff\u0000\u01c9\u01af\u0001\u0000\u0000\u0000\u01c9\u01b1\u0001\u0000"+
		"\u0000\u0000\u01c9\u01b3\u0001\u0000\u0000\u0000\u01c9\u01b5\u0001\u0000"+
		"\u0000\u0000\u01c9\u01b7\u0001\u0000\u0000\u0000\u01c9\u01b9\u0001\u0000"+
		"\u0000\u0000\u01c9\u01bb\u0001\u0000\u0000\u0000\u01c9\u01bd\u0001\u0000"+
		"\u0000\u0000\u01c9\u01bf\u0001\u0000\u0000\u0000\u01c9\u01c1\u0001\u0000"+
		"\u0000\u0000\u01c9\u01c3\u0001\u0000\u0000\u0000\u01c9\u01c5\u0001\u0000"+
		"\u0000\u0000\u01c9\u01c7\u0001\u0000\u0000\u0000\u01ca1\u0001\u0000\u0000"+
		"\u0000\u01cb\u01cc\u0005\"\u0000\u0000\u01cc\u01cd\u0006\u0019\uffff\uffff"+
		"\u0000\u01cd3\u0001\u0000\u0000\u0000\u001cAM^|\u0086\u0093\u0095\u00a2"+
		"\u00ae\u00b8\u00c2\u00d5\u00d7\u00e4\u011b\u0123\u012d\u0137\u0141\u014b"+
		"\u015c\u015e\u016c\u017a\u0199\u019f\u01ac\u01c9";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}