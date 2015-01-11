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


public class GA {

	/* Genetic Algorithm constant */
	public static final String version = "Genetic Algorithm V5.1.3\t --by Beeno Tung";
	public static final String inifilepath = "resources/GA.ini";
	public static final String demoinifilepath = "resources/DEMO.ini";
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
	public BigInteger IGENS;

	/* Genetic Algorithm option */
	public String MODE_CROSSOVER;
	public String MODE_MUTATION;

	/* Interface option */
	public double Report_rate = 1;
	public boolean Report_code = false;
	public char[] Report_survivor = new char[] { ' ', ' ' };
	public boolean Report_value = false;
	public boolean Report_fitness = false;

	/* Threads interval */
	public static final long GENERAL_INTERVAL = 5;

	/* problem specific */
	public BigInteger NVAR;// dimension of the problem
	public BigDecimal[] Target = { new BigDecimal(40), new BigDecimal(190),
			new BigDecimal(418), new BigDecimal(1796) };
	public BigDecimal[][] Example = {
			{ new BigDecimal(1), new BigDecimal(2), new BigDecimal(3),
					new BigDecimal(4) },
			{ new BigDecimal(2), new BigDecimal(6), new BigDecimal(12),
					new BigDecimal(24) },
			{ new BigDecimal(12), new BigDecimal(72), new BigDecimal(17),
					new BigDecimal(22) },
			{ new BigDecimal(864), new BigDecimal(8), new BigDecimal(6),
					new BigDecimal(4) } };

	/*
	 * Threads
	 */
	public static final int NThread=8;
	Report report;
	SelectX selectX;

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
				this.code[i][0] = Utils.random_bitset(NSIGB.intValue());
				this.code[i][1] = Utils.random_bitset(NSIGA.intValue());
			}
		}

		public void check() {

		}

		public BigDecimal getvalue(int var) {
			BigDecimal value = Utils.zero();
			BigDecimal rate = Utils.one();
			for (int i = this.NSIGB.intValue() - 1; i >= 0; i--) {
				if (this.code[var][0].get(i))
					value = value.add(rate);
				rate = rate.multiply(Utils.two);
			}
			rate = Utils.one.divide(Utils.two);
			for (int i = 0; i < this.NSIGA.intValue(); i++) {
				if (this.code[var][1].get(i))
					value = value.add(rate);
				rate = rate.divide(Utils.two);
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
			this.fitness = Utils.zero();
			BigDecimal temp;
			for (int i = 0; i < NVAR.intValue(); i++) {
				temp = Utils.zero();
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
				System.out.println("Population Size : " + this.POPSIZE
						+ Utils.Space(n));
			}
			if (sfromfile.matches("Generation Limit ?.* ?:.*")) {
				this.MAXGENS = new BigInteger(s.trim());
				System.out.println("Generation Limit : " + this.MAXGENS
						+ Utils.Space(n));
			}
			if (sfromfile
					.matches("Bit length of each chromosome before decimal place ?.* ?:.*")) {
				this.NSIGB = new BigInteger(s.trim());
				System.out.println("Length of each chromosome : " + this.NSIGB
						+ Utils.Space(n));
			}
			if (sfromfile
					.matches("Bit length of each chromosome after decimal place ?.* ?:.*")) {
				this.NSIGA = new BigInteger(s.trim());
				System.out.println("Length of each chromosome : " + this.NSIGA
						+ Utils.Space(n));
			}
			if (sfromfile.matches("Probability of Mutation ?.* ?:.*")) {
				this.PMUTATION = Double.parseDouble(s.trim());
				System.out.println("Probability of Mutation : "
						+ this.PMUTATION + Utils.Space(n));
			}
			if (sfromfile.matches("Amount of Mutation ?.* ?:.*")) {
				this.AMUTATION = Double.parseDouble(s.trim());
				System.out.println("Amount of Mutation : " + this.AMUTATION
						+ Utils.Space(n));
			}
			if (sfromfile.matches("Cutoff ?.* ?:.*")) {
				this.CUTOFF = Double.parseDouble(s.trim());
				System.out.println("CUTOFF : " + this.CUTOFF + Utils.Space(n));
			}
			/* Algorithm option */
			if (sfromfile.matches("Crossover ?.* ?:.*")) {
				MODE_CROSSOVER = s.trim();
				System.out.println("Crossover (parent kill/keep) : "
						+ MODE_CROSSOVER + Utils.Space(n));
			}
			if (sfromfile.matches("Mutation ?.* ?:.*")) {
				MODE_MUTATION = s.trim();
				System.out.println("Mutation (all or new_only) : "
						+ MODE_MUTATION + Utils.Space(n));
			}
			/* Interface option */
			if (sfromfile.matches("Report rate ?.* ?:.*")) {
				this.Report_rate = Double.parseDouble(s.trim());
				System.out.println("Report rate : " + this.Report_rate
						+ Utils.Space(n));
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
						System.out.println("Report survivor : "
								+ this.Report_survivor[0]
								+ this.Report_survivor[1] + Utils.Space(n));
					}
					break;
				}
			}
			/* problem specific */
			if (sfromfile.matches("NVARS ?:.*")) {
				NVAR = new BigInteger(s.trim());
				System.out.println("NVARS : " + NVAR + Utils.Space(n));
			}
			if (sfromfile.matches("Target ?:.*")) {
				// Target = new BigDecimal(s.trim());
				System.out.println("Target : " + Target + Utils.Space(n));
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
		this.IGENS = Utils.zero.toBigInteger();
		GA[] gaIN = { this };
		this.report = new Report(gaIN);
		this.selectX = new SelectX(gaIN);
		System.out.println("\tFinished");
	}

	public void nextGeneration() {
		// this.Rfitness();
		switch (MODE_CROSSOVER) {
		case "kill":
			this.crossover_killParent();
			break;
		case "keep":
			this.crossover_keepParent();
			break;
		}
		switch (MODE_MUTATION) {
		case "all":
			this.mutation_all();
			break;
		case "new_only":
			this.mutation_newonly();
			break;
		}
		// this.check();
		this.evaluate();
		selectX.run();// sort and label for crossover
	}

	public void report() {
		if (this.report == null) {
			GA[] gaIN = { this };
			this.report = new Report(gaIN);
		}
		this.report.show();
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
				c[list[Utils.random.nextInt(li + 1)]] = (char) (Utils.random
						.nextInt(10) + 48);
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

	public void crossover_killParent() {
		int a;
		Gen[] pop_new = new Gen[this.population.length];
		for (int i = 0; i < this.population.length; i++) {
			do {
				a = Utils.random.nextInt(this.POPSIZE.intValue());
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
					a = Utils.random.nextInt(this.POPSIZE.intValue());
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
			if (Utils.random.nextBoolean()) {
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
				if (Utils.random.nextDouble() <= this.PMUTATION)
					mutation(this.population[i].code[j]);
	}

	public void mutation_newonly() {
		for (int i = 0; i < this.population.length; i++)
			if (!this.population[i].Survivor)
				for (int j = 0; j < this.NVAR.intValue(); j++)
					if (Utils.random.nextDouble() <= this.PMUTATION)
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
				if (Utils.random.nextDouble() <= this.AMUTATION)
					bs[k].set(l, !bs[k].get(l));
		}
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
			writer.println("Report rate (in sec, e.g. 0,0.5,1) : 0.25");
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
	public void start(String[] args) {
		for (int i = 0; i < 256; i++)
			System.out.println();
		System.out.println(GA.version);
		System.out.println();

		GA.CreateDemo(GA.demoinifilepath, GA.fileformat);

		GA ga = new GA();
		GA.ReadFromINI(GA.inifilepath, ga);
		System.out.println();

		/* initialize */
		ga.initialize();

		/* breeding */
		System.out.print("\nBreeding...");
		{
			ga.evaluate();
			ga.selectX.run();
			double temp = ga.Report_rate;
			ga.Report_rate = 1;
			// ga.report(Utils.zero.toBigInteger());
			ga.Report_rate = temp;
		}
		ga.report.start();
		for (ga.IGENS = Utils.zero.toBigInteger(); !ga.IGENS.equals(ga.MAXGENS); ga.IGENS = ga.IGENS
				.add(Utils.one.toBigInteger())) {
			ga.nextGeneration();
			// ga.report(ga.IGENS.add(Utils.one.toBigInteger()));
		}
		ga.report.stop();

		/* end */
		while (ga.report.isAlive()) {
			Utils.sleep();
		}
		System.out.println(Utils.Space(50));
		System.out.println(Utils.Space(5) + "End." + Utils.Space(45));
	}
}
