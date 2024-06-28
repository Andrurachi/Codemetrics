// import ANTLR's runtime libraries
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception {
// create a CharStream that reads from standard input / file
// create a lexer that feeds off of input CharStream
        PythonLexer lexer;
        CharStream file = CharStreams.fromFileName("input/input.txt");
        if (args.length>0)
            lexer = new PythonLexer(file);
        else
            lexer = new PythonLexer(CharStreams.fromStream(System.in));
        // Calcular el número de caracteres por linea en la entrada



// create a buffer of tokens pulled from the lexer
        CommonTokenStream tokens = new CommonTokenStream(lexer);
// create a parser that feeds off the tokens buffer
        PythonParser parser = new PythonParser(tokens);
        ParseTree tree = parser.file_input(); // begins parsing at init rule

        // Create a generic parse tree walker that can trigger callbacks
        ParseTreeWalker walker = new ParseTreeWalker();
        // Walk the tree created during the parse, trigger callbacks
        PythonToAnalysis listener = new PythonToAnalysis(file.toString());
        listener.countCommentsAndLines(tokens);
        walker.walk(listener, tree);
    }
}

