package oop.ex6.main.conditions;

import java.util.List;

import oop.ex6.main.IllegalCodeException;
import oop.ex6.main.Scope;

public class Condition extends Scope {

	protected Condition(Scope newParent, List<String> newContent) {
		super(newParent, newContent);
	}

	public void readScope() throws IllegalCodeException {
		// TODO Auto-generated method stub

	}

}
