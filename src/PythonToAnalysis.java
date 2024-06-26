import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.util.HashMap;
import java.util.Map;

public class PythonToAnalysis extends PythonParserBaseListener {
    private final Map<String, String> functionComplexity = new HashMap<>();
    private boolean isRecursive = false;
    private String currentFunction = null;
    private int loopDepth = 0;
    private int maxLoopDepth = 0;

//    @Override
//    public void enterFile_input(PythonParser.File_inputContext ctx) {
//        System.out.println("Inicio programa!");
//    }

    @Override
    public void exitFile_input(PythonParser.File_inputContext ctx) {
        //System.out.println("Fin programa!");
        for (Map.Entry<String, String> entry : functionComplexity.entrySet()) {
            System.out.println("Función: " + entry.getKey() + " - Complejidad: " + entry.getValue());
        }
    }

    @Override
    public void enterFunction_def(PythonParser.Function_defContext ctx) {
        currentFunction = ctx.function_def_raw().NAME().getText();
        isRecursive = false;
        loopDepth = 0;
        //System.out.println("Inicio de la función: " + currentFunction);
    }

    @Override
    public void exitFunction_def(PythonParser.Function_defContext ctx) {
        if (isRecursive) {
            functionComplexity.put(currentFunction, "No calculable (Recursiva)");
        } else {
            String complexity = calculateComplexity();
            functionComplexity.put(currentFunction, complexity);
        }
        //System.out.println("Fin de la función: " + currentFunction);
        currentFunction = null;
    }

    @Override
    public void enterExpression(PythonParser.ExpressionContext ctx) {
        if (currentFunction != null && ctx.getText().contains(currentFunction)) {
            isRecursive = true;
        }
    }

    @Override
    public void enterFor_stmt(PythonParser.For_stmtContext ctx) {
        loopDepth++;
        if (loopDepth > maxLoopDepth){
            maxLoopDepth = loopDepth;
        }
    }

    @Override
    public void exitFor_stmt(PythonParser.For_stmtContext ctx) {
        loopDepth--;
    }

    @Override
    public void enterWhile_stmt(PythonParser.While_stmtContext ctx) {
        loopDepth++;
        if (loopDepth > maxLoopDepth){
            maxLoopDepth = loopDepth;
        }
    }

    @Override
    public void exitWhile_stmt(PythonParser.While_stmtContext ctx) {
        loopDepth--;
    }

    private String calculateComplexity() {
        // System.out.println("Loop depth: " + loopDepth);
        // System.out.println("Loop depth: " + maxLoopDepth);
        if (maxLoopDepth == 0) {
            return "O(1)";
        } else if (maxLoopDepth == 1) {
            return "O(n)";
        } else {
            return "O(n^" + maxLoopDepth + ")";
        }
    }
}