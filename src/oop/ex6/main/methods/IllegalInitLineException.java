package oop.ex6.main.methods;

import oop.ex6.main.IllegalCodeException;

public class IllegalInitLineException extends IllegalMethodException {


	private static final long serialVersionUID = 1L;
	
	public IllegalInitLineException() {
		super("Illegal method initialization line");
	}
	
	public IllegalInitLineException(String msg) {
		super(msg);
	}
	

}
