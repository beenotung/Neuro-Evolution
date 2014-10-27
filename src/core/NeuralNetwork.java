package core;

import java.sql.SQLException;
import java.util.ArrayList;

import myutils.Mysql;
import myutils.SqlServerInfo;

public class NeuralNetwork {
	private SqlServerInfo sqlServerInfo;
	private String mode;
	private int[] layers;

	public NeuralNetwork(SqlServerInfo sqlServerInfo, String mode, int[] layers)
			throws CloneNotSupportedException {
		if (mode != "BackNN") {
			System.out.println("[" + "mode" + "] Mode not supported");
			System.exit(1);
		}
		this.sqlServerInfo = (SqlServerInfo) sqlServerInfo.clone();
		this.mode = mode;
		this.layers = layers.clone();
	}

	public void removeDatabase() {
		try {
			Mysql.sqlExecUpdate(sqlServerInfo.noDB(), "DROP DATABASE IF EXISTS " + sqlServerInfo.databaseName);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			System.out.println();
			System.out.println("failed to DROP DATABASE " + sqlServerInfo.databaseName);
			System.out.println("SQLException ErrorCode: " + e.getErrorCode());
			System.out.println("SQLException SQLState: " + e.getSQLState());
			System.out.println("SQLException Message: " + e.getMessage());
		}
	}

	public void createDatabase() {
		ArrayList<String> sqls = new ArrayList<String>();
		sqls.add("CREATE DATABASE IF NOT EXISTS " + sqlServerInfo.databaseName + ";");
		sqls.add("CREATE TABLE cells (cellid INT, delta DOUBLE, layer INT, PRIMARY KEY (cellid));");
		 sqls.add("CREATE TABLE connections (incomeid INT, outcomeid INT, weight DOUBLE, PRIMARY KEY (incomeid, outcomeid));");
		//sqls.add("CREATE TABLE CELLS(layer int, PRIMARY KEY (layer));");
		try {
			// Mysql.sqlExecBatch(sqlServerInfo, sqls);
			Mysql.sqlExec(sqlServerInfo.noDB(), sqls.get(0));
			sqls.remove(0);
			Mysql.sqlExecBatch(sqlServerInfo, sqls);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			System.out.println();
			System.out.println("failed when creating database/table");
			System.out.println("SQLException ErrorCode: " + e.getErrorCode());
			System.out.println("SQLException SQLState: " + e.getSQLState());
			System.out.println("SQLException Message: " + e.getMessage());
		}
	}

	public void setRandomly() {
		// TODO Auto-generated method stub
		ArrayList<String> sqls = new ArrayList<String>();
for(int iLayer=0;iLayer<layers.length;iLayer++)
	for(int iCell=0;iCell<layers[iLayer];iCell++)
		sqls.add(
				CREATE TABLE cells
				(
				id int NOT NULL AUTO_INCREMENT,
				incomeid varchar(255) NOT NULL,
				outcomeid varchar(255),
				Address varchar(255),
				City varchar(255),
				PRIMARY KEY (ID)
				)
				);
	}

	public void save() {
		// TODO Auto-generated method stub

	}

}
