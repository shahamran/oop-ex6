package oop.ex6.main.parsing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileParser {
	
	public ArrayList<String> readFile(File file) throws Exception {
		ArrayList<String> content = new ArrayList<String>();
		try (
				FileReader reader = new FileReader(file);
				Scanner scanner = new Scanner(reader)
				) {
			while (scanner.hasNext()) {
				content.add(scanner.nextLine());
			}
			return content;
		} catch (FileNotFoundException e) {
			throw new Exception();
		} catch (IOException e) {
			throw new Exception();
		}
	}
}
