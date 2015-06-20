package oop.ex6.main.parsing;

import oop.ex6.main.SJavaException;

/**
 * Thrown if a file is not a valid SJava file (for example: no .sjava suffix, not a file).
 */
public class IllegalSJavaFileException extends SJavaException {
	private static final long serialVersionUID = 1L;
	
	public IllegalSJavaFileException() {
		super("Invalid SJava file path was given.");
	}
	
	public IllegalSJavaFileException(String pathName) {
		super("The file: " + pathName + " is not a valid SJava file");
	}
}
