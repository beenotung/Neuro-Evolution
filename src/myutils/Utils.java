package myutils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.Vector;

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

	public static Vector<Object> getShuffled(Vector<Object> ori) {
		Vector<Object> result = new Vector<Object>();
		for (Object o : ori)
			result.add(o);
		int t;
		Object tmp;
		int size = ori.size();
		for (int i = 0; i < size; i++) {
			t = random.nextInt(size);
			tmp = result.get(i);
			result.set(i, result.get(t));
			result.set(t, tmp);
		}
		return result;
	}
}
