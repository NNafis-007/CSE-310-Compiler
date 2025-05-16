#include<iostream>
#include <stdlib.h>
#include <string>
using namespace std;

int lineno = 0;

void printKeywordToken(std::string keyword){
    
    for (int i = 0; i < keyword.length(); i++)
        keyword[i] = std::toupper(keyword[i]);

    std::cout << keyword;
}

int main(){
    int line = 10;
   	string lineno = to_string(line);
    cout << "Line no " << lineno << endl;

}