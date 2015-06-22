package oop.ex6.main.parsing;

import oop.ex6.main.SJavaException;

/**
 * Thrown if a file is not a valid SJava file (for example: no .sjava suffix, not a file).
 */
public class IllegalSJavaFileException extends SJavaException {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructs a new exception with a default message.
	 */
	public IllegalSJavaFileException() {
		super("Invalid SJava file path was given.");
	}
	
	/**
	 * Constructs a new exception specifying the bad path that was given.
	 * @param pathName The bad path string.
	 */
	public IllegalSJavaFileException(String pathName) {
		super("The file: " + pathName + " is not a valid SJava file");
	}
}
