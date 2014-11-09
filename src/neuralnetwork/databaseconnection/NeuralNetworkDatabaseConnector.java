package neuralnetwork.databaseconnection;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import neuralnetwork.Cell;
import neuralnetwork.Connection;
import neuralnetwork.Example;
import neuralnetwork.Layer;
import myutils.FileUtils;
import myutils.connection.MyDatabaseConnector;

public class NeuralNetworkDatabaseConnector {
	private String mode;

	/** static methods **/
	public static Vector<Example> getExamples() {
		Vector<Example> examples = new Vector<Example>();
		String sql = "select * from nndb.examples;";
		try {
			ResultSet rs = MyDatabaseConnector.executeQuery(sql);
			while (rs.next()) {				
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

	public static void createDatabase() {
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

	public static boolean save(Vector<Layer> layers) {
		// TODO saveToDB()
		boolean ok;
		try {
			createDatabase();
			clearTableCells();
			clearTableConnections();
			Vector<Cell> cells = new Vector<Cell>();
			Vector<Connection> connections = new Vector<Connection>();
			for (Layer layer : layers)
				for (Cell cell : layer.cells) {
					cells.add(cell);
					for (Connection connection : cell.connections)
						connections.add(connection);
				}
			Vector<PreparedStatement> preparedStatements = new Vector<PreparedStatement>();
			for (Cell cell : cells)
				preparedStatements.add(getInsertCellStatement(cell));
			for (Connection connection : connections)
				preparedStatements.add(getInsertConnectionStatement(connection));
			Vector<Integer> resultStatuss = MyDatabaseConnector
					.executeUpdate_PreparedStatements(preparedStatements);
			ok = true;
			for (Integer integer : resultStatuss)
				ok &= integer == 1;
		} catch (IOException | SQLException e) {
			ok = false;
			System.out.println("Failed to save to database!");
			if (e instanceof SQLException)
				MyDatabaseConnector.printSQLException((SQLException) e);
			else
				System.out.println("File System Access Denied");
		}
		return ok;
	}

	public static void load() {
	}

	public static boolean clearTableCells() throws IOException, SQLException {
		boolean ok = true;
		List<String> sqlQuerys = FileUtils.readFileAsStrings("res/ClearTableCells.sql");
		for (String string : sqlQuerys)
			if (string.length() > 0)
				ok &= MyDatabaseConnector.execute(string);
		return ok;
	}

	public static boolean clearTableConnections() throws IOException, SQLException {
		boolean ok = true;
		List<String> sqlQuerys = FileUtils
				.readFileAsStrings("res/ClearTableConnections.sql");
		for (String string : sqlQuerys)
			if (string.length() > 0)
				ok &= MyDatabaseConnector.execute(string);
		return ok;
	}

	public static PreparedStatement getInsertCellStatement(Cell cell) throws IOException,
			SQLException {
		PreparedStatement preparedStatement = MyDatabaseConnector
				.getPreparedStatementFromSQLFile("res/InsertCell.sql");
		preparedStatement.setInt(1, cell.id);
		preparedStatement.setInt(2, cell.layerid);
		preparedStatement.setDouble(3, cell.bias);
		return preparedStatement;
	}

	public static PreparedStatement getInsertConnectionStatement(Connection connection)
			throws IOException, SQLException {
		PreparedStatement preparedStatement = MyDatabaseConnector
				.getPreparedStatementFromSQLFile("res/InsertConnection.sql");
		preparedStatement.setInt(1, connection.src.id);
		preparedStatement.setInt(2, connection.dest.id);
		preparedStatement.setDouble(3, connection.weight);
		return preparedStatement;
	}

	/** neural network staff **/
	public NeuralNetworkDatabaseConnector(String mode) {
		if (mode != "BackNN") {
			System.out.println("[" + "mode" + "] Mode not supported");
			System.exit(1);
		}
		this.mode = mode;
	}
}
