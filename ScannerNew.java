import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScannerNew {

	private static final char[] SPECIAL_CHARACTERS = {'*', '&', '+', '-', '=', '#', '<', '>', ':', ';', ',', '.', '(', ')'};

	private static final List<String> KEYWORDS = Arrays.asList("begin", "end", "program", "var", "integer", "string");

	public static boolean isCharacter(char c) {
		for (char specialChar : SPECIAL_CHARACTERS) {
			if (c == specialChar) {
				return true;
			}
		}
		return false;
	}

	public static boolean isKeyword(String s) {
		return KEYWORDS.contains(s);
	}

	public static List<Element> get(String path) throws FileNotFoundException {
		File text = new File(path);
		java.util.Scanner scanner = new java.util.Scanner(text);
		List<Element> cells = new ArrayList<>();

		while (scanner.hasNext()) {
			String str = scanner.next();
			char[] myChar = str.toCharArray();
			int index = 0;

			while (index < myChar.length) {
				StringBuilder lexeme = new StringBuilder();

				if (Character.isLetter(myChar[index])) {
					while (index < myChar.length &&
							(Character.isLetter(myChar[index]) || Character.isDigit(myChar[index]))) {
						lexeme.append(myChar[index]);
						index++;
					}

					if (isKeyword(lexeme.toString())) {
						cells.add(new Element(lexeme.toString(), "special"));
					} else {
						cells.add(new Element(lexeme.toString(), "identifier"));
					}
				} else if (Character.isDigit(myChar[index])) {
					while (index < myChar.length && Character.isDigit(myChar[index])) {
						lexeme.append(myChar[index]);
						index++;
					}
					cells.add(new Element(lexeme.toString(), "number"));
				} else if (isCharacter(myChar[index])) {
					lexeme.append(myChar[index]);

					if (index + 1 < myChar.length && (myChar[index] == '<' || myChar[index] == '>' || myChar[index] == ':')
							&& myChar[index + 1] == '=') {
						lexeme.append(myChar[index + 1]);
						index += 2;
					} else {
						index++;
					}

					cells.add(new Element(lexeme.toString(), "operator"));
				} else {
					lexeme.append(myChar[index]);
					index++;
				}
			}
		}

		return cells;
	}
}
