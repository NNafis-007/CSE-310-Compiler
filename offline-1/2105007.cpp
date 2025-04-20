#include <bits/stdc++.h>
#include "SymbolTable.cpp"

string removeSpaces(string line)
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

int main()
{
    ifstream inputFile("sample_input.txt");
    if (!inputFile.is_open())
    {
        cerr << "Error: Could not open input file.\n";
        return 1;
    }

    int num_buckets;
    inputFile >> num_buckets;
    cout << "Number of buckets is " << num_buckets << endl;
    SymbolTable symbolTable(num_buckets);

    string command;

    int i = 0;

    while (inputFile >> command)
    {
        i++;
        if (command == "I") // Insert
        {
            string line;
            getline(inputFile, line);
            line = removeSpaces(line);
            cout << "Cmd " << i << ": " << command << " " << line << "\n";
            SymbolInfo symbol(line);
            symbolTable.insertSymbol(symbol);
        }
        else if (command == "L")
        {
            string line;
            getline(inputFile, line);
            line = removeSpaces(line);
            cout << "Cmd " << i << ": " << command << " " << line << "\n";
            SymbolInfo symbol(line);
            SymbolInfo *found = symbolTable.lookUp(symbol);
        }
        else if (command == "D") // Delete
        {
            string line;
            getline(inputFile, line);
            line = removeSpaces(line);
            cout << "Cmd " << i << ": " << command << " " << line << "\n";
            SymbolInfo symbol(line);
            symbolTable.removeSymbol(symbol);
        }
        else if (command == "P") // Print
        {
            string scopeType;
            inputFile >> scopeType;
            if (scopeType == "C")
            {
                cout << "Cmd " << i << ": " << command << " " << scopeType << "\n";
                symbolTable.printCurrentScopeTable();
            }
            else if (scopeType == "A")
            {
                cout << "Cmd " << i << ": " << command << " " << scopeType << "\n";
                symbolTable.printAllScopeTable();
            }
        }
        else if (command == "S") // Enter new scope
        {
            cout << "Cmd " << i << ": S\n";
            symbolTable.enterScope();
        }
        else if (command == "E") // Exit current scope
        {
            cout << "Cmd " << i << ": E\n";
            symbolTable.exitScope();
        }
        else if (command == "Q") // Quit
        {
            cout << "Cmd " << i << ": Q\n";
            break;
        }
    }

    inputFile.close();
    return 0;
}