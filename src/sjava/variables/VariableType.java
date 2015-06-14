package sjava.variables;

public class VariableType<T> {
	private static final String STRING = "String", INT = "int", DOUBLE = "double", BOOLEAN = "boolean";
	private static final String[] ACCEPTED_TYPES = {STRING, INT, DOUBLE, BOOLEAN};
	private String myType;
	
	public VariableType(String typeName) throws BadVariableTypeException {
		for (String type : ACCEPTED_TYPES) {
			if (type.equals(typeName)) {
				myType = typeName;
				return;
			}
		}
		throw new BadVariableTypeException(typeName);
	}
	
	@SuppressWarnings("unchecked")
	public T stringToValue(String strVal) throws BadVariableAssignmentException {
		T value = null;
		try {
			switch (myType) {
			case STRING:
				value = (T) strVal;
				break;
			case INT:
				value = (T) Integer.valueOf(strVal);
				break;
			case DOUBLE:
				value = (T) Double.valueOf(strVal);
				break;
			case BOOLEAN:
				value = (T) Boolean.valueOf(strVal);
				break;
			}
			return value;
		} catch (IllegalArgumentException badVal) {
			throw new BadVariableAssignmentException(myType, strVal);
		}
	}
}
