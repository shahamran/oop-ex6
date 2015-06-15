package oop.ex6.main;

public class IllegalCodeException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public IllegalCodeException() {
		super("Illegal S-Java code!");
	}
	
	public IllegalCodeException(String msg) {
		super(msg);
	}
	
}
