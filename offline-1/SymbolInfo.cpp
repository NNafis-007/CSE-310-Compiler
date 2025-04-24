#include <iostream>
#include <string>
#include <sstream>
using namespace std;

class SymbolInfo
{
    string name;
    string symbolType;
    string typeInfo;
    string formatInputString(string &line)
    {
        stringstream ss(line);
        string var_name, kind;
        ss >> var_name >> kind;
        this->name = var_name;
        this->symbolType = kind;
        
        //cout << "Name : " << var_name << ", type :" << kind << endl;
        string result;
        if (kind == "STRUCT" || kind == "UNION")
        {
            result = kind + ",{";
            bool first = true;
            string type, var;

            while (ss >> type >> var)
            {
                if (!first)
                {
                    result += ",";
                }
                result += "(" + type + "," + var + ")";
                first = false;
            }
            result += "}";
            //cout << "STRUCT FORMATTING : " << result << endl;
        }
        else if (kind == "FUNCTION")
        {
            string returnType;
            ss >> returnType; // the first type is return type

            // get argument types
            string arg;
            string args;
            bool start = true;
            while (ss >> arg)
            {
                if (!start)
                {
                    args += ",";
                }
                args += arg;
                start = false;
            }

            // combine everything
            result = "FUNCTION," + returnType + "<==(" + args + ")";
        }
        else
        {
            result = kind;
        }
        this->typeInfo = result;
        return result;
    }

public:
    SymbolInfo *next;
    SymbolInfo(string line, SymbolInfo *next = NULL)
    {
        formatInputString(line);
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
        return "<" + name + "," + this->typeInfo + ">";
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