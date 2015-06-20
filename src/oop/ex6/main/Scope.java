package oop.ex6.main;

import java.util.*;

import oop.ex6.main.variables.Variable;

public abstract class Scope {
	protected Scope myParent;
	protected List<String> myContent;
	protected HashMap<String, Variable> myVariables;
	protected ArrayList<Scope> mySubScopes;
	protected int bracketCount = 0;
	protected int scopeStart = 0;
	
	protected Scope(Scope newParent, List<String> newContent) {
		myParent = newParent;
		myContent = newContent;
		mySubScopes = new ArrayList<Scope>();
		myVariables = new HashMap<String, Variable>();
	}
	
	public abstract void readScope() throws IllegalCodeException;
	
	public Scope getParent(){
		return myParent;
	}
	
	protected boolean isVariableLine(String line) {
		return false;
	}
	
	public HashMap<String, Variable> getVariables() {
		return myVariables;
	}
	
	/**
	 * Searches for the variable initialized with given name in
	 * the scope and all the outer ones
	 * @param varName name of the VAriable we are looking for
	 * @return the variable with such name or null if not found
	 */
	public Variable getVariable(String varName){
		Variable theVar = null;
		Scope theScope = this;
		while(theScope!= null){
			theVar = theScope.getVariables().get(varName);
			if(theVar != null){
				return theVar;
			}else{
				theScope = theScope.getParent();
			}
		}
		return theVar;
	}
}