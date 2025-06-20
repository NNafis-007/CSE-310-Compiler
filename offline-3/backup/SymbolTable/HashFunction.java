package SymbolTable;

public class HashFunction {
    public static int sdbmHash(String str) {
        int hash = 0;
        int len = str.length();
        for (int i = 0; i < len; i++) {
            hash = (str.charAt(i) + (hash << 6) + (hash << 16) - hash);
        }
        return Math.abs(hash); // Ensure positive value
    }
}
