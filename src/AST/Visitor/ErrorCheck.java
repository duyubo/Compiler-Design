package AST.Visitor;

import java.util.*;
import AST.*;
import TypeNodes.*;

public class ErrorCheck extends SymbolTable {
	
	//symbol table
	Hashtable<String, ClassNode> classTable = new Hashtable<String, ClassNode>();
	//current class name
	ClassNode currentClass = null;
	//current method name
	 MethodNode currentMethod = null;
	//current type name
	String currentTypeName = new String();
	//get symbol table and setup in this class
	public void setClassDef(Hashtable<String, ClassNode> classesDef) {
		this.classTable = classesDef;
	}
	// Display added for toy example language.  Not used in regular MiniJava
	public void visit(Display n) {
	}
	// MainClass m;
	  // ClassDeclList cl;
	  public void visit(Program n) {
	    n.m.accept(this);
	    for ( int i = 0; i < n.cl.size(); i++ ) {
	        n.cl.get(i).accept(this);
	    }
	    System.out.println("finish error checking");
	  }
	  
	  // Identifier i1,i2;
	  // Statement s;
	  public void visit(MainClass n) {
	    //n.i1.accept(this);
	    //n.i2.accept(this);
	    this.currentClass = this.classTable.get(n.i1.toString());
	    this.currentMethod = null;
	    n.s.accept(this);
	  }

	  // Identifier i;
	  // VarDeclList vl;
	  // MethodDeclList ml;
	  public void visit(ClassDeclSimple n) {
	    //n.i.accept(this);
	    this.currentClass = this.classTable.get(n.i.toString());
	    this.currentMethod = null;
	    for ( int i = 0; i < n.vl.size(); i++ ) {
	        n.vl.get(i).accept(this);
	    }
	    for ( int i = 0; i < n.ml.size(); i++ ) {
	        n.ml.get(i).accept(this);
	    }
	  }
	 
	  // Identifier i;
	  // Identifier j;
	  // VarDeclList vl;
	  // MethodDeclList ml;
	  public void visit(ClassDeclExtends n) {
	    //n.i.accept(this);
	    this.currentClass = this.classTable.get(n.i.toString());
	    this.currentMethod = null;
	    if (!this.classTable.containsKey(n.j.toString())) {
	    	System.out.println("class " + n.j.toString() +
    				"doesn't exist in line" + n.line_number);
	    	System.exit(1);
	    }
	    this.currentClass = this.classTable.get(n.i.toString());
	    for ( int i = 0; i < n.vl.size(); i++ ) {
	        n.vl.get(i).accept(this);
	    }
	    for ( int i = 0; i < n.ml.size(); i++ ) {
	        n.ml.get(i).accept(this);
	    }
	  }
	  
	  // Type t;
	  // Identifier i;
	  // FormalList fl;
	  // VarDeclList vl;
	  // StatementList sl;
	  // Exp e;
	  public void visit(MethodDecl n) {
	    n.t.accept(this);
	    String typeClass = getTypeName(n.t);
		String typeName = getTypeName(n.t);
		if (typeClass.equals("class")) {
			 typeName = ((IdentifierType)(n.t)).s;
			 if(!this.classTable.containsKey(typeName)) {
				System.out.println("return type " + typeName + 
		    				"doesn't exist in line" + n.line_number);
			    System.exit(1);
			 }
		}
		
		this.currentMethod = this.currentClass.getMethod(n.i.toString());
	    for ( int i = 0; i < n.fl.size(); i++ ) {
	        n.fl.get(i).accept(this);
	    }
	    for ( int i = 0; i < n.vl.size(); i++ ) {
	        n.vl.get(i).accept(this);
	    }
	    for ( int i = 0; i < n.sl.size(); i++ ) {
	        n.sl.get(i).accept(this);
	    }
	    n.e.accept(this);
	  }
	  
	  // Type t;
	  // Identifier i;
	  //check if the declared class type is defined
	  public void visit(VarDecl n) {
	    String typeClass = getTypeName(n.t);
		String typeName = getTypeName(n.t);
		if (typeClass.equals("class")) {
			 typeName = ((IdentifierType)(n.t)).s;
		}
	    this.currentTypeName = typeName;
	  }

	  // Type t;
	  // Identifier i;
	  public void visit(Formal n) {
		n.t.accept(this);
		String typeClass = getTypeName(n.t);
		String typeName = getTypeName(n.t);
		if (typeClass.equals("class")) {
			 typeName = ((IdentifierType)(n.t)).s;
		}
		this.currentTypeName = typeName;
	  }

	  // String s;
	  public void visit(IdentifierType n) {
		  String typeName = n.s;
	      if (!this.classTable.containsKey(typeName)) {
	    	System.out.println("class" + typeName +
	    				"doesn't exist in line" + n.line_number);
	        System.exit(1);
	      }
	  }

	  // StatementList sl;
	  public void visit(Block n) {
	    for ( int i = 0; i < n.sl.size(); i++ ) {
	        n.sl.get(i).accept(this);
	    }
	  }

	  // Exp e;
	  // Statement s1,s2;
	  public void visit(If n) {
	    n.e.accept(this);
	    if (!this.currentTypeName.equals("boolean")) {
	    	System.out.println("the codition inside the \"if\" is not boolean" +
	        " In line " + n.line_number);
			System.exit(1);
	    }
	    this.currentTypeName = null;
	    n.s1.accept(this);
	    n.s2.accept(this);
	  }

	  // Exp e;
	  // Statement s;
	  public void visit(While n) {
	    n.e.accept(this);
	    if (!this.currentTypeName.equals("boolean")) {
	    	System.out.println("the codition inside the \"while\" is not boolean" +
	        " In line " + n.line_number);
			System.exit(1);
	    }
	    this.currentTypeName = null;
	    n.s.accept(this);
	  }

	  // Exp e;
	  public void visit(Print n) {
	    n.e.accept(this);
	    if (!this.currentTypeName.equals("integer")) {
	    	System.out.println("can not print non int" +
	        " In line " + n.line_number);
			System.exit(1);
	    }
	    this.currentTypeName = "boolean";
	  }
	  
	  // Identifier i;
	  // Exp e;
	  public void visit(Assign n) {
	    n.i.accept(this);
	    String s = this.currentTypeName;
	    n.e.accept(this);
	    if (!this.currentTypeName.equals(s)) {
	    	System.out.println("assign type doesn't match: the left is " 
	    			+ s + "the right is " + this.currentTypeName
	    			+ " In line " + n.line_number);
			System.exit(1);
	    }
	    this.currentTypeName = "boolean";
	  }

	  // Identifier i;
	  // Exp e1,e2;
	  public void visit(ArrayAssign n) {
	    n.i.accept(this);
	    if (!this.currentTypeName.equals("intarray")) {
	    	System.out.println("the referenced identifier is not an int array!"
	    			+ " In line" + n.line_number);
			System.exit(1);
	    }
	    n.e1.accept(this);
	    if (!this.currentTypeName.equals("integer")) {
	    	System.out.println("reference index is not int!"
	    			+ " In line" + n.line_number);
			System.exit(1);
	    }
	    n.e2.accept(this);
	    if (!this.currentTypeName.equals("integer")) {
	    	System.out.println("the number on the right of = is not int!"
	    			+ " In line" + n.line_number);
			System.exit(1);
	    }
	    this.currentTypeName = "boolean";
	  }

	  // Exp e1,e2;
	  public void visit(And n) {
	    n.e1.accept(this);
	    
	    String leftE = new String(this.currentTypeName);
	    if (!leftE.equals("boolean")) {
	    	System.out.println("the expression on the left of && is not boolean! In line" + n.line_number);
	    	System.exit(1);
	    }
	    n.e2.accept(this);
	    String rightE = new String(this.currentTypeName);
	    if (!rightE.equals("boolean")) {
	    	System.out.println("the expression on the right of && is not boolean! In line" + n.line_number);;
	    	System.exit(1);
	    }
	    this.currentTypeName = "boolean";
	  }

	  // Exp e1,e2;
	  public void visit(LessThan n) {
		  n.e1.accept(this);
		    if (!this.currentTypeName.equals("integer")) {
		    	System.out.println("the number on the left of < is not int!"
		    			+ " In line" + n.line_number);
				System.exit(1);
		    }
		    n.e2.accept(this);
		    if (!this.currentTypeName.equals("integer")) {
		    	System.out.println("the number on the right of < is not int!"
		    			+ " In line" + n.line_number);
				System.exit(1);
		    }
		    this.currentTypeName = "boolean";
	  }

	  // Exp e1,e2;
	  public void visit(Plus n) {
		  n.e1.accept(this);
		    if (!this.currentTypeName.equals("integer")) {
		    	System.out.println("the number on the left of + is not int!"
		    			+ " In line" + n.line_number);
				System.exit(1);
		    }
		    n.e2.accept(this);
		    if (!this.currentTypeName.equals("integer")) {
		    	System.out.println("the number on the right of + is not int!"
		    			+ " In line" + n.line_number);
				System.exit(1);
		    }
		    this.currentTypeName = "integer";
	  }

	  // Exp e1,e2;
	  public void visit(Minus n) {
		  n.e1.accept(this);
		  if (!this.currentTypeName.equals("integer")) {
		    System.out.println("the number on the left of - is not int!"
		    			+ " In line" + n.line_number);
			System.exit(1);
		  }
		  n.e2.accept(this);
		  if (!this.currentTypeName.equals("integer")) {
		    System.out.println("the number on the right of - is not int!"
		    			+ " In line" + n.line_number);
			System.exit(1);
		  }
		  this.currentTypeName = "integer";   
	  }

	  // Exp e1,e2;
	  public void visit(Times n) {
	    n.e1.accept(this);
	    if (!this.currentTypeName.equals("integer")) {
	    	System.out.println("the number on the left of * is not int!"
	    			+ " In line" + n.line_number);
			System.exit(1);
	    }
	    n.e2.accept(this);
	    if (!this.currentTypeName.equals("integer")) {
	    	System.out.println("the number on the right of * is not int!"
	    			+ " In line" + n.line_number);
			System.exit(1);
	    }
	    this.currentTypeName = "integer";
	  }

	  // Exp e1,e2;
	  //check if the array exist and is int array type
	  //check if it reference an int index
	  public void visit(ArrayLookup n) {
	    n.e1.accept(this);
	    if (!this.currentTypeName.equals("intarray")) {
	    	System.out.println("can not reference from non int array type!"
	    			+ " In line" + n.line_number);
			System.exit(1);
	    }
	    n.e2.accept(this);
	    if (!this.currentTypeName.equals("integer")) {
	    	System.out.println("can not reference an array with non int type!"
	    			+ " In line" + n.line_number);
			System.exit(1);
	    }
	    this.currentTypeName = "integer";
	  }

	  // Exp e;
	  //check if the expression is a int array type
	  public void visit(ArrayLength n) {
	    n.e.accept(this);
	    if (!this.currentTypeName.equals("intarray")) {
	    	System.out.println("get length from non array type!"
	    			+ " In line" + n.line_number);
			System.exit(1);
	    }
	    this.currentTypeName = "integer";
	  }

	  // Exp e (method called from class);
	  // Identifier i (method name);
	  // ExpList el (parameter list); 
	  public void visit(Call n) {
		//check if the expression is a class  
	    n.e.accept(this);
	    if (!this.classTable.containsKey(this.currentTypeName)){
	    	System.out.println("method is not call from a class or class doesn't exist!"
	    			+ " In line" + n.line_number);
			System.exit(1);
	    } 
	    
	    //do not accept identifier here
	    //check if the identifier is a method in the class
	    ClassNode cn = this.classTable.get(this.currentTypeName);
	    if (!cn.checkMethods(n.i.toString())){
	    	System.out.println("method doesn't exist in this class!"
	    			+ " In line" + n.line_number);
			System.exit(1);
	    } 
	    
	    //check if the parameter is defined and type match
	    MethodNode mn = cn.getMethod(n.i.toString());
	    
	    for ( int i = 0; i < n.el.size(); i++ ) {
	    	 n.el.get(i).accept(this);
	    	 if (!mn.checkParameters(this.currentTypeName, i)) {
	    		System.out.println("the "+ i + "th parameter doesn't match"
	 	    			+ " In line" + n.line_number);
	 			System.exit(1);
	    	 }	 
	    }
	    
	    //set current type as the method's return type
	    this.currentTypeName = mn.getReturnType();
	    
	  }

	  // int i;
	  public void visit(IntegerLiteral n) {
		  this.currentTypeName = "integer";
	  }

	  public void visit(True n) {
		  this.currentTypeName = "boolean";
	  }

	  public void visit(False n) {
		  this.currentTypeName = "boolean";
	  }

	  // String s;
	  //check if the identifier has been defined (in class/method)or not
	  public void visit(IdentifierExp n) {
		  if (this.currentMethod == null || (!this.currentMethod.checkVars(n.s) && !this.currentMethod.checkParameter(n.s))) {
			  if (!this.currentClass.checkVars(n.s)) {
				  System.out.println("Identiifer hasn't been defined! In line" + n.line_number);
				  System.exit(1);
			  }
		  }
		  Node var = null;
		  if (this.currentMethod != null ) {
			 if (this.currentMethod.checkVars(n.s) )
				 var = this.currentMethod.getVar(n.s);
			 else if (this.currentMethod.checkParameter(n.s))
				 var = this.currentMethod.getParameter(n.s);
		  }
		  if (var == null && this.currentClass.checkVars(n.s)) {
			 var = this.currentClass.getVar(n.s);
		  }
		  this.currentTypeName = var.getTypeName();
	  }
	  
	  //this always exist and set up the current typename
	  public void visit(This n) {
		  this.currentTypeName = this.currentClass.getTypeName();
	  }

	  // Exp e;
	  //expression should be integer type
	  public void visit(NewArray n) {
	    n.e.accept(this);
	    if(!this.currentTypeName.equals("integer")) {
	    	System.out.println("int array should has an int size! In line" + n.line_number);
	    	System.exit(1);
	    }
	    this.currentTypeName = "intarray";
	  }
	  
	  // Identifier i;
	  //check if the new class is validate
	  public void visit(NewObject n) {
		  //n.i.accept(this);
		  if (!this.classTable.containsKey(n.i.toString())) {
			  System.out.println("The class doesn't exist! In line" + n.line_number);
			  System.exit(1);
		  }
		  this.currentTypeName = n.i.toString();
	  }

	  // Exp e;
	  //check if the expression is a boolean
	  public void visit(Not n) {
	    n.e.accept(this);
	    
	    if (!this.currentTypeName.equals("boolean")) {
	    	System.out.println("The variable is not boolean! In line" + n.line_number);
			System.exit(1);
	    }
	    this.currentTypeName = "boolean";
	  }

	  // String s;
	  //check if the identifier exists in the current method/class
	  public void visit(Identifier n) {
		  if (this.currentMethod == null || (!this.currentMethod.checkVars(n.s) && !this.currentMethod.checkParameter(n.s))) {
			  if (!this.currentClass.checkVars(n.s)) {
				  System.out.println("Identiifer hasn't been defined! In line" + n.line_number);
				  System.exit(1);
			  }
		  }
		  Node var = null;
		  //System.out.println(this.currentMethod.getTypeName());
		  //System.out.println(this.currentMethod.getParameter(n.s));
		 // System.out.println(n.s);
		  //this.currentMethod.PrintAll("");
		  //System.out.println(this.currentMethod.checkParameter(n.s));
		  if (this.currentMethod != null ) {
			 //System.out.println(n.s);
			 if (this.currentMethod.checkVars(n.s) )
				 var = this.currentMethod.getVar(n.s);
			 else if (this.currentMethod.checkParameter(n.s))
				 var = this.currentMethod.getParameter(n.s);
		  }
		  if (var == null && this.currentClass.checkVars(n.s)) {
			 var = this.currentClass.getVar(n.s);
		  }
		  //System.out.println(var);
		  this.currentTypeName = var.getTypeName();
	  }

}
