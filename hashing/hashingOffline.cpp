#include <bits/stdc++.h>
using namespace std;
#define NO_DATA 0
#define INSERTED 1
#define DELETED 2

bool isPrime(int num)
{
    if (num <= 1)
        return false;
    for (int i = 2; i * i <= num; ++i)
    {
        if (num % i == 0)
            return false;
    }
    return true;
}

// min tableSize given, run the func as it is
// getting tableSize while rehash, send 2*min as param from driver
int generateNextPrime(int min)
{
    for (int num = min; num <= 50000; ++num)
    {
        if (isPrime(num))
            return num;
    }
}

long long int hash1(string key)
{
    int hash = 0;
    for (int i = 0; i < key.length(); i++)
    {
        hash = hash + pow(key.at(i) % 40 + 1, i % 6);
    }
    return hash;
}

long long int hash2(string key)
{
    int hash = 0;
    for (int i = 0; i < key.length(); i++)
    {
        hash = 5 * hash + key.at(i);
    }
    return hash;
}

long long int auxHash(string key)
{
    int hash = 0;
    for (int i = 0; i < key.length(); i++)
    {
        hash = hash + (i + 1) * key.at(i);
    }
    return hash;
}

int customHash(string k, int i)
{
    // return (hash2(k) + 3 * i * auxHash(k) + 5 * i * i);
    return (hash1(k) + 3 * i * auxHash(k) + 5 * i * i);
}

int doubleHash(string k, int i)
{
    return (hash1(k) + i * auxHash(k));
    // return (hash2(k) + i * auxHash(k));
}

string generateRandomWord()
{
    string alphas = "abcdefghijklmnopqrstuvwxyz";
    string word;
    int len = 5 + rand() % 6; // random word len between 1 and 15
    for (int i = 0; i < len; i++)
    {
        int randomIndex = rand() % 26;
        word = word + alphas.at(randomIndex);
    }
    return word;
}

class data
{
public:
    string key;
    int value;
    int state;

    data(string k = "", int val = -1e9, int s = NO_DATA)
    {
        key = k;
        value = val;
        state = s;
    }
};

// TEST FUNCTION
class node
{
public:
    int val;
    node *next;
    string key;
    node(int val = -1e9, string k = "")
    {
        this->val = val;
        next = NULL;
        key = k;
    }
};

// Chaining
class hashTableChained
{
public:
    vector<node *> chainingTable;
    int elementCount;
    string type;
    int collisionCount;
    int probes;
    int maxChainLen;
    int insertCount;
    int delCount;
    int initialSize;

    hashTableChained() {}

    // Constructor
    hashTableChained(int size, string t, int chain)
    {
        // perform everything for seperate chaining
        size = generateNextPrime(size);
        chainingTable = vector<node *>(size, NULL);
        type = t;
        elementCount = 0;
        collisionCount = 0;
        maxChainLen = chain;
        insertCount = 0;
        delCount = 0;
        probes = 0;
        initialSize = size;
    }

    int findProbe()
    {
        unordered_set<string> words;
        int probeCount;
        for (int i = 0; words.size() < 1002; i++)
        {
            string word = generateRandomWord();
            words.insert(word);
        }

        for (const auto &word : words)
        {
            int foundVal = find(word);
        }
        probeCount = this->probes;
        // cout << "probeCount from the fn is " << probeCount << endl;
        return probeCount;
    }

    void rehash(string prompt)
    {
        cout << "Before Rehash : " << endl;
        double loadF = ((float)elementCount / chainingTable.size());
        cout << "\tLoad factor : " << loadF << endl;
        resetProbe();
        double probesCount = ((float)findProbe() / 1000);
        cout << "\taverage probe count : " << probesCount << endl;
        cout << "\tmaximum chain length : " << maximumChainLength() << endl;

        int prevSize = this->chainingTable.size();
        int nextSize;

        vector<node *> oldHashTable(chainingTable);

        // make the table 2 times larger
        if (prompt.compare("insert") == 0)
        {
            // cout << "rehashing for insert" << endl;
            nextSize = generateNextPrime(prevSize * 2);
        }

        // make the table half size smaller
        else if (prompt.compare("delete") == 0)
        {
            // cout << "rehashing for delete" << endl;
            nextSize = generateNextPrime(prevSize / 2);
            if (nextSize < initialSize)
            {
                nextSize = prevSize;
                return;
            }
        }

        // re-initializing the underlying hashtable
        chainingTable = vector<node *>(nextSize, NULL);

        // perform insert in newTable for every entry
        for (int i = 0; i < oldHashTable.size(); i++)
        {
            node *n = oldHashTable[i];

            while (n != NULL)
            {
                node *newNode = new node(n->val, n->key);
                // insert all nodes of a chain in newTable hashed index
                int newIndex = hash1(n->key) % nextSize;
                // int newIndex = hash2(n->key) % nextSize;

                if (chainingTable[newIndex] == NULL)
                {
                    chainingTable[newIndex] = newNode;
                }

                else
                {
                    newNode->next = chainingTable[newIndex]->next;
                    chainingTable[newIndex]->next = newNode;
                }
                n = n->next;
            }
        }

        // free the old hashtable
        for (int i = 0; i < oldHashTable.size(); i++)
        {
            node *n = oldHashTable[i];
            while (n != NULL)
            {
                node *temp = n;
                n = n->next;
                delete temp;
            }
        }

        oldHashTable.clear();

        cout << "\nAfter Rehash : " << endl;
        loadF = ((float)elementCount / chainingTable.size());
        cout << "\tLoad factor : " << loadF << endl;
        resetProbe();
        probesCount = ((float)findProbe() / 1000);
        cout << "\taverage probe count : " << probesCount << endl;
        cout << "\tmaximum chain length : " << maximumChainLength() << endl;
    }

    int maximumChainLength()
    {
        int maximumChain = 0;
        for (int i = 0; i < chainingTable.size(); i++)
        {
            int c = 0;
            node *n = chainingTable[i];
            while (n != NULL)
            {
                c++;
                n = n->next;
            }
            if (c > maximumChain)
            {
                maximumChain = c;
            }
        }
        return maximumChain;
    }

    void insert(string key)
    {
        // cout << "insertCount : " << insertCount << " maximum chain : " << maximumChainLength() << endl;
        if (insertCount % 100 == 0)
        {
            int maximumChain = maximumChainLength();
            if (maximumChain > maxChainLen)
            {
                rehash("insert");
                // cout << "rehashing for insert" << endl;
            }
        }
        int value = elementCount;
        int index = hash1(key) % chainingTable.size();
        // int index = hash2(key) % chainingTable.size();

        // if the index is empty
        if (chainingTable[index] == NULL)
        {
            chainingTable[index] = new node(value, key);
        }

        // index already has a member, so COLLISION hence chain
        else
        {
            node *newNode = new node(value, key);
            newNode->next = chainingTable[index]->next;
            chainingTable[index]->next = newNode;

            collisionCount++;
        }
        elementCount++;
        insertCount++;
    }

    int find(string key)
    {
        int index = hash1(key) % chainingTable.size();
        // int index = hash2(key) % chainingTable.size();

        int val;

        // if Empty
        if (chainingTable[index] == NULL)
        {
            probes++;
            return -1e9; // error
        }

        // found
        else
        {
            bool found = false;
            node *head = chainingTable[index]; // go to the head of the chain in the given index
            do
            {
                if (key == head->key)
                {
                    val = head->val;
                    found = true;
                    probes++;
                    break;
                }
                else
                {
                    head = head->next;
                    probes++;
                }

            } while (head != NULL);
            if (found)
            {
                return val;
            }
            else
            {
                return -1e9; // DNE
            }
        }
    }

    void remove(string key)
    {
        if (delCount % 100 == 0)
        {
            int maximumChain = maximumChainLength();
            if (maximumChain / maxChainLen < 0.8)
            {
                rehash("delete");
                // cout << "rehashing for delete" << endl;
            }
        }
        int index = hash1(key) % chainingTable.size();
        // int index = hash2(key) % chainingTable.size();
        if (chainingTable[index] == NULL)
        {
            // cout << "Key DNE" << endl;
            return;
        }
        node *head = chainingTable[index];

        // if found at head, delete
        if (head->key == key)
        {
            chainingTable[index] = head->next;
            delete head;
            elementCount--;
            delCount++;
        }

        // if not found at head, traverse
        else
        {
            while (head->next != NULL)
            {
                node *prev = head;
                head = head->next;
                if (head->key == key)
                {
                    prev->next = head->next;
                    delete head;
                    elementCount--;
                    delCount++;
                    break;
                }
            }
        }
    }

    int getCapacity()
    {
        return chainingTable.size();
    }

    void resetProbe()
    {
        probes = 0;
    }
};

// DOUBLE HASHING
class doubleHashTable
{
public:
    vector<data> hashTable;
    int elementCount;
    string type;
    int collisionCount;
    int probes;
    int insertCount;
    int delCount;
    int size;
    int initialSize;

    doubleHashTable() {}

    doubleHashTable(int size, string t)
    {
        size = generateNextPrime(size);
        type = t;
        hashTable = vector<data>(size);
        collisionCount = 0;
        insertCount = 0;
        delCount = 0;
        probes = 0;
        this->size = size;
        initialSize = size;
        elementCount = 0;
    }

    void insert(string k)
    {
        // cout << "insertCount : " << insertCount << endl;

        double loadF = (double)elementCount / getCapacity();
        if (loadF > 0.5)
        {
            rehash("insert");
        }

        int i = 0; // for double hashing
        int initialIndex = doubleHash(k, 0) % hashTable.size();
        int index = initialIndex;
        int iter = 0;
        do
        {
            iter++;
            if (hashTable[index].state == NO_DATA || hashTable[index].state == DELETED)
            {
                // if no data exists in the index then just insert
                hashTable[index] = data(k, ++elementCount, INSERTED);
                insertCount++;
                break;
            }

            // increase i and continue
            else
            {
                i++;
                collisionCount++;
                index = doubleHash(k, i) % hashTable.size();
            }

        } while (i != 0 && index != initialIndex && hashTable.size() != elementCount);
    }

    int find(string key)
    {
        int i = 0; // for double hashing
        int iter = 0;
        int val = -1e9;
        int initialIndex = doubleHash(key, 0) % hashTable.size();
        int index = initialIndex;

        do
        {
            iter++;
            probes++;
            if (hashTable[index].state == NO_DATA || hashTable[index].state == DELETED)
            {
                // data DNE
                break;
            }

            // increase i and continue
            else
            {
                // data exists in the index so check
                if (hashTable[index].key.compare(key) == 0)
                {
                    // data found
                    val = hashTable[index].value;

                    break;
                }
                i++; // continue finding

                index = doubleHash(key, i) % hashTable.size();
            }

        } while (i != 0 && index != initialIndex);
        return val;
    }

    void remove(string key)
    {
        int i = 0; // for double hashing
        int index;
        int iter = 0;
        do
        {
            iter++;
            index = doubleHash(key, i) % hashTable.size();
            if (hashTable[index].state == NO_DATA || hashTable[index].state == DELETED)
            {
                // data DNE
                // cout << "key does not exist while deleting" << endl;
                break;
            }

            // increase i and continue
            else
            {
                // data exists in the index so check
                if (hashTable[index].key.compare(key) == 0)
                {
                    hashTable[index].key = "";
                    hashTable[index].value = -1e9;
                    hashTable[index].state = DELETED;
                    elementCount--;
                    break;
                }
                i++;
            }

        } while (iter < hashTable.size());
    }

    void resetProbe()
    {
        probes = 0;
    }

    int getCapacity()
    {
        return hashTable.size();
    }

    void rehash(string prompt)
    {
        int prevSize = hashTable.size();
        int nextSize;
        vector<data> oldTable(hashTable);

        if (prompt.compare("insert") == 0)
        {
            // cout << "rehashing for insert" << endl;
            nextSize = generateNextPrime(prevSize * 2);
            size = nextSize;
        }

        hashTable = vector<data>(nextSize);
        // printTable();
        for (int i = 0; i < oldTable.size(); i++)
        {
            if (oldTable[i].state == INSERTED && oldTable[i].value != -1e9)
            {
                // insert into newHashtable

                int j = 0; // for double hashing
                int initialIndex = doubleHash(oldTable[i].key, 0) % hashTable.size();
                int index = initialIndex;
                int iter = 0;
                do
                {
                    iter++;
                    if (hashTable[index].state == NO_DATA || hashTable[index].state == DELETED)
                    {
                        // if no data exists in the index then just insert
                        hashTable[index] = data(oldTable[i].key, oldTable[i].value, INSERTED);
                        // cout << "inserting from oldTable ";
                        // cout << "i = " << i << ", key - " << oldTable[i].key << ", value - " << oldTable[i].value << ", state - " << oldTable[i].state << endl;
                        break;
                    }

                    // increase i and continue
                    else
                    {
                        j++;
                        index = doubleHash(oldTable[i].key, j) % hashTable.size();
                    }

                } while (j != 0 && index != initialIndex);
            }
        }

        oldTable.clear();
    }

    void printTable()
    {
        for (int i = 0; i < hashTable.size(); i++)
        {
            if (hashTable[i].state == INSERTED)
            {
                cout << "i = " << i << "key - " << hashTable[i].key << ", value - " << hashTable[i].value << ", state - " << hashTable[i].state << endl;
            }
        }
    }
};

// CUSTOM PROBING
class customHashTable
{
public:
    vector<data> hashTable;
    int elementCount;
    string type;
    int collisionCount;
    int probes;
    int insertCount;
    int delCount;
    int size;
    int initialSize;

    customHashTable() {}

    customHashTable(int size, string t)
    {
        size = generateNextPrime(size);
        type = t;
        hashTable = vector<data>(size);
        collisionCount = 0;
        insertCount = 0;
        delCount = 0;
        probes = 0;
        this->size = size;
        initialSize = size;
        elementCount = 0;
    }

    void insert(string k)
    {
        // cout << "insertCount : " << insertCount << endl;

        double loadF = (double)elementCount / getCapacity();
        if (loadF > 0.5)
        {
            rehash("insert");
        }

        int i = 0; // for double hashing
        int initialIndex = customHash(k, 0) % hashTable.size();
        int index = initialIndex;
        int iter = 0;
        do
        {
            iter++;
            if (hashTable[index].state == NO_DATA || hashTable[index].state == DELETED)
            {
                // if no data exists in the index then just insert
                hashTable[index] = data(k, ++elementCount, INSERTED);
                insertCount++;
                break;
            }

            // increase i and continue
            else
            {
                i++;
                collisionCount++;
                index = customHash(k, i) % hashTable.size();
            }

        } while (i != 0 && index != initialIndex && hashTable.size() != elementCount);
    }

    int find(string key)
    {
        int i = 0; // for double hashing
        int iter = 0;
        int val = -1e9;
        int initialIndex = customHash(key, 0) % hashTable.size();
        int index = initialIndex;

        do
        {
            iter++;
            probes++;
            if (hashTable[index].state == NO_DATA || hashTable[index].state == DELETED)
            {
                // data DNE
                break;
            }

            // increase i and continue
            else
            {
                // data exists in the index so check
                if (hashTable[index].key.compare(key) == 0)
                {
                    // data found
                    val = hashTable[index].value;

                    break;
                }
                i++; // continue finding
                index = customHash(key, i) % hashTable.size();
            }

        } while (i != 0 && index != initialIndex);
        return val;
    }

    void remove(string key)
    {
        int i = 0; // for double hashing
        int index;
        int iter = 0;
        do
        {
            iter++;
            index = customHash(key, i) % hashTable.size();
            if (hashTable[index].state == NO_DATA || hashTable[index].state == DELETED)
            {
                // data DNE
                // cout << "key does not exist while deleting" << endl;
                break;
            }

            // increase i and continue
            else
            {
                // data exists in the index so check
                if (hashTable[index].key.compare(key) == 0)
                {
                    hashTable[index].key = "";
                    hashTable[index].value = -1e9;
                    hashTable[index].state = DELETED;
                    elementCount--;
                    break;
                }
                i++;
            }

        } while (iter < hashTable.size());
    }

    void resetProbe()
    {
        probes = 0;
    }

    int getCapacity()
    {
        return hashTable.size();
    }

    void rehash(string prompt)
    {
        int prevSize = hashTable.size();
        int nextSize;
        vector<data> oldTable(hashTable);

        if (prompt.compare("insert") == 0)
        {
            // cout << "rehashing for insert" << endl;
            nextSize = generateNextPrime(prevSize * 2);
            size = nextSize;
        }

        hashTable = vector<data>(nextSize);
        // printTable();
        for (int i = 0; i < oldTable.size(); i++)
        {
            if (oldTable[i].state == INSERTED && oldTable[i].value != -1e9)
            {
                // insert into newHashtable

                int j = 0; // for double hashing
                int initialIndex = customHash(oldTable[i].key, 0) % hashTable.size();
                int index = initialIndex;
                int iter = 0;
                do
                {
                    iter++;
                    if (hashTable[index].state == NO_DATA || hashTable[index].state == DELETED)
                    {
                        // if no data exists in the index then just insert
                        hashTable[index] = data(oldTable[i].key, oldTable[i].value, INSERTED);
                        // cout << "inserting from oldTable ";
                        // cout << "i = " << i << ", key - " << oldTable[i].key << ", value - " << oldTable[i].value << ", state - " << oldTable[i].state << endl;
                        break;
                    }

                    // increase i and continue
                    else
                    {
                        j++;
                        index = customHash(oldTable[i].key, j) % hashTable.size();
                    }

                } while (j != 0 && index != initialIndex);
            }
        }

        oldTable.clear();
    }

    void printTable()
    {
        for (int i = 0; i < hashTable.size(); i++)
        {
            if (hashTable[i].state == INSERTED)
            {
                cout << "i = " << i << "key - " << hashTable[i].key << ", value - " << hashTable[i].value << ", state - " << hashTable[i].state << endl;
            }
        }
    }
};

class hashMap
{
    hashTableChained htc;
    customHashTable cht;
    doubleHashTable dht;
    string type;

public:
    hashMap(int size, string type, int maxChainLength)
    {
        if (type.compare("chaining") == 0)
        {
            htc = hashTableChained(size, type, maxChainLength);
            this->type = type;
        }

        else if (type.compare("doubleHashing") == 0)
        {
            dht = doubleHashTable(size, type);
            this->type = type;
        }

        else if (type.compare("customProbing") == 0)
        {
            cht = customHashTable(size, type);
            this->type = type;
        }
    }

    void insert(string key)
    {
        if (type.compare("chaining") == 0)
        {
            htc.insert(key);
        }

        else if (type.compare("doubleHashing") == 0)
        {
            dht.insert(key);
        }

        else if (type.compare("customProbing") == 0)
        {
            cht.insert(key);
        }
    }

    void remove(string key)
    {
        if (type.compare("chaining") == 0)
        {
            htc.remove(key);
        }

        else if (type.compare("doubleHashing") == 0)
        {
            dht.remove(key);
        }

        else if (type.compare("customProbing") == 0)
        {
            cht.remove(key);
        }
    }

    int find(string key)
    {
        int val;
        if (type.compare("chaining") == 0)
        {
            val = htc.find(key);
        }

        else if (type.compare("doubleHashing") == 0)
        {
            val = dht.find(key);
        }

        else if (type.compare("customProbing") == 0)
        {
            val = cht.find(key);
        }
        return val;
    }

    int getCollisionCount()
    {
        if (type.compare("chaining") == 0)
        {
            return htc.collisionCount;
        }

        else if (type.compare("doubleHashing") == 0)
        {
            return dht.collisionCount;
        }

        else if (type.compare("customProbing") == 0)
        {
            return cht.collisionCount;
        }
    }

    int getProbeCount()
    {
        unordered_set<string> words;
        int probeCount;
        for (int i = 0; words.size() < 1002; i++)
        {
            string word = generateRandomWord();
            words.insert(word);
        }

        if (type.compare("chaining") == 0)
        {
            htc.resetProbe();
            for (const auto &word : words)
            {
                int foundVal = htc.find(word);
            }
            probeCount = htc.probes;
        }

        else if (type.compare("doubleHashing") == 0)
        {
            dht.resetProbe();
            for (const auto &word : words)
            {
                int foundVal = dht.find(word);
            }
            probeCount = dht.probes;
        }

        else if (type.compare("customProbing") == 0)
        {
            cht.resetProbe();
            for (const auto &word : words)
            {
                int foundVal = cht.find(word);
            }
            probeCount = cht.probes;
        }
        return probeCount;
    }

    int getSize()
    {
        if (type.compare("chaining") == 0)
        {
            return htc.getCapacity();
        }

        else if (type.compare("doubleHashing") == 0)
        {
            return dht.getCapacity();
        }

        else if (type.compare("customProbing") == 0)
        {
            return cht.getCapacity();
        }
    }

    int getElementCount()
    {
        if (type.compare("chaining") == 0)
        {
            return htc.elementCount;
        }

        else if (type.compare("doubleHashing") == 0)
        {
            return dht.elementCount;
        }

        else if (type.compare("customProbing") == 0)
        {
            return cht.elementCount;
        }
    }
};

int main()
{
    hashMap h(5000, "customProbing", 4);
    cout << "size : " << h.getSize() << endl;

    unordered_set<string> words;
    unordered_set<string> searchWords;

    for (int i = 0; words.size() < 10002; i++)
    {
        string word = generateRandomWord();
        words.insert(word);
    }

    int count = 0;
    for (const auto &word : words)
    {
        h.insert(word);
        if (count++ <= 1000)
        {
            searchWords.insert(word);
        }
    }

    cout << "\nDone inserting\n";

    cout << "Collision Count : " << h.getCollisionCount() << endl;
    cout << "Probe Count : " << (double)h.getProbeCount() / 1000.00 << endl;
}