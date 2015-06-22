package oop.ex6.main;

/**
 * This is thrown if a certain SJava code is illegal.
 */
public class IllegalCodeException extends SJavaException {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructs a new exception with a default message
	 */
	public IllegalCodeException() {
		super("Illegal S-Java code!");
	}
	
	/**
	 * Constructs a new exception with a given message
	 * @param msg The message
	 */
	public IllegalCodeException(String msg) {
		super(msg);
	}
	
	/**
	 * Contructs a new exception specifying in what line something went wrong, and shows the line.
	 * @param lineNum The line number
	 * @param badLine The string of the bad line.
	 */
	public IllegalCodeException(int lineNum, String badLine) {
		super("Error in line " + lineNum + ": " + badLine);
	}
	
	/**
	 * Contructs a new exception specifying in what line something went wrong, and shows the line.
	 * Also prints an informative message
	 * @param lineNum The line number
	 * @param badLine The string of the bad line.
	 * @param errMsg The informative message.
	 */
	public IllegalCodeException(int lineNum, String badLine, String errMsg) {
		this(lineNum, badLine + ":\n" + errMsg);
	}
	
}
