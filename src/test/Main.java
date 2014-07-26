package test;

import myutils.SqlServerInfo;
import core.NeuralNetwork;

public class Main {

	public static void main(String[] args) {
		System.out.println("main start");
		// debug
		String sqlServerUrl = "jdbc:mysql://localhost:3306";
		String user = "root";
		String password = "mysqlB(10v2TC";
		String databaseName = "nndb";
		
		SqlServerInfo sqlServerInfo = new SqlServerInfo(sqlServerUrl, databaseName, user, password);
		String mode="BackNN";
		int [] layers={2,3,1};
		// \\debug
		NeuralNetwork neuralNetwork;
		try {
			neuralNetwork = new NeuralNetwork(sqlServerInfo,mode,layers);
			neuralNetwork.removeDatabase();
			neuralNetwork.createDatabase();
			neuralNetwork.setRandomly();
			neuralNetwork.save();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("main end");
	}

}
