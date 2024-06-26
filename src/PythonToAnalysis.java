/** Creates an analysis */
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.util.HashMap;
import java.util.Map;
public class PythonToAnalysis extends PythonParserBaseListener {
    // Calcular el número de clases, funciones, variables, comentarios, declaraciones condicionales, bucles y lineas de código
    private int comments = 0;
    private int numberIfStmt = 0;
    private int numberForStmt = 0;
    private int numerLines = 0;
    private Map<String, Integer> variablesMap = new HashMap<>();
    private Map<String, Integer> functionsMap = new HashMap<>();
    private Map<String, Integer> classesMap = new HashMap<>();
    private Map<String, Integer> commentsMap = new HashMap<>();



}