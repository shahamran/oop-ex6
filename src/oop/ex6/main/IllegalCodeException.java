package oop.ex6.main;

public class IllegalCodeException extends SJavaException {
	private static final long serialVersionUID = 1L;
	
	public IllegalCodeException() {
		super("Illegal S-Java code!");
	}
	
	public IllegalCodeException(String msg) {
		super(msg);
	}
	
	public IllegalCodeException(int lineNum, String badLine) {
		super("Error in line " + lineNum + ": " + badLine);
	}
	
	public IllegalCodeException(int lineNum, String badLine, String errMsg) {
		this(lineNum, badLine + ":\n" + errMsg);
	}
	
}
