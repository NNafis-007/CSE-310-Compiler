#include <string>
#include <iostream>
using namespace std;

class HashFunction
{
public:
    // Define a function pointer type for hash functions
    typedef unsigned long long (*HashFunc)(string, unsigned int);

    // Static hash functions
    static unsigned long long SDBMHash(string str, unsigned int num_buckets)
    {
        unsigned long long hash = 0;
        unsigned int len = str.length();

        for (unsigned int i = 0; i < len; i++)
        {
            hash = ((str[i]) + (hash << 6) + (hash << 16) - hash) % num_buckets;
        }

        return hash;
    }

    //peter J. Weinberger hash function, Compilers (Principles, Techniques and Tools) by Aho, Sethi and Ulman
    static unsigned long long PJWHash(string str, unsigned int num_buckets)
    {
        const unsigned int BitsInUnsignedInt = (unsigned int)(sizeof(unsigned int) * 8);
        const unsigned int ThreeQuarters = (unsigned int)((BitsInUnsignedInt * 3) / 4);
        const unsigned int OneEighth = (unsigned int)(BitsInUnsignedInt / 8);
        const unsigned int HighBits =
            (unsigned int)(0xFFFFFFFF) << (BitsInUnsignedInt - OneEighth);
        unsigned long long hash = 0;
        unsigned int test = 0;
        unsigned int length = str.length();

        for (unsigned int i = 0; i < length; i++)
        {
            hash = ((hash << OneEighth) + (str[i])) % num_buckets;

            if ((test = hash & HighBits) != 0)
            {
                hash = ((hash ^ (test >> ThreeQuarters)) & (~HighBits));
            }
        }

        return hash;
    }

    //Professor Daniel J. Bernstein hash function
    //https://theartincode.stanis.me/008-djb2/
    static unsigned long long DJBHash(string str, unsigned int num_buckets)
    {
        unsigned long long hash = 5381;
        unsigned int i = 0;
        unsigned int length = str.length();

        for (i = 0; i < length; i++)
        {
            hash = (((hash << 5) + hash) + str[i]) % num_buckets;
        }

        return hash;
    }

    // Static method to map hash function names to function pointers
    static HashFunc getHashFunction(const string &hashFuncName)
    {
        if (hashFuncName == "SDBM")
        {
            return SDBMHash;
        }
        else if (hashFuncName == "PJW")
        {
            return PJWHash;
        }
        else if(hashFuncName == "DJB"){
            return DJBHash;
        }
        else
        {
            cerr << "Unknown hash function name: " << hashFuncName << ". Defaulting to SDBM.\n";
            return SDBMHash;
        }
    }
};
