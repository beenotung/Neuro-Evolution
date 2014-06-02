// math
import java.math.BigDecimal;

// string regex
import java.util.regex.Pattern;
import java.util.regex.Matcher;

// io utility
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

// general utility
import java.util.Random;

public  class GA {

	public GA(){

	}


	/* Genetic Algorithm constant */
	public static final String version = "Genetic Algorithm V1.0\t --by Beeno Tung";
	public static final String inifilepath = "GA.ini";
	public static final String demoinifilepath = "DEMO.ini";
	public static final String fileformat = "UTF-8";

	/* Genetic Algorithm variable */
	public static int POPSIZE;
	public static int MAXGENS;
	public static int NSIG;
	public static double PXOVER;
	public static double PMUTATION;
	public static double CUTOFF;
	public static int[] BestList = new int[(int) (POPSIZE * CUTOFF) + 1];

	/* problem specific */
	public static int NVARS = 2;// number of characteristic(s)
	public static BigDecimal PRECISION = new BigDecimal("10").pow(NSIG).pow(NVARS);
	public static BigDecimal Target;

	/*
	 * Gen code Class container of chromosome(s)
	 */
	public static class Gen {

		/* each gen's element */
		public BigDecimal[] code;
		public BigDecimal fitness;
		public boolean Survivor;

		public Gen() {
			this.code = new BigDecimal[NVARS];
			for (int i = 0; i < NVARS; i++) {
				this.code[i] = random(NSIG);
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

	public static void SetVar(String sfromfile) {
		Pattern MY_PATTERN = Pattern.compile(":(.*)");
		Matcher m = MY_PATTERN.matcher(sfromfile);
		while (m.find()) {
			String s = m.group(1);
			// s now contains " <Num>"
			if (sfromfile.matches("Target ?:.*")) {
				Target = new BigDecimal(s.trim());
				System.out.println("Target : " + Target);
			}
		}
	}

	/* utility variables */
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

	public static BigDecimal random(int length) {
		BigDecimal result = new BigDecimal("0.0");

		for (int i = 0; i < length; i++) {
			result = result.multiply(ten()).add(new BigDecimal(random.nextInt(10)));
		}
		return result;
	}

	public static void CreateDemo(String filepath, String coding) {
		System.out.print("Creating " + filepath + " ...");
		PrintWriter writer;
		try {
			writer = new PrintWriter(filepath, coding);
			writer.println("Number of input Node: 2");
			writer.println("Number of output Node: 1");
			writer.println("Number of Hidden Layer: 3");
			writer.println("Number of Node per Hidden Layer: 2");
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
				SetVar(sCurrentLine);
			}
			target.PRECISION = new BigDecimal("10").pow(NSIG).pow(NVARS);
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

	public static void showsettings() {
		System.out.println("Population Size: " + POPSIZE);
		System.out.println("Generation Limit (Iteration Steps): " + MAXGENS);
		System.out.println("Length of each chromosome (detail level): " + NSIG);
		System.out.println("Probability of crossover: " + PXOVER);
		System.out.println("Probability of mutations: " + PMUTATION);
		System.out.println("Cutoff: " + CUTOFF);

		/* problem specific */
		public static int NVARS;// number of characteristic(s)
		public static BigDecimal PRECISION = new BigDecimal("10").pow(NSIG).pow(NVARS);
	}

	/*
	 * standard program entry point
	 */

	public static void main(String[] args) {
		System.out.println();
		System.out.println(version);
		System.out.println();

		CreateDemo(demoinifilepath, fileformat);

		Gen[] population = new Gen[POPSIZE];

		ReadFromINI(inifilepath, population);
		System.out.println();

		/* initialize */
		for (int i = 0; i < POPSIZE; i++) {
			population[i] = new Gen();
		}

		/* breeding */
		for (int IGENS = 0; IGENS < MAXGENS; IGENS++) {
			for (int i = 0; i < POPSIZE; i++) {
				population[i].evaluate();
				population[i].report();

			}
		}

		System.out.println("End.");
	}

}
