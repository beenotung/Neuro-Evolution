package myutils;

import java.util.Random;
import java.util.Vector;

public class Utils {
	public static Random random = new Random(System.currentTimeMillis());

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
