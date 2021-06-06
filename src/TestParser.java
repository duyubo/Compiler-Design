import Scanner.*;
import Parser.*;
import AST.*;
import AST.Visitor.*;
import java_cup.runtime.Symbol;
import java_cup.runtime.ComplexSymbolFactory;
import java.util.*;
import java.io.*;

public class TestParser {
    public static void main(String [] args) {
        try {
            // create a scanner on the input file
        	ComplexSymbolFactory sf = new ComplexSymbolFactory();
            //File f = new File("test1.txt");//~/Desktop/csep501-minijava-starter-18sp/src/
            File f = new File("/home/yubodu/Desktop/csep501-minijava-starter-18sp/SamplePrograms/SampleMiniJavaPrograms/BinaryTree.java");
            FileReader fr = new FileReader(f);
            Reader in = new BufferedReader(fr);
            scanner s = new scanner(in, sf);
            //ComplexSymbolFactory sf = new ComplexSymbolFactory();
            //Reader in = new BufferedReader(new InputStreamReader(System.in));
            //scanner s = new scanner(in, sf);
            parser p = new parser(s, sf);
            Symbol root;
            // replace p.parse() with p.debug_parse() in next line to see trace of
            // parser shift/reduce actions during parse
           
            root = p.parse();
            Program program = (Program)root.value;
            SymbolTable st = new SymbolTable();
            program.accept(st);
            //st.printAll();
            ErrorCheck ec = new ErrorCheck();
            ec.setClassDef(st.getClassTable());
            (ec).visit(program);
            System.out.print("\nParsing completed"); 
        } catch (Exception e) {
            // yuck: some kind of error in the compiler implementation
            // that we're not expecting (a bug!)
            System.err.println("Unexpected internal compiler error: " + 
                               e.toString());
            // print out a stack dump
            e.printStackTrace();
        }
    }
}
