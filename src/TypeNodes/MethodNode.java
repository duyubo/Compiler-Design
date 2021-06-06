package TypeNodes;
import TypeNodes.Node;
import java.util.*;

public class MethodNode extends Node {
	//local variables
	private Hashtable<String, Node> vars = new Hashtable();
	//parameters
	private List<Node> parameters = new ArrayList<Node>();
	//return type
	private String returnType;
	
	//init node 
	public MethodNode (String typeName) {
		super("method",typeName, (String)null);
	}
	//add local variables
	public void addVars(String s, Node n) {
		this.vars.put(s,n);
	}
	public Set<String> getVars(){
		return this.vars.keySet();
	}
	public Node getVar(String varName){
		return this.vars.get(varName);
	}
	public Node getParameter(String varName){
		for (Node n : this.parameters) {
			if (n.getVarName().equals(varName)) {
				return n;
			}
		}
		return null;
	}
	//add parameters
	public void addParams(Node n) {
		this.parameters.add(n);
	}
	//setup return type
	public void setReturnType(String s) {
		this.returnType = s;
	}
	//get return type
	public String getReturnType() {
		return this.returnType;
	}
	//check if a referenced variable is the local variable in the method 
	public boolean checkVars(String name) {
		return this.vars.containsKey(name);
	}
	//check if parameter legal
	public boolean checkParameters(String typeName, int location) {
		return this.parameters.get(location).getTypeName().equals(typeName);
	}
	
	//check if parameter legal
	public boolean checkParameter(String varName) {
		for (Node n : this.parameters) {
			if (n.getVarName().equals(varName)) {
				return true;
			}
		}
		return false;
	}
	//print all information
	@Override
	public void PrintAll(String indents) {
		System.out.println(indents+"typeclass: " + this.getTypeClass()
				+ " typename: " + this.getTypeName() + " varName: " + this.getVarName()
				+ " return type: "+this.returnType);
		System.out.println(indents+'\t'+"vars: ");
		for (Map.Entry<String, Node> v:this.vars.entrySet()) {
			v.getValue().PrintAll(indents+"\t\t");
		}
		System.out.println(indents+'\t'+"params: ");
		for (Node n:this.parameters) {
			n.PrintAll(indents+"\t\t");
		}
	} 
}
