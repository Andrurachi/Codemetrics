/** Creates an analysis */
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.util.HashMap;
import java.util.Map;
public class PythonToAnalysis extends PythonParserBaseListener {
    // Calcular el número de clases, funciones, variables, comentarios, declaraciones condicionales, bucles y lineas de código
    private int comments = 0;
    private int numberIf = 0;
    private int numberFor = 0;
    private int numberWhile = 0;
    private int numerLines = 0;
    private int numberClasses = 0;
    private int numberGlobalVariables = 0;
    private int numberFunctions = 0;

    @Override
    public void enterFile_input(PythonParser.File_inputContext ctx) {
        numerLines = ctx.getStop().getLine();
    }

    @Override
    public void enterFunction_def_raw(PythonParser.Function_def_rawContext ctx) {
        numberFunctions++;
    }

    @Override
    public void enterIf_stmt(PythonParser.If_stmtContext ctx) {
        numberIf++;
    }

    @Override
    public void enterFor_stmt(PythonParser.For_stmtContext ctx) {
        numberFor++;
    }

    @Override
    public void enterWhile_stmt(PythonParser.While_stmtContext ctx) {
        numberWhile++;
    }

    @Override
    public void enterGlobal_stmt(PythonParser.Global_stmtContext ctx) {
        numberGlobalVariables ++;
    }

    @Override
    public void enterClass_def_raw(PythonParser.Class_def_rawContext ctx) {
        numberClasses++;
    }

    @Override
    public void exitFile_input(PythonParser.File_inputContext ctx) {
        System.out.println("Number of comments: " + comments);
        System.out.println("Number of classes: " + numberClasses);
        System.out.println("Number of functions: " + numberFunctions);
        System.out.println("Number of global variables: " + numberGlobalVariables);
        System.out.println("Number of if statements: " + numberIf);
        System.out.println("Number of for statements: " + numberFor);
        System.out.println("Number of while statements: " + numberWhile);
        System.out.println("Number of lines: " + numerLines);
    }
}