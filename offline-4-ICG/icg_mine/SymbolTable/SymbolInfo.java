package SymbolTable;

//imports for java Arraylist
import java.util.ArrayList;
public class SymbolInfo {
    private String name;
    private String symbolScope;
    private String d_type; // Data type, if applicable
    private ArrayList<String> parameters; // Parameters for functions
    private boolean isFunction; // Flag to indicate if this symbol is a function
    public int offset;
    
    public SymbolInfo next;

    // Constructor with name, scope, data type, and parameters
    public SymbolInfo(String name, String scope, String d_type, ArrayList<String> parameters) {
        this.name = name;
        this.symbolScope = scope;
        this.d_type = d_type;
        this.parameters = parameters != null ? new ArrayList<>(parameters) : new ArrayList<>();
        this.isFunction = false; // Default isFunction is false
        this.next = null; // Default next is null
        this.offset = 0; // Default offset is 0
    }

    // Copy constructor
    public SymbolInfo(SymbolInfo other) {
        this.name = other.name;
        this.symbolScope = other.symbolScope;
        this.d_type = other.d_type; // Copy data type
        this.parameters = new ArrayList<>(other.parameters); // Deep copy of parameters
        this.isFunction = other.isFunction; // Copy function flag
        this.next = null; // Deep copy does not copy the linked list structure
        this.offset = other.offset; // Copy offset
    }

    // Default constructor
    public SymbolInfo() {
        this.name = "";
        this.symbolScope = "";
        this.d_type = ""; // Default data type is ""
        this.parameters = new ArrayList<>(); // Initialize parameters list
        this.isFunction = false; // Default isFunction is false
        this.next = null;
    }

    // Getters and setters
    public void setOffset(int offset) {
        this.offset = offset;
        // System.out.println("Set offset for symbol: " + this.showSymbol());
    }

    public int getOffset() {
        return this.offset;
    }

    public String getName() {
        return this.name;
    }

    public String getScope() {
        return this.symbolScope;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScope(String scope) {
        this.symbolScope = scope;
    }
    public String getDataType() {
        return this.d_type;
    }
    public void setDataType(String d_type) {
        this.d_type = d_type;
    }

    public void setIsFunction(boolean isFunction) {
        this.isFunction = isFunction;
    }

    public boolean getIsFunction() {
        return this.isFunction;
    }

    public ArrayList<String> getParameters() {
        return this.parameters;
    }
    public void setParameters(ArrayList<String> parameters) {
        if (parameters != null) {
            this.parameters = new ArrayList<>(parameters);
        } else {
            this.parameters = new ArrayList<>();
        }
    }
    public void addParameter(String parameter) {
        if (parameter != null && !parameter.isEmpty()) {
            this.parameters.add(parameter);
        }
    }
    public void removeParameter(String parameter) {
        this.parameters.remove(parameter);
    }
    public void clearParameters() {
        this.parameters.clear();
    }

    public boolean isParameterListEmpty() {
        return this.parameters.isEmpty();
    }

    public boolean isFunction() {
        return isFunction;
    }

    public String showSymbol() {
        return "< " + this.name + " : " + this.symbolScope + ", Offset: " + this.offset + " >";
    }
}
