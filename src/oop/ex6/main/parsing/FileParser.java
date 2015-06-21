package oop.ex6.main.parsing;

import java.io.*;
import java.util.*;
import java.util.regex.*;

/**
 * Parses the SJava file by lines, ignoring whitespace lines and comments.
 */
public class FileParser {
	private static final String SJAVA_SUFFIX_REGEX = "(?:\\.sjava)$", COMMENT_LINE = "^(?:\\s*//)";
	private static Pattern sjavaFilePattern = Pattern.compile(SJAVA_SUFFIX_REGEX),
						   commentPattern = Pattern.compile(COMMENT_LINE),
						   hasNonWhitespacePattern = Pattern.compile("\\S");
	
	/**
	 * Gets a file path and reads the file's content. Returns a trimmed list of lines.
	 * @param filePath The path for the SJava file.
	 * @return A trimmed list of content. Each element in this list is a line. Comment lines 
	 * and whitespace lines are excluded.
	 * @throws IllegalSJavaFileException If the file given is not a valid SJava file. 
	 */
	public static List<String> getFileContent(String filePath) throws IllegalSJavaFileException {
		File myFile = new File(filePath);
		if (myFile.exists() && myFile.isFile() && isMatch(sjavaFilePattern, filePath)) {
			try {
				return readFile(myFile);
			} catch (IOException e) {
				throw new IllegalSJavaFileException(filePath);
			}
		} else {
			throw new IllegalSJavaFileException(filePath);
		}
	}
	
	/**
	 * Reads the file line by line and adds it to the contents list.
	 * @param file The SJava file object.
	 * @return A list of the file's lines, excluding whitespace and comment lines.
	 * @throws IOException
	 */
	private static ArrayList<String> readFile(File file) throws IOException {
		ArrayList<String> content = new ArrayList<String>();
		try ( // Try with resources...
				FileReader reader = new FileReader(file);
				BufferedReader br = new BufferedReader(reader);
				Scanner scanner = new Scanner(br) 
													) {
			String line;
			while (scanner.hasNext()) {
				line = scanner.nextLine();
				if (isMatch(commentPattern,line) || !isMatch(hasNonWhitespacePattern,line))
					continue; // Skip comments and whitespace lines
				content.add(line);
			}
			return content;
		} catch (IOException e) {
			throw e;
		}
	}
	
	/**
	 * A small helper method to check for pattern matches in a string.
	 * @param p A regex pattern to match.
	 * @param s The string to check.
	 * @return True if the string matches the pattern, false otherwise.
	 */
	private static boolean isMatch(Pattern p, String s) {
		Matcher m = p.matcher(s);
		return m.find();
	}
}
