#include <bits/stdc++.h>
using namespace std;

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
    // SymbolTable symbolTable(num_buckets);

    string command;
    int i = 1;
    while (inputFile >> command)
    {
        if (command == "I") // Insert
        {
            string line;
            getline(inputFile, line);
            line = removeSpaces(line);
            cout << "Cmd " << i << ": Inserting: " << line << "\n";
            // SymbolInfo symbol(line);
            // if (symbolTable.insertSymbol(symbol))
            // {
            //     cout << "Inserted: " << symbol.showSymbol() << "\n";
            // }
        }
        else if (command == "L") // Lookup
        {
            string line;
            getline(inputFile, line);
            line = removeSpaces(line);
            cout << "Cmd " << i << ": Looking up: " << line << "\n";
            // SymbolInfo symbol(line);
            // SymbolInfo *found = symbolTable.lookUp(symbol);
            // if (found)
            // {
            //     cout << "Found: " << found->showSymbol() << "\n";
            // }
            // else
            // {
            //     cout << "Not Found: " << symbol.getName() << "\n";
            // }
        }
        else if (command == "D") // Delete
        {
            string line;
            getline(inputFile, line);
            line = removeSpaces(line);
            cout << "Cmd " << i << ": Deleting: " << line << "\n";
            // SymbolInfo symbol(line);
            // if (symbolTable.removeSymbol(symbol))
            // {
            //     cout << "Deleted: " << symbol.getName() << "\n";
            // }
            // else
            // {
            //     cout << "Not Found: " << symbol.getName() << "\n";
            // }
        }
        else if (command == "P") // Print
        {
            string scopeType;
            inputFile >> scopeType;
            if (scopeType == "C")
            {
                cout << "Cmd " << i << ": Printing Current Scope Table:\n";
                // symbolTable.printCurrentScopeTable();
            }
            else if (scopeType == "A")
            {
                cout << "Cmd " << i << ": Printing All Scope Tables:\n";
                // symbolTable.printAllScopeTable();
            }
        }
        else if (command == "S") // Enter new scope
        {
            // symbolTable.enterScope();
            cout << "Cmd " << i << ": Entered new scope.\n";
        }
        else if (command == "E") // Exit current scope
        {
            // symbolTable.exitScope();
            cout << "Cmd " << i << ": Exited current scope.\n";
        }
        else if (command == "Q") // Quit
        {
            cout << "Cmd " << i << ": Exiting program.\n";
            break;
        }
        i+=1;
    }

    inputFile.close();
    return 0;
}
