// math
import java.math.BigDecimal;

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
	public double PXOVER;
	public double PMUTATION;
	public double CUTOFF;
	public Gen[] population;

	/* problem specific */
	public BigDecimal NVARS = two();// number of characteristic(s)
	public BigDecimal PRECISION;
	public BigDecimal Target;
	public BigDecimal SUM;

	/*
	 * Gen code Class container of chromosome(s)
	 */
	public class Gen implements Comparable<Gen> {

		/* each gen's element */
		public BigDecimal[] code;
		public BigDecimal fitness;
		public boolean Survivor;

		public Gen(int Ncode) {
			this.code = new BigDecimal[Ncode];
			for (BigDecimal i : this.code) {
				i = random();
			}
		}

		public void check() {
			while (this.code[1].equals(zero)) {
				code[1] = random();
			}
		}

		// find fractional expression of target, PI in this example
		public void evaluate() {
			this.fitness = this.code[0].divide(this.code[1], Math.min(this.code[0].precision() + this.code[1].precision(), Integer.MAX_VALUE), BigDecimal.ROUND_HALF_UP).subtract(Target).abs();
		}

		// uniform mutation
		public void mutation(double rate) {
			BigDecimal count = zero();

		}

		// uniform crossover
		public void crossover(double rate) {
			BigDecimal count = zero();
		}

		// print gen info, for debug only
		public void report() {
			System.out.println(this.fitness);
		}

		@Override
		public int compareTo(Gen target) {
			int result = this.fitness.compareTo(target.fitness);
			return result;
			// ascending order
			// return this.quantity - compareQuantity;
			// descending order
			// return compareQuantity - this.quantity;
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
			if (sfromfile.matches("Probability of Crossover ?.* ?:.*")) {
				this.PXOVER = Double.parseDouble(s.trim());
				System.out.println("Probability of Crossover : " + this.PXOVER);
			}
			if (sfromfile.matches("Probability of Mutation ?.* ?:.*")) {
				this.PMUTATION = Double.parseDouble(s.trim());
				System.out.println("Probability of Mutation : " + this.PMUTATION);
			}
			if (sfromfile.matches("this.CUTOFF ?.* ?:.*")) {
				this.CUTOFF = Double.parseDouble(s.trim());
				System.out.println("this.CUTOFF : " + this.CUTOFF);
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

	public void initialize() {
		/* initialize */
		String amoung = "" + this.POPSIZE;
		this.population = new Gen[this.POPSIZE.intValue()];
		System.out.print("Generating Gens...1/" + amoung);
		for (BigDecimal i = zero(); !i.equals(this.POPSIZE); i = i.add(one)) {
			String s = (i) + "/" + this.POPSIZE;
			int l = s.length();
			s = "";
			for (int j = 0; j < l; j++) {
				s += "\u0008";
			}
			// console.printf("%s", s + i.add(one) + "/" + amoung);
			System.out.print(s + i.add(one) + "/" + amoung);
			this.population[i.intValue()] = new Gen(this.NVARS.intValue());
			// sleep(150);
		}
		System.out.println("\tFinished");
	}

	public void nextGeneration() {
		this.check();
		this.evaluate();
		this.Rfitness();
		this.select(); // for crossover
		this.crossover();
		this.mutation();

	}

	public void report() {
		for (Gen i : this.population) {
			System.out.println(i.fitness);
		}
	}

	public void check() {
		for (Gen i : this.population) {
			i.check();
		}
	}

	public void evaluate() {
		this.SUM = zero();
		for (Gen i : this.population) {
			i.evaluate();
			this.SUM = this.SUM.add(i.fitness);
		}
	}

	public void Rfitness() {
		for (Gen i : this.population) {
			i.fitness = i.fitness.divide(this.SUM, Math.min(i.fitness.precision() + this.SUM.precision(), Integer.MAX_VALUE), BigDecimal.ROUND_HALF_UP);
		}
	}

	public void select() {
		Arrays.sort(this.population);
		for (Gen i : this.population) {
			i.Survivor = i.fitness.doubleValue() <= this.PXOVER;
		}
	}

	private void crossover() {
		for (Gen i : this.population) {
			if (!i.Survivor) {
				int a, b;
				do {
					a = random.nextInt(this.POPSIZE.intValue());
				} while (!this.population[a].Survivor);
				do {
					b = random.nextInt(this.POPSIZE.intValue());
				} while (!this.population[b].Survivor);
				i = crossover(this.population[a], this.population[b]);
			}
		}

	}

	public Gen crossover(Gen a, Gen b) {
		Gen result = new Gen(a.code.length);
		for (int i = 0; i < result.code.length; i++) {
			result.code[i] = crossover(a.code[i], a.code[i]);
		}
		return result;
	}
	public BigDecimal crossover(BigDecimal a, BigDecimal b) {
		String as=a.toString();
		String bs=b.toString();
		String c="";
		while (as.length()<bs.length()){
			as="0"+as;
		}
		while (bs.length()<as.length()){
			bs="0"+bs;
		}
		int l=as.length();
		while (c.length()<l)
		{
			if(random.nextInt(2)==0){
				c+=as[c.length()];
			}
			else{
				c+=bs[c.length()];
			}
		}

		return new BigDecimal(c);
	}

	private void mutation() {

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

	public BigDecimal random() {
		BigDecimal result = new BigDecimal("0.0");
		for (BigDecimal i = zero(); !i.equals(this.NSIG); i = i.add(one)) {
			result = result.multiply(ten()).add(new BigDecimal(random.nextInt(10)));
		}
		return result;
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
			writer.println("Probability of Crossover : 0.2");
			writer.println("Probability of Mutation : 0.02");
			writer.println("this.CUTOFF : 0.25");
			writer.println("");
			writer.println("/* problem specific */");
			writer.println("NVARS ： 2");
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
		for (BigDecimal IGENS = zero(); !IGENS.equals(ga.MAXGENS); IGENS = IGENS.add(one)) {
			ga.nextGeneration();
		}
		// ga.report();

		// ga.showsettings();

		System.out.println("End.");
	}
}
