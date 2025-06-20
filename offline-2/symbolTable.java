import java.io.*;
import java.util.*;

/**
 * Hash function implementation using SDBM algorithm
 */
class HashFunction {
    public static int sdbmHash(String str) {
        int hash = 0;
        int len = str.length();
        for (int i = 0; i < len; i++) {
            hash = (str.charAt(i) + (hash << 6) + (hash << 16) - hash);
        }
        return Math.abs(hash); // Ensure positive value
    }
}

/**
 * SymbolInfo class representing a symbol in the symbol table
 */
class SymbolInfo {
    private String name;
    private String symbolType;
    public SymbolInfo next;

    // Constructor with parameters
    public SymbolInfo(String name, String type, SymbolInfo next) {
        this.name = name;
        this.symbolType = type;
        this.next = next;
    }

    // Constructor without next parameter
    public SymbolInfo(String name, String type) {
        this(name, type, null);
    }

    // Copy constructor
    public SymbolInfo(SymbolInfo other) {
        this.name = other.name;
        this.symbolType = other.symbolType;
        this.next = null; // Deep copy does not copy the linked list structure
    }

    // Default constructor
    public SymbolInfo() {
        this.name = "";
        this.symbolType = "";
        this.next = null;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public String getType() {
        return symbolType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.symbolType = type;
    }

    public String showSymbol() {
        return "< " + this.name + " : " + this.symbolType + " >";
    }
}

/**
 * ScopeTable class representing a single scope in the symbol table
 */
class ScopeTable {
    private SymbolInfo[] hashTable;
    private ScopeTable parentScope;
    private int numBuckets;
    private String scopeId;
    private PrintWriter out;

    // Constructor
    public ScopeTable(int numBuckets, String scopeId, PrintWriter outputStream) {
        this.numBuckets = numBuckets;
        this.scopeId = scopeId;
        this.parentScope = null;
        this.out = outputStream;

        hashTable = new SymbolInfo[numBuckets];
        for (int i = 0; i < numBuckets; i++) {
            hashTable[i] = null;
        }
    }

    // Constructor with System.out as default output
    public ScopeTable(int numBuckets, String scopeId) {
        this(numBuckets, scopeId, new PrintWriter(System.out, true));
    }

    // Getters and setters
    public ScopeTable getParentScope() {
        return this.parentScope;
    }

    public void setParentScope(ScopeTable s) {
        this.parentScope = s;
    }

    // Insert symbol into the scope table
    public boolean insertSymbol(SymbolInfo s) {
        SymbolInfo found = this.lookUp(s, false);
        if (found != null) {
            return false;
        }

        int hash = HashFunction.sdbmHash(s.getName());
        int index = hash % numBuckets;
        // int chainPosition = 0;

        if (hashTable[index] == null) {
            hashTable[index] = new SymbolInfo(s);
        } else {
            // chainPosition += 1;
            SymbolInfo temp = hashTable[index];
            while (temp.next != null) {
                temp = temp.next;
                // chainPosition += 1;
            }
            temp.next = new SymbolInfo(s);
        }
        return true;
    }

    // Look up symbol in the scope table
    public SymbolInfo lookUp(SymbolInfo s, boolean showOutput) {
        String name = s.getName();
        int hash = HashFunction.sdbmHash(s.getName());
        int index = hash % numBuckets;
        // int chainPosition = 0;
        SymbolInfo temp = hashTable[index];
        
        while (temp != null) {
            // chainPosition++;
            if (temp.getName().equals(name)) {
                return temp;
            }
            temp = temp.next;
        }
        return null;
    }

    // Look up symbol and return position information
    public SymbolInfo lookUp(SymbolInfo s, int[] foundIndex, int[] chainPos, boolean showOutput) {
        String name = s.getName();
        int hash = HashFunction.sdbmHash(s.getName());
        int index = hash % numBuckets;
        // int chainPosition = 0;
        SymbolInfo temp = hashTable[index];
        
        while (temp != null) {
            // chainPosition++;
            if (temp.getName().equals(name)) {
                foundIndex[0] = index;
                // chainPos[0] = chainPosition;
                return temp;
            }
            temp = temp.next;
        }
        foundIndex[0] = -1;
        chainPos[0] = -1;
        return null;
    }

    // Delete symbol from the scope table
    public boolean deleteSymbol(SymbolInfo s) {
        int hash = HashFunction.sdbmHash(s.getName());
        int index = hash % numBuckets;
        
        if (hashTable[index] == null) {
            return false; // symbol not found
        }

        SymbolInfo head = hashTable[index];
        // int chainPosition = 0;
        
        // if found at head, delete
        if (head.getName().equals(s.getName())) {
            hashTable[index] = head.next; // delete the head node
            return true; // symbol deleted successfully
        }
        // else traverse the chain
        else {
            while (head.next != null) {
                SymbolInfo prev = head;
                head = head.next;
                // chainPosition++;
                if (head.getName().equals(s.getName())) {
                    prev.next = head.next;
                    return true; // symbol deleted successfully
                }
            }
            // if at the last elem
            if (head != null && head.getName().equals(s.getName())) {
                return true; // symbol deleted successfully
            }
        }
        return false; // symbol not found
    }

    // Print the scope table
    public void print(int indentLevel) {
        StringBuilder indent = new StringBuilder();
        for (int i = 0; i < indentLevel; i++) {
            indent.append('\t');
        }
        
        out.println(indent + "ScopeTable # " + scopeId);
        for (int i = 0; i < numBuckets; i++) {
            SymbolInfo temp = hashTable[i];
            if (temp != null) {
                out.print(indent);
                out.print(i + " --> ");
                while (temp != null) {
                    out.print("< " + temp.getName() + " : " + temp.getType() + " >");
                    temp = temp.next;
                }
                out.println();
            }
        }
    }

    public void print() {
        print(0);
    }
}

/**
 * SymbolTable class managing multiple scopes
 */
class SymbolTable {
    private ScopeTable currentScope;
    private int numBuckets;
    private int numberOfScopes;
    private String currentScopeId;
    private PrintWriter out;

    private String getScopeId() {
        StringBuilder sId = new StringBuilder("1");
        int nScopes = this.numberOfScopes;
        while (nScopes > 1) {
            sId.append(".1");
            nScopes -= 1;
        }
        return sId.toString();
    }

    // Create the root scope
    public SymbolTable(int numBuckets, PrintWriter outputStream) {
        this.numBuckets = numBuckets;
        this.numberOfScopes = 1;
        this.out = outputStream;
        this.currentScope = new ScopeTable(this.numBuckets, "1", this.out);
        this.currentScopeId = "1";
        this.currentScope.setParentScope(null);
    }

    // Constructor with System.out as default output
    public SymbolTable(int numBuckets) {
        this(numBuckets, new PrintWriter(System.out, true));
    }

    public String getCurrentScopeId() {
        return this.currentScopeId;
    }

    public void enterScope() {
        this.numberOfScopes += 1;
        String sId = this.getScopeId();
        this.currentScopeId = sId;
        ScopeTable newScopeTable = new ScopeTable(this.numBuckets, sId, this.out);
        newScopeTable.setParentScope(this.currentScope);
        this.currentScope = newScopeTable;
    }

    public void exitScope() {
        if (this.currentScope.getParentScope() != null) {
            ScopeTable parentScope = this.currentScope.getParentScope();
            this.currentScope = parentScope;
            this.numberOfScopes -= 1;
            String sId = this.getScopeId();
            this.currentScopeId = sId;
        } else {
            out.println("ERROR EXITING SCOPE : No scope to exit");
        }
    }

    public boolean insertSymbol(SymbolInfo s) {
        return this.currentScope.insertSymbol(s);
    }

    public boolean removeSymbol(SymbolInfo s) {
        return this.currentScope.deleteSymbol(s);
    }

    public SymbolInfo lookUp(SymbolInfo s, int[] index, int[] chainPos) {
        SymbolInfo found = null;
        ScopeTable currScope = this.currentScope;
        while (currScope != null) {
            found = currScope.lookUp(s, index, chainPos, false);
            if (found != null) {
                return found;
            }
            currScope = currScope.getParentScope();
        }
        return null;
    }

    public SymbolInfo currentScopeLookup(SymbolInfo s, int[] index, int[] chainPos) {
        ScopeTable currScope = this.currentScope;
        SymbolInfo found = null;
        found = currScope.lookUp(s, index, chainPos, false);
        if (found != null) {
            return found;
        }
        return null;
    }

    public void printCurrentScopeTable() {
        this.currentScope.print();
    }

    public void printAllScopeTable() {
        ScopeTable currScope = this.currentScope;
        int indentLevel = 0;
        while (currScope != null) {
            currScope.print(indentLevel);
            currScope = currScope.getParentScope();
        }
        this.out.println();
    }

    public void close() {
        if (out != null) {
            out.close();
        }
    }
}

/**
 * Main class to test the SymbolTable implementation
 */
public class symbolTable {
    public static void main(String[] args) {
        try {
            FileWriter fileWriter = new FileWriter("output.txt");
            PrintWriter out = new PrintWriter(fileWriter);
            
            SymbolTable st = new SymbolTable(7, out);
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
