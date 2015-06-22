package oop.ex6.main;

public class UnbalancedBracketsException extends IllegalCodeException {
	private static final long serialVersionUID = 1L;
	
	public UnbalancedBracketsException() {
		super("Unbalanced brackets in file");
	}
	
	public UnbalancedBracketsException(int lineNum) {
		super("Unbalanced bracket token in line: " + lineNum);
	}

}
