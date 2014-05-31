import java.util.Random;
import java.math.BigDecimal;

public class NN_GA {

	/*
	 * Gen code Class container of chromosome(s)
	 */
	public static class Gen {

		public BigDecimal[] code;

		public Gen() {
			this.code = new BigDecimal[NVARS];
			for (int i = 0; i < NVARS; i++) {
				this.code[i] = random(NSIG);
			}
		}

		// uniform mutation
		public void mutation(double rate) {
			BigDecimal count = zero();

		}

		// uniform crossover
		public void crossover(double rate){
			a
		}

		public BigDecimal evaluate(){
			asd;
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

	/*
	 * global variables
	 */

	public static int POPSIZE = 50;// population size
	public static int MAXGENS = 1000; // generation limit (iteration steps)
	public static int NVARS = 3;// number of characteristic(s)
	public static int NSIG = 20; // length of each chromosome (detail level)
									// significance of the code (BigDecial)

	/*
	 * standard program entry point
	 */

	public static void main(String[] args) {
		/* initialize */
		Gen[] population = new Gen[POPSIZE];
		for (int i = 0; i < POPSIZE; i++) {
			population[i] = new Gen();
		}


	}

}
