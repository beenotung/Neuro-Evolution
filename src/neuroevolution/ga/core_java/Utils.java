package neuroevolution.ga.core_java;

import java.math.BigDecimal;
import java.util.BitSet;
import java.util.Random;

public class Utils {
	/* utility variables */
	// public static Console console = System.console();
	public static final BigDecimal zero = new BigDecimal("0");
	public static final BigDecimal one = new BigDecimal("1");
	public static final BigDecimal two = new BigDecimal("2");
	public static final BigDecimal ten = new BigDecimal("10");
	public static BigDecimal idCount = zero();
	public static int GB = (int) Math.pow(2, 30);

	public static Random random = new Random(System.currentTimeMillis());

	/* utility methods */
	public final static void gotorc(int x, int y) {
		System.out.print((char) 27 + "[" + x + ";" + y + "H");
	}

	public static void sleep(long a) {
		try {
			Thread.sleep(a);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}

	public static void sleep() {
		try {
			Thread.sleep(GA.GENERAL_INTERVAL);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}

	public static String BackSpace(int l) {
		return man_char('\u0008', l);
	}

	public static String Space(int l) {
		return man_char(' ', l);
	}

	public static String man_char(char c, int l) {
		String s = "";
		for (int i = 0; i < l; i++) {
			s += c;
		}
		return s;
	}

	public static BigDecimal zero() {
		return new BigDecimal("0");
	}

	public static BigDecimal one() {
		return new BigDecimal("1");
	}

	public static BigDecimal two() {
		return new BigDecimal("2");
	}

	public static BigDecimal ten() {
		return new BigDecimal("10");
	}

	public static BitSet random_bitset(int l) {
		BitSet result = new BitSet();
		for (int i = 0; i < l; i++) {
			if (random.nextBoolean())
				result.set(i, true);
		}
		return result;
	}

	public static long rate2ms(double rate) {
		// return (long)((1.0-rate)*1000);
		return (long) (rate * 1000);
	}

	public static boolean someAlive(MyRunnable[] list) {
		boolean alive = false;
		for (int i = 0; i < GA.NThread; i++)
			alive |= list[i].isAlive();
		return alive;
	}
}
