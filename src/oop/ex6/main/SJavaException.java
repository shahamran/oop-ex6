package oop.ex6.main;

/**
 * An abstract super class for all SJava exceptions.
 */
public abstract class SJavaException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public SJavaException(String msg) {
		super(msg);
	}
}
