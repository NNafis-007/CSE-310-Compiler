/* Definition Section */
%option noyywrap yylineno
%x STRING_STATE

%{
#include <cstdlib>
#include <cstdio>
#include <iostream>
#include <fstream>
using namespace std;

int lineno = 1;
int lines_in_str = 0; 
string str_lit = "";
string raw_str = "";
ofstream output_log;
ofstream output_token;

void to_upper(string& str){
    for (char& c : str)
        c = toupper(c);
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

void printKeywordToken(string keyword){
    to_upper(keyword);
    string token = "<" + keyword + ">";
    cout << token << "\n";
}

void handle_char_lit(string text){
	string str = text;
	str.erase(str.begin()); //rmv first '
	str.pop_back(); //rmv last '
	if(str.length() == 0){
		cout << "Line no " << lineno << " --> ERROR : EMPTY CHARACTER " << text << "\n";
		return;
	}
	bool need_ascii = false;
	if(str[0] == '\\'){
		if(str.length() == 1){ //only escape_slash in char 
			cout << "Line no " << lineno << " --> ERROR : Unterminated CHAR " << text << "\n";
			return;
		}
		str.erase(str.begin()); //rmv first
		need_ascii = true;
	}

	if(str.length() > 1){
		cout << "Line no " << lineno << " --> ERROR : MULTICHAR CONSTANT " << text << "\n";
	}
	else{
		char ch = str[0];
		if(need_ascii)
			ch = get_ASCII(ch);
		cout << "<CONST_CHAR," << ch << ">\n";
	}
}

void show_unfinished_char_err(string text){
	cout << "Line no " << lineno << " --> ERROR : Unterminated CHAR " << text << "\n";
}

void show_invalid_IDNUM_err(string text){
	cout << "Line no " << lineno << " --> \
ERROR : INVALID SUFFIX ON NUMERICS CONSTANT or \
INVALID PREFIX ON IDENTIFIER " << text << "\n";

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
"unsigned"  { printKeywordToken(yytext); }

"+"		|
"-"		{
			cout << "<ADDOP, " << yytext << ">\n";
		}

"*"		|
"/"     |
"%"		{
			cout << "<MULOP, " << yytext << ">\n";
		}

"++"	|
"--"	{
            // operators start here
			cout << "<INCOP, " << yytext << ">\n";
		}

"<="	|
"=="    |
">="	|
">"     |
"<"		|
"!="	{
			cout << "<RELOP, " << yytext << ">\n";
		}

"="     {
			cout << "<ASSIGNOP, " << yytext << ">\n";
        }

"&&"	|
"||"	{
			cout << "<LOGICOP, " << yytext << ">\n";
		}

"!"     {
			cout << "<NOT, " << yytext << ">\n";
        }

"("     {
			cout << "<LPAREN, " << yytext << ">\n";
        }

")"     {
			cout << "<RPAREN, " << yytext << ">\n";
        }

"{"     {
			cout << "<LCURL, " << yytext << "> -> Entering Scope\n";
        }

"}"     {
			cout << "<RCURL, " << yytext << "> -> Exiting Scope\n";
        }

"["     {
			cout << "<LTHIRD, " << yytext << ">\n";
        }

"]"     {
			cout << "<RTHIRD, " << yytext << ">\n";
        }

","     {
			cout << "<COMMA, " << yytext << ">\n";
        }

";"     {
			cout << "<SEMICOLON, " << yytext << ">\n";
        }



{FPNUM} {cout << "<CONST_FLOAT," << yytext << ">\n";}

{DIGIT}+(\.{DIGIT}+)(\.{DIGIT}+)+ {cout << "LINE NO " << lineno << " --> ERROR : TOO MANY DECIMAL POINTS " << yytext << "\n";}
{FPNUM}(\.{DIGIT}*)+ {cout << "LINE NO " << lineno << " --> ERROR : ILL FORMED NUMBER " << yytext << "\n";}

{DIGIT}+ { cout << "<CONST_INT," << yytext << ">\n"; }

{DIGIT}+{IDENTIFIER}+ |
{FPNUM}+{IDENTIFIER}+ {show_invalid_IDNUM_err(yytext);}

{IDENTIFIER} {cout << "<ID," << yytext << ">\n"; }

\"  {
    BEGIN(STRING_STATE);
	str_lit = "";
	raw_str = "";
	raw_str += yytext;
	lines_in_str = 1;
    // cout << "---- ENTERING STRING STATE ---- (" << yytext << ")\n";
}

<STRING_STATE>\\({ESCAPE_CHAR}|\")  {
	// cout << "(SS)Matched special : " << yytext << "\n";
	char special_ch = get_ASCII(yytext[yyleng-1]);
	str_lit += special_ch;
	raw_str += yytext;
}

<STRING_STATE><<EOF>> {

	cout << "LINE : " << yylineno - lines_in_str + 1 << " --> " << 
		"Error: Unterminated string literal(eof): " << raw_str << "\n";
    BEGIN(INITIAL);
}

<STRING_STATE>\n {

	cout << "LINE : " << yylineno - lines_in_str << " --> " << 
		"Error: Unterminated string literal(nl): " << raw_str << "\n";
	BEGIN(INITIAL);
}

<STRING_STATE>\\{NEWLINE}  {
    // continue the string on next line (\\n)
    // cout << "(SS)Matched line continuation (\\n) (" << yytext << ")\n";
	raw_str += yytext;
	lines_in_str++;
}

<STRING_STATE>\"  {
	str_lit = "<STRING, " + str_lit + ">";
	cout << "String : " << str_lit << "\n";
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

\'{CHAR_CONTENT}*\' { handle_char_lit(yytext); }
{UNFINISHED_CHAR} { show_unfinished_char_err(yytext); }


<<EOF>> { printf("Total lines : %d, nijer %d\n", yylineno, lineno); return 0; }
. {cout << "LINE NO " << lineno << " --> ERROR : UNRECOGNIZED CHAR " << yytext << "\n";}

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
	

	yyin= fin;
	yylineno = 1;
	yylex();
	fclose(yyin);
	return 0;
}


