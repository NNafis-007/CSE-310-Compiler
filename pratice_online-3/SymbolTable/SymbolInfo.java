package SymbolTable;

//imports for java Arraylist
import java.util.ArrayList;
public class SymbolInfo {
    private String name;
    private String symbolType;
    private String d_type; // Data type, if applicable
    private ArrayList<String> parameters; // Parameters for functions
    private boolean isFunction; // Flag to indicate if this symbol is a function
    public int offset;
    
    public SymbolInfo next;


    // // Constructor without next parameter
    // public SymbolInfo(String name, String type) {
    //     this.name = name;
    //     this.symbolType = type;
    //     this.d_type = null; // Default data type is null
    //     this.parameters = new ArrayList<>(); // Initialize parameters list
    //     this.next = null; // Default next is null
    //     this.isFunction = false; // Default isFunction is false
    // }

    // Constructor with parameters
    // public SymbolInfo(String name, String type, SymbolInfo next) {
    //     this.name = name;
    //     this.symbolType = type;
    //     this.next = next;
    // }

    // Constructor with name, type, and data type
    // public SymbolInfo(String name, String type, String d_type) {
    //     this.name = name;
    //     this.symbolType = type;
    //     this.d_type = d_type;
    //     this.parameters = new ArrayList<>(); // Initialize parameters list
    //     this.next = null;
    // }

    // Constructor with name, type, data type, and parameters
    public SymbolInfo(String name, String type, String d_type, ArrayList<String> parameters) {
        this.name = name;
        this.symbolType = type;
        this.d_type = d_type;
        this.parameters = parameters != null ? new ArrayList<>(parameters) : new ArrayList<>();
        this.isFunction = false; // Default isFunction is false
        this.next = null; // Default next is null
        this.offset = 0; // Default offset is 0
        System.out.println("balsal");
    }

    // Copy constructor
    public SymbolInfo(SymbolInfo other) {
        this.name = other.name;
        this.symbolType = other.symbolType;
        this.d_type = other.d_type; // Copy data type
        this.parameters = new ArrayList<>(other.parameters); // Deep copy of parameters
        this.isFunction = other.isFunction; // Copy function flag
        this.next = null; // Deep copy does not copy the linked list structure
        this.offset = other.offset; // Copy offset
        System.out.println("balsal2");

    }

    // Default constructor
    public SymbolInfo() {
        this.name = "";
        this.symbolType = "";
        this.d_type = ""; // Default data type is ""
        this.parameters = new ArrayList<>(); // Initialize parameters list
        this.isFunction = false; // Default isFunction is false
        this.next = null;
        System.out.println("balsal3");

    }

    // Getters and setters
    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getOffset() {
        return this.offset;
    }

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
    public String getDataType() {
        return d_type;
    }
    public void setDataType(String d_type) {
        this.d_type = d_type;
    }

    public void setIsFunction(boolean isFunction) {
        this.isFunction = isFunction;
    }

    public boolean getIsFunction() {
        return isFunction;
    }

    public ArrayList<String> getParameters() {
        return parameters;
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
        return "< " + this.name + " : " + this.symbolType + ", Offset: " + this.offset + " >";
    }
}
