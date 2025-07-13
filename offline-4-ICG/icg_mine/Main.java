import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.io.*;

public class Main {
    public static BufferedWriter parserLogFile;
    public static BufferedWriter errorFile;
    public static BufferedWriter lexLogFile;
    public static BufferedWriter ICGFile;
    public static BufferedWriter tempFile;

    public static int syntaxErrorCount = 0;

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

        // write boilerplate code to the ICG file
        ICGFile.write(".MODEL SMALL\n");
        ICGFile.write(".STACK 1000H\n");
        ICGFile.write(".DATA\n");
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

        // Close files
        parserLogFile.close();
        errorFile.close();
        lexLogFile.close();
        ICGFile.close();
        tempFile.close();

        System.out.println("Parsing completed. Check the output files for details.");
    }
}
