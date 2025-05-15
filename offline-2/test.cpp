#include<iostream>
#include <stdlib.h>
#include <string>


int lineno = 0;

void printKeywordToken(std::string keyword){
    
    for (int i = 0; i < keyword.length(); i++)
        keyword[i] = std::toupper(keyword[i]);

    std::cout << keyword;
}

int main(){
    
    std::string ch = "\v";
    std::cout << "printing char : " << ch;
    std::cout << "END\n";
}