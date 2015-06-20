package oop.ex6.main.parsing;

import java.io.*;
import java.util.*;
import java.util.regex.*;

public class FileParser {
	private List<String> fileContent = new ArrayList<String>();
	private static final String SJAVA_SUFFIX_REGEX = "(?:\\.sjava)$", COMMENT_LINE = "^(?:\\s*//)";
	private static Pattern sjavaFilePattern = Pattern.compile(SJAVA_SUFFIX_REGEX),
						   commentPattern = Pattern.compile(COMMENT_LINE),
						   hasNonWhitespacePattern = Pattern.compile("\\S");
	
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
	
	public List<String> getFileContent() {
		return fileContent;
	}
	
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
	
	private static boolean isMatch(Pattern p, String s) {
		Matcher m = p.matcher(s);
		return m.find();
	}
}
