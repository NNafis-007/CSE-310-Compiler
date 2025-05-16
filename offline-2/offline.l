/* Definition Section */
%option noyywrap yylineno
%x STRING_STATE
%x SL_COMMENT
%x ML_COMMENT

%{
#include <cstdlib>
#include <cstdio>
#include <iostream>
#include <fstream>
#include "symbol_table.hpp"
using namespace std;

int lineno = 1;
int error_no = 0;
int lines_in_str = 0; 
int starting_cmnt_line = 0;
string str_lit = "";
string raw_str = "";
string raw_cmnt = "";
ofstream output_log("2105007_log.txt");
ofstream output_token("2105007_tokens.txt");
SymbolTable* st = new SymbolTable(7, output_log);
bool scope_created = false;

// void create_scope(){
// 	if(!scope_created){
// 		scope_created = true;
// 		cout << "First LCURL, scope aldy created " << "\n";
// 	}
// 	else{
// 		st->enterScope();
// 		cout << "Creating Scope, s_id" << st->get_curr_scope_id() << "\n";
// 	}
// }

void insert_symbol(string lexeme, string token){
	SymbolInfo s(lexeme, token);
	int indx, chain_pos;
	if(st->currScope_lookup(s, indx, chain_pos) != NULL){
        output_log << s.showSymbol() << " already exists in ScopeTable# " 
            << st->get_curr_scope_id() << " at position " << indx << ", " << chain_pos-1 << "\n\n";
	}
	else{
		st->insertSymbol(s);
		st->printAllScopeTable();
	}
}



string to_upper(const string& str){
	string ans = str;
    for (char& c : ans)
        c = toupper(c);
	return ans;
}

char get_ASCII(char ch) {
	// printf("Calleeed!!!");
	switch(ch) {
		case 'n' : return '\n';
		case 'r' : return '\n';
		case 't' : return '\t';
		case 'a' : return '\a';
		case 'f' : return '\f';
		case '\'': return ch;		
		case '\"': return ch;
		case '\\': return ch;
		case 'b' : return '\b';
		case 'v' : return '\v';
		case '0' : return '\0';
		default:
			return ch;
	}
	
}

void log_keyword(string token, string lexeme, int lineno){
	string line = to_string(lineno);
	string printable = "Line no " + line + ": Token <" + token + "> Lexeme " + lexeme + " found";
	output_log << printable << "\n\n";  
	output_token << "<" << token << "> ";
}

void log_operators(string token, string lexeme, int lineno){

	output_log << "Line no " << lineno << ": Token <" << token << "> Lexeme " << lexeme << " found" << "\n\n";
	output_token << "<" << token << ", " << lexeme << "> ";
}

void log_constant(string token, string lexeme, int lineno){
	output_log << "Line no " << lineno << ": Token <" << token << "> Lexeme " << lexeme << " found" << "\n\n";
	output_token << "<" << token << ", " << lexeme << "> ";
	insert_symbol(lexeme, token);
}

void handle_char_lit(string text, int lineno){
	string str = text;
	str.erase(str.begin()); //rmv first '
	str.pop_back(); //rmv last '
	if(str.length() == 0){
		// cout << "Line no " << lineno << " --> ERROR : EMPTY CHARACTER " << text << "\n";
		output_log << "Error at line no " << lineno 
			<< ": Empty character constant error " << text << "\n\n";
		error_no++;

		return;
	}
	bool need_ascii = false;
	string og_str = "'" + str + "'";
	if(str[0] == '\\'){
		if(str.length() == 1){ //only escape_slash in char 
			// cout << "Line no " << lineno << " --> ERROR : Unterminated CHAR " << text << "\n";
			output_log << "Error at line no " << lineno 
				<< ": Unterminated character " << text << "\n\n";		
			error_no++;	
			return;
		}

		str.erase(str.begin()); //rmv first
		need_ascii = true;
	}

	if(str.length() > 1){
		output_log << "Error at line no " << lineno 
			<< ": Multicharacter constant error " << text << "\n\n";
		error_no++;
		// cout << "Line no " << lineno << " --> ERROR : MULTICHAR CONSTANT " << text << "\n";
	}
	else{
		char ch = str[0];
		if(need_ascii)
			ch = get_ASCII(ch);

		
		//Line no 5: Token <CONST_CHAR> Lexeme 'p' found --> <CONST_CHAR, p>
		output_log << "Line no " << lineno << ": Token <CONST_CHAR> Lexeme " << text << " found --> ";
		output_log << "<CONST_CHAR, " << ch << ">\n\n";
		output_token << "<CONST_CHAR, " << ch << "> ";
		insert_symbol(text, "CONST_CHAR");

	}
}

void handle_string_lit(string made_token, string raw_str, int lineno){
	output_log << "Line no " << lineno << ": Token <STRING> Lexeme " 
		<< raw_str << " found --> " << made_token << "\n\n";

	output_token << made_token << " ";
}

void handle_comment(string raw_cmnt, int lineno){
	output_log << "Line no " << lineno << ": Token <COMMENT> Lexeme " 
		<< raw_cmnt << " found\n\n";
}

void show_unfinished_char_err(string text){
	// cout << "Line no " << lineno << " --> ERROR : Unterminated CHAR " << text << "\n";
	output_log << "Error at line no " << lineno 
		<< ": Unterminated character " << text << "\n\n";
	error_no++;
}

void show_invalid_IDNUM_err(string text){
	output_log << "Error at line no " << yylineno 
		<< ": Invalid prefix on ID or invalid suffix on Number " 
		<< text << "\n\n";
	error_no++;

}

%}

/*Define RegEx Subpatterns*/
NEWLINE ((\r)?\n)
DIGIT [0-9]
EXPONENT ([eE][+-]?{DIGIT}+)
ALPHABET [a-zA-Z_]
WHITESPACE [ \t\f\r\v]+
FPNUM (({DIGIT}+\.{DIGIT}+{EXPONENT})|({DIGIT}+{EXPONENT})|({DIGIT}*\.{DIGIT}+)|(\.{DIGIT}+{EXPONENT}?)|({DIGIT}+\.))
ESCAPE_CHAR [nt\'\\afbvr0]
CHAR_CONTENT (\\({ESCAPE_CHAR}|\")|([^\'\"\n\r]))
UNFINISHED_CHAR (\'(\\{ESCAPE_CHAR}+|[^\'\"\n\r]+)|(\'))
ALPHANUM ({ALPHABET}|{DIGIT})
IDENTIFIER (({ALPHABET}|_){ALPHANUM}*)

/* Rules Section */
%%
{WHITESPACE} {}
{NEWLINE} { //printf("line %d\n", lineno); 
            lineno++; 
          }


"if"        |
"for"       |
"do"        |
"int"       |
"float"     |
"void"      |
"switch"    |
"default"   |
"else"      |
"while"     |
"break"     |
"char"      |
"double"    |
"return"    |
"case"      |
"continue"  |
"goto"      |
"long"      |
"short"     |
"static"    |
"unsigned"  { 
				log_keyword(to_upper(yytext) ,yytext, yylineno);
			}

"+"		|
"-"		{
			log_operators("ADDOP", yytext, yylineno);
		}

"*"		|
"/"     |
"%"		{
			log_operators("MULOP", yytext, yylineno);				
		}

"++"	|
"--"	{
			log_operators("INCOP", yytext, yylineno);
		}

"<="	|
"=="    |
">="	|
">"     |
"<"		|
"!="	{
			log_operators("RELOP", yytext, yylineno);
		}

"="     {
			log_operators("ASSIGNOP", yytext, yylineno);
		}

"&&"	|
"||"	{
			log_operators("LOGICOP", yytext, yylineno);
		}

"!"     {
			log_operators("NOT", yytext, yylineno);
        }

"("     {
			log_operators("LPAREN", yytext, yylineno);
        }

")"     {
			log_operators("RPAREN", yytext, yylineno);
        }

"{"     {
			log_operators("LCURL", yytext, yylineno);
			st->enterScope();
			//enter scope in symbol table
        }

"}"     {
			log_operators("RCURL", yytext, yylineno);
			st->exitScope();
			//exit scope in symbol table
        }

"["     {
			log_operators("LTHIRD", yytext, yylineno);	
        }

"]"     {
			log_operators("RTHIRD", yytext, yylineno);	
        }

","     {
			log_operators("COMMA", yytext, yylineno);	
        }

";"     {
			log_operators("SEMICOLON", yytext, yylineno);	
        }



{FPNUM} {
			// cout << "<CONST_FLOAT," << yytext << ">\n";
			log_constant("CONST_FLOAT", yytext, yylineno);
		}

{DIGIT}+(\.{DIGIT}+)(\.{DIGIT}+)+ {
		output_log << "Error at line no " << yylineno << ": Too many decimal points " << yytext << "\n\n";
		error_no++;
	}

{FPNUM}(\.{DIGIT}*)+ {
		output_log << "Error at line no " << yylineno << ": Ill formed number " << yytext << "\n\n";
		error_no++;
	}

{DIGIT}+ { 
			// cout << "<CONST_INT," << yytext << ">\n"; 
			log_constant("CONST_INT", yytext, yylineno);
		}

{DIGIT}+{IDENTIFIER}+ |
{FPNUM}+{IDENTIFIER}+ {show_invalid_IDNUM_err(yytext);}

{IDENTIFIER} {
	//Line no 7: Token <ID> Lexeme ch3 found
	output_log << "Line no " << yylineno << ": Token <ID> Lexeme " << yytext << " found\n\n";
	output_token << "<ID, " << yytext << "> ";
	insert_symbol(yytext, "ID");
	// cout << "<ID," << yytext << ">\n"; 
	}

\"  {
    BEGIN(STRING_STATE);
	str_lit = "";
	raw_str = "";
	raw_str += yytext;
	lines_in_str = 1;
    // cout << "---- ENTERING STRING STATE ---- (" << yytext << ")\n";
}

\/\/ {
	BEGIN(SL_COMMENT);
	raw_cmnt = "";
	raw_cmnt += yytext;
	starting_cmnt_line = yylineno;
	// cout << "Line no : " << yylineno << " --> Reading a single line comment : " << yytext << "\n";
}

"/*" {
	BEGIN(ML_COMMENT);
	raw_cmnt = "";
	raw_cmnt += yytext;
	starting_cmnt_line = yylineno;
	// cout << "Line no : " << yylineno << " --> Reading a MULTI line comment : " << yytext << "\n";
}

<ML_COMMENT>"*/" {
	raw_cmnt += yytext;

	handle_comment(raw_cmnt, yylineno);
	// cout << "Line no : " << yylineno << " --> END OF MULTI LINE COMMENT : " << raw_cmnt << "\n";
	BEGIN(INITIAL);

}

<ML_COMMENT><<EOF>> {
	// cout << "LINE : " << starting_cmnt_line << " --> " << 
	// 	"Error: Unterminated MULTI LINE COMMNET(eof): " << raw_cmnt << "\n";
    
	output_log << "Error at line no " << starting_cmnt_line
		<< ": Unterminated comment " << raw_cmnt << "\n\n";

	error_no++;

	BEGIN(INITIAL);
}

<ML_COMMENT>(.|\n) {
	raw_cmnt += yytext;
}

<SL_COMMENT>\\{NEWLINE}  {
    // continue the string on next line (\\n)
    // cout << "(SS)Matched line continuation (\\n) (" << yytext << ")\n";
	raw_cmnt += yytext;
}

<SL_COMMENT>\n {
	handle_comment(raw_cmnt, yylineno-1);
	// cout << "Line no : " << yylineno-1 << " --> END OF SINGLE LINE COMMENT : " << raw_cmnt << "\n";
	BEGIN(INITIAL);
}

<SL_COMMENT>. {
	raw_cmnt += yytext;
}

<STRING_STATE>\\({ESCAPE_CHAR}|\")  {
	// cout << "(SS)Matched special : " << yytext << "\n";
	char special_ch = get_ASCII(yytext[yyleng-1]);
	str_lit += special_ch;
	raw_str += yytext;
}

<STRING_STATE><<EOF>> {

	// cout << "LINE : " << yylineno - lines_in_str + 1 << " --> " << 
	// 	"Error: Unterminated string literal(eof): " << raw_str << "\n";

	output_log << "Error at line no " << yylineno - lines_in_str + 1
		<< ": Unterminated string " << raw_str << "\n\n";

	error_no++;

    BEGIN(INITIAL);
}

<STRING_STATE>\n {

	// output_log << "LINE : " << yylineno - lines_in_str << " --> " << 
	// 	"Error: Unterminated string literal(nl): " << raw_str << "\n";
	
	output_log << "Error at line no " << yylineno - lines_in_str
		<< ": Unterminated string " << raw_str << "\n\n";

	error_no++;

	BEGIN(INITIAL);
}

<STRING_STATE>\\{NEWLINE}  {
    // continue the string on next line (\\n)
    // cout << "(SS)Matched line continuation (\\n) (" << yytext << ")\n";
	raw_str += yytext;
	lines_in_str++;
}

<STRING_STATE>\"  {
	raw_str += yytext;
	str_lit = "<STRING, " + str_lit + ">";
	// cout << "Line no " << yylineno << ": String : " << raw_str << " --> " << str_lit << "\n";

	handle_string_lit(str_lit, raw_str, yylineno);

    // cout << "(SS)---- EXITING STRING STATE ---- (" << yytext << ")\n";

	str_lit = "";
	raw_str = "";
	BEGIN(INITIAL);
}

<STRING_STATE>.   {
	str_lit += yytext;
	raw_str += yytext;
	// cout << "(SS)Matched non escape : " << yytext << "\n";
}

\'{CHAR_CONTENT}*\' { handle_char_lit(yytext, yylineno); }
{UNFINISHED_CHAR} { show_unfinished_char_err(yytext); }


<<EOF>> { 
			st->printAllScopeTable();
			output_log << "Total lines: " << yylineno << "\n";
			output_log << "Total errors: " << error_no << "\n";
			return 0; 
		}
. {
	// cout << "Line no " << yylineno << " --> ERROR : UNRECOGNIZED CHAR " << yytext << "\n";
	output_log << "Error at line no " << yylineno 
		<< ": Unrecognized character " << yytext << "\n\n";
	error_no++;
}

%%

/* Subroutines Section */
int main(int argc,char *argv[]){
	
	if(argc!=2){
		printf("Please provide input file name and try again\n");
		return 0;
	}
	
	FILE *fin=fopen(argv[1],"r");
	if(fin==NULL){
		printf("Cannot open specified file\n");
		return 0;
	}

	// output_log.open("2105007_log.txt");
	// output_token.open("2105007_tokens.txt");

	yyin= fin;
	yylineno = 1;
	yylex();
	fclose(yyin);
	output_log.close();
	output_token.close();
	return 0;
}


