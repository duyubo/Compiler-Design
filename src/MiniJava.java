import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.Symbol;
import java.io.*;
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.io.Reader;
//import java.io.File;

import AST.Program;
import Parser.parser;
import Parser.sym;
import Scanner.scanner;

public class MiniJava {
	public static void main(String[] args) {
		try {
            // create a scanner on the input file
            ComplexSymbolFactory sf = new ComplexSymbolFactory();
            //File f = new File("test1.txt");//~/Desktop/csep501-minijava-starter-18sp/src/
            File f = new File("/home/yubodu/Desktop/csep501-minijava-starter-18sp/src/test1.txt");
            FileReader fr = new FileReader(f);
            Reader in = new BufferedReader(fr);
            scanner s = new scanner(in, sf);
            Symbol t = s.next_token();
            while (t.sym != sym.EOF){ 
                // print each token that we scan
                System.out.print(s.symbolToString(t) + " ");
                t = s.next_token();
            }
            System.out.print("\nLexical analysis completed"); 
        } catch (Exception e) {
            // yuck: some kind of error in the compiler implementation
            // that we're not expecting (a bug!)
            System.err.println("Unexpected internal compiler error: " + 
                        e.toString());
            // print out a stack dump
            e.printStackTrace();
            System.exit(1);
        }
	}
}