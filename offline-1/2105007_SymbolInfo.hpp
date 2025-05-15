#include <iostream>
#include <string>
#include <sstream>
using namespace std;

class SymbolInfo
{
    string name;
    string symbolType;
    string typeInfo;

public:
    SymbolInfo *next;
    SymbolInfo(string name, string type, SymbolInfo *next = NULL)
    {
        this->name = name;
        this->symbolType = type;
        this->next = next;
    }
    //Copy constructor
    SymbolInfo(const SymbolInfo &other) {
        this->name = other.name;
        this->symbolType = other.symbolType;
        this->typeInfo = other.typeInfo;
        this->next = NULL; // Deep copy does not copy the linked list structure
    }
    SymbolInfo()
    {
        name = "";
        symbolType = "";
        // type = "";
        next = NULL;
    }
    ~SymbolInfo()
    {
        delete next; // This will delete the entire linked list
    }

    string getName()
    {
        return name;
    }
    string getType()
    {
        return symbolType;
    }
    string getTypeInfo(){
        return this->typeInfo;
    }
    void setName(string name)
    {
        this->name = name;
    }
    void setType(string type)
    {
        symbolType = type;
    }
    string showSymbol()
    {
        return "<" + this->name + "," + this->symbolType + ">";
    }
};

// TESTING CODE
// int main()
// {
//     string s = "foo FUNCTION INT INT FLOAT INT";
//     SymbolInfo *s1 = new SymbolInfo("car STRUCT INT n_doors BOOL is_electric STRING brand");
//     SymbolInfo *s2 = new SymbolInfo("foo FUNCTION INT INT FLOAT INT");
//     SymbolInfo *s3 = new SymbolInfo("23 NUMBER");
//     cout << s2->showSymbol();
//     cout << "\n";
//     cout << s1->showSymbol();
//     cout << "\n";
//     cout << s3->showSymbol();
//     return 0;
// }