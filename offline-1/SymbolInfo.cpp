#include<bits/stdc++.h>
using namespace std;

class SymbolInfo{
    string name;
    string type;
    
    public:
    SymbolInfo* next;
    SymbolInfo(string name, string type, SymbolInfo* next = NULL){
        this->name = name;
        this->type = type;
        this->next = next;
    }
    SymbolInfo(){
        name = "";
        type = "";
        next = NULL;
    }
    
    string getName(){
        return name;
    }
    string getType(){
        return type;
    }
    void setName(string name){
        this->name = name;
    }
    void setType(string type){
        this->type = type;
    }
    void show(){
        cout << "SymbolInfo < " << name << ", " << type << " >\n";
        // while(next != NULL){
        //     cout << " -> " << "SymbolInfo < " << next->name << ", " << next->type << " >";
        //     next = next->next;
        // }
        // cout << "\n";
    }
};

//TESTING CODE
// int main(){
//     SymbolInfo* s1 = new SymbolInfo("x", "int");
//     SymbolInfo* s2 = new SymbolInfo("y", "float", s1);
//     s2->show();
//     s1->show();
//     delete s1;
//     s2->show(); 
//     return 0;
// }