/** Creates an analysis */
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.util.HashMap;
import java.util.Map;
public class PythonToAnalysis extends PythonParserBaseListener {
    @Override         	/** Translate { to " */
    public void enterRoot(PythonParser.RootContext ctx) {
        System.out.println("Inicio programa!");
    }
    @Override         	/** Translate { to " */
    public void exitRoot(PythonParser.RootContext ctx) {
        System.out.println("Fin programa!");
    }
}