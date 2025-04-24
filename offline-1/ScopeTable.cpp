#include <bits/stdc++.h>
#include "SymbolInfo.cpp"
#include "HashFunction.cpp"
using namespace std;

class ScopeTable
{
    SymbolInfo **hash_table;
    ScopeTable *parentScope;
    int num_buckets;
    int scope_id;
    HashFunction::HashFunc hashFunction; // Function pointer for the hash function

public:
    // Constructor that takes the hash function name as a string
    ScopeTable(int num_buckets, int scope_id, const string &hashFuncName = "SDBM")
    {
        cout << "\tScopeTable# " << scope_id << " created\n";
        this->num_buckets = num_buckets;
        this->scope_id = scope_id;
        this->parentScope = NULL;
        this->hashFunction = HashFunction::getHashFunction(hashFuncName); // Set hash function

        hash_table = new SymbolInfo *[num_buckets];
        for (int i = 0; i < num_buckets; i++)
        {
            hash_table[i] = NULL;
        }
    }

    ~ScopeTable()
    {
        for (int i = 0; i < num_buckets; i++)
        {
            if (hash_table[i] != NULL)
            {
                delete hash_table[i];
            }
        }
        delete[] hash_table;
        cout << "\tScopeTable# " << this->scope_id << " removed\n";
    }

    ScopeTable *getParentScope()
    {
        return this->parentScope;
    }

    void setParentScope(ScopeTable *s)
    {
        this->parentScope = s;
    }

    void setHashFunction(const string &hashFuncName)
    {
        this->hashFunction = HashFunction::getHashFunction(hashFuncName);
    }

    // Modify insertSymbol to use the hashFunction object
    bool insertSymbol(SymbolInfo s)
    {
        SymbolInfo *found = this->lookUp(s, false);
        if (found != NULL)
        {
            cout << "\t'" << s.getName() << "' already exists in the current ScopeTable\n";
            return false;
        }

        unsigned long long hash = hashFunction(s.getName(), this->num_buckets);
        int index = hash % num_buckets;
        int chain_position = 0;

        if (hash_table[index] == NULL)
        {
            hash_table[index] = new SymbolInfo(s);
        }
        else
        {
            chain_position += 1;
            SymbolInfo *temp = hash_table[index];
            while (temp->next != NULL)
            {
                temp = temp->next;
                chain_position += 1;
            }
            temp->next = new SymbolInfo(s);
        }
        cout << "\tInserted in ScopeTable# " << this->scope_id << " at position " << index + 1
             << ", " << chain_position + 1 << "\n";
        return true;
    }

    // Modify lookUp to use the hashFunction object
    SymbolInfo *lookUp(SymbolInfo s, bool showOutput = true)
    {
        string name = s.getName();
        unsigned long long hash = hashFunction(name, this->num_buckets) % num_buckets;
        int index = hash % num_buckets;
        int chain_position = 0;
        SymbolInfo *temp = hash_table[index];
        while (temp != NULL)
        {
            chain_position++;
            if (temp->getName() == name)
            {
                if (showOutput)
                {
                    cout << "\t'" << name << "' found in ScopeTable# " << this->scope_id
                         << " at position " << index + 1 << ", " << chain_position << "\n";
                }
                return temp;
            }
            temp = temp->next;
        }

        return NULL;
    }

    bool deleteSymbol(SymbolInfo s)
    {
        int index = hashFunction(s.getName(), this->num_buckets) % num_buckets;
        if (hash_table[index] == NULL)
        {
            cout << "\tNot found in the current ScopeTable\n";
            return false; // symbol not found
        }

        SymbolInfo *head = hash_table[index];
        int chain_position = 0;
        // if found at head, delete
        if (head->getName() == s.getName())
        {
            hash_table[index] = head->next; // delete the head node
            delete head;
            cout << "\tDeleted '" << s.getName() << "' from ScopeTable# " << this->scope_id
                 << " at position " << index + 1 << ", " << chain_position + 1 << "\n";

            return true; // symbol deleted successfully
        }
        // else traverse the chain
        else
        {
            while (head->next != NULL)
            {
                SymbolInfo *prev = head;
                head = head->next;
                chain_position++;
                if (head->getName() == s.getName())
                {
                    prev->next = head->next;
                    delete head;
                    cout << "\tDeleted '" << s.getName() << "' from ScopeTable# " << this->scope_id
                         << " at position " << index + 1 << ", " << chain_position + 1 << "\n";
                    return true; // symbol deleted successfully
                }
            }
            // if at the last elem
            if (head != NULL && head->getName() == s.getName())
            {
                delete head;
                cout << "\tDeleted '" << s.getName() << "' from ScopeTable# " << this->scope_id
                     << " at position " << index + 1 << ", " << chain_position + 1 << "\n";

                return true; // symbol deleted successfully
            }
        }
    }

    void print(int indentLevel = 0)
    {
        string indent(indentLevel, '\t');
        cout << indent << "ScopeTable# " << scope_id << "\n";
        for (int i = 0; i < num_buckets; i++)
        {
            cout << indent << i + 1 << "--> ";
            SymbolInfo *temp = hash_table[i];
            if (temp != NULL)
            {
                cout << temp->showSymbol() << " ";
                while (temp->next != NULL)
                {
                    temp = temp->next;
                    cout << temp->showSymbol() << " ";
                }
            }
            cout << "\n";
        }
    }
};

// TESTING CODE
// int main()
// {
//     //  Create a ScopeTable with 5 buckets and scope ID 1
//     ScopeTable scopeTable(5, 1);

//     // Insert symbols into the ScopeTable
//     SymbolInfo symbol1("x NUMBER");
//     SymbolInfo symbol2("y NUMBER");
//     SymbolInfo symbol3("z FUNCTION INT INT FLOAT");
//     SymbolInfo symbol4("a STRUCT INT id STRING name");

//     cout << "Inserting symbols:\n";
//     scopeTable.insertSymbol(symbol1);
//     scopeTable.insertSymbol(symbol2);
//     scopeTable.insertSymbol(symbol3);
//     scopeTable.insertSymbol(symbol4);

//     // Print the ScopeTable
//     cout << "\nPrinting ScopeTable:\n";
//     scopeTable.print();

//     // Look up symbols
//     cout << "\nLooking up symbols:\n";
//     SymbolInfo *foundSymbol = scopeTable.lookUp(symbol1);
//     if (foundSymbol)
//         cout << "Found: " << foundSymbol->showSymbol() << "\n";
//     else
//         cout << "Symbol not found\n";

//     foundSymbol = scopeTable.lookUp(SymbolInfo("nonexistent NUMBER"));
//     if (foundSymbol)
//         cout << "Found: " << foundSymbol->showSymbol() << "\n";
//     else
//         cout << "Symbol not found\n";

//     // Delete a symbol
//     cout << "\nDeleting symbols:\n";
//     scopeTable.deleteSymbol(symbol4);                          // Delete symbol 'y'
//     scopeTable.deleteSymbol(SymbolInfo("nonexistent NUMBER")); // Try to delete a non-existent symbol

//     // Print the ScopeTable after deletion
//     cout << "\nPrinting ScopeTable after deletion:\n";
//     scopeTable.print(1);
//     // cout << "NAFIS NAHIAN\n";
//     // cout << "\tNAFIS NAHIAN\n";
//     return 0;
// }
