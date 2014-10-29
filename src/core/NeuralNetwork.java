package core;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.text.html.HTMLDocument.HTMLReader.PreAction;

import com.mysql.jdbc.PreparedStatement;

import myutils.Mysql;
import myutils.SqlServerInfo;
import myutils.Utils;

public class NeuralNetwork {
	private SqlServerInfo sqlServerInfo;
	private String mode;
	private int[] layers;

	public NeuralNetwork(SqlServerInfo sqlServerInfo, String mode, int[] layers) {
		if (mode != "BackNN") {
			System.out.println("[" + "mode" + "] Mode not supported");
			System.exit(1);
		}
		this.sqlServerInfo = (SqlServerInfo) sqlServerInfo.clone();
		this.mode = mode;
		this.layers = layers.clone();
	}

	public void create() {
		try {
			removeDatabase();
			createDatabase();
			initCells();
			save();
		} catch (SQLException e) {
			Mysql.printSQLException(e);
		}
	}

	public void removeDatabase() {
		try {
			Mysql.sqlExecUpdate(sqlServerInfo.noDB(), "DROP DATABASE IF EXISTS "
					+ sqlServerInfo.databaseName);
		} catch (SQLException e) {
			System.out.println("failed to DROP DATABASE " + sqlServerInfo.databaseName);
			Mysql.printSQLException(e);
		}
	}

	public void createDatabase() {
		ArrayList<String> sqls = new ArrayList<String>();
		sqls.add("CREATE DATABASE IF NOT EXISTS " + sqlServerInfo.databaseName + ";");
		sqls.add("CREATE TABLE cells (cellid INT, delta DOUBLE, layer INT, PRIMARY KEY (cellid));");
		sqls.add("CREATE TABLE connections (incomeid INT, outcomeid INT, weight DOUBLE, PRIMARY KEY (incomeid, outcomeid));");
		// sqls.add("CREATE TABLE CELLS(layer int, PRIMARY KEY (layer));");
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

	public void initCells() throws SQLException {
		System.out.println("create cells");
		createCells();
		System.out.println("createConnections");
		createConnections();
		System.out.println("csetRandomly");
		setRandomly();
	}

	public int getLastID() {
		String sqlQuery = "SELECT cellid FROM cells ORDER BY cellid DESC LIMIT 1;";
		int result;
		ResultSet resultSet;
		try {
			resultSet = Mysql.sqlExecQuery(sqlServerInfo, sqlQuery);
			result = resultSet.getInt("cellid");
		} catch (SQLException e) {
			result = 0;
		}
		return result;
	}

	public int getNewID() {
		return getLastID() + 1;
	}

	public String createCell(int cellid, int layer) {
		String result = "INSERT INTO cells (cellid,layer) VALUES (" + cellid + ","
				+ layer + ")";
		System.out.println("create cell-" + cellid);
		return result;
	}

	public void createCells() throws SQLException {
		ArrayList<String> sqls = new ArrayList<String>();
		int newCellid = getNewID();
		for (int iLayer = 0; iLayer < layers.length; iLayer++)
			for (int iCell = 0; iCell < layers[iLayer]; iCell++)
				sqls.add(createCell(newCellid++, iLayer));
		Mysql.sqlExecBatch(sqlServerInfo, sqls);
	}

	private String createConnection(Integer iCellFrom, Integer iCellTo) {
		System.out.println("create connection "+iCellFrom+"-"+iCellTo);
		return "INSERT INTO connections (incomeid, outcomeid) VALUES ("
				+ iCellFrom.intValue() + "," + iCellTo.intValue() + ")";
	}

	public void createConnections() throws SQLException {
		ArrayList<String> sqls = new ArrayList<String>();
		Vector<Integer> cellFrom = new Vector<Integer>();
		Vector<Integer> cellTo = new Vector<Integer>();
		String sqlQuery;
		ResultSet resultSet;
		for (int iLayer = 0; iLayer < (layers.length - 1); iLayer++) {
			sqlQuery = "SELECT cellid FROM cells WHERE layer=" + iLayer + ";";
			resultSet = Mysql.sqlExecQuery(sqlServerInfo, sqlQuery);
			cellFrom.removeAllElements();
			while (resultSet.next()) {
				cellFrom.add(resultSet.getInt("cellid"));
			}
			sqlQuery = "SELECT cellid FROM cells WHERE layer=" + (iLayer + 1) + ";";
			resultSet = Mysql.sqlExecQuery(sqlServerInfo, sqlQuery);
			cellTo.removeAllElements();
			while (resultSet.next()) {
				cellTo.add(resultSet.getInt("cellid"));
			}
			for (Integer iCellFrom : cellFrom) {
				for (Integer iCellTo : cellTo) {
					sqls.add(createConnection(iCellFrom, iCellTo));
				}
			}
		}
		Mysql.sqlExecBatch(sqlServerInfo, sqls);
	}

	public void setRandomly() throws SQLException {
		setCellsRandomly();
		setConnectionsRandomly();
	}

	private void setCellsRandomly() throws SQLException {
		Vector<Integer> cells = new Vector<Integer>();
		String sqlQuery = "SELECT cellid from cells";
		ResultSet resultSet = Mysql.sqlExecQuery(sqlServerInfo, sqlQuery);
		while (resultSet.next()) {
			cells.add(Integer.valueOf(resultSet.getInt("cellid")));
		}
		ArrayList<String> sqls = new ArrayList<String>();
		Connection connection = Mysql.getConnection(sqlServerInfo);
		String sql = "UPDATE cells SET delta=? WHERE cellid=?";
		for (Integer iCell : cells) {
			PreparedStatement statement = (PreparedStatement) connection
					.prepareStatement(sql);
			// TODO debug
			statement.setDouble(1, Utils.random.nextDouble());
			statement.setInt(2, iCell.intValue());
			String tmp = statement.toString();
			tmp = tmp.substring(tmp.indexOf(':') + 1);
			sqls.add(tmp);
		}
		Mysql.removeConnection();
		Mysql.sqlExecBatch(sqlServerInfo, sqls);
	}

	private void setConnectionsRandomly() throws SQLException {
		ArrayList<String> sqls = new ArrayList<String>();
		Connection connection = Mysql.getConnection(sqlServerInfo);
		String sql = "UPDATE connections SET weight=? WHERE incomeid=? AND outcomeid=?";
		Vector<Integer> cellFrom = new Vector<Integer>();
		Vector<Integer> cellTo = new Vector<Integer>();
		String sqlQuery;
		ResultSet resultSet;
		for (int iLayer = 0; iLayer < (layers.length - 1); iLayer++) {
			sqlQuery = "SELECT cellid FROM cells WHERE layer=" + iLayer + ";";
			resultSet = Mysql.sqlExecQuery(sqlServerInfo, sqlQuery);
			while (resultSet.next()) {
				cellFrom.add(resultSet.getInt("cellid"));
			}
			sqlQuery = "SELECT cellid FROM cells WHERE layer=" + (iLayer + 1) + ";";
			resultSet = Mysql.sqlExecQuery(sqlServerInfo, sqlQuery);
			while (resultSet.next()) {
				cellTo.add(resultSet.getInt("cellid"));
			}
			for (Integer iCellFrom : cellFrom) {
				for (Integer iCellTo : cellTo) {
					PreparedStatement statement = (PreparedStatement) connection
							.prepareStatement(sql);
					statement.setDouble(1, Utils.random.nextDouble());
					statement.setInt(2, iCellFrom);
					statement.setInt(3, iCellTo);
					String tmp = statement.toString();
					tmp = tmp.substring(tmp.indexOf(':') + 1);
					sqls.add(tmp);
				}
			}
		}
		Mysql.removeConnection();
		Mysql.sqlExecBatch(sqlServerInfo, sqls);
	}

	public void save() {
		// dummy
	}

	public void close(Closeable c) {
		if (c != null)
			try {
				c.close();
			} catch (IOException e) {
				// dummy
			}
	}
}
