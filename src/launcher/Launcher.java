package launcher;

import neuralnetwork.NeuralNetwork;
import myutils.SqlServerInfo;

public class Launcher {

	public static void main(String[] args) {
		System.out.println("main start");
		// debug
		String sqlServerUrl = "jdbc:mysql://localhost:3306";
		String user = "root";
		String password = "mysqlB(10v2TC";
		String databaseName = "nndb";

		SqlServerInfo sqlServerInfo = new SqlServerInfo(sqlServerUrl, databaseName, user,
				password);
		String mode = "BackNN";
		int NInput = 10 * 17 * 5;
		int NOutput = 17 * 5;
		int NMiddle2 = (NInput + NOutput) / 2;
		int NMiddle1 = (NInput + NMiddle2) / 2;
		int NMiddle3 = (NMiddle2 + NOutput) / 2;
		int[] layers = { NInput, NMiddle1, NMiddle3, NOutput };
		// \\debug
		NeuralNetwork neuralNetwork;
		neuralNetwork = new NeuralNetwork(sqlServerInfo, mode, layers);
		neuralNetwork.create();
		//neuralNetwork.save();

		System.out.println("main end");
	}

}
