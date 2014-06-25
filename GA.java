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

public class GA {

	/* Genetic Algorithm constant */
	public static final String version = "Genetic Algorithm V3.0\t --by Beeno Tung";
	public static final String inifilepath = "GA.ini";
	public static final String demoinifilepath = "DEMO.ini";
	public static final String fileformat = "UTF-8";

	/* Genetic Algorithm variable */
	public BigInteger POPSIZE;
	public BigInteger MAXGENS;
	public BigInteger NSIGB;
	public BigInteger NSIGA;
	public double PMUTATION;
	public double AMUTATION;
	public double CUTOFF;
	public Gen[] population;

	/* Genetic Algorithm option */
	public String Mode_Crossover;
	public String Mode_Mutation;

	/* Interface option */
	public double Report_rate = 1;
	public boolean Report_code = false;
	public char[] Report_survivor = new char[] { ' ', ' ' };
	public boolean Report_value = false;
	public boolean Report_fitness = false;

	/* problem specific */
	public BigInteger NVAR;// dimension of the problem
	public BigDecimal[] Target = { new BigDecimal(40), new BigDecimal(190), new BigDecimal(418), new BigDecimal(1796) };
	public BigDecimal[][] Example = { { new BigDecimal(1), new BigDecimal(2), new BigDecimal(3), new BigDecimal(4) },
			{ new BigDecimal(2), new BigDecimal(6), new BigDecimal(12), new BigDecimal(24) },
			{ new BigDecimal(12), new BigDecimal(72), new BigDecimal(17), new BigDecimal(22) },
			{ new BigDecimal(864), new BigDecimal(8), new BigDecimal(6), new BigDecimal(4) } };

	/*
	 * Gen code Class container of chromosome(s)
	 */
	public class Gen implements Comparable<Gen> {

		/* each gen's element */
		// public BigDecimal[] code;
		public BigInteger NSIGB;
		public BigInteger NSIGA;
		public BitSet[][] code;
		public BigDecimal fitness;
		public boolean Survivor;

		public Gen(BigInteger NVAR, BigInteger NSIGB, BigInteger NSIGA) {
			// this.code = new BigDecimal[NVAR];
			this.NSIGB = NSIGB;
			this.NSIGA = NSIGA;
			this.code = new BitSet[NVAR.intValue()][2];
			for (int i = 0; i < NVAR.intValue(); i++) {
				// this.code[i] = random_no_decimal_place(NSIG);
				// this.code[i] = random_with_decimal_place(NSIG);
				this.code[i][0] = random_bitset(NSIGB.intValue());
				this.code[i][1] = random_bitset(NSIGA.intValue());
			}
		}

		public void check() {

		}

		public BigDecimal getvalue(int var) {
			BigDecimal value = zero();
			BigDecimal rate = one();
			for (int i = this.NSIGB.intValue() - 1; i >= 0; i--) {
				if (this.code[var][0].get(i))
					value = value.add(rate);
				rate = rate.multiply(two);
			}
			rate = one.divide(two);
			for (int i = 0; i < this.NSIGA.intValue(); i++) {
				if (this.code[var][1].get(i))
					value = value.add(rate);
				rate = rate.divide(two);
			}
			return value;
		}

		public String getcode(int var) {
			String result = "";
			for (int i = 0; i < this.NSIGB.intValue(); i++) {
				result += (this.code[var][0].get(i)) ? "1" : 0;
			}
			result += ".";
			for (int i = 0; i < this.NSIGA.intValue(); i++) {
				result += (this.code[var][1].get(i)) ? "1" : 0;
			}
			return result;
		}

		// find fractional expression of target, PI in this example
		public void evaluate() {
			// this.fitness = Target.subtract(this.getvalue()).abs();
			// BigDecimal x=this.getvalue();
			// this.fitness
			// =x.multiply(x).add(x).subtract(one).subtract(Target).abs();
			// this.fitness = this.getvalue().subtract(Target).abs();
			this.fitness = zero();
			BigDecimal temp;
			for (int i = 0; i < NVAR.intValue(); i++) {
				temp = zero();
				for (int j = 0; j < NVAR.intValue(); j++) {
					temp = temp.add(this.getvalue(j).multiply(Example[i][j]));
				}
				this.fitness = this.fitness.add(temp.subtract(Target[i]).abs());
			}
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
		int n = 32;
		Pattern Colon_Pattern = Pattern.compile(":(.*)");
		Matcher m = Colon_Pattern.matcher(sfromfile);
		while (m.find()) {
			String s = m.group(1);
			// s now contains " <value of parameter>"
			/* Genetic Algorithm constant */
			if (sfromfile.matches("Population Size ?:.*")) {
				this.POPSIZE = new BigInteger(s.trim());
				System.out.println("Population Size : " + this.POPSIZE + Space(n));
			}
			if (sfromfile.matches("Generation Limit ?.* ?:.*")) {
				this.MAXGENS = new BigInteger(s.trim());
				System.out.println("Generation Limit : " + this.MAXGENS + Space(n));
			}
			if (sfromfile.matches("Bit length of each chromosome before decimal place ?.* ?:.*")) {
				this.NSIGB = new BigInteger(s.trim());
				System.out.println("Length of each chromosome : " + this.NSIGB + Space(n));
			}
			if (sfromfile.matches("Bit length of each chromosome after decimal place ?.* ?:.*")) {
				this.NSIGA = new BigInteger(s.trim());
				System.out.println("Length of each chromosome : " + this.NSIGA + Space(n));
			}
			if (sfromfile.matches("Probability of Mutation ?.* ?:.*")) {
				this.PMUTATION = Double.parseDouble(s.trim());
				System.out.println("Probability of Mutation : " + this.PMUTATION + Space(n));
			}
			if (sfromfile.matches("Amount of Mutation ?.* ?:.*")) {
				this.AMUTATION = Double.parseDouble(s.trim());
				System.out.println("Amount of Mutation : " + this.AMUTATION + Space(n));
			}
			if (sfromfile.matches("Cutoff ?.* ?:.*")) {
				this.CUTOFF = Double.parseDouble(s.trim());
				System.out.println("CUTOFF : " + this.CUTOFF + Space(n));
			}
			/* Algorithm option */
			if (sfromfile.matches("Crossover ?.* ?:.*")) {
				Mode_Crossover = s.trim();
				System.out.println("Crossover (parent kill/keep) : " + Mode_Crossover + Space(n));
			}
			if (sfromfile.matches("Mutation ?.* ?:.*")) {
				Mode_Mutation = s.trim();
				System.out.println("Mutation (all or new_only) : " + Mode_Mutation + Space(n));
			}
			/* Interface option */
			if (sfromfile.matches("Report rate ?.* ?:.*")) {
				this.Report_rate = Double.parseDouble(s.trim());
				System.out.println("Report rate : " + this.Report_rate + Space(n));
			}
			if (sfromfile.matches("Report Item ?.* ?:.*")) {
				switch (s.trim()) {
				case "code":
					this.Report_code = true;
					System.out.println("Report Item : " + s.trim());
					break;
				case "value":
					this.Report_value = true;
					System.out.println("Report Item : " + s.trim());
					break;
				case "fitness":
					this.Report_fitness = true;
					System.out.println("Report Item : " + s.trim());
					break;
				default:
					String ss = s.trim();
					if (ss.matches("survivor.*")) {
						this.Report_survivor[0] = ss.charAt(ss.length() - 1);
						this.Report_survivor[1] = ss.charAt(ss.length() - 2);
						System.out.println("Report survivor : " + this.Report_survivor[0] + this.Report_survivor[1]
								+ Space(n));
					}
					break;
				}
			}
			/* problem specific */
			if (sfromfile.matches("NVARS ?:.*")) {
				NVAR = new BigInteger(s.trim());
				System.out.println("NVARS : " + NVAR + Space(n));
			}
			if (sfromfile.matches("Target ?:.*")) {
				// Target = new BigDecimal(s.trim());
				System.out.println("Target : " + Target + Space(n));
			}
		}
	}

	public GA() {
		super();
	}

	public GA(BigInteger POPSIZE, BigInteger NVARS) {
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
			this.population[i] = new Gen(this.NVAR, this.NSIGB, this.NSIGA);
			// sleep(150);
		}
		this.check();
		System.out.println("\tFinished");
	}

	public void nextGeneration() {
		// this.Rfitness();
		switch (Mode_Crossover) {
		case "kill":
			this.crossover_killParent();
			break;
		case "keep":
			this.crossover_keepParent();
			break;
		}
		switch (Mode_Mutation) {
		case "all":
			this.mutation_all();
			break;
		case "new_only":
			this.mutation_newonly();
			break;
		}
		// this.check();
		this.evaluate();
		this.select_X(); // sort for crossover
	}

	public void report(BigInteger I) {
		String s;
		int n = 4;
		if (random.nextFloat() > this.Report_rate)
			return;
		gotorc(1, 1);
		System.out.println("Generation " + I + Space(n));
		// for (int i = 0; i < 1 * this.POPSIZE.intValue(); i++) {
		for (int i = 0; i < Math.min(32, this.POPSIZE.intValue()); i++) {
			if (i < 10)
				System.out.print(" ");
			System.out.print(i);
			// System.out.println(Space(n) + this.population[i].getcode() +
			// Space(n) +
			// this.population[i].getvalue()+Space(n)+this.population[i].fitness.doubleValue()+Space(n));
			s = "";
			if (this.Report_code) {
				for (int j = 0; j < this.NVAR.intValue(); j++) {
					s += Space(n);
					s += this.population[i].getcode(j);
				}
			}
			if (this.Report_value) {
				for (int j = 0; j < this.NVAR.intValue(); j++) {
					s += Space(n);
					s += this.population[i].getvalue(j).doubleValue();
				}
			}
			if (this.Report_survivor[0] != ' ') {
				s += Space(n);
				s += this.population[i].Survivor ? "T" : "F";
			}
			if (this.Report_fitness) {
				s += Space(n);
				s += this.population[i].fitness.doubleValue();
			}
			s += Space(n);
			System.out.println(s);
		}
		/*
		 * for (Gen i : this.population) { System.out.println(i.fitness); }
		 */
		// sleep(150);
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
		for (int i = 0; i < this.population.length; i++) {
			this.population[i].evaluate();
		}
	}

	public void select_X() {
		Arrays.sort(this.population);
		for (int i = 0; i < this.population.length; i++) {
			this.population[i].Survivor = ((double) i / this.population.length) <= this.CUTOFF;
		}
		this.population[0].Survivor = true;
		this.population[1].Survivor = true;
	}

	public void crossover_killParent() {
		int a;
		Gen[] pop_new = new Gen[this.population.length];
		for (int i = 0; i < this.population.length; i++) {
			do {
				a = random.nextInt(this.POPSIZE.intValue());
			} while ((!this.population[a].Survivor) || (a == i));
			pop_new[i] = crossover(this.population[a], this.population[i]);
		}
		this.population = pop_new;
	}

	public void crossover_keepParent() {
		int a;
		Gen[] pop_new = new Gen[this.population.length];
		for (int i = 0; i < this.population.length; i++) {
			if (this.population[i].Survivor) {
				pop_new[i] = this.population[i];
			} else {
				do {
					a = random.nextInt(this.POPSIZE.intValue());
				} while ((!this.population[a].Survivor) || (a == i));
				pop_new[i] = crossover(this.population[a], this.population[i]);
			}
		}
		this.population = pop_new;
	}

	public Gen crossover(Gen a, Gen b) {
		Gen result = new Gen(this.NVAR, this.NSIGB, this.NSIGA);
		for (int i = 0; i < this.NVAR.intValue(); i++) {
			for (int j = 0; j < 2; j++)
				result.code[i][j] = crossover(a.code[i][j], b.code[i][j]);
		}
		return result;
	}

	public BitSet crossover(BitSet a_ori, BitSet b_ori) {
		BitSet a = (BitSet) a_ori.clone();
		BitSet b = (BitSet) b_ori.clone();
		BitSet result = new BitSet();
		if (a.length() < b.length()) {
			a.set(b.length() - 1, true);
		} else if (b.length() < a.length()) {
			b.set(a.length() - 1, true);
		}

		for (int i = 0; i < a.length(); i++) {
			if (random.nextBoolean()) {
				result.set(i, a.get(i));
			} else {
				result.set(i, b.get(i));
			}
		}
		return result;
	}

	public void mutation_all() {
		for (int i = 0; i < this.population.length; i++)
			for (int j = 0; j < this.NVAR.intValue(); j++)
				if (random.nextDouble() <= this.PMUTATION)
					mutation(this.population[i].code[j]);
	}

	public void mutation_newonly() {
		for (int i = 0; i < this.population.length; i++)
			if (!this.population[i].Survivor)
				for (int j = 0; j < this.NVAR.intValue(); j++)
					if (random.nextDouble() <= this.PMUTATION)
						mutation(this.population[i].code[j]);
	}

	private void mutation(BitSet[] bs) {
		int len;
		for (int k = 0; k < 2; k++) {
			if (k == 0)
				len = this.NSIGB.intValue();
			else
				len = this.NSIGA.intValue();
			for (int l = 0; l < len; l++)
				if (random.nextDouble() <= this.AMUTATION)
					bs[k].set(l, !bs[k].get(l));
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

	public static void CreateDemo(String filepath, String coding) {
		System.out.print("Creating " + filepath + " ...");
		PrintWriter writer;
		try {
			writer = new PrintWriter(filepath, coding);
			/* Genetic Algorithm constant */
			writer.println("/* Genetic Algorithm constant */");
			writer.println("Population Size : 50");
			writer.println("Generation Limit (Iteration Steps) : 1000");
			writer.println("Bit length of each chromosome before decimal place : 4");
			writer.println("Bit length of each chromosome after decimal place : 64");
			writer.println("Probability of Mutation : 0.02");
			writer.println("Amount of Mutation : 0.1");
			writer.println("Cutoff : 0.25");
			writer.println("");
			/* Algorithm option */
			writer.println("/* Algorithm option */");
			writer.println("Crossover (parent kill/keep) : keep");
			writer.println("Mutation (all or new_only) : new_only");
			writer.println("");
			/* Interface option */
			writer.println("/* Interface option */");
			writer.println("Report rate (i.e. 0/1/0.01) : 0.01");
			writer.println("Report Item (i.e. code, survivor(together with two char), value, fitness)");
			writer.println("Report Item : code");
			writer.println("Report Item : survivor TF");
			writer.println("Report Item : value");
			writer.println("Report Item : fitness");
			writer.println("");
			/* problem specific */
			writer.println("/* problem specific */");
			writer.println("NVARS : 1");
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
			// target.PRECISION = target.NSIG.multiply(target.NVAR);
			// System.out.println("Precision: " + target.PRECISION);
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
		for (int i = 0; i < 256; i++)
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
		{
			ga.evaluate();
			ga.select_X();
			double temp = ga.Report_rate;
			ga.Report_rate = 1;
			ga.report(zero.toBigInteger());
			ga.Report_rate = temp;
		}
		for (BigInteger IGENS = zero.toBigInteger(); !IGENS.equals(ga.MAXGENS); IGENS = IGENS.add(one.toBigInteger())) {
			ga.nextGeneration();
			ga.report(IGENS.add(one.toBigInteger()));
		}

		/* end */
		System.out.println(Space(50));
		System.out.println(Space(5) + "End." + Space(5));
	}
}
