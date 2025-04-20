#include <bits/stdc++.h>
#include "SymbolInfo.cpp"
using namespace std;

// Might have to pass num_buckets to avoid overflow
unsigned long long SDBMHash(string str)
{
    unsigned long long hash = 0;
    unsigned int i = 0;
    unsigned int len = str.length();

    for (i = 0; i < len; i++)
    {
        hash = (str[i]) + (hash << 6) + (hash << 16) - hash;
    }

    return hash;
}

class ScopeTable
{
    SymbolInfo **hash_table;
    ScopeTable *parentScope;
    int num_buckets;
    int scope_id;

public:
    ScopeTable(int num_buckets, int scope_id, ScopeTable *parentScope = NULL)
    {
        this->num_buckets = num_buckets;
        this->scope_id = scope_id;
        this->parentScope = parentScope;
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
    }

    bool insertSymbol(SymbolInfo s)
    {
        // Check if the symbol already exists in the current scope
        SymbolInfo *found = this->lookUp(s);
        if (found != NULL)
        {
            cout << s.getName() << " Already exists in the current scope\n";
            return false; // symbol with this name exists in the current scope
        }

        // calculate hash value and index
        unsigned long long hash = SDBMHash(s.getName());
        int index = hash % num_buckets;

        // if index is empty, insert the symbol
        if (hash_table[index] == NULL)
        {
            hash_table[index] = new SymbolInfo(s);
        }
        else
        {
            // collision, insert at the end of the linked list
            SymbolInfo *temp = hash_table[index];
            while (temp->next != NULL)
            {
                temp = temp->next;
            }
            temp->next = new SymbolInfo(s);
        }
        cout << s.getName() << " inserted successfully at index" << index << "\n";
        return true; // symbol inserted successfully
    }

    SymbolInfo *lookUp(SymbolInfo s)
    {
        string name = s.getName();
        unsigned long long hash = SDBMHash(name) % num_buckets;
        int index = hash % num_buckets;
        SymbolInfo *temp = hash_table[index];
        while (temp != NULL)
        {
            // no duplicate names allowed in the same scope
            if (temp->getName() == name)
            {
                return temp;
            }
            temp = temp->next;
        }
        return NULL;
    }

    bool deleteSymbol(SymbolInfo s)
    {
        int index = SDBMHash(s.getName()) % num_buckets;
        if (hash_table[index] == NULL)
        {
            cout << s.getName() << " not found in the current scope\n";
            return false; // symbol not found
        }

        SymbolInfo *head = hash_table[index];
        // if found at head, delete
        if (head->getName() == s.getName())
        {
            hash_table[index] = head->next; // delete the head node
            delete head;
            cout << s.getName() << " deleted successfully\n";
            return true; // symbol deleted successfully
        }
        // else traverse the chain
        else
        {
            while (head->next != NULL)
            {
                SymbolInfo *prev = head;
                head = head->next;
                if (head->getName() == s.getName())
                {
                    prev->next = head->next;
                    delete head;
                    cout << s.getName() << " deleted successfully\n";
                    return true; // symbol deleted successfully
                }
            }
            // if at the last elem
            if (head != NULL && head->getName() == s.getName())
            {
                delete head;
                cout << s.getName() << " deleted successfully\n";
                return true; // symbol deleted successfully
            }
        }
    }

    void print()
    {
        cout << "ScopeTable #" << scope_id << "\n";
        for (int i = 0; i < num_buckets; i++)
        {
            cout << i + 1 << "--> ";
            SymbolInfo *temp = hash_table[i];
            if (temp != NULL)
            {
                cout << temp->showSymbol();
                while (temp->next != NULL)
                {
                    temp = temp->next;
                    cout << " " << temp->showSymbol();
                }
                
            }
            cout << "\n";
        }
        cout << "\n";
    }
};


//TESTING CODE
// int main()
// {
//     // Create a ScopeTable with 5 buckets and scope ID 1
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
//     scopeTable.deleteSymbol(symbol4); // Delete symbol 'y'
//     scopeTable.deleteSymbol(SymbolInfo("nonexistent NUMBER")); // Try to delete a non-existent symbol

//     // Print the ScopeTable after deletion
//     cout << "\nPrinting ScopeTable after deletion:\n";
//     scopeTable.print();

//     return 0;
// }
