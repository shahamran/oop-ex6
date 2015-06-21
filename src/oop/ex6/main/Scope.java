package oop.ex6.main;

import java.util.*;

import oop.ex6.main.variables.*;

/**
 * This class represents a scope in the SJava file. A scope is an object that holds a map
 * of variables defined in it, a reference to the scope in which it is nested, the lines of code
 * in this scope and a list of all Scopes nested in it.
 */
public abstract class Scope {
	protected Scope myParent;
	protected List<String> myContent;
	protected HashMap<String, Variable> myVariables;
	protected int bracketCount = 0, scopeStart = 0;
	
	/**
	 * Constructs a new Scope object.
	 * @param newParent The scope in which this scope is nested.
	 * @param newContent The lines of code this scope holds.
	 */
	protected Scope(Scope newParent, List<String> newContent) {
		myParent = newParent;
		myContent = newContent;
		myVariables = new HashMap<String, Variable>();
	}
	
	/**
	 * This method reads each line in this scope and checks it's validity.
	 * @throws IllegalCodeException If invalid code is found in this scope.
	 */
	public abstract void readScope() throws IllegalCodeException;
	
	/**
	 * @return The Scope object this scope is nested in.
	 */
	public Scope getParent(){
		return myParent;
	}
	
	/**
	 * @return A map of all variables defined in this scope, mapped by name.
	 */
	public Map<String, Variable> getVariables() {
		return myVariables;
	}
	
	/**
	 * Gets a variable line and adds all variables defined by it to this scope's list of variables.
	 * If no variables were defined, this method does not change this scope's members.
	 * @param line The string of the variable line.
	 * @throws VariableException If the line given was not a valid variable line, or if there has been
	 * an attempt to create more than one variable with the same name.
	 */
	protected void handleVariableLine(String line) throws VariableException {
		List<Variable> newVariables = VariableFactory.parseVariableLine(line, this);
		if (newVariables != null) {
			for (Variable var : newVariables) {
				myVariables.put(var.getName(), var);
			}
		}
	}
	
	/**
	 * Searches for the variable initialized with given name in
	 * the scope and all the outer ones
	 * @param varName name of the Variable we are looking for
	 * @return the variable with such name or null if not found
	 */
	public Variable getVariable(String varName){
		Variable theVar = null;
		Scope theScope = this;
		while (theScope != null) {
			theVar = theScope.getVariables().get(varName);
			if (theVar != null) {
				return theVar;
			} else {
				theScope = theScope.getParent();
			}
		}
		return theVar;
	}
}