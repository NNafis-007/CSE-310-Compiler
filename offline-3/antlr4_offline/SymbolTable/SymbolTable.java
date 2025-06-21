package SymbolTable;

import java.io.*;
import java.util.*;

/**
 * SymbolTable class managing multiple scopes
 */
public class SymbolTable {
    private ScopeTable currentScope;
    private int numBuckets;
    private String currentScopeId;
    private BufferedWriter out;
    
    // Map to track the next child number for each scope
    private Map<String, Integer> scopeChildCounters;

    private String generateNextScopeId(String parentScopeId) {
        if (parentScopeId == null) {
            // This is the global scope
            return "1";
        }
        
        // Get the next child number for this parent scope
        int nextChildNumber = scopeChildCounters.getOrDefault(parentScopeId, 0) + 1;
        scopeChildCounters.put(parentScopeId, nextChildNumber);
        
        // Generate the new scope ID
        return parentScopeId + "." + nextChildNumber;
    }

    // Create the root scope
    public SymbolTable(int numBuckets, BufferedWriter outputStream) {
        this.numBuckets = numBuckets;
        this.out = outputStream;
        this.scopeChildCounters = new HashMap<>();
        this.currentScopeId = generateNextScopeId(null); //generate "1" for global scope
        this.currentScope = new ScopeTable(this.numBuckets, this.currentScopeId, this.out);
        this.currentScope.setParentScope(null);
    }

    // Constructor with System.out as default output
    public SymbolTable(int numBuckets) {
        this(numBuckets, new BufferedWriter(new OutputStreamWriter(System.out)));
    }

    public String getCurrentScopeId() {
        return this.currentScopeId;
    }

    public void enterScope() {
        String newScopeId = this.generateNextScopeId(this.currentScopeId);
        this.currentScopeId = newScopeId;
        ScopeTable newScopeTable = new ScopeTable(this.numBuckets, newScopeId, this.out);
        newScopeTable.setParentScope(this.currentScope);
        this.currentScope = newScopeTable;
    }

    public void exitScope() {
        if (this.currentScope.getParentScope() != null) {
            ScopeTable parentScope = this.currentScope.getParentScope();
            this.currentScope = parentScope;
            this.currentScopeId = parentScope.getScopeId();
        } else {
            try {
                out.write("ERROR EXITING SCOPE : No scope to exit\n");
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
            System.out.println();
        }
        try {
            this.out.write("\n");
            this.out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

