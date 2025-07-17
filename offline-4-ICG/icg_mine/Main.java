import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import SymbolTable.SymbolTable;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static BufferedWriter parserLogFile;
    public static BufferedWriter errorFile;
    public static BufferedWriter lexLogFile;
    public static BufferedWriter ICGFile;
    public static BufferedWriter tempFile;

    public static SymbolTable st;

    public static int syntaxErrorCount = 0;

    // Helper method to find the next line that contains an actual instruction
    private static int findNextNonEmptyLine(List<String> lines, int startIndex) {
        for (int i = startIndex; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (!line.isEmpty() && !line.startsWith(";")) { // Not empty and not just a comment
                return i;
            }
        }
        return -1; // No non-empty line found
    }

    /**
     * Performs all optimizations in place using minimal file I/O operations
     * Uses only one temporary file to reduce disk operations
     */
    public static void performAllOptimizations(String inputFile, String outputFile, String tempFile) throws IOException {
        System.out.println("------- Optimizing ---------");
        
        // Read the input file once into memory
        List<String> lines = readFileToList(inputFile);
        
        // Apply all optimizations in sequence on the in-memory data
        lines = optimizePushPop(lines);
        lines = optimizeOps(lines);
        lines = optimizeMov(lines);
        lines = optimizeLabels(lines);
        
        // Write optimized content to output file
        writeListToFile(lines, outputFile);
        
        System.out.println("Comprehensive optimization completed.");
    }
    
    private static List<String> readFileToList(String filename) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }
    
    private static void writeListToFile(List<String> lines, String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (String line : lines) {
                writer.write(line + "\n");
            }
        }
    }
    
    private static List<String> optimizePushPop(List<String> lines) {
        System.out.println("Optimizing PUSH POP instructions in memory...");
        List<String> optimizedLines = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            String currentLine = lines.get(i).trim();
            boolean isRedundant = false;

            if (i < lines.size() - 1 && currentLine.startsWith("PUSH ")) {
                String nextLine = lines.get(i + 1).trim();

                if (nextLine.startsWith("POP ")) {
                    // Extract operands
                    int pushCmntIdx = currentLine.indexOf(';');
                    int nextCmntIdx = nextLine.indexOf(';');
                    if (pushCmntIdx == -1)
                        pushCmntIdx = currentLine.length();
                    if (nextCmntIdx == -1)
                        nextCmntIdx = nextLine.length();

                    String pushOperand = currentLine.substring(5, pushCmntIdx).trim();
                    String popOperand = nextLine.substring(4, nextCmntIdx).trim();

                    // Check if PUSH and POP operate on same register/address
                    if (pushOperand.equals(popOperand)) {
                        isRedundant = true;
                        i++; // Skip the next line as well
                    }
                }
            }

            if (!isRedundant) {
                optimizedLines.add(lines.get(i));
            }
        }
        
        System.out.println("**** DONE Optimizing PUSH POP instructions in memory...");
        return optimizedLines;
    }
    
    private static List<String> optimizeOps(List<String> lines) {
        System.out.println("Optimizing redundant operations in memory...");
        List<String> optimizedLines = new ArrayList<>();
        boolean[] skipLines = new boolean[lines.size()]; // Track which lines to skip

        for (int i = 0; i < lines.size(); i++) {
            String currentLine = lines.get(i).trim();

            // Check for ADD, SUB, and MUL instructions
            if (currentLine.startsWith("ADD ") || currentLine.startsWith("SUB ") || currentLine.startsWith("MUL ")) {
                // Extract operands using same method as other functions
                int cmntIdx = currentLine.indexOf(';');
                if (cmntIdx == -1)
                    cmntIdx = currentLine.length();

                String instructionPart = currentLine.substring(0, cmntIdx).trim();
                String opcode = instructionPart.substring(0, 4).trim(); // ADD, SUB, MUL are 3 chars

                // Determine how many instructions to look back
                int lookBackCount = 0;
                String targetValue = "";

                if (opcode.equals("ADD")) {
                    lookBackCount = 2;
                    targetValue = "0";
                } else if (opcode.equals("SUB")) {
                    lookBackCount = 3;
                    targetValue = "0";
                } else if (opcode.trim().equals("MUL")) {
                    lookBackCount = 3;
                    targetValue = "1";
                }

                // Look backwards for the pattern
                if (i >= lookBackCount) {
                    String targetLine = lines.get(i - lookBackCount).trim();
                    boolean shouldOptimize = false;

                    // Check for MOV AX, targetValue pattern
                    int targetCmntIdx = targetLine.indexOf(';');
                    if (targetCmntIdx == -1)
                        targetCmntIdx = targetLine.length();

                    if (targetLine.startsWith("MOV ")) {
                        String[] targetParts = targetLine.substring(4, targetCmntIdx).split(",");

                        if (targetParts.length == 2) {
                            String targetSrc = targetParts[0].trim();
                            String targetDest = targetParts[1].trim();
                            if (targetSrc.equals("AX") && targetDest.equals(targetValue)) {
                                shouldOptimize = true;
                            }
                        }
                    }

                    if (shouldOptimize) {
                        // PUSH AX is always right before MOV AX, targetValue
                        int pushIndex = i - lookBackCount - 1;

                        System.out.println("Optimizing redundant " + opcode + " operation at line " + (i + 1));
                        System.out.println("Removing instructions from PUSH AX at line " + (pushIndex + 1) + " to "
                                + opcode + " at line " + (i + 1));

                        // Mark all lines from PUSH AX to current instruction for skipping
                        for (int k = pushIndex; k <= i; k++) {
                            skipLines[k] = true;
                        }
                    }
                }
            }
        }

        // Add lines that are not marked for skipping
        for (int i = 0; i < lines.size(); i++) {
            if (!skipLines[i]) {
                optimizedLines.add(lines.get(i));
            }
        }

        System.out.println("---- DONE Optimizing redundant operations in memory...");
        return optimizedLines;
    }
    
    private static List<String> optimizeMov(List<String> lines) {
        System.out.println("Optimizing MOV instructions in memory...");
        List<String> optimizedLines = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            String currentLine = lines.get(i).trim();
            boolean isRedundant = false;

            if (currentLine.startsWith("MOV ")) {
                int nextIndex = findNextNonEmptyLine(lines, i + 1);
                if (nextIndex != -1) {
                    String nextLine = lines.get(nextIndex).trim();

                    if (nextLine.startsWith("MOV ")) {
                        // Extract operands
                        int currCmntIdx = currentLine.indexOf(';');
                        int nextCmntIdx = nextLine.indexOf(';');
                        if (currCmntIdx == -1)
                            currCmntIdx = currentLine.length();
                        if (nextCmntIdx == -1)
                            nextCmntIdx = nextLine.length();

                        // Extract operands from MOV instructions
                        String[] currentParts = currentLine.substring(4, currCmntIdx).split(",");
                        String[] nextParts = nextLine.substring(4, nextCmntIdx).split(",");

                        if (currentParts.length == 2 && nextParts.length == 2) {
                            String src1 = currentParts[0].trim();
                            String dest1 = currentParts[1].trim();
                            String src2 = nextParts[0].trim();
                            String dest2 = nextParts[1].trim();

                            // Check if second MOV reverses the first one OR if they are identical
                            if ((src1.equals(dest2) && dest1.equals(src2)) || (src1.equals(src2) && dest1.equals(dest2))) {
                                isRedundant = true;

                                // Add the FIRST MOV instruction only
                                optimizedLines.add(lines.get(i));

                                // Add any lines between the two MOV instructions (comments, empty lines, etc.)
                                for (int j = i + 1; j < nextIndex; j++) {
                                    optimizedLines.add(lines.get(j));
                                }

                                // Skip to after the redundant second MOV instruction
                                i = nextIndex;
                            }
                        }
                    }
                }
            }

            if (!isRedundant) {
                optimizedLines.add(lines.get(i));
            }
        }

        System.out.println("---- DONE Optimizing MOV instructions in memory...");
        return optimizedLines;
    }
    
    private static List<String> optimizeLabels(List<String> lines) {
        System.out.println("Optimizing redundant labels in memory...");
        List<String> optimizedLines = new ArrayList<>();
        Map<String, String> labelReplacements = new HashMap<>(); // Map of removed labels -> replacement label
        boolean[] skipLines = new boolean[lines.size()]; // Track which lines to skip

        // Step 1: Find consecutive labels and mark redundant ones for removal
        for (int i = 0; i < lines.size(); i++) {
            String currentLine = lines.get(i).trim();
            
            // Check if current line is a label (ends with :)
            if (currentLine.endsWith(":") && !currentLine.startsWith(";")) {
                List<String> consecutiveLabels = new ArrayList<>();
                consecutiveLabels.add(currentLine);
                
                // Find all consecutive labels
                int j = i + 1;
                while (j < lines.size()) {
                    String nextLine = lines.get(j).trim();
                    if (nextLine.endsWith(":") && !nextLine.startsWith(";")) {
                        consecutiveLabels.add(nextLine);
                        j++;
                    } else if (nextLine.isEmpty() || nextLine.startsWith(";")) {
                        // Skip empty lines and comments between labels
                        j++;
                    } else {
                        // Hit a non-label instruction, stop
                        break;
                    }
                }
                
                // If we have more than one consecutive label, optimize
                if (consecutiveLabels.size() > 1) {
                    String keepLabel = consecutiveLabels.get(consecutiveLabels.size() - 1); // Keep the last label
                    
                    // Mark redundant labels for removal and store replacements
                    for (int k = 0; k < consecutiveLabels.size() - 1; k++) {
                        String removeLabel = consecutiveLabels.get(k);
                        labelReplacements.put(removeLabel, keepLabel);
                        
                        // Find the line index of this label and mark for skipping
                        for (int lineIdx = i; lineIdx < j; lineIdx++) {
                            if (lines.get(lineIdx).trim().equals(removeLabel)) {
                                skipLines[lineIdx] = true;
                                break;
                            }
                        }
                    }
                }
                
                // Skip to after all consecutive labels
                i = j - 1;
            }
        }

        // Step 2: Build optimized lines (without redundant labels)
        for (int i = 0; i < lines.size(); i++) {
            if (!skipLines[i]) {
                optimizedLines.add(lines.get(i));
            }
        }

        // Step 3: Replace label references in the code
        for (int i = 0; i < optimizedLines.size(); i++) {
            String currentLine = optimizedLines.get(i);
            String modifiedLine = currentLine;
            
            // Check for label references in jump instructions
            for (Map.Entry<String, String> entry : labelReplacements.entrySet()) {
                String oldLabel = entry.getKey().replace(":", ""); // Remove colon for comparison
                String newLabel = entry.getValue().replace(":", ""); // Remove colon for comparison
                
                // Replace in various jump instructions
                if (modifiedLine.contains("JMP " + oldLabel) || 
                    modifiedLine.contains("JE " + oldLabel) || 
                    modifiedLine.contains("JNE " + oldLabel) ||
                    modifiedLine.contains("JL " + oldLabel) ||
                    modifiedLine.contains("JLE " + oldLabel) ||
                    modifiedLine.contains("JG " + oldLabel) ||
                    modifiedLine.contains("JGE " + oldLabel) ||
                    modifiedLine.contains("JZ " + oldLabel) ||
                    modifiedLine.contains("JNZ " + oldLabel) ||
                    modifiedLine.contains("CALL " + oldLabel)) {
                    
                    modifiedLine = modifiedLine.replace(" " + oldLabel, " " + newLabel);
                    System.out.println("Replaced reference: " + oldLabel + " -> " + newLabel + " in line: " + currentLine.trim());
                }
            }
            
            optimizedLines.set(i, modifiedLine);
        }

        System.out.println("---- DONE Optimizing redundant labels in memory...");
        return optimizedLines;
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.err.println("Usage: java Main <input_file>");
            return;
        }

        File inputFile = new File(args[0]);
        if (!inputFile.exists()) {
            System.err.println("Error opening input file: " + args[0]);
            return;
        }

        String outputDirectory = "output/";
        String parserLogFileName = outputDirectory + "parserLog.txt";
        String errorFileName = outputDirectory + "errorLog.txt";
        String lexLogFileName = outputDirectory + "lexerLog.txt";
        String ICGFileName = "code_asm.txt";
        String tempFileName = "temp.txt";

        new File(outputDirectory).mkdirs();

        parserLogFile = new BufferedWriter(new FileWriter(parserLogFileName));
        errorFile = new BufferedWriter(new FileWriter(errorFileName));
        lexLogFile = new BufferedWriter(new FileWriter(lexLogFileName));
        ICGFile = new BufferedWriter(new FileWriter(ICGFileName));
        tempFile = new BufferedWriter(new FileWriter(tempFileName));

        st = new SymbolTable(7, parserLogFile);

        // write boilerplate code to the ICG file
        ICGFile.write(".MODEL SMALL\n");
        ICGFile.write(".STACK 1000H\n");
        ICGFile.write(".DATA\n");
        ICGFile.write("\tnumber DB \"00000$\"\n");
        ICGFile.flush();

        // Create lexer and parser
        CharStream input = CharStreams.fromFileName(args[0]);
        C2105007Lexer lexer = new C2105007Lexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        C2105007Parser parser = new C2105007Parser(tokens);

        // Remove default error listener
        parser.removeErrorListeners();

        // Begin parsing
        ParseTree tree = parser.start();
        // parserLogFile.write("Parse tree: " + tree.toStringTree(parser) + "\n");

        ICGFile.write(".CODE\n");

        // merge tempFile into ICGFile
        BufferedReader tempReader = new BufferedReader(new FileReader(tempFileName));
        String line;
        while ((line = tempReader.readLine()) != null) {
            ICGFile.write(line + "\n");
        }
        ICGFile.flush();
        tempReader.close();

        // Perform all optimizations in place with minimal file I/O
        performAllOptimizations(ICGFileName, "optCode.asm", tempFileName);


        // merge printProcLib into both ICG files
        BufferedReader printLibReader = new BufferedReader(new FileReader("printProc.lib"));
        BufferedWriter optICGFile = new BufferedWriter(new FileWriter("optCode.asm", true)); // Append
        while ((line = printLibReader.readLine()) != null) {
            ICGFile.write(line + "\n");
            optICGFile.write(line + "\n");
        }
        ICGFile.write("END main\n");
        optICGFile.write("END main\n");
        ICGFile.flush();
        optICGFile.flush();

        // Close files
        printLibReader.close();
        parserLogFile.close();
        errorFile.close();
        lexLogFile.close();
        ICGFile.close();
        optICGFile.close();

        System.out.println("Parsing completed. Check the output files for details.");
    }
}
