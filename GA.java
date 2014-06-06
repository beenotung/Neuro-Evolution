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

// general utility
import java.util.Random;
import java.util.Arrays;

public class GA {

	/* Genetic Algorithm constant */
	public static final String version = "Genetic Algorithm V1.0\t --by Beeno Tung";
	public static final String inifilepath = "GA.ini";
	public static final String demoinifilepath = "DEMO.ini";
	public static final String fileformat = "UTF-8";

	/* Genetic Algorithm variable */
	public BigDecimal POPSIZE;
	public BigDecimal MAXGENS;
	public BigDecimal NSIG;
	public double PMUTATION;
	public double AMUTATION;
	public double CUTOFF;
	public Gen[] population;

	/* problem specific */
	public BigDecimal NVARS = two();// number of characteristic(s)
	public BigDecimal PRECISION;
	public BigDecimal Target;

	/*
	 * Gen code Class container of chromosome(s)
	 */
	public class Gen implements Comparable<Gen> {

		/* each gen's element */
		public BigDecimal[] code;
		public BigDecimal fitness;
		public boolean Survivor;

		public Gen(int Ncode, BigDecimal NSIG) {
			this.code = new BigDecimal[Ncode];
			for (int i = 0; i < Ncode; i++) {
				// this.code[i] = random_no_decimal_place(NSIG);
				this.code[i] = random_with_decimal_place(NSIG);
			}
		}

		public void check() {

		}

		// find fractional expression of target, PI in this example
		public void evaluate() {
			this.fitness = this.code[0].pow(2).add(this.code[0]).add(one).abs();
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
				System.out.println("Probability of Mutation : "
						+ this.PMUTATION);
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
				NVARS = new BigDecimal(s.trim());
				System.out.println("NVARS : " + NVARS);
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
		this.NVARS = NVARS;
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
			this.population[i] = new Gen(this.NVARS.intValue(), this.NSIG);
			// sleep(150);
		}
		System.out.println("\tFinished");
	}

	public void nextGeneration() {
		this.check();
		this.evaluate();
		// this.Rfitness();
		this.select_X(); // for crossover
		this.crossover_keepParent();
		// this.crossover_killParent();
		this.mutation();
	}

	public void report() {
		// System.out.println(this.population[0].code[0].divide(
		// this.population[0].code[1], 238, BigDecimal.ROUND_HALF_UP));
		gotorc(1, 1);
		for (int i = 0; i < this.population.length; i++) {
			// System.out.println(this.population[i].code[0].divide(this.population[i].code[1],
			// this.NSIG.intValue(), BigDecimal.ROUND_HALF_UP)+"     ");
			System.out.println(this.population[i].code[0] + "     ");
		}
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
				this.population[i] = crossover(this.population[a],
						this.population[b]);
			}
		}
	}

	public void crossover_killParent() {
		GA ga_new = new GA();
		for (int i = 0; i < this.population.length; i++) {
			if (!this.population[i].Survivor) {
				int a, b;
				do {
					a = random.nextInt(this.POPSIZE.intValue());
				} while (!this.population[a].Survivor);
				do {
					b = random.nextInt(this.POPSIZE.intValue());
				} while ((!this.population[b].Survivor) || (a == b));
				this.population[i] = crossover(this.population[a],
						this.population[b]);
			}
		}
	}

	public Gen crossover(Gen a, Gen b) {
		Gen result = new Gen(a.code.length, this.NSIG);
		for (int i = 0; i < result.code.length; i++) {
			result.code[i] = crossover(a.code[i], b.code[i]);
		}
		return result;
	}

	public BigDecimal crossover(BigDecimal a, BigDecimal b) {
		BigInteger a1=a.toBigInteger();
		BigInteger a2=a.subtract(new BigDecimal(a1)).scaleByPowerOfTen(a.scale()).toBigInteger();

		BigInteger b1=b.toBigInteger();
		BigInteger b2=b.subtract(new BigDecimal(b1)).scaleByPowerOfTen(b.scale()).toBigInteger();

		BigInteger c1=crossover(a1,b1);
		BigInteger c2=crossover(a2,b2);

		BigDecimal c=BigDecimal

		return new BigDecimal(c);
	}

	public BigInteger crossover(BigInteger a, BigInteger b) {
		String as = a.toString();
		String bs = b.toString();
		String cs = "";
		while ((cs.length() < as.length()) && (cs.length() < bs.length()))
		{
			if (random.nextBoolean()){
				cs+=as.charAt(cs.length());
			}
			else{
				cs+=bs.charAt(cs.length());
			}
		}
		while (cs.length()<as.length())
		{
			cs+=as.charAt(cs.length());
		}
		while (cs.length()<bs.length())
		{
			cs+=bs.charAt(cs.length());
		}
		return new BigInteger(String.valueOf(cs));
	}

	public void mutation() {
		for (int i = 1; i < this.population.length; i++) {
			if (random.nextDouble() <= this.PMUTATION) {
				for (int j = 0; j < this.population[i].code.length; j++) {
					char[] s = this.population[i].code[j].toString()
							.toCharArray();
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
		String s = "";
		for (int i = 0; i < l; i++) {
			s += "\u0008";
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

	public BigDecimal random_no_decimal_place(BigDecimal l) {
		BigDecimal result = new BigDecimal("0.0");
		for (BigDecimal i = zero(); !i.equals(l); i = i.add(one)) {
			result = result.multiply(ten()).add(
					new BigDecimal(random.nextInt(10)));
		}
		return result;
	}

	public static BigDecimal random_with_decimal_place(BigDecimal l) {
		int[] s = new int[l.intValue()];
		for (int i = 0; i < l.intValue(); i++) {
			s[i] = 10;
		}
		int li;
		do {
			int[] list = new int[32767];
			li = -1;
			for (int i = 0; i < l.intValue(); i++) {
				if (s[i] == 10) {
					list[++li] = i;
				}
			}
			if (li > 0) {
				s[list[random.nextInt(li + 1)]] = random.nextInt(10);
			} else {
				break;
			}
		} while (true);
		String str = "";

		for (int i = 0; i < l.intValue(); i++) {
			if (s[i] != 10) {
				str += Integer.toString(s[i]);
			} else {
				str += '.';
			}
		}
		return new BigDecimal(str);
	}

	public static void CreateDemo(String filepath, String coding) {
		System.out.print("Creating " + filepath + " ...");
		PrintWriter writer;
		try {
			writer = new PrintWriter(filepath, coding);
			writer.println("/* Genetic Algorithm constant */");
			writer.println("Population Size : 50");
			writer.println("Generation Limit (Iteration Steps) : 1000");
			writer.println("Length of each chromosome (detail level) : 20");
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
			target.PRECISION = target.NSIG.multiply(target.NVARS);
			System.out.println("Precision: " + target.PRECISION);
		} catch (IOException e) {
			System.out.println();
			System.out.println(filepath + " does not exist");
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					System.out.print("Closing FileReader on " + filepath
							+ " ...");
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
		for (BigDecimal IGENS = zero(); !IGENS.equals(ga.MAXGENS); IGENS = IGENS
				.add(one)) {
			// l = s.length();
			// s = IGENS.toString()+ ":  "+
			// ga.population[0].code[0].divide(ga.population[0].code[1],40,
			// BigDecimal.ROUND_HALF_UP);
			// System.out.print(BackSpace(l) + s);
			ga.nextGeneration();
			ga.report();
		}

		System.out.println("End.");
	}
}
