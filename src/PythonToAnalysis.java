import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.util.HashMap;
import java.util.Map;

public class PythonToAnalysis extends PythonParserBaseListener {
    private final Map<String, ComplexityInfo> functionComplexity = new HashMap<>();
    private boolean isRecursive = false;
    private String currentFunction = null;
    private int loopDepth = 0;
    private int maxLoopDepth = 0;
    private int cyclomaticComplexity = 1;

    private static class ComplexityInfo {
        String bigOComplexity;
        int cyclomaticComplexity;
        ComplexityInfo(String bigOComplexity, int cyclomaticComplexity) {
            this.bigOComplexity = bigOComplexity;
            this.cyclomaticComplexity = cyclomaticComplexity;
        }
    }

    @Override
    public void exitFile_input(PythonParser.File_inputContext ctx) {
        for (Map.Entry<String, ComplexityInfo> entry : functionComplexity.entrySet()) {
            System.out.println(STR."Función: \{entry.getKey()} - Complejidad Big(O): \{entry.getValue().bigOComplexity} - Complejidad Ciclomática: \{entry.getValue().cyclomaticComplexity}");
        }
    }

    @Override
    public void enterFunction_def(PythonParser.Function_defContext ctx) {
        currentFunction = ctx.function_def_raw().NAME().getText();
        isRecursive = false;
        loopDepth = 0;
        maxLoopDepth = 0;
        cyclomaticComplexity = 1;
    }

    @Override
    public void exitFunction_def(PythonParser.Function_defContext ctx) {
        String complexity = isRecursive ? "No calculable (Recursiva)" : calculateBigOComplexity();
        functionComplexity.put(currentFunction, new ComplexityInfo(complexity, cyclomaticComplexity));
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
        cyclomaticComplexity++;
        loopDepth++;
        if (loopDepth > maxLoopDepth) {
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
        cyclomaticComplexity++;
        if (loopDepth > maxLoopDepth) {
            maxLoopDepth = loopDepth;
        }
    }

    @Override
    public void exitWhile_stmt(PythonParser.While_stmtContext ctx) {
        loopDepth--;
    }

    @Override
    public void enterIf_stmt(PythonParser.If_stmtContext ctx) {
        cyclomaticComplexity++;
    }

    @Override
    public void enterElif_stmt(PythonParser.Elif_stmtContext ctx) {
        cyclomaticComplexity++;
    }

    @Override
    public void enterElse_block(PythonParser.Else_blockContext ctx) {
        cyclomaticComplexity++;
    }

    @Override
    public void enterTry_stmt(PythonParser.Try_stmtContext ctx) {
        cyclomaticComplexity++;
    }

    @Override
    public void enterExcept_block(PythonParser.Except_blockContext ctx) {
        cyclomaticComplexity++;
    }

    private String calculateBigOComplexity() {
        if (maxLoopDepth == 0) {
            return "O(1)";
        } else if (maxLoopDepth == 1) {
            return "O(n)";
        } else {
            return STR."O(n^\{maxLoopDepth})";
        }
    }
}
