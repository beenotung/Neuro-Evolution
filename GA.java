// math
import java.math.BigDecimal;
import java.math.BigInteger;

// string regex
import java.util.regex.Pattern;
import java.util.regex.Matcher;

// io utility
//import java.io.Console;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import java.util.BitSet;
// general utility
import java.util.Random;
import java.util.Arrays;
import java.util.Vector;

public class GA {

	/* Genetic Algorithm constant */
	public static final String version = "Genetic Algorithm V3.0\t --by Beeno Tung";
	public static final String inifilepath = "GA.ini";
	public static final String demoinifilepath = "DEMO.ini";
	public static final String fileformat = "UTF-8";

	/* Genetic Algorithm variable */
	public BigInteger POPSIZE;
	public BigInteger MAXGENS;
	public BigInteger NSIG;
	public double PMUTATION;
	public double AMUTATION;
	public double CUTOFF;
	public Gen[] population;

	/* problem specific */
	public BigInteger NVAR;// number of characteristic(s)
	public BigInteger PRECISION;
	public BigDecimal Target;

	/*
	 * Gen code Class container of chromosome(s)
	 */
	public class Gen implements Comparable<Gen> {

		/* each gen's element */
		// public BigDecimal[] code;
		public BigInteger NSIG;
		public BitSet[] code;
		public BigDecimal fitness;
		public boolean Survivor;

		public Gen(int NVAR, BigInteger NSIG) {
			// this.code = new BigDecimal[NVAR];
			this.NSIG = NSIG;
			this.code = new BitSet[NVAR];
			for (int i = 0; i < NVAR; i++) {
				// this.code[i] = random_no_decimal_place(NSIG);
				// this.code[i] = random_with_decimal_place(NSIG);
				this.code[i] = random_bitset(NSIG);
			}
		}

		public void check() {

		}

		// find fractional expression of target, PI in this example
		public void evaluate() {
			this.fitness = this.code[0].pow(2).add(this.code[0]).subtract(one).abs();
			// this.fitness =
			// this.code[0].divide(this.code[1],Math.min(this.code[0].precision()+
			// this.code[1].precision(),Integer.MAX_VALUE),BigDecimal.ROUND_HALF_UP).subtract(Target).abs();
		}

		@Override
		public int compareTo(Gen target) {
			/* ascending order */
			return this.fitness.compareTo(target.fitness);

			/* descending order */
			// return -this.fitness.compareTo(target.fitness);
		}
	} /* end of class Gen */

	public void SetVar(String sfromfile) {
		Pattern MY_PATTERN = Pattern.compile(":(.*)");
		Matcher m = MY_PATTERN.matcher(sfromfile);
		while (m.find()) {
			String s = m.group(1);
			// s now contains " <value of parameter>"
			if (sfromfile.matches("Population Size ?:.*")) {
				this.POPSIZE = new BigDecimal(s.trim());
				System.out.println("Population Size : " + this.POPSIZE);
			}
			if (sfromfile.matches("Generation Limit ?.* ?:.*")) {
				this.MAXGENS = new BigDecimal(s.trim());
				System.out.println("Generation Limit : " + this.MAXGENS);
			}
			if (sfromfile.matches("Length of each chromosome ?.* ?:.*")) {
				this.NSIG = new BigDecimal(s.trim());
				System.out.println("Length of each chromosome : " + this.NSIG);
			}
			if (sfromfile.matches("Probability of Mutation ?.* ?:.*")) {
				this.PMUTATION = Double.parseDouble(s.trim());
				System.out.println("Probability of Mutation : " + this.PMUTATION);
			}
			if (sfromfile.matches("Amount of Mutation ?.* ?:.*")) {
				this.AMUTATION = Double.parseDouble(s.trim());
				System.out.println("Amount of Mutation : " + this.AMUTATION);
			}
			if (sfromfile.matches("Cutoff ?.* ?:.*")) {
				this.CUTOFF = Double.parseDouble(s.trim());
				System.out.println("CUTOFF : " + this.CUTOFF);
			}
			if (sfromfile.matches("NVARS ?:.*")) {
				NVAR = new BigDecimal(s.trim());
				System.out.println("NVARS : " + NVAR);
			}
			if (sfromfile.matches("Target ?:.*")) {
				Target = new BigDecimal(s.trim());
				System.out.println("Target : " + Target);
			}
		}
	}

	public GA() {
		super();
	}

	public GA(BigDecimal POPSIZE, BigDecimal NVARS) {
		this.POPSIZE = POPSIZE;
		this.NVAR = NVARS;
		this.initialize();
	}

	public void initialize() {
		/* initialize */
		String amoung = "" + this.POPSIZE;
		this.population = new Gen[this.POPSIZE.intValue()];
		System.out.print("Generating Gens...1/" + amoung);
		for (int i = 0; i < this.POPSIZE.intValue(); i++) {
			String s = (i) + "/" + this.POPSIZE;
			int l = s.length();
			s = "";
			for (int j = 0; j < l; j++) {
				s += "\u0008";
			}
			// console.printf("%s", s + i.add(one) + "/" + amoung);
			System.out.print(s + (i + 1) + "/" + amoung);
			this.population[i] = new Gen(this.NVAR.intValue(), this.NSIG);
			// sleep(150);
		}
		System.out.println("\tFinished");
	}

	public void nextGeneration() {
		this.check();
		this.evaluate();
		// this.Rfitness();
		this.select_X(); // for crossover
		// this.crossover_keepParent();
		this.crossover_killParent();
		this.mutation();
	}

	public void report() {
		// System.out.println(this.population[0].code[0].divide(
		// this.population[0].code[1], 238, BigDecimal.ROUND_HALF_UP));
		// gotorc(1, 1);
		// for (int i = 0; i < this.population.length; i++) {
		for (int i = 0; i < 60; i++) {
			// System.out.println(this.population[i].code[0].divide(this.population[i].code[1],
			// this.NSIG.intValue(), BigDecimal.ROUND_HALF_UP)+"     ");
			System.out.println(this.population[i].code[0] + Space(5));
			// System.out.println(this.population[i].code[0]);// + "        ");
			// System.out.println(this.population[i].fitness + "           ");
		}
		System.out.println(Space(50));
		// sleep(150);

		/*
		 * for (Gen i : this.population) { System.out.println(i.fitness); }
		 */
	}

	public void check() {
		for (int i = 0; i < this.population.length; i++)
			this.population[i].check();
	}

	public String fix(String ori) {
		char[] c = new char[ori.length()];
		int[] list = new int[c.length];
		int li;
		do {
			li = -1;
			for (int i = 0; i < c.length; i++) {

				if (c[i] == '.') {
					list[++li] = i;
				}
			}
			if (li > 0) {
				c[list[random.nextInt(li + 1)]] = (char) (random.nextInt(10) + 48);
			} else {
				break;
			}
		} while (true);
		return String.valueOf(c);
	}

	public void evaluate() {
		// int amoung = this.population.length;
		// System.out.print("Evaluating Gens...");
		// String s = "";
		// int l;
		for (int i = 0; i < this.population.length; i++) {
			// l = s.length();
			// s = (i + 1) + "/" + amoung;
			// System.out.print(BackSpace(l) + s);
			this.population[i].evaluate();
		}
		// System.out.print(BackSpace(("Evaluating Gens..." + s).length()));
	}

	public void select_X() {
		Arrays.sort(this.population);
		for (int i = 0; i < this.population.length; i++) {
			this.population[i].Survivor = ((double) i / this.population.length) <= this.CUTOFF;
		}
		this.population[0].Survivor = true;
	}

	public void crossover_keepParent() {
		for (int i = 0; i < this.population.length; i++) {
			if (!this.population[i].Survivor) {
				int a, b;
				do {
					a = random.nextInt(this.POPSIZE.intValue());
				} while (!this.population[a].Survivor);
				do {
					b = random.nextInt(this.POPSIZE.intValue());
				} while ((!this.population[b].Survivor) || (a == b));
				this.population[i] = crossover(this.population[a], this.population[b]);
			}
		}
	}

	public void crossover_killParent() {
		Gen[] pop_new = new Gen[this.population.length];
		for (int i = 0; i < this.population.length; i++) {
			int a;
			do {
				a = random.nextInt(this.POPSIZE.intValue());
			} while ((!this.population[a].Survivor) || (a == i));
			pop_new[i] = crossover(this.population[a], this.population[i]);
		}
		this.population = pop_new;
	}

	public Gen crossover(Gen a, Gen b) {
		Gen result = new Gen(a.code.length, this.NSIG);
		for (int i = 0; i < result.code.length; i++) {
			result.code[i] = crossover(a.code[i], b.code[i]);
		}
		return result;
	}

	public BigDecimal crossover(BigDecimal a, BigDecimal b) {
		BigInteger a1 = a.toBigInteger();
		BigInteger a2 = a.subtract(new BigDecimal(a1)).scaleByPowerOfTen(a.scale()).toBigInteger();

		BigInteger b1 = b.toBigInteger();
		BigInteger b2 = b.subtract(new BigDecimal(b1)).scaleByPowerOfTen(b.scale()).toBigInteger();

		BigDecimal c1 = new BigDecimal(crossover_keepsame(a1, b1));
		BigDecimal c2 = new BigDecimal(crossover_keepmore(a2, b2));

		BigDecimal c = c1.add(c2.movePointLeft(c2.precision()));

		return c;
	}

	public BigInteger crossover_keepsame(BigInteger a, BigInteger b) {
		String as = a.toString();
		String bs = b.toString();
		String cs = "";
		int al = random.nextInt(as.length() + 1);
		int bl = random.nextInt(bs.length() + 1);
		// /
		// System.out.println();
		// System.out.println("as="+as);
		// System.out.println("bs="+bs);
		// /
		if (random.nextBoolean()) {
			while (cs.length() < al)
				cs += as.charAt(cs.length());
			// System.out.println("al="+al);
			// System.out.println("cs="+cs);
			// System.out.println("bl="+bl);
			while (bl < bs.length())
				cs += bs.charAt(bl++);
			// System.out.println("cs="+cs);
		} else {
			while (cs.length() < bl)
				cs += bs.charAt(cs.length());
			// System.out.println("bl="+bl);
			// System.out.println("cs="+cs);
			// System.out.println("al="+al);
			while (al < as.length())
				cs += as.charAt(al++);
			// System.out.println("cs="+cs);
		}
		return new BigInteger(String.valueOf("0" + cs));
	}

	public BigInteger crossover_keepmore(BigInteger a, BigInteger b) {
		String as = a.toString();
		String bs = b.toString();
		String cs = "";
		while ((cs.length() < as.length()) && (cs.length() < bs.length())) {
			if (random.nextBoolean()) {
				cs += as.charAt(cs.length());
			} else {
				cs += bs.charAt(cs.length());
			}
		}
		while (cs.length() < as.length()) {
			cs += as.charAt(cs.length());
		}
		while (cs.length() < bs.length()) {
			cs += bs.charAt(cs.length());
		}
		// System.out.println();
		// System.out.println(as);
		// System.out.println(bs);
		// System.out.println(cs);
		return new BigInteger(String.valueOf(cs));
	}

	public void mutation() {
		for (int i = 0; i < this.population.length; i++) {
			if (random.nextDouble() <= this.PMUTATION) {
				for (int j = 0; j < this.population[i].code.length; j++) {
					char[] s = this.population[i].code[j].toString().toCharArray();
					for (int k = 0; k < s.length; k++) {
						if (random.nextDouble() <= this.AMUTATION)
							s[k] = (char) (random.nextInt(10) + 48);
					}
					this.population[i].code[j] = new BigDecimal(s);
				}
			}
		}
	}

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

	public static void sleep(long a) {
		try {
			Thread.sleep(a);
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

	public final static void gotorc(int x, int y) {
		System.out.print((char) 27 + "[" + x + ";" + y + "H");
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

	/*
	 * public BigDecimal random_no_decimal_place(BigDecimal l) { BigDecimal
	 * result = new BigDecimal("0.0"); for (BigDecimal i = zero(); !i.equals(l);
	 * i = i.add(one)) { result = result.multiply(ten()).add(new
	 * BigDecimal(random.nextInt(10))); } return result; }
	 *
	 * public static BigDecimal random_with_decimal_place(BigDecimal l) { int[]
	 * s = new int[l.intValue()]; for (int i = 0; i < l.intValue(); i++) { s[i]
	 * = 10; } int li; do { int[] list = new int[32767]; li = -1; for (int i =
	 * 0; i < l.intValue(); i++) { if (s[i] == 10) { list[++li] = i; } } if (li
	 * > 0) { s[list[random.nextInt(li + 1)]] = random.nextInt(10); } else {
	 * break; } } while (true); String str = "";
	 *
	 * for (int i = 0; i < l.intValue(); i++) { if (s[i] != 10) { str +=
	 * Integer.toString(s[i]); } else { str += '.'; } } return new
	 * BigDecimal(str); }
	 */

	public static void CreateDemo(String filepath, String coding) {
		System.out.print("Creating " + filepath + " ...");
		PrintWriter writer;
		try {
			writer = new PrintWriter(filepath, coding);
			writer.println("/* Genetic Algorithm constant */");
			writer.println("Population Size : 50");
			writer.println("Generation Limit (Iteration Steps) : 1000");
			writer.println("Length of each chromosome (num of bits) : 64");
			writer.println("Probability of Mutation : 0.02");
			writer.println("Amount of Mutation : 0.1");
			writer.println("Cutoff : 0.25");
			writer.println("");
			writer.println("/* problem specific */");
			writer.println("NVARS : 2");
			writer.println("Target : 3.14");
			writer.close();
			System.out.println("\tFinished");
		} catch (FileNotFoundException e) {
			System.out.println();
			System.out.println(filepath + " does not exist");
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			System.out.println();
			System.out.println(coding + " is not supported");
			e.printStackTrace();
		}
	} /* end of CreateDemo() */

	public static void ReadFromINI(String filepath, GA target) {
		System.out.println("Reading from " + filepath + " ...");
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filepath));
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				target.SetVar(sCurrentLine);
			}
			target.PRECISION = target.NSIG.multiply(target.NVAR);
			System.out.println("Precision: " + target.PRECISION);
		} catch (IOException e) {
			System.out.println();
			System.out.println(filepath + " does not exist");
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					System.out.print("Closing FileReader on " + filepath + " ...");
					br.close();
					System.out.println("\tFinished");
				}
			} catch (IOException ex) {
				System.out.println("FileReader not open");
				ex.printStackTrace();
			}
		}
	} /* end of ReadFromINI(string) */

	/*
	 * standard program entry point
	 */

	public static void main(String[] args) {
		System.out.println();
		System.out.println(version);
		System.out.println();

		CreateDemo(demoinifilepath, fileformat);

		GA ga = new GA();
		ReadFromINI(inifilepath, ga);
		System.out.println();

		/* initialize */
		ga.initialize();

		/* breeding */
		System.out.print("\nBreeding...");
		// System.out.print("\nBest value of Generation ");
		// String s = "";
		// int l;
		for (BigDecimal IGENS = zero(); !IGENS.equals(ga.MAXGENS); IGENS = IGENS.add(one)) {
			// l = s.length();
			// s = IGENS.toString()+ ":  "+
			// ga.population[0].code[0].divide(ga.population[0].code[1],40,
			// BigDecimal.ROUND_HALF_UP);
			// System.out.print(BackSpace(l) + s);
			ga.nextGeneration();
			gotorc(1, 1);
			System.out.println(IGENS + "     ");
			ga.report();
		}

		System.out.println("End.");
	}
}
