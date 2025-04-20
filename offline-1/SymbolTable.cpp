#include <bits/stdc++.h>
#include "ScopeTable.cpp"
using namespace std;

class SymbolTable
{
    ScopeTable *currentScope;
    int num_buckets;
    int no_of_scopes;

public:
    // Create the root scope
    SymbolTable(int num_buckets)
    {
        this->num_buckets = num_buckets;
        this->no_of_scopes = 1;
        this->currentScope = new ScopeTable(this->num_buckets, this->no_of_scopes);
        this->currentScope->setParentScope(NULL);
    }
    ~SymbolTable()
    {
        while(this->currentScope->getParentScope() != NULL){
            ScopeTable* parent = this->currentScope->getParentScope();
            delete this->currentScope;
            this->currentScope = parent;
        }
        delete this->currentScope;
    }

    void enterScope()
    {
        this->no_of_scopes += 1;
        ScopeTable *new_scope_table = new ScopeTable(this->num_buckets, this->no_of_scopes);
        new_scope_table->setParentScope(this->currentScope);
        this->currentScope = new_scope_table;
    }
    void exitScope()
    {
        if (this->currentScope->getParentScope() != NULL)
        {
            ScopeTable *parent_scope = this->currentScope->getParentScope();
            delete this->currentScope;
            this->currentScope = parent_scope;
            // this->no_of_scopes -= 1;
        }
        else
        {
            cout << "No scope to exit\n";
        }
    }
    bool insertSymbol(SymbolInfo s)
    {
        return this->currentScope->insertSymbol(s);
    }

    bool removeSymbol(SymbolInfo s)
    {
        return this->currentScope->deleteSymbol(s);
    }

    SymbolInfo *lookUp(SymbolInfo s)
    {
        SymbolInfo *found = NULL;
        ScopeTable *curr_Scope = this->currentScope;
        while (curr_Scope != NULL)
        {
            found = curr_Scope->lookUp(s);
            if (found != NULL)
            {
                return found;
            }
            curr_Scope = curr_Scope->getParentScope();
        }
        cout << "\t'" << s.getName() << "' not found in any of the ScopeTables\n";
        return NULL;
    }

    void printCurrentScopeTable()
    {
        this->currentScope->print();
    }

    void printAllScopeTable()
    {
        ScopeTable *curr_Scope = this->currentScope;
        int indentLevel = 0;
        while (curr_Scope != NULL)
        {
            curr_Scope->print(indentLevel++);
            curr_Scope = curr_Scope->getParentScope();
        }
    }
};