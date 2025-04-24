#include "HashFunction.cpp"
#include <iostream>
#include <string>
#include <unordered_set>
#include <vector>
using namespace std;

void testHashFunction(const string &name, HashFunction::HashFunc hashFn, const vector<string> &testStrings, const vector<int> &bucketCounts)
{
    for (int bucketCount : bucketCounts)
    {
        unordered_set<unsigned long long> hashes;
        int collisions = 0;

        for (const auto &str : testStrings)
        {
            unsigned long long hashVal = hashFn(str, bucketCount);
            if (hashes.find(hashVal) != hashes.end())
            {
                ++collisions;
            }
            else
            {
                hashes.insert(hashVal);
            }
        }
        float ratio = (float)(collisions) / bucketCount;

        // Write results to the console
        cout << name << " ratio (Buckets: " << bucketCount << "): " << ratio << " for "
             << testStrings.size() << " strings\n";
    }
    cout << "---------------------------------------------" << endl;
}

int main()
{
    freopen("report.txt", "w", stdout);
    cout << "---------------------------------------------" << endl;
    vector<int> bucketCount = {150, 200, 250}; // You can change this to control hash bucket range
    vector<string> testStrings = {
        "x", "y", "z", "apple", "banana", "grape", "test", "collision",
        "123", "456", "789", "foo", "bar", "baz", "qux", "lorem", "ipsum",
        "dolor", "sit", "amet", "hello", "world", "a", "b", "c", "d", "e", "f",
        "username", "user123", "user_456", "admin", "administrator", "root",
        "secure", "password", "pass123", "Pa$$w0rd", "letmein", "trustno1",
        "abc", "abcd", "abcde", "abcdef", "abcdefg", "abcdefgh", "abcdefghij",
        "aaaaaaaa", "aaaabaaa", "aaabaaaa", "aaaaaaab", "abcdabcd", "zxywvu",
        "random", "random1", "random2", "random3", "string", "string1",
        "string_test", "test_string", "unit_test", "testing123", "tester",
        "test_case", "value", "value1", "value2", "value3", "hashmap",
        "hashtable", "map", "dictionary", "dict", "table", "entry", "record",
        "log", "logfile", "log123", "data", "dataset", "data123", "input",
        "output", "input1", "input2", "output1", "output2", "cache", "buffer",
        "memory", "mem123", "heap", "stack", "queue", "deque", "node", "edge",
        "graph", "tree", "binary", "binarytree", "btree", "avltree", "redblack",
        "matrix", "vector", "list", "array", "arraylist", "linkedlist",
        "pointer", "reference", "ref123", "ptr456", "nullptr", "null", "none",
        "true", "false", "yes", "no", "maybe", "hello_world", "hello-world",
        "welcome123", "greetings", "salutations", "farewell", "goodbye",
        "exit", "start", "stop", "begin", "end", "open", "close", "read", "write"};

    testHashFunction("SDBM", HashFunction::SDBMHash, testStrings, bucketCount);
    testHashFunction("PJW", HashFunction::PJWHash, testStrings, bucketCount);
    testHashFunction("DJB", HashFunction::DJBHash, testStrings, bucketCount);

    return 0;
}
