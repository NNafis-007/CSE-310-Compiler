%option noyywrap yylineno

%{
#include <cstdlib>
#include <cstdio>
#include <iostream>
#include <fstream>
using namespace std;

int no_nums = 0;
string till_now = "";
int nest = 0;

%}

ALPHABET [a-zA-Z_]
WHITESPACE [ \t\v\r\f]+
NEWLINE ((\r)?\n)

%%

{WHITESPACE} {}
{ALPHABET} {till_now += yytext;}
"<" {
    nest += 1;
    till_now += yytext;
}
">" {
    nest -= 1;
    till_now += yytext;
    if(nest == 0){
        cout << "Template " << till_now << " detected\n";
    }
}

<<EOF>> {
    if(nest != 0){
        cout << "line no " << yylineno-1 << ": ERROR Unfinished Template " << till_now << "\n";
        nest = 0;
    }
    return 0;
}

\n {
    if(nest != 0){
        cout << "line no " << yylineno-1 << ": ERROR Unfinished Template " << till_now << "\n";
        nest = 0;
    }
    till_now = "";}
. {}



%%
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