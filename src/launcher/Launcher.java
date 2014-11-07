package launcher;

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
		// \\debug
		NeuralNetworkDatabaseConnector databaseConnector = new NeuralNetworkDatabaseConnector(
				mode);
		NeuralNetwork neuralNetwork = new NeuralNetwork(mode, layers, databaseConnector);
		neuralNetwork.create();
		neuralNetwork.learnFromDatabase(1);
		// neuralNetwork.save();

		System.out.println("main end");
	}

}
