// import java.io.*;
// import java.util.*;

// import SymbolTable.SymbolInfo;
import SymbolTable.HashFunction;

public class test {
    public static void main(String[] args) {
        try {
            //test sdbmHash
            String testString = "main";
            int hashValue = HashFunction.sdbmHash(testString) % 7;
            System.out.println("Hash value for '" + testString + "': " + hashValue);

        } catch (Exception e) {
            System.err.println("Error opening file: " + e.getMessage());
        }
    }
}
