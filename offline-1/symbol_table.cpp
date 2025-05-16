#include <bits/stdc++.h>
using namespace std;

unsigned int sdbmHash(string str)
{
    unsigned int hash = 0;
    unsigned int len = str.length();
    for (unsigned int i = 0; i < len; i++)
    {
        hash = ((str[i]) + (hash << 6) + (hash << 16) - hash);
    }
    return hash;
}

class SymbolInfo
{
    string name;
    string symbolType;

public:
    SymbolInfo *next;
    SymbolInfo(string name, string type, SymbolInfo *next = NULL)
    {
        this->name = name;
        this->symbolType = type;
        this->next = next;
    }
    // Copy constructor
    SymbolInfo(const SymbolInfo &other)
    {
        this->name = other.name;
        this->symbolType = other.symbolType;
        this->next = NULL; // Deep copy does not copy the linked list structure
    }
    SymbolInfo()
    {
        name = "";
        symbolType = "";
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
        return "< " + this->name + " : " + this->symbolType + " >";
    }
};

class ScopeTable
{
    SymbolInfo **hash_table;
    ScopeTable *parentScope;
    int num_buckets;
    string scope_id;
    ostream& out;
    // HashFunction::HashFunc sdbmHash; // Function pointer for the hash function

public:
    // Constructor that takes the hash function name as a string
    ScopeTable(int num_buckets, string scope_id, ostream& output_stream = cout) : out(output_stream)
    {
        //this->out << "\tScopeTable# " << scope_id << " created\n";
        this->num_buckets = num_buckets;
        this->scope_id = scope_id;
        this->parentScope = NULL;
        
        // this->hashFunction = HashFunction::getHashFunction(hashFuncName); // Set hash function

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
        // this->out << "\tScopeTable# " << this->scope_id << " removed\n";
    }

    ScopeTable *getParentScope()
    {
        return this->parentScope;
    }

    void setParentScope(ScopeTable *s)
    {
        this->parentScope = s;
    }

    // void setHashFunction(const string &hashFuncName)
    // {
    //     this->hashFunction = HashFunction::getHashFunction(hashFuncName);
    // }

    // Modify insertSymbol to use the hashFunction object
    bool insertSymbol(SymbolInfo s)
    {
        SymbolInfo *found = this->lookUp(s, false);
        if (found != NULL)
        {
            // this->out << "\t'" << s.getName() << "' already exists in the current ScopeTable\n";
            return false;
        }

        // unsigned long long hash = sdbmHash(s.getName(), this->num_buckets);
        unsigned int hash = sdbmHash(s.getName());
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
        // this->out << "\tInserted in ScopeTable# " << this->scope_id << " at position " << index + 1
        //      << ", " << chain_position + 1 << "\n";
        return true;
    }

    //OG ACTUAL LOOKUP
    SymbolInfo *lookUp(SymbolInfo s, bool showOutput = false) 
    {
        string name = s.getName();
        // unsigned long long hash = sdbmHash(name, this->num_buckets) % num_buckets;
        unsigned int hash = sdbmHash(s.getName());
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
                    
                    // this->out << "\t'" << name << "' found in ScopeTable# " << this->scope_id
                    //      << " at position " << index + 1 << ", " << chain_position << "\n";
                }
                return temp;
            }
            temp = temp->next;
        }
        return NULL;
    }
    //

    // FOR OFFLINE 2 LMAO
    SymbolInfo *lookUp(SymbolInfo s, int& found_index, int& chain_pos, bool showOutput = false) 
    {
        string name = s.getName();
        // unsigned long long hash = sdbmHash(name, this->num_buckets) % num_buckets;
        unsigned int hash = sdbmHash(s.getName());
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
                    
                    // this->out << "\t'" << name << "' found in ScopeTable# " << this->scope_id
                    //      << " at position " << index + 1 << ", " << chain_position << "\n";
                }
                found_index = index;
                chain_pos = chain_position;
                return temp;
            }
            temp = temp->next;
        }
        found_index = -1;
        chain_pos = -1;
        return NULL;
    }

    bool deleteSymbol(SymbolInfo s)
    {
        // int index = sdbmHash(s.getName(), this->num_buckets) % num_buckets;
        unsigned int hash = sdbmHash(s.getName());
        int index = hash % num_buckets;
        if (hash_table[index] == NULL)
        {
            // this->out << "\tNot found in the current ScopeTable\n";
            return false; // symbol not found
        }

        SymbolInfo *head = hash_table[index];
        int chain_position = 0;
        // if found at head, delete
        if (head->getName() == s.getName())
        {
            hash_table[index] = head->next; // delete the head node
            delete head;
            // this->out << "\tDeleted '" << s.getName() << "' from ScopeTable# " << this->scope_id
            //      << " at position " << index + 1 << ", " << chain_position + 1 << "\n";

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
                    // this->out << "\tDeleted '" << s.getName() << "' from ScopeTable# " << this->scope_id
                    //      << " at position " << index + 1 << ", " << chain_position + 1 << "\n";
                    return true; // symbol deleted successfully
                }
            }
            // if at the last elem
            if (head != NULL && head->getName() == s.getName())
            {
                delete head;
                // this->out << "\tDeleted '" << s.getName() << "' from ScopeTable# " << this->scope_id
                //      << " at position " << index + 1 << ", " << chain_position + 1 << "\n";

                return true; // symbol deleted successfully
            }
        }
        return false; // symbol not found
    }

    // void print(int indentLevel = 0)
    // {
    //     string indent(indentLevel, '\t');
    //     this->out << indent << "ScopeTable# " << scope_id << "\n";
    //     for (int i = 0; i < num_buckets; i++)
    //     {
    //         this->out << indent << i + 1 << "--> ";
    //         SymbolInfo *temp = hash_table[i];
    //         if (temp != NULL)
    //         {
    //             this->out << temp->showSymbol() << " ";
    //             while (temp->next != NULL)
    //             {
    //                 temp = temp->next;
    //                 this->out << temp->showSymbol() << " ";
    //             }
    //         }
    //         this->out << "\n";
    //     }
    // }
    void print(int indentLevel = 0)
    {
        string indent(indentLevel, '\t');
        this->out << indent << "ScopeTable # " << scope_id << "\n";
        for (int i = 0; i < num_buckets; i++)
        {
            SymbolInfo *temp = hash_table[i];
            if (temp != NULL)
            {
                this->out << indent << i << " --> ";
                while (temp != NULL)
                {
                    this->out << "< " << temp->getName() << " : " << temp->getType() << " >";
                    temp = temp->next;
                    // if (temp != NULL)
                    //     this->out << " ";
                }
                this->out << "\n";
            }
        }
    }
};

class SymbolTable
{
    ScopeTable *currentScope;
    int num_buckets;
    int no_of_scopes;
    string curr_scope_id;
    ostream& out;
    
    string get_scope_id()
    {
        string s_id = "1";
        int n_scopes = this->no_of_scopes;
        while(n_scopes > 1)
        {
            s_id += ".1";
            n_scopes -= 1;
        }
        return s_id;
    }

public:
    // Create the root scope
    SymbolTable(int num_buckets, ostream& output_stream = cout) : out(output_stream)
    {
        this->num_buckets = num_buckets;
        this->no_of_scopes = 1;
        this->currentScope = new ScopeTable(this->num_buckets, "1", this->out);
        curr_scope_id = "1";
        this->currentScope->setParentScope(NULL);
    }
    ~SymbolTable()
    {
        while (this->currentScope->getParentScope() != NULL)
        {
            ScopeTable *parent = this->currentScope->getParentScope();
            delete this->currentScope;
            this->currentScope = parent;
        }
        delete this->currentScope;
    }

    string get_curr_scope_id()
    {
        return this->curr_scope_id;
    }

    void enterScope()
    {
        this->no_of_scopes += 1;
        string s_id = this->get_scope_id();
        this->curr_scope_id = s_id;
        ScopeTable *new_scope_table = new ScopeTable(this->num_buckets, s_id, this->out);
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
            this->no_of_scopes -= 1;
        }
        else
        {
            // cout << "No scope to exit\n";
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

    SymbolInfo *lookUp(SymbolInfo s, int& indx, int& chain_pos)
    {
        SymbolInfo *found = NULL;
        ScopeTable *curr_Scope = this->currentScope;
        while (curr_Scope != NULL)
        {
            found = curr_Scope->lookUp(s ,indx, chain_pos);
            if (found != NULL)
            {
                return found;
            }
            curr_Scope = curr_Scope->getParentScope();
        }
        // cout << "\t'" << s.getName() << "' not found in any of the ScopeTables\n";
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
            curr_Scope->print(indentLevel);
            curr_Scope = curr_Scope->getParentScope();
        }
    }
};

int main(int argc, char *argv[])
{   
    ofstream out("output.txt");
    if (!out.is_open())
    {
        cout << "Error opening file" << endl;
        return 1;
    }
    SymbolTable st(7);
    SymbolInfo s1("main", "ID");
    st.insertSymbol(s1);
    st.printAllScopeTable();

    st.enterScope();
    SymbolInfo s2("_i", "ID");
    SymbolInfo s3("_i", "ID");
    st.insertSymbol(s2);
    int indx, chain_pos;
    if(st.lookUp(s3, indx, chain_pos) != NULL)
    {
        out << s3.showSymbol() << " already exists in the ScopeTable# " 
            << st.get_curr_scope_id() << " at position " << indx << ", " << chain_pos << endl;
    }
    else
    {
        cout << "Not found" << endl;
    }
    st.printAllScopeTable();
    out.close();


    return 0;
}