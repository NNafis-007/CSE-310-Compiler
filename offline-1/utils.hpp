#include <bits/stdc++.h>
using namespace std;

int get_no_of_args(string line)
{
    stringstream ss(line);
    string temp;
    int args = 0;
    while (ss >> temp)
    {
        args++;
    }
    return args;
}

string removeSpaces(string &line)
{
    size_t start = line.find_first_not_of(" \t\n\r\f\v");
    size_t end = line.find_last_not_of(" \t\n\r\f\v");
    if (start == std::string::npos)
    {
        // The string is all whitespace
        line.clear();
    }
    else
    {
        line = line.substr(start, end - start + 1);
    }
    return line;
}

pair<string,string> formatInputString(string &line)
{
    stringstream ss(line);
    string var_name, kind;
    ss >> var_name >> kind;
    string name = var_name;
    string symbolType;
    string typeInfo;

    // cout << "Name : " << var_name << ", type :" << kind << endl;
    string result;
    if (kind == "STRUCT" || kind == "UNION")
    {
        symbolType = kind + ",{";
        bool first = true;
        string type, var;

        while (ss >> type >> var)
        {
            if (!first)
            {
                symbolType += ",";
            }
            symbolType += "(" + type + "," + var + ")";
            first = false;
        }
        symbolType += "}";
        //cout << "STRUCT FORMATTING : " << symbolType << endl;
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
        symbolType = kind + "," + returnType + "<==(" + args + ")";
    }
    else
    {
        symbolType = kind;
    }
    //cout << "Name : " << name << ", type : " << symbolType << endl;

    return {name, symbolType};
}