/* Definition Section */
%option noyywrap yylineno

%{
#include <cstdlib>
#include <cstdio>
#include <iostream>
#include <fstream>
using namespace std;

int lineno = 1;
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

%}

/*Define RegEx Subpatterns*/
NEWLINE ((\r)?\n)
DIGIT [0-9]
EXPONENT ([eE][+-]?{DIGIT}+)
ALPHABET [a-zA-Z_]
WHITESPACE [ \t\f\r\v]+
FPNUM (({DIGIT}+\.{DIGIT}+{EXPONENT})|({DIGIT}+{EXPONENT})|({DIGIT}*\.{DIGIT}+)|(\.{DIGIT}+{EXPONENT}?)|({DIGIT}+\.))
ESCAPE_CHAR [nt\'\\afbvr0]
CHAR_CONTENT (\\({ESCAPE_CHAR})|([^\'\"\n\r]))
UNFINISHED_CHAR (\'(\\{ESCAPE_CHAR}+|[^\'\"\n\r]+)|(\'))


/* Rules Section */
%%
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



{FPNUM} {cout << "<CONST_FLOAT," << yytext << ">\n";}

{DIGIT}+(\.{DIGIT}+)(\.{DIGIT}+)+ {cout << "LINE NO " << lineno << "ERROR : TOO MANY DECIMAL POINTS " << yytext << "\n";}
{FPNUM}(\.{DIGIT}*)+ {cout << "LINE NO " << lineno << "ERROR : ILL FORMED NUMBER " << yytext << "\n";}


{DIGIT}+ { cout << "<CONST_INT," << yytext << ">\n"; }

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


