package neuralnetwork.databaseconnection;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import neuralnetwork.Example;
import myutils.Mysql;
import myutils.SqlServerInfo;

public class NeuralNetworkDatabaseConnector {
	private SqlServerInfo sqlServerInfo;
	private String mode;

	/**general staff**/
	public void executeSqlFile(String path){
		Runtime
	}
	/**neural network staff**/ 
	public NeuralNetworkDatabaseConnector(SqlServerInfo sqlServerInfo, String mode) {
		if (mode != "BackNN") {
			System.out.println("[" + "mode" + "] Mode not supported");
			System.exit(1);
		}
		this.sqlServerInfo = (SqlServerInfo) sqlServerInfo.clone();
		this.mode = mode;
	}

	public void createDatabase() {
		ArrayList<String> sqls = new ArrayList<String>();
		sqls.add("CREATE DATABASE IF NOT EXISTS " + sqlServerInfo.databaseName + ";");
		sqls.add("CREATE TABLE IF NOT EXISTS cells (cell_id INT,  DOUBLE, layer_id INT, PRIMARY KEY (cell_id));");
		sqls.add("CREATE TABLE IF NOT EXISTS connections (src_cell_id INT, dest_cell_id INT, weight DOUBLE, PRIMARY KEY (src_cell_id, dest_cell_id));");
		try {
			// Mysql.sqlExecBatch(sqlServerInfo, sqls);
			Mysql.sqlExec(sqlServerInfo.noDB(), sqls.get(0));
			sqls.remove(0);
			Mysql.sqlExecBatch(sqlServerInfo, sqls);
		} catch (SQLException e) {
			System.out.println("failed when creating database/table");
			Mysql.printSQLException(e);
		}
	}

	public Vector<Example> getExamples() {
		Vector<Example> examples = new Vector<Example>();
		return examples;
	}
}
