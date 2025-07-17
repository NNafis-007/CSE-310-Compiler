import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import SymbolTable.SymbolTable;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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

    public static void optimizeMov(String inFile, BufferedWriter writer) throws IOException {
        System.out.println("Optimizing MOV instructions...");
        BufferedReader reader = new BufferedReader(new FileReader(inFile));
        List<String> lines = new ArrayList<>();
        String line;

        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        reader.close();

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

                            // Check if second MOV reverses the first one
                            if (src1.equals(dest2) && dest1.equals(src2)) {
                                isRedundant = true;
                                System.out.println("Found redundant MOV: " + currentLine + " and " + nextLine);

                                // Add the FIRST MOV instruction only
                                optimizedLines.add(lines.get(i));
                                System.out.println("Adding line to optimized lines: " + lines.get(i));

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

        // Write optimized content back to file
        for (String optimizedLine : optimizedLines) {
            writer.write(optimizedLine + "\n");
        }
        System.out.println("---- DONE Optimizing MOV instructions...");
    }

    public static void optimizePushPop(String inFile, BufferedWriter writer) throws IOException {
        System.out.println("Optimizing PUSH POP instructions...");
        BufferedReader reader = new BufferedReader(new FileReader(inFile));
        List<String> lines = new ArrayList<>();
        String line;

        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        reader.close();

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

        // Write optimized content back to file
        for (String optimizedLine : optimizedLines) {
            writer.write(optimizedLine + "\n");
        }
        System.out.println("**** DONE Optimizing PUSH POP instructions...");

    }

    public static void optimizeRedundantOperations(String inFile, BufferedWriter writer) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(inFile));
        List<String> lines = new ArrayList<>();
        String line;

        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        reader.close();

        List<String> optimizedLines = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            String currentLine = lines.get(i).trim();
            boolean isRedundant = false;

            // Check for ADD, SUB, and MUL instructions
            if (currentLine.startsWith("ADD ") || currentLine.startsWith("SUB ") || currentLine.startsWith("MUL ")) {
                // Extract operands
                int cmntIdx = currentLine.indexOf(';');
                if (cmntIdx == -1)
                    cmntIdx = currentLine.length();

                String instructionPart = currentLine.substring(0, cmntIdx).trim();
                String opcode = instructionPart.substring(0, 4).trim();
                String operands = instructionPart.substring(4, cmntIdx).trim(); 
                String[] operandParts = operands.split(",");

                if (operandParts.length == 2) {
                    String operand1 = operandParts[0].trim();
                    String operand2 = operandParts[1].trim();

                    // Check for redundant operations
                    if ((opcode.equals("ADD") || opcode.equals("SUB")) && operand2.equals("0")) {
                        isRedundant = true;
                    } else if (opcode.equals("MUL") && operand2.equals("1")) {
                        isRedundant = true;
                    }
                }
            }

            if (!isRedundant) {
                optimizedLines.add(lines.get(i));
            }
        }

        // Write optimized content back to file
        for (String optimizedLine : optimizedLines) {
            writer.write(optimizedLine + "\n");
        }
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

        // optimize push/pop instructions
        BufferedWriter optICGFile = new BufferedWriter(new FileWriter("optCode.asm"));
        optimizePushPop(ICGFileName, optICGFile);
        optICGFile.close(); // Close after first optimization
        
        // optimize mov instructions - read from optCode.asm and write to a temp file
        BufferedWriter tempOptFile = new BufferedWriter(new FileWriter("optCode_temp.asm"));
        optimizeMov("optCode.asm", tempOptFile);
        tempOptFile.close(); // Close after second optimization
        
        // optimize redundant operations - read from temp and write back to optCode.asm
        optICGFile = new BufferedWriter(new FileWriter("optCode.asm"));
        optimizeRedundantOperations("optCode_temp.asm", optICGFile);


        // merge printProcLib into ICGFILE
        BufferedReader printLibReader = new BufferedReader(new FileReader("printProc.lib"));
        while ((line = printLibReader.readLine()) != null) {
            ICGFile.write(line + "\n");
            optICGFile.write(line + "\n");
        }
        ICGFile.write("END main\n");
        optICGFile.write("END main\n");
        ICGFile.flush();

        // Close files
        printLibReader.close();
        parserLogFile.close();
        errorFile.close();
        lexLogFile.close();
        ICGFile.close();
        optICGFile.close();
        tempFile.close();
        // Clean up temporary file
        new File("optCode_temp.asm").delete();

        System.out.println("Parsing completed. Check the output files for details.");
    }
}
