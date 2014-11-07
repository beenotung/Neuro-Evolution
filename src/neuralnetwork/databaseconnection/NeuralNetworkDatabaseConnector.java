package neuralnetwork.databaseconnection;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import neuralnetwork.Example;
import myutils.connection.MyDatabaseConnector;

public class NeuralNetworkDatabaseConnector {
	private String mode;

	/** general staff **/
	public void executeSqlFile(String path) {
		// Runtime
	}

	/** neural network staff **/
	public NeuralNetworkDatabaseConnector(String mode) {
		if (mode != "BackNN") {
			System.out.println("[" + "mode" + "] Mode not supported");
			System.exit(1);
		}
		this.mode = mode;
	}

	public void createDatabase() {
		try {
			MyDatabaseConnector.executeSQLFile("res/CreateDatabase.sql");
			MyDatabaseConnector.executeSQLFile("res/CreateTableCells.sql");
			MyDatabaseConnector.executeSQLFile("res/CreateTableConnections.sql");
		} catch (SQLException | IOException e) {
			System.out.println("failed when creating database/table");
			if (e instanceof SQLException)
				MyDatabaseConnector.printSQLException((SQLException) e);
		}
	}

	public Vector<Example> getExamples() {
		Vector<Example> examples = new Vector<Example>();
		String sql = "select * from nndb.examples;";
		try {
			ResultSet rs = MyDatabaseConnector.executeQuery(sql);
			while (rs.next()) {
				int id = rs.getInt(1);
				Vector<Double> input = new Vector<Double>();
				Vector<Double> output = new Vector<Double>();
				input.add(rs.getDouble(2));
				input.add(rs.getDouble(3));
				output.add(rs.getDouble(4));
				Example example = new Example(input, output);
				examples.add(example);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return examples;
	}
}
