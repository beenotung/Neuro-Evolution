package myutils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

public class Utils {
	public static Random random = new Random(System.currentTimeMillis());

	public static String readFileAsString(String filename) throws IOException {
		String content = "";
		List<String> lines = readFileAsStrings(filename);
		for (String line : lines)
			content += " " + line + " ";
		return content;
	}

	public static List<String> readFileAsStrings(String filename) throws IOException {
		return Files.readAllLines(Paths.get(filename), Charset.defaultCharset());
	}
}
