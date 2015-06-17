package oop.ex6.main.methods;

import java.util.*;

import oop.ex6.main.IllegalCodeException;
import oop.ex6.main.Scope;

public class Method extends Scope {
	protected String myName;
	protected String[] arguments;
	
	protected Method(String newName, String[] newArguments, Scope newParent, List<String> newContent) {
		super(newParent, newContent);
		myName = newName;
		arguments = newArguments;
	}

	@Override
	public void readScope() throws IllegalCodeException {
		

	}

}
