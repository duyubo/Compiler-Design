package AST.Visitor;
import java.util.*;

import AST.*;
import TypeNodes.*;
//import jdk.nashorn.internal.runtime.linker.NameCodec;

public class SymbolTable implements Visitor {
	
	//defined class type
	private Hashtable<String, ClassNode> classesDef = new Hashtable<String, ClassNode>();
	//classes which have already be declared 
	private Hashtable<String, ClassNode> classesDecl = new Hashtable<String, ClassNode>();
	//current visit class
	private Stack<ClassNode> currentClass = new Stack<ClassNode>();
	//current visit method
	private Stack<MethodNode> currentMethod = new Stack<MethodNode>();
	//return the symbol table
	public Hashtable<String, ClassNode> getClassTable(){
		return this.classesDef;
	}
	//read type name
	public String getTypeName(Type t) {
		if (t instanceof BooleanType)
			return "boolean";
		if (t instanceof IntArrayType)
			return "intarray";
		if (t instanceof IntegerType)
			return "integer";
		return "class";
	}
	
	//print all information
	public void printAll() {
		for (Map.Entry<String, ClassNode> set:classesDef.entrySet()) {
			set.getValue().PrintAll("");
		}
	} 
	
	// MainClass m;
	// ClassDeclList cl;
	public void visit(Program n) {
		n.m.accept(this);
		for ( int i = 0; i < n.cl.size(); i++) {
			n.cl.get(i).accept(this);
		}
	}
	
	// Identifier i1,i2;
	// Statement s;
	public void visit(MainClass n) {
	    ClassNode c = new ClassNode(n.i1.toString(),(String)null);
	    this.classesDef.put(n.i1.toString(),c);
	    this.currentClass.push(c);
	    n.i1.accept(this); 
	    n.i2.accept(this);
	    n.s.accept(this);
	    this.currentClass.pop();
	}

	// Identifier i;
	// VarDeclList vl;
	// MethodDeclList ml;
	public void visit(ClassDeclSimple n) {
		ClassNode c = new ClassNode(n.i.toString(),(String)null);
		this.currentClass.push(c);
		for ( int i = 0; i < n.vl.size(); i++ ) {
			String typeClass = getTypeName(n.vl.get(i).t);
			String typeName = getTypeName(n.vl.get(i).t);
			if (typeClass.equals("class")) {
				typeName = ((IdentifierType)(n.vl.get(i).t)).s;
			}
			this.currentClass.peek().addVars(n.vl.get(i).i.toString(),new Node(typeClass, typeName,n.vl.get(i).i.toString()));		
	    }
	    for ( int i = 0; i < n.ml.size(); i++ ) {
	    	n.ml.get(i).accept(this);
	    }
	    this.classesDef.put(n.i.toString(),this.currentClass.pop());
	}
	 
	  // Identifier i;
	  // Identifier j;
	  // VarDeclList vl;
	  // MethodDeclList ml;
	  public void visit(ClassDeclExtends n) {
		  ClassNode c = new ClassNode(n.i.toString(),n.j.toString());
		  this.currentClass.push(c);
		  for ( int i = 0; i < n.vl.size(); i++ ) {
			  String typeClass = getTypeName(n.vl.get(i).t);
			  String typeName = getTypeName(n.vl.get(i).t);
			  if (typeClass.equals("class")) {
				  typeName = ((IdentifierType)(n.vl.get(i).t)).s;
			  }
			  this.currentClass.peek().addVars(n.vl.get(i).i.toString(),new Node(typeClass, typeName,n.vl.get(i).i.toString()));
		  }
		  for ( int i = 0; i < n.ml.size(); i++ ) {
			  n.ml.get(i).accept(this);
		  }
		  this.classesDef.put(n.i.toString(),this.currentClass.pop());
	  }


	  // Type t;
	  // Identifier i;
	  // FormalList fl;
	  // VarDeclList vl;
	  // StatementList sl;
	  // Exp e;
	  public void visit(MethodDecl n) {
		  MethodNode mNode = new MethodNode(n.i.toString());
		  this.currentMethod.push(mNode);
		  String typeClass0 = getTypeName(n.t);
		  String typeName0 = getTypeName(n.t);
		  if (typeClass0.equals("class")) {
			  typeName0 = ((IdentifierType)(n.t)).s;
		  }
		  this.currentMethod.peek().setReturnType(typeName0);

		  for ( int i = 0; i < n.fl.size(); i++ ) {
			  String typeClass = getTypeName(n.fl.get(i).t);
			  String typeName = getTypeName(n.fl.get(i).t);
			  if (typeClass.equals("class")) {
				  typeName = ((IdentifierType)(n.fl.get(i).t)).s;
			  }
			  if (!this.currentMethod.empty())
				  this.currentMethod.peek().addParams(new Node(typeClass,typeName,n.fl.get(i).i.toString()));
		  }
		  for ( int i = 0; i < n.vl.size(); i++ ) {
			  String typeClass = getTypeName(n.vl.get(i).t);
			  String typeName = getTypeName(n.vl.get(i).t);
			  if (typeClass.equals("class")) {
				  typeName = ((IdentifierType)(n.vl.get(i).t)).s;
			  }
			  this.currentMethod.peek().addVars(n.vl.get(i).i.toString(),new Node(typeClass, typeName,n.vl.get(i).i.toString()));
		  }
		  
		  this.currentClass.peek().addMethods(this.currentMethod.peek().getTypeName(),this.currentMethod.peek());
		  this.currentMethod.pop();
	  }

	  // Type t;
	  // Identifier i;
	  public void visit(Formal n) {}

	  public void visit(IntArrayType n) {}

	  public void visit(BooleanType n) {}

	  public void visit(IntegerType n) {}

	  // String s;
	  public void visit(IdentifierType n) {}

	  // StatementList sl;
	  public void visit(Block n) {}

	  // Exp e;
	  // Statement s1,s2;
	  public void visit(If n) {}

	  // Exp e;
	  // Statement s;
	  public void visit(While n) {}

	  // Exp e;
	  public void visit(Print n) {}
	  
	  // Identifier i;
	  // Exp e;
	  public void visit(Assign n) {}

	  // Identifier i;
	  // Exp e1,e2;
	  public void visit(ArrayAssign n) {}

	  // Exp e1,e2;
	  public void visit(And n) {}

	  // Exp e1,e2;
	  public void visit(LessThan n) {}

	  // Exp e1,e2;
	  public void visit(Plus n) {}

	  // Exp e1,e2;
	  public void visit(Minus n) {}

	  // Exp e1,e2;
	  public void visit(Times n) {}

	  // Exp e1,e2;
	  public void visit(ArrayLookup n) {}

	  // Exp e;
	  public void visit(ArrayLength n) {}

	  // Exp e;
	  // Identifier i;
	  // ExpList el;
	  public void visit(Call n) {}

	  // int i;
	  public void visit(IntegerLiteral n) {}

	  public void visit(True n) {}

	  public void visit(False n) {}

	  // String s;
	  public void visit(IdentifierExp n) {}

	  public void visit(This n) {}

	  // Exp e;
	  public void visit(NewArray n) {}

	  // Identifier i;
	  public void visit(NewObject n) {}

	  // Exp e;
	  public void visit(Not n) {}

	  // String s;
	  public void visit(Identifier n) {}
	  
	  //display
	  public void visit(Display n) {}

	  public void visit(VarDecl n) {}
}
