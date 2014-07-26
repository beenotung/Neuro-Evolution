package test;

import myutils.SqlServerInfo;
import core.NeuralNetwork;

public class Main {

	public static void main(String[] args) {
		// debug
		String sqlServerUrl = "jdbc:mysql://localhost:3306";
		String user = "root";
		String password = "mysqlB(10v2TC";
		String databaseName = "nndb";
		SqlServerInfo sqlServerInfo = new SqlServerInfo(sqlServerUrl, databaseName, user, password);
		// \\debug
		NeuralNetwork neuralNetwork;
		try {
			neuralNetwork = new NeuralNetwork(sqlServerInfo);
			neuralNetwork.removeDatabase();
			neuralNetwork.createDatabase();
			neuralNetwork.setRandomly();
			neuralNetwork.save();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
