package oop.ex6.main;

import java.util.*;

import oop.ex6.main.variables.Variable;

public abstract class Scope {
	protected Scope myParent;
	protected List<String> myContent;
	protected HashSet<Variable> myVariables;
	
	protected Scope(Scope newParent, List<String> newContent) {
		myParent = newParent;
		myContent = newContent;
	}
	
	public abstract void readScope() throws IllegalCodeException;
	
	public Scope getParent(){
		return myParent;
	}
	
	protected boolean isVariableLine(String line) {
		return false;
	}
	
	public Set<Variable> getVariables() {
		return myVariables;
	}
}
