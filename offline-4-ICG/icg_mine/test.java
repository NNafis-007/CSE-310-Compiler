import java.io.*;
// import java.util.*;

import SymbolTable.SymbolInfo;
import SymbolTable.SymbolTable;


public class test {
    public static void main(String[] args) {
        try {
            FileWriter fileWriter = new FileWriter("output.txt");
            PrintWriter out = new PrintWriter(fileWriter);
            
            SymbolTable st = new SymbolTable(7);
            SymbolInfo s1 = new SymbolInfo("main", "ID");
            st.insertSymbol(s1);
            st.printAllScopeTable();

            st.enterScope();
            SymbolInfo s2 = new SymbolInfo("_i", "ID");
            SymbolInfo s3 = new SymbolInfo("_i", "ID");
            st.insertSymbol(s2);
            
            int[] index = new int[1];
            int[] chainPos = new int[1];
            
            if (st.lookUp(s3, index, chainPos) != null) {
                out.println(s3.showSymbol() + " already exists in the ScopeTable# "
                        + st.getCurrentScopeId() + " at position " + index[0] + ", " + chainPos[0]);
            } else {
                System.out.println("Not found");
            }
            
            st.printAllScopeTable();
            st.close();
            out.close();
            
            System.out.println("SymbolTable test completed successfully! Check output.txt for results.");
            
        } catch (IOException e) {
            System.err.println("Error opening file: " + e.getMessage());
        }
    }
}
