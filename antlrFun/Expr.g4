grammar Expr;

// Parser rules (start with lowercase)
start : program ;

program : program unit
        | unit
        ;

unit : var_declaration
     | func_declaration
     | func_definition
     ;

var_declaration : type_specifier declaration_list SEMICOLON ;

type_specifier : INT
               | FLOAT
               | VOID
               ;

declaration_list : declaration_list COMMA ID
                 | declaration_list COMMA ID LTHIRD CONST_INT RTHIRD
                 | ID
                 | ID LTHIRD CONST_INT RTHIRD
                 ;

func_declaration : type_specifier ID LPAREN parameter_list RPAREN SEMICOLON ;

func_definition : type_specifier ID LPAREN parameter_list RPAREN compound_statement ;

parameter_list : parameter_list COMMA type_specifier ID
               | parameter_list COMMA type_specifier ID LTHIRD RTHIRD
               | type_specifier ID
               | type_specifier ID LTHIRD RTHIRD
               | /* empty */
               ;

compound_statement : LCURL statements RCURL
                   | LCURL RCURL
                   ;

var_declaration_list : var_declaration_list var_declaration
                     | /* empty */
                     ;

statements : statements statement
           | /* empty */
           ;

statement : var_declaration
          | expression_statement
          | compound_statement
          | IF LPAREN expression RPAREN statement
          | IF LPAREN expression RPAREN statement ELSE statement
          | WHILE LPAREN expression RPAREN statement
          | FOR LPAREN expression_statement expression_statement expression RPAREN statement
          | RETURN expression SEMICOLON
          | RETURN SEMICOLON
          | PRINTLN LPAREN ID RPAREN SEMICOLON
          ;

expression_statement : SEMICOLON
                     | expression SEMICOLON
                     ;

variable : ID
         | ID LTHIRD expression RTHIRD
         ;

expression : logic_expression
           | variable ASSIGNOP logic_expression
           ;

logic_expression : rel_expression
                 | rel_expression LOGICOP rel_expression
                 ;

rel_expression : simple_expression
               | simple_expression RELOP simple_expression
               ;

simple_expression : term
                  | simple_expression ADDOP term
                  ;

term : unary_expression
     | term MULOP unary_expression
     ;

unary_expression : ADDOP unary_expression
                 | NOT unary_expression
                 | factor
                 ;

factor : variable
       | ID LPAREN argument_list RPAREN
       | LPAREN expression RPAREN
       | CONST_INT
       | CONST_FLOAT
       | variable INCOP
       | variable DECOP
       ;

argument_list : arguments
              | /* empty */
              ;

arguments : arguments COMMA logic_expression
          | logic_expression
          ;

// Lexer rules (start with uppercase)
LINE_COMMENT : '//' ~[\r\n]* -> skip ;

BLOCK_COMMENT : '/*' .*? '*/' -> skip ;

STRING : '"' ( '\\' . | ~["\\\r\n] )* '"' -> skip ;

WS : [ \t\r\n\f]+ -> skip ;

// Keywords
IF : 'if' ;
ELSE : 'else' ;
FOR : 'for' ;
WHILE : 'while' ;
PRINTLN : 'println' ;
RETURN : 'return' ;
INT : 'int' ;
FLOAT : 'float' ;
VOID : 'void' ;

// Symbols
LPAREN : '(' ;
RPAREN : ')' ;
LCURL : '{' ;
RCURL : '}' ;
LTHIRD : '[' ;
RTHIRD : ']' ;
SEMICOLON : ';' ;
COMMA : ',' ;

ADDOP : [+\-] ;
MULOP : [*/%] ;
INCOP : '++' ;
DECOP : '--' ;
NOT : '!' ;
RELOP : '<=' | '==' | '>=' | '>' | '<' | '!=' ;
LOGICOP : '&&' | '||' ;
ASSIGNOP : '=' ;

// Identifiers and constants
ID : [A-Za-z_][A-Za-z0-9_]* ;

CONST_INT : [0-9]+ ;

CONST_FLOAT : [0-9]+ ('.' [0-9]*)? ([Ee][+\-]? [0-9]+)?
            | '.' [0-9]+ ([Ee][+\-]? [0-9]+)?
            | [0-9]+ '.'
            ;

