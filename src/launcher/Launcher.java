package launcher;

import java.util.Vector;

import neuralnetwork.NeuralNetwork;
import neuralnetwork.databaseconnection.NeuralNetworkDatabaseConnector;

public class Launcher {

	public static void main(String[] args) {
		System.out.println("main start");

		String mode = "BackNN";
		int NInput = 10 * 17 * 5;
		int NOutput = 17 * 5;
		int NMiddle2 = (NInput + NOutput) / 2;
		int NMiddle1 = (NInput + NMiddle2) / 2;
		int NMiddle3 = (NMiddle2 + NOutput) / 2;
		 int[] layers = { NInput, NMiddle1, NMiddle3, NOutput };
		//int[] layers = { 2, 2, 1 };
		// \\debug
		NeuralNetwork neuralNetwork = new NeuralNetwork(mode, layers, 0.1);
		neuralNetwork.create();
		neuralNetwork.learnFromDatabase();
		test(neuralNetwork, 1, 1);
		test(neuralNetwork, 0, 0);
		test(neuralNetwork, 1, 0);
		test(neuralNetwork, 0, 1);
		neuralNetwork.saveToDB();

		System.out.println("main end");
	}

	private static void test(NeuralNetwork neuralNetwork, double i, double j) {
		Vector<Double> inputs = new Vector<Double>();
		inputs.add(i);
		inputs.add(j);
		Vector<Double> output = neuralNetwork.run(inputs);
		System.out.println(output.toString());
	}

}
