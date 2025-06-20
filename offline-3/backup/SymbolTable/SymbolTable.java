package SymbolTable;

import java.io.*;
// import java.util.*;

/**
 * SymbolTable class managing multiple scopes
 */
public class SymbolTable {
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

