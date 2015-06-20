package oop.ex6.main.parsing;

import oop.ex6.main.IllegalCodeException;

public class IllegalSJavaFileException extends IllegalCodeException {
	private static final long serialVersionUID = 1L;
	
	public IllegalSJavaFileException() {
		super("Invalid SJava file path was given.");
	}
	
	public IllegalSJavaFileException(String pathName) {
		super("The file: " + pathName + " is not a valid SJava file");
	}
}
