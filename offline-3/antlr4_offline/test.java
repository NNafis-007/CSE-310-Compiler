// import java.io.*;
import java.util.*;

// import SymbolTable.SymbolInfo;
import SymbolTable.HashFunction;

public class test {

    public static String typeDetector(String s) {
        s = s.trim();
        
        // Check if it's a logical expression first (logical expressions always return int)
        if (isLogicalExpression(s)) {
            return "int";
        }
        
        // Base case: single literal int
        try {
            Integer.parseInt(s);
            return "int";
        } catch (Exception ignored) {}

        // Base case: single literal float
        try {
            Float.parseFloat(s);
            return "float";
        } catch (Exception ignored) {}

        // Remove outermost parentheses for cleaner parsing
        while (s.startsWith("(") && s.endsWith(")")) {
            s = s.substring(1, s.length() - 1).trim();
        }

        // Split expression by arithmetic operations (+, -, *, /, %)
        // Use a more robust splitting approach
        List<String> expressions = splitByArithmeticOperators(s);
        
        boolean hasFloat = false;
        boolean hasUnknown = false;
        
        // Evaluate the type of each expression
        for (String expr : expressions) {
            System.out.println("Individual Expression: " + expr);
            String exprType = evaluateExpressionType(expr.trim());
            
            if (exprType.equals("unknown")) {
                hasUnknown = true;
            } else if (exprType.equals("float")) {
                hasFloat = true;
            }
            // int type doesn't change any flags
        }
        
        // If there's no unknown expression type, check priority: float > int
        if (!hasUnknown) {
            return hasFloat ? "float" : "int";
        }
        
        return "unknown";
    }
    
    // Helper method to split expression by arithmetic operators
    private static List<String> splitByArithmeticOperators(String s) {
        List<String> expressions = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        int parenCount = 0;
        
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            
            if (c == '(') {
                parenCount++;
                current.append(c);
            } else if (c == ')') {
                parenCount--;
                current.append(c);
            } else if ((c == '+' || c == '-' || c == '*' || c == '/' || c == '%') && parenCount == 0) {
                // Found an operator at top level (not inside parentheses)
                if (current.length() > 0) {
                    expressions.add(current.toString().trim());
                    current = new StringBuilder();
                }
                // Skip the operator itself
            } else {
                current.append(c);
            }
        }
        
        // Add the last expression
        if (current.length() > 0) {
            expressions.add(current.toString().trim());
        }
        
        return expressions;
    }
    
    // Helper method to evaluate the type of a single expression
    private static String evaluateExpressionType(String expr) {
        if (expr.isEmpty()) {
            return "unknown";
        }
        
        // Remove outer parentheses if present
        while (expr.startsWith("(") && expr.endsWith(")")) {
            expr = expr.substring(1, expr.length() - 1).trim();
        }
        
        // Check if it's a logical expression (logical expressions always return int)
        if (isLogicalExpression(expr)) {
            return "int";
        }
        
        // Check if it's an integer literal
        try {
            Integer.parseInt(expr);
            return "int";
        } catch (NumberFormatException ignored) {}
        
        // Check if it's a float literal
        try {
            Float.parseFloat(expr);
            return "float";
        } catch (NumberFormatException ignored) {}
        
        // Check if it's a function call
        if (expr.contains("(") && expr.contains(")")) {
            // For now, assume function calls return int
            // This should be replaced with actual function type lookup
            return "int";
        }
        
        // Check if it's a variable (identifier)
        if (expr.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
            // This should be replaced with actual symbol table lookup
            // For now, return unknown for variables
            return "unknown";
        }
        
        // If it contains arithmetic operators, recursively evaluate
        if (expr.matches(".*[+\\-*/%].*")) {
            return typeDetector(expr);
        }
        
        return "unknown";
    }
    
    // Helper method to check if an expression is a logical expression
    private static boolean isLogicalExpression(String expr) {
        expr = expr.trim();
        
        // Check for logical operators: &&, ||, !, ==, !=, <, >, <=, >=
        if (expr.contains("&&") || expr.contains("||") || 
            expr.contains("==") || expr.contains("!=") || 
            expr.contains("<=") || expr.contains(">=") || 
            expr.contains("<") || expr.contains(">")) {
            return true;
        }
        
        // Check for negation operator at the beginning
        if (expr.startsWith("!")) {
            return true;
        }
        
        // Check for boolean literals
        if (expr.equals("true") || expr.equals("false")) {
            return true;
        }
        
        return false;
    }

    public static void main(String[] args) {
        try {
            // Test various expressions
            String[] testExpressions = {
                "var(1,2*3)+3.5*2",
                "2*3+(5%3 < 4 && 8) || 2"
            };
            
            for (String expr : testExpressions) {
                String type = typeDetector(expr);
                System.out.println("Type of '" + expr + "': " + type);
                System.out.println("---");
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
