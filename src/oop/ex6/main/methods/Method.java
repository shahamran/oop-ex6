package oop.ex6.main.methods;

import java.util.List;

import oop.ex6.main.IllegalCodeException;
import oop.ex6.main.Scope;

public class Method extends Scope {
	private String myName;
	private Object[] arguments;
	
	protected Method(String newName,Object[] newArguments, Scope newParent, List<String> newContent) {
		super(newParent, newContent);
		myName = newName;
		arguments = newArguments;
	}

	@Override
	public void readScope() throws IllegalCodeException {
		

	}

}
