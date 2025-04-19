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
            hash_table[index] = new SymbolInfo(s.getName(), s.getType());
        }
        else
        {
            // collision, insert at the end of the linked list
            SymbolInfo *temp = hash_table[index];
            while (temp->next != NULL)
            {
                temp = temp->next;
            }
            temp->next = new SymbolInfo(s.getName(), s.getType());
        }
        cout << s.getName() << " inserted successfully at index" << index << "\n";
        return true; // symbol inserted successfully
    }

    SymbolInfo *lookUp(SymbolInfo s)
    {
        string name = s.getName();
        string type = s.getType();
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
            if (hash_table[i] != NULL)
            {

                hash_table[i]->show(); // chain handled in SymbolInfo class
            }
        }

        cout << "\n";
    }
};

int main()
{
    // Create a ScopeTable with 5 buckets and scope ID 1
    ScopeTable *scopeTable = new ScopeTable(5, 1);

    // Test insertSymbol
    SymbolInfo s1("x", "int");
    SymbolInfo s2("y", "float");
    SymbolInfo s3("z", "char");
    SymbolInfo s4("x", "double"); // Duplicate name, different type

    cout << "Inserting symbols...\n";
    scopeTable->insertSymbol(s1); // Should succeed
    scopeTable->insertSymbol(s2); // Should succeed
    scopeTable->insertSymbol(s3); // Should succeed
    scopeTable->insertSymbol(s4); // Should fail (duplicate name)

    // Test lookUp
    cout << "\nLooking up symbols...\n";
    SymbolInfo *found = scopeTable->lookUp(s1);
    if (found)
    {
        cout << "Found: ";
        found->show();
    }
    else
    {
        cout << "Symbol not found.\n";
    }

    SymbolInfo s5("a", "int"); // Non-existent symbol
    found = scopeTable->lookUp(s5);
    if (found)
    {
        cout << "Found: ";
        found->show();
    }
    else
    {
        cout << "Symbol not found.\n";
    }

    // Test deleteSymbol
    cout << "\nDeleting symbols...\n";
    scopeTable->deleteSymbol(s1);
    scopeTable->deleteSymbol(s5);
    // Test print
    cout << "\nPrinting ScopeTable...\n";
    scopeTable->print();

    // Clean up
    delete scopeTable;

    return 0;
}
