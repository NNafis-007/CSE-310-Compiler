#include <bits/stdc++.h>
#include "2105007_SymbolTable.hpp"
#include "utils.hpp"

int main(int argc, char *argv[])
{
    string inputFileName, outputFileName;
    string hashName = "SDBM";
    if (argc < 3 || argc > 4)
    {
        cerr << "No of COMMAND LINE ARGUMENTS mismatch\n";
        return 1;
    }

    else if (argc == 3)
    {
        inputFileName = argv[1];
        outputFileName = argv[2];
    }
    else if (argc == 4)
    {
        inputFileName = argv[1];
        outputFileName = argv[2];
        hashName = argv[3];
    }

    ifstream inputFile(inputFileName);
    // cout << "Input file name is " << inputFileName << "\n";
    freopen(outputFileName.c_str(), "w", stdout);
    if (!inputFile.is_open())
    {
        cerr << "Error: Could not open input file.\n";
        return 1;
    }

    int num_buckets;
    inputFile >> num_buckets;
    // cout << "Number of buckets is " << num_buckets << endl;
    SymbolTable symbolTable(num_buckets, hashName);

    string command;
    string error_args_msg = "\tNumber of parameters mismatch for the command ";
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
            int args = get_no_of_args(line);
            if (args < 2)
            {
                cout << error_args_msg << command << "\n";
                continue;
            }
            auto name_type = formatInputString(line);
            SymbolInfo symbol(name_type.first, name_type.second);
            symbolTable.insertSymbol(symbol);
        }
        else if (command == "L")
        {
            string line;
            getline(inputFile, line);
            line = removeSpaces(line);
            cout << "Cmd " << i << ": " << command << " " << line << "\n";
            int args = get_no_of_args(line);
            if (args != 1)
            {
                cout << error_args_msg << command << "\n";
                continue;
            }
            auto name_type = formatInputString(line);
            SymbolInfo symbol(name_type.first, name_type.second);

            SymbolInfo *found = symbolTable.lookUp(symbol);
        }
        else if (command == "D") // Delete
        {
            string line;
            getline(inputFile, line);
            line = removeSpaces(line);
            cout << "Cmd " << i << ": " << command << " " << line << "\n";
            int args = get_no_of_args(line);
            if (args != 1)
            {
                cout << error_args_msg << command << "\n";
                continue;
            }
            auto name_type = formatInputString(line);
            SymbolInfo symbol(name_type.first, name_type.second);

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
            else
            {
                cout << "Cmd " << i << ": " << command << " " << scopeType << "\n";
                cout << error_args_msg << command << "\n";
                continue;
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