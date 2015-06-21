import java.util.Scanner;
import java.util.regex.*;

public class RegexTest {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		Pattern p = Pattern.compile("\\s*;\\s*$");
		System.out.println(p.matcher(scanner.nextLine()).find());
		scanner.close();
	}
}
