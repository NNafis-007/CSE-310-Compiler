// import java.io.*;
// import java.util.*;

// import SymbolTable.SymbolInfo;
// import SymbolTable.SymbolTable;

class RuleReturnInfo{
    public int lineNo;
    public String matchedText;
    public RuleReturnInfo(){
        lineNo = 0;
        matchedText = "";
    }

    // Copy constructor
    public RuleReturnInfo(RuleReturnInfo other) {
        this.lineNo = other.lineNo;
        this.matchedText = other.matchedText;
    }
}

public class test {
    public static void main(String[] args) {
        try {
            RuleReturnInfo rri = new RuleReturnInfo();
            rri.lineNo = 1;
            rri.matchedText = "SHEI";
            System.out.println("(OG) Line " + rri.lineNo + " : " + rri.matchedText);
            
            RuleReturnInfo copied = new RuleReturnInfo(rri);
            System.out.println("(copy) Line " + copied.lineNo + " : " + copied.matchedText);

        } catch (Exception e) {
            System.err.println("Error opening file: " + e.getMessage());
        }
    }
}
