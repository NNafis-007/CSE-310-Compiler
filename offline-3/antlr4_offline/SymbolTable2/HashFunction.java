package SymbolTable2;

public class HashFunction {
    public static int sdbmHash(String str) {
        long hash = 0;
        for (char c : str.toCharArray()) {
            hash = (c + (hash << 6) + (hash << 16) - hash);
            hash &= 0xFFFFFFFFL; // simulate 32-bit unsigned overflow
        }
        return (int)(hash % 7);
    }
}
