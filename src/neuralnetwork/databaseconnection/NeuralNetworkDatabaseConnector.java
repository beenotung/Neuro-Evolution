package neuralnetwork.databaseconnection;

import java.io.IOException;
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
		return examples;
	}
}
