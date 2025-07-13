package SymbolTable;

public class SymbolInfo {
    private String name;
    private String symbolType;
    private int stackOffest;
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

    public int getOffset(){
        return stackOffest;
    }

    public String getType() {
        return symbolType;
    }

    public void setOffset(int offset) {
        this.stackOffest = offset;
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
