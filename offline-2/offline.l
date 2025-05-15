/* Definition Section */
%option noyywrap yylineno

%{
#include <stdlib.h>
#include <iostream>
using namespace std;

int lineno = 0;

void to_upper(string& str){
    for (char& c : str)
        c = toupper(c);
}

void printKeywordToken(string keyword){
    to_upper(keyword);
    string token = "<" + keyword + ">";
    cout << token << "\n";
}

%}

/*Define RegEx Subpatterns*/
NEWLINE (\n)


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

<<EOF>> { printf("Total lines : %d\n", lineno); return 0; }
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
	//yylineno = 1;
	yylex();
	fclose(yyin);
	return 0;
}


