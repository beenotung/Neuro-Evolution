package myutils.file;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class MyFileUtils {
	public static String readFileAsString(String filename) throws IOException {
		String content = "";
		List<String> lines = Files.readAllLines(Paths.get(filename),
				Charset.defaultCharset());
		for (String line : lines)
			content += " " + line + " ";
		return content;
	}
}
