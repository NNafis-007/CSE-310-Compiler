package SymbolTable;

import java.io.PrintWriter;

public class ScopeTable {
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
