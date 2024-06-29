/** Creates an analysis */
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.util.ArrayList;
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

    public static class Dependencia{
        String name;
        String nameFrom;
        int timesUsed;
        ArrayList<Integer> linesUsed;
        ArrayList<String> funcUsed;
        public Dependencia(String name,String nameFrom){
            this.name = name;
            this.nameFrom = nameFrom;
            this.timesUsed = 0;
            this.linesUsed = new ArrayList<Integer>();
            this.funcUsed = new ArrayList<String>();
        }
        public String getName() { return name; }
        public String getNameFrom() { return nameFrom; }
        public int getTimesUsed() { return timesUsed;}
        public ArrayList<Integer> getLinesUsed(){return linesUsed;}
        public ArrayList<String> getFuncUsed(){return funcUsed;}
        public void increaseTimesUsed() { this.timesUsed +=1;}
        public void addLineUsed(int lineNumber){this.linesUsed.add(lineNumber);}
        public void addFuncUsed(String nameFunc){this.funcUsed.add(nameFunc);}
    }

    ArrayList<Dependencia> dependencias = new ArrayList<Dependencia>();
    ArrayList<String> funcionesLlamadas = new ArrayList<String>();

    @Override
    public void enterFile_input(PythonParser.File_inputContext ctx) {
        numerLines = ctx.getStop().getLine();
    }

    @Override
    public void enterFunction_def_raw(PythonParser.Function_def_rawContext ctx) {
        if (ctx.NAME() != null){
            funcionesLlamadas.add(ctx.NAME().getText());
        }
        numberFunctions++;
    }

    @Override public void exitFunction_def_raw(PythonParser.Function_def_rawContext ctx) {
        funcionesLlamadas.removeLast();
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
        System.out.println("DEPENDENCY:");
        for (int i = 0; i < dependencias.size(); i++) {
            System.out.println(dependencias.get(i).getName()+", Times used:"+dependencias.get(i).getTimesUsed()+", Lines:"+dependencias.get(i).getLinesUsed()+", Used in functions:"+dependencias.get(i).getFuncUsed());
            //for (int j = 0; j < dependencias.get(j).getLinesUsed().size(); j++){
                //System.out.println();
            //}
        }
    }

    @Override public void enterImport_name(PythonParser.Import_nameContext ctx) {
        dependencias.add(new Dependencia(ctx.dotted_as_names().getText(),ctx.dotted_as_names().getText()));
        //{System.out.println(ctx.dotted_as_names().getText());
    }
    @Override public void enterImport_from(PythonParser.Import_fromContext ctx) {
        String[] depens = ctx.import_from_targets().getText().split(",");
        for (int i=0; i < depens.length;i++){
            dependencias.add(new Dependencia(depens[i],ctx.dotted_name().getText()));
        }

        //System.out.println(ctx.import_as_names().getText());
    }
    
    @Override public void enterAtom(PythonParser.AtomContext ctx) {
        for (int i=0; i < dependencias.size();i++){
            if (ctx.getText().equals(dependencias.get(i).getName())){
                dependencias.get(i).increaseTimesUsed();
                dependencias.get(i).addLineUsed(ctx.getStop().getLine());
                if (!funcionesLlamadas.isEmpty()){
                    dependencias.get(i).addFuncUsed(funcionesLlamadas.getLast());
                }
                break;
            }
        }

    }




}