package oop.ex6.main.parsing;

import java.io.*;
import java.util.*;
import java.util.regex.*;

/**
 *
 */
public class FileParser {
	private List<String> fileContent = new ArrayList<String>();
	private static final String SJAVA_SUFFIX_REGEX = "(?:\\.sjava)$", COMMENT_LINE = "^(?:\\s*//)";
	private static Pattern sjavaFilePattern = Pattern.compile(SJAVA_SUFFIX_REGEX),
						   commentPattern = Pattern.compile(COMMENT_LINE),
						   hasNonWhitespacePattern = Pattern.compile("\\S");
	
	/**
	 * Constructs a new FileParser object and tries to parse the file.
	 * @param pathName The path of a SJava file.
	 * @throws IllegalSJavaFileException If the file given was not a valid SJava file.
	 */
	public FileParser(String pathName) throws IllegalSJavaFileException {			
		File myFile = new File(pathName);
		if (myFile.exists() && myFile.isFile() && isMatch(sjavaFilePattern, pathName)) {
			try {
				fileContent = readFile(myFile);
			} catch (IOException e) {
				throw new IllegalSJavaFileException(pathName);
			}
		} else {
			throw new IllegalSJavaFileException(pathName);
		}
	}
	
	/**
	 * @return A trimmed list of content. Each element in this list is a line. Comment lines 
	 * and whitespace lines are excluded.
	 */
	public List<String> getFileContent() {
		return fileContent;
	}
	
	/**
	 * Reads the file line by line and adds it to the contents list.
	 * @param file The sjava file.
	 * @return A list of the file's lines, excluding whitespace and comment lines.
	 * @throws IOException
	 */
	private ArrayList<String> readFile(File file) throws IOException {
		ArrayList<String> content = new ArrayList<String>();
		try (
				FileReader reader = new FileReader(file);
				Scanner scanner = new Scanner(reader)
				) {
			String line;
			while (scanner.hasNext()) {
				line = scanner.nextLine();
				if (isMatch(commentPattern,line) || !isMatch(hasNonWhitespacePattern,line))
					continue;
				content.add(line);
			}
			return content;
		} catch (IOException e) {
			throw e;
		}
	}
	
	/**
	 * @param p A regex pattern to match.
	 * @param s The string to check.
	 * @return True if the string matches the pattern, false otherwise.
	 */
	private static boolean isMatch(Pattern p, String s) {
		Matcher m = p.matcher(s);
		return m.find();
	}
}
