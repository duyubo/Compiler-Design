package TypeNodes;

import java.util.*;

public class ClassNode extends Node{
	//parent name
	private String parentName = null;
	//all ancestors for checking loops in extends
	private List<String> parents = new ArrayList();
	//local variables
	private Hashtable<String, Node> vars = new Hashtable();
	//methods
	private Hashtable<String, MethodNode> methods = new Hashtable();
	//init node 
	public ClassNode (String typeName, String varName) {
		super("class",typeName, varName);
	}
	//add local variables
	public void addVars(String s, Node n) {
		this.vars.put(s,n);
	}
	//add methods
	public void addMethods(String s, MethodNode n) {
		this.methods.put(s,n);
	}
	//set parent name
	public void setParent(String p) {
		this.parentName = p;
	}
	//get parent name
	public String getParent() {
		return this.parentName;
	}
	public Node getVar(String varName){
		return this.vars.get(varName);
	}
	//
	public Set<String> getVars(){
		return this.vars.keySet();
	}
	public MethodNode getMethod(String MethodName){
		return this.methods.get(MethodName);
	}
	
	//check if a referenced variable is the local variable in the method 
	public boolean checkVars(String name) {
		return this.vars.containsKey(name);
	}
	//check if a referenced variable is the local variable in the method 
	public boolean checkMethods(String name) {
		return this.methods.containsKey(name);
	}
	//print all information
	@Override
	public void PrintAll(String indents) {
		System.out.println(indents+"typeclass: " + this.getTypeClass()
				+ " typename: " + this.getTypeName() + " varName: " + this.getVarName()
				+ " parentName of class: "+this.parentName);
		System.out.println(indents+'\t'+"vars in class: ");
		for (Map.Entry<String, Node> v:this.vars.entrySet()) {
			v.getValue().PrintAll(indents+"\t\t");
		}
		System.out.println(indents+'\t'+"methods in class: ");
		for (Map.Entry<String, MethodNode> m:this.methods.entrySet()) {
			System.out.println(indents+"\t\t"+m.getKey());
			m.getValue().PrintAll(indents+"\t\t");
		}
	} 
}
