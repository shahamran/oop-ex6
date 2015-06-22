package oop.ex6.main.inner_scopes;

import oop.ex6.main.inner_scopes.InnerScopeFactory.ValidInnerScope;

/**
 * Thrown when trying to define an inner scope with an invalid argument (something other than boolean)
 */
public class BadInnerScopeArgumentException extends IllegalInnerScopeException {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new exception with the given scope and specifying the bad argument(s)
	 * @param scope The inner scope object
	 * @param badArg The argument.
	 */
	public BadInnerScopeArgumentException(ValidInnerScope scope, String badArg) {
		super("Can't define '" + scope.toString() + "' block with the argument(s): " + badArg);
	}
}
