package myutils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FileUtils {
	public static String readFileAsString(String filename) throws IOException {
		return FileUtils.readFileAsString(filename, " ");
	}

	public static String readFileAsString(String filename, String newline)
			throws IOException {
		String content = "";
		List<String> lines = readFileAsStrings(filename);
		for (String line : lines)
			content += line + newline;
		return content;
	}

	public static List<String> readFileAsStrings(String filename) throws IOException {
		return Files.readAllLines(Paths.get(filename), Charset.defaultCharset());
	}
}
