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

	/*
	 * Gen code Class container of chromosome(s)
	 */
	public class Gen {

		/* each gen's element */
		public BigDecimal[] code;
		public BigDecimal fitness;
		public boolean Survivor;

		public Gen(BigDecimal NVARS, BigDecimal NSIG) {
			this.code = new BigDecimal[NVARS.intValue()];
			for (BigDecimal i = zero(); !i.equals(NVARS); i = i.add(one)) {
				this.code[i.intValue()] = random();
			}
		}

		// find fractional expression of target, PI in this example
		public void evaluate() {
			this.fitness = this.code[0].divide(this.code[1], PRECISION.intValue(), BigDecimal.ROUND_HALF_UP);
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
			// System.out.println(this.fitness);
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
			this.population[i.intValue()] = new Gen(this.NVARS, this.NSIG);
			// sleep(150);
		}
		System.out.println("\tFinished");
	}

	public void showsettings() {
		System.out.println("Population Size: " + this.POPSIZE);
		System.out.println("Generation Limit (Iteration Steps): " + this.MAXGENS);
		System.out.println("Length of each chromosome (detail level): " + this.NSIG);
		System.out.println("Probability of crossover: " + this.PXOVER);
		System.out.println("Probability of mutations: " + this.PMUTATION);
		System.out.println("CUTOFF: " + this.CUTOFF);

		// problem specific
		String msg = "number of characteristic";
		if (!NVARS.equals(one)) {
			msg += "s";
		}
		System.out.println(msg + ": " + NVARS);
		// print or not? public static BigDecimal PRECISION = new
		// BigDecimal("10").pow(this.NSIG).pow(NVARS);
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
			target.PRECISION = new BigDecimal("10").pow(target.NSIG.intValue()).pow(target.NVARS.intValue());
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
			for (BigDecimal i = zero(); !i.equals(ga.POPSIZE); i = i.add(one)) {
				ga.population[i.intValue()].evaluate();
				ga.population[i.intValue()].report();
			}
		}

		// ga.showsettings();

		System.out.println("End.");
	}

}
