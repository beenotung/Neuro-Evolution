// math
import java.math.BigDecimal;

// string regex
import java.util.regex.Pattern;
import java.util.regex.Matcher;

// io utility
import java.io.Console;
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
	public long POPSIZE;
	public long MAXGENS;
	public long NSIG;
	public double PXOVER;
	public double PMUTATION;
	public double CUTOFF;
	public Gen[] population;

	/* problem specific */
	public int NVARS = 2;// number of characteristic(s)
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

		public Gen(int NVARS, long NSIG) {
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

	public void SetVar(String sfromfile) {
		Pattern MY_PATTERN = Pattern.compile(":(.*)");
		Matcher m = MY_PATTERN.matcher(sfromfile);
		while (m.find()) {
			String s = m.group(1);
			// s now contains " <value of parameter>"
			if (sfromfile.matches("Population Size ?:.*")) {
				POPSIZE = Integer.parseInt(s.trim());
				System.out.println("Population Size : " + POPSIZE);
			}
			if (sfromfile.matches("Generation Limit ?.* ?:.*")) {
				MAXGENS = Integer.parseInt(s.trim());
				System.out.println("Generation Limit : " + MAXGENS);
			}
			if (sfromfile.matches("Length of each chromosome ?.* ?:.*")) {
				NSIG = Integer.parseInt(s.trim());
				System.out.println("Length of each chromosome : " + NSIG);
			}
			if (sfromfile.matches("Probability of Crossover ?.* ?:.*")) {
				PXOVER = Double.parseDouble(s.trim());
				System.out.println("Probability of Crossover : " + PXOVER);
			}
			if (sfromfile.matches("Probability of Mutation ?.* ?:.*")) {
				PMUTATION = Double.parseDouble(s.trim());
				System.out.println("Probability of Mutation : " + PMUTATION);
			}
			if (sfromfile.matches("Cutoff ?.* ?:.*")) {
				CUTOFF = Double.parseDouble(s.trim());
				System.out.println("Cutoff : " + CUTOFF);
			}
			if (sfromfile.matches("NVARS ?:.*")) {
				NVARS = Integer.parseInt(s.trim());
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
		this.population = new Gen[this.POPSIZE];
		console.printf("Generating Gens...1/"+POPSIZE);
		for (int i = 0; i < this.POPSIZE; i++) {
			String s = (i)+"/"+POPSIZE;
			int l=s.length();
			s="";
			for (int j=0;j<l;j++)
			{
				s+="\u0008";
			}
			console.printf("%s", s);
			System.out.print((i+1)+"/"+POPSIZE);
			this.population[i] = new Gen(this.NVARS, this.NSIG);
			//sleep(150);
		}
		System.out.println("\tFinished");
	}

	public void showsettings() {
		System.out.println("Population Size: " + POPSIZE);
		System.out.println("Generation Limit (Iteration Steps): " + MAXGENS);
		System.out.println("Length of each chromosome (detail level): " + NSIG);
		System.out.println("Probability of crossover: " + PXOVER);
		System.out.println("Probability of mutations: " + PMUTATION);
		System.out.println("Cutoff: " + CUTOFF);

		// problem specific
		String msg = "number of characteristic";
		if (NVARS > 1) {
			msg += "s";
		}
		System.out.println(msg+": " + NVARS);
		// print or not? public static BigDecimal PRECISION = new
		// BigDecimal("10").pow(NSIG).pow(NVARS);
	}

	/* utility variables */
	public static Console console = System.console();
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
			writer.println("/* Genetic Algorithm constant */");
			writer.println("Population Size : 50");
			writer.println("Generation Limit (Iteration Steps) : 1000");
			writer.println("Length of each chromosome (detail level) : 20");
			writer.println("Probability of Crossover : 0.2");
			writer.println("Probability of Mutation : 0.02");
			writer.println("Cutoff : 0.25");
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
			target.PRECISION = new BigDecimal("10").pow(target.NSIG).pow(target.NVARS);
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
		for (int IGENS = 0; IGENS < ga.MAXGENS; IGENS++) {
			for (int i = 0; i < ga.POPSIZE; i++) {
				ga.population[i].evaluate();
				ga.population[i].report();

			}
		}

		//ga.showsettings();

		System.out.println("End.");
	}

}
