package sjava.variables;

public class VariableFactory {
	private static final String STRING = "String", INT = "int", DOUBLE = "double", BOOLEAN = "boolean";
	
	public static Variable<?> createVariable(String[] variableArgs) throws VariableException {
		if (variableArgs == null || variableArgs.length < 2) {
			throw new BadVariableAssignmentException("","");
		}
		String type = variableArgs[0], name = variableArgs[1], value = variableArgs[2];
		try {
			switch (type) {
			case STRING:
				return new Variable<String>(name, value);
			case INT:
				return new Variable<Integer>(name, Integer.parseInt(value));
			case DOUBLE:
				return new Variable<Double>(name, Double.parseDouble(value));
			case BOOLEAN:
				return new Variable<Boolean>(name, Boolean.parseBoolean(value));
				default:
					throw new BadVariableTypeException(type);
			}
		} catch (IllegalArgumentException e) {
			throw new BadVariableAssignmentException(type, value);
		}
	}
}
