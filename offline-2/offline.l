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

void handle_char_lit(string str){
	str.erase(str.begin()); //rmv first '
	str.pop_back(); //rmv last '
	cout << "String : " << str << ", and length " << str.length() << "\n";
	if(str.length() == 0){
		cout << "ERROR : EMPTY CHARACTER\n";
		return;
	}
	if(str[0] == '\\'){
		if(str.length() == 1){ //only escape_slash in char 
			cout << "ERROR : UNFINISHED CHAR\n";
			return;
		}
		str.erase(str.begin()); //rmv first
	}

	if(str.length() > 1){
		cout << "ERROR : MULTICHAR CONSTANT\n";
	}
	else{
		cout << "<CONST_CHAR," << get_ASCII(str[0]) << ">\n";
	}
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
CHAR_LIT (\'{CHAR_CONTENT}\')


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

{DIGIT}+(\.{DIGIT}+)(\.{DIGIT}+)+ {cout << "ERROR : TOO MANY DECIMAL POINTS " << yytext << "\n";}
{FPNUM}(\.{DIGIT}*)+ {cout << "ERROR : ILL FORMED NUMBER " << yytext << "\n";}


{DIGIT}+ { cout << "<CONST_INT," << yytext << ">\n"; }

\'{CHAR_CONTENT}*\' { handle_char_lit(yytext); }


<<EOF>> { printf("Total lines : %d, nijer %d\n", yylineno, lineno); return 0; }
. {}

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


