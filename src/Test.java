import java.util.HashSet;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
	private static final String NAME_STR_REGEX = "(?:(?:[A-Za-z]\\w*)|(?:_\\w+))",
			TYPE_STR_REGEX = "^[A-Za-z]+",
			ASSIGN_STR_REGEX = "=\\s*[^,]+", LINE_END_REGEX = ";$",
			INIT_STR_REGEX = "(?:" + NAME_STR_REGEX + ")\\s*" + "(?:" + ASSIGN_STR_REGEX + ")?",
			LINE_STR_REGEX = TYPE_STR_REGEX + "\\s+" + INIT_STR_REGEX + 
			"\\s*(?:,\\s*" + INIT_STR_REGEX + ")*" + "\\s*" + LINE_END_REGEX;
	private static final Pattern initPattern = Pattern.compile(INIT_STR_REGEX),
			 					typePattern = Pattern.compile(TYPE_STR_REGEX),
			 					linePattern = Pattern.compile(LINE_STR_REGEX);
	private static final int TYPE_ARG = 0, NAME_ARG = 1, ASSIGN_ARG = 2, VALUE_ARG = 3, MIN_ARGS = 2;
	private static final String[] validTypes = {"String", "int", "double", "boolean"};
	private static Scanner scanner = new Scanner(System.in);
	
	public static void main(String[] args) {
		String userInput;
		System.out.println("String to search:");
		while (!(userInput = scanner.nextLine()).equals("end")) {
			foo(userInput);
		}
		
	}
	
	
	private static void foo(String line) {
		Matcher match = linePattern.matcher(line);
		if (!match.find()) {
			System.out.println("Not found");
			return;
		}
		match = typePattern.matcher(line);
		match.find();
		System.out.println("Type: " + match.group());
		line = line.substring(match.end(), line.length());
		match = initPattern.matcher(line);
		while (match.find()) {
			System.out.println(match.group());
			line = line.substring(match.end(), line.length());
			match = initPattern.matcher(line);
		}
	}
	
	private static boolean isValidType(String aType) {
		for (String type : validTypes) {
			if (type.equals(aType))
				return true;
		}
		return false;
	}
}