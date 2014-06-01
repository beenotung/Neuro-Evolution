import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;

import NN_BP_v0_3.Network;

public class GA {

	/* Genetic Algorithm constant */
	public static final String version = "Genetic Algorithm V1.0\t --by Beeno Tung";
	public static final String inifilepath = "GA.ini";
	public static final String fileformat = "UTF-8";

	public static int POPSIZE = 50;// population size
	public static int MAXGENS = 1000; // generation limit (iteration steps)
	public static int NVARS = 2;// number of characteristic(s)
	public static int NSIG = 20; // length of each chromosome (detail level)
	// index list for the top 25% gens
	public static double PXOVER = 0.2; // Probability of crossover
	// default 0.2
	public static double PMUTATION = 0.02; // Proportion of mutated variables
	// default 0.05
	public static int[] BestList = new int[(int) (POPSIZE * 0.25)];
	public static BigDecimal PRECISION = new BigDecimal("10").pow(NVARS);

	/* problem specific */

	/*
	 * Gen code Class container of chromosome(s)
	 */
	public static class Gen {

		public BigDecimal[] code;
		public BigDecimal fitness;
		public boolean Survivor;

		public Gen() {
			this.code = new BigDecimal[NVARS];
			for (int i = 0; i < NVARS; i++) {
				this.code[i] = random(NSIG);
			}
		}

		// find fractional expression of PI in this example
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
			System.out.println(this.fitness);
		}
	} /* end of class Gen */

	public static void SetVar(String sfromfile) {
		Pattern MY_PATTERN = Pattern.compile(":(.*)");
		Matcher m = MY_PATTERN.matcher(sfromfile);
		while (m.find()) {
			String s = m.group(1);
			// s now contains " <Num>"
			if (sfromfile.matches("Number of input Node ?:.*")) {
				network.Ninput = new BigDecimal(s.trim());
				System.out.println("Number of input Node : " + network.Ninput.toString());
			}
			if (sfromfile.matches("Number of output Node ?:.*")) {
				network.Noutput = new BigDecimal(s.trim());
				System.out.println("Number of output Node : " + network.Noutput.toString());
			}
			if (sfromfile.matches("Number of Hidden Layer ?:.*")) {
				network.NhiddenLayer = new BigDecimal(s.trim());
				System.out.println("Number of Hidden Layer : " + network.NhiddenLayer.toString());
			}
			if (sfromfile.matches("Number of Node per Hidden Layer ?:.*")) {
				network.NnodeperhiddenLayer = new BigDecimal(s.trim());
				System.out.println("Number of Node per Hidden Layer : " + network.NnodeperhiddenLayer.toString());
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

	public static void ReadFromINI(String filepath) {
		System.out.println("Reading from " + filepath + " ...");
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filepath));
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				SetVar(sCurrentLine, network);
			}
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
	}

	/*
	 * standard program entry point
	 */

	public static void main(String[] args) {
		System.out.println();
		System.out.println(version);
		System.out.println();

		ReadFromINI(inifilepath);

		/* initialize */
		Gen[] population = new Gen[POPSIZE];
		for (int i = 0; i < POPSIZE; i++) {
			population[i] = new Gen();
		}

		/* breeding */
		for (int Iround = 0; Iround < MAXGENS; Iround++) {
			for (int i = 0; i < POPSIZE; i++) {
				population[i].evaluate();
				population[i].report();

			}
		}

		System.out.println("End.");
	}

}
