package oop.ex6.main;

public class IllegalCodeException extends SJavaException {
	private static final long serialVersionUID = 1L;
	
	public IllegalCodeException() {
		super("Illegal S-Java code!");
	}
	
	public IllegalCodeException(String msg) {
		super(msg);
	}
	
	public IllegalCodeException(String badLine, int lineNum) {
		super("Line number: " + lineNum + " is not valid: " + badLine);
	}
	
}
