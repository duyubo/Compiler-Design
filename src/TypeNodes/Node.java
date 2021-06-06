package TypeNodes;


public class Node {
	//type name, e.g. int boolean...self define class names 
	private String typeName;
	//type class, which should be included in "typeClasses"
	private String typeClass;
	//variable name, indentifier
	private String varName;
	
	private static final String[] typeClasses= {"int", "boolean", "intarray", "class", "method"};  
	
	//the node name and node type
	public Node( String typeClass, String typeName, String varName) {
		this.typeClass = typeClass;
		this.typeName = typeName;
		this.varName = varName;
	}
	
	// get variable name 
	public String getVarName() {
		return this.varName;
	}
	
	// get variable name 
	public String getTypeClass() {
		return this.typeClass;
	}
	//get type name
	public String getTypeName() {
		return this.typeName;
	}
	
	//check if the type is legal, if legal return type index of not return -1
	public int checkType() {
		int l = Node.typeClasses.length;
		for (int i = 0; i < l; i++) {
			if (this.typeClass.equals(Node.typeClasses[i])) {
				return i;
			}
		}
		return -1;
	}
	
	//print all information
	public void PrintAll(String indents) {
		System.out.println(indents+"typeclass: " + this.typeClass
				+ " typename: " + this.typeName + " varName: " + this.varName);
	} 
	

}

