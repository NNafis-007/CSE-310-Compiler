# ANTLR4 C-like Language Grammar

This directory contains a complete C-like language grammar for testing ANTLR4 setup.

## Files
- `Expr.g4` - The complete grammar file containing lexer and parser rules
- `test_simple.txt` - Simple program with variables and arithmetic
- `test_function.txt` - Program with function definition and recursion
- `test_declarations.txt` - Various declarations and function prototypes
- `README.md` - This file

## Grammar Features

This grammar supports:
- **Data Types**: `int`, `float`, `void`
- **Variables**: Simple variables and arrays
- **Functions**: Declarations, definitions, parameters, return values
- **Control Flow**: `if-else`, `while`, `for` loops
- **Operators**: Arithmetic (`+`, `-`, `*`, `/`, `%`), relational (`<`, `<=`, `>`, `>=`, `==`, `!=`), logical (`&&`, `||`), assignment (`=`), increment/decrement (`++`, `--`)
- **Expressions**: Complex expressions with proper precedence
- **Comments**: Single-line (`//`) and multi-line (`/* */`)
- **Built-ins**: `println()` for output, `return` statements

## Testing Instructions

1. **Generate the lexer and parser classes:**
   ```bash
   antlr4 Expr.g4
   ```

2. **Compile the generated Java files:**
   ```bash
   javac *.java
   ```

3. **Test the lexer (tokenization):**
   ```bash
   grun Expr start -tokens < test_simple.txt
   ```

4. **Test the parser (parse tree):**
   ```bash
   grun Expr start -tree < test_simple.txt
   ```

5. **Test with GUI (if available):**
   ```bash
   grun Expr start -gui < test_simple.txt
   ```

6. **Interactive testing:**
   ```bash
   grun Expr start
   # Then type code and press Ctrl+D (Linux/Mac) or Ctrl+Z (Windows)
   ```

## Test Cases

### Simple Program (`test_simple.txt`)
```c
int main() {
    int x, y;
    x = 5;
    y = x + 3;
    println(y);
    return 0;
}
```

### Function with Recursion (`test_function.txt`)
```c
int factorial(int n) {
    if (n <= 1) {
        return 1;
    } else {
        return n * factorial(n - 1);
    }
}

int main() {
    int result;
    result = factorial(5);
    println(result);
    return 0;
}
```

### Declarations (`test_declarations.txt`)
```c
// Variable declarations
int x, y, z;
float a[10], b;

// Function declaration
int add(int a, int b);

// Function definition
int add(int a, int b) {
    return a + b;
}
```

## Expected Output

For `test_simple.txt`, you should see:
- **Tokens**: Keywords (int, return), identifiers (main, x, y), operators, literals, etc.
- **Parse Tree**: Proper hierarchical structure showing program → function → statements → expressions

## Grammar Structure

The grammar follows a typical C-like language structure:
- **Program**: Collection of declarations and function definitions
- **Declarations**: Variables and function prototypes
- **Statements**: Control flow, expressions, compound statements
- **Expressions**: Hierarchical with proper operator precedence
- **Lexical Elements**: Keywords, operators, identifiers, constants, comments

## Troubleshooting

If you encounter issues:
1. Make sure ANTLR4 is properly installed
2. Check that Java is available and configured
3. Verify the grammar file has no syntax errors
4. Test with simpler inputs first

## Quick Test Commands

```bash
# Generate and compile
antlr4 Expr.g4 && javac *.java

# Test all examples
grun Expr start -tree < test_simple.txt
grun Expr start -tree < test_function.txt
grun Expr start -tree < test_declarations.txt
```
