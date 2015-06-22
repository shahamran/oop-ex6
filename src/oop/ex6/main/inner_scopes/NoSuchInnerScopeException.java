package oop.ex6.main.inner_scopes;

/**
 * Thrown when trying to open an inner scope with an invalid name (not if/while, in our case)
 */
public class NoSuchInnerScopeException extends IllegalInnerScopeException {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructs a new exception specifying the illegal scope name.
	 * @param innerScopeName The bad scope name given
	 */
	public NoSuchInnerScopeException(String innerScopeName) {
		super("Illegal block: '" + innerScopeName + "'");
	}

}
