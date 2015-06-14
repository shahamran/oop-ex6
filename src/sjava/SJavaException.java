package sjava;

public abstract class SJavaException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public SJavaException(String msg) {
		super(msg);
	}
	
}
